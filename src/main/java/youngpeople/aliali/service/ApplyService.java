package youngpeople.aliali.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import youngpeople.aliali.alarm.AlarmInfo;
import youngpeople.aliali.alarm.aop.AlarmInfoCreatorForAop;
import youngpeople.aliali.dto.BasicResDto;
import youngpeople.aliali.entity.club.*;
import youngpeople.aliali.entity.clubmember.ClubMember;
import youngpeople.aliali.entity.enumerated.MemberRole;
import youngpeople.aliali.entity.enumerated.ResultType;
import youngpeople.aliali.entity.member.Member;
import youngpeople.aliali.exception.apply.ApplyAuthorityException;
import youngpeople.aliali.exception.apply.ExistingApplyException;
import youngpeople.aliali.exception.apply.ExistingClubMemberException;
import youngpeople.aliali.exception.apply.NotPendencyApplyException;
import youngpeople.aliali.exception.clubmember.ClubMemberRemoveAdminException;
import youngpeople.aliali.exception.clubmember.ClubMemberRoleException;
import youngpeople.aliali.exception.common.NotFoundEntityException;
import youngpeople.aliali.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static youngpeople.aliali.dto.ApplyDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ApplyService {

    private final MemberRepository memberRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final ApplyRepository applyRepository;
    private final QuestionRepository questionRepository;
    private final ClubRepository clubRepository;

    public AppliesResDto findApplies(String kakaoId, Long recruitmentId) {
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElseThrow(NotFoundEntityException::new);
        Club club = recruitment.getClub();
        ClubMember clubMember = clubMemberRepository.findByMemberAndClub(member, club).orElseThrow(NotFoundEntityException::new);

        if (clubMember.getMemberRole().equals(MemberRole.GENERAL)) {
            throw new ApplyAuthorityException();
        }

        return new AppliesResDto("successful", recruitment.getApplies());
    }

    public ApplyDetailResDto findApply(String kakaoId, Long applyId) {
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        Apply apply = applyRepository.findById(applyId).orElseThrow(NotFoundEntityException::new);
        Club club = apply.getRecruitment().getClub();
        Optional<ClubMember> findClubMember = clubMemberRepository.findByMemberAndClub(member, club);

        if (!member.equals(apply.getMember()) && (findClubMember.isEmpty() || findClubMember.get().getMemberRole().equals(MemberRole.GENERAL))) {
            throw new ApplyAuthorityException();
        }

        return new ApplyDetailResDto("successful", apply);
    }

    /**
     * school이 있는 경우 member의 school과 같아야 하는 조건 추가
     */
    public AlarmInfo registerApply(String kakaoId, Long recruitmentId, ApplyReqDto applyReqDto) {
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElseThrow(NotFoundEntityException::new);
        Club club = recruitment.getClub();

        clubMemberRepository.findByMemberAndClub(member, club).ifPresent(m -> {
            throw new ExistingClubMemberException();
        });

        applyRepository.findByMember(member).ifPresent(m -> {
            throw new ExistingApplyException();
        });

        Apply apply = new Apply(member, recruitment);

        List<AnswerReqDto> answerList = applyReqDto.getAnswerList();
        for (AnswerReqDto answerReqDto : answerList) {
            Question question = questionRepository.findById(answerReqDto.getQuestionId()).orElseThrow(NotFoundEntityException::new);
            Answer answer = AnswerReqDto.toEntity(answerReqDto, apply, question);
            apply.getAnswers().add(answer);
        }

        applyRepository.save(apply);

        return AlarmInfoCreatorForAop.createInfoInRegisterApply(clubMemberRepository, recruitment);
    }

    public BasicResDto deleteApply(String kakaoId, Long applyId) {
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        Apply apply = applyRepository.findById(applyId).orElseThrow(NotFoundEntityException::new);

        if (!apply.getMember().equals(member)) {
            throw new ApplyAuthorityException();
        }

        applyRepository.delete(apply);

        return BasicResDto.builder()
                .message("successful").build();
    }

    public AlarmInfo judgeApplies(String kakaoId, Long clubId, ApplyProcessingDto applyProcessingDto) {
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        Club club = clubRepository.findById(clubId).orElseThrow(NotFoundEntityException::new);
        ClubMember myClubMember = clubMemberRepository.findByMemberAndClub(member, club).orElseThrow(ClubMemberRoleException::new);

        if (myClubMember.getMemberRole().equals(MemberRole.GENERAL)) {
            throw new ClubMemberRemoveAdminException();
        }

        List<Apply> applies = applyRepository.findAllById(applyProcessingDto.getApplyProcessingId());
        for (Apply apply : applies) {
            if (!apply.getResultType().equals(ResultType.PENDENCY)) {
                throw new NotPendencyApplyException();
            }

            apply.setResultType(ResultType.APPROVE);
        }

        return AlarmInfoCreatorForAop.createInfoInJudgeApplies(applies);
    }

    public MyAppliesDto findMyApplies(String kakaoId){
        Member member = memberRepository.findByKakaoId(kakaoId).get();
        LocalDateTime dateLimit = LocalDateTime.now().plusDays(30);
        List<Apply> applies = applyRepository.findByMemberAndUpdatedDateAfter(member, dateLimit);
        if (applies == null) {
            // 익셉션
        }
        log.info("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★{}", applies);
        return new MyAppliesDto("successful", applies);
    }


    public AlarmInfo approveMyApply(String kakaoId, Long applyId) {
        Member member = memberRepository.findByKakaoId(kakaoId).get();
        Apply apply = applyRepository.findById(applyId).orElseThrow(() -> new IllegalArgumentException("apply doesn't exist"));

        Club club = apply.getRecruitment().getClub();

        if (apply.getResultType() != ResultType.STANDBY) {
            // 익셉션
        }

        apply.setResultType(ResultType.APPROVE);
        applyRepository.save(apply);

        boolean clubmemberexist = clubMemberRepository.existsByMemberAndClub(member, club);
        String myMessage = "successful";
        if (clubmemberexist) {
            // 익셉션
            myMessage = "fail";
        }
        else {
            clubMemberRepository.save(new ClubMember(member, club));
        }

        return AlarmInfoCreatorForAop.createInfoInJoinClub(clubMemberRepository, apply);
    }

    public BasicResDto refuseMyApply(String kakaoId, Long applyId) {
        Member member = memberRepository.findByKakaoId(kakaoId).get();
        Apply apply = applyRepository.findById(applyId).orElseThrow(() -> new IllegalArgumentException("apply doesn't exist"));
        Club club = apply.getRecruitment().getClub();

        if (apply.getResultType() != ResultType.STANDBY){
            // 익셉션
        }

        apply.setResultType(ResultType.REFUSAL);
        applyRepository.save(apply);

        return BasicResDto.builder()
                .message("successful")
                .build();
    }

}