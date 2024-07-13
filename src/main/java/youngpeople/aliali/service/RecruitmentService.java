package youngpeople.aliali.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import youngpeople.aliali.alarm.AlarmInfo;
import youngpeople.aliali.alarm.aop.AlarmInfoCreatorForAop;
import youngpeople.aliali.alarm.aop.AlarmTargetMethod;
import youngpeople.aliali.dto.BasicResDto;
import youngpeople.aliali.entity.club.Club;
import youngpeople.aliali.entity.club.Question;
import youngpeople.aliali.entity.club.Recruitment;
import youngpeople.aliali.entity.clubmember.ClubMember;
import youngpeople.aliali.entity.enumerated.MemberRole;
import youngpeople.aliali.entity.member.Member;
import youngpeople.aliali.exception.clubmember.ClubMemberRoleException;
import youngpeople.aliali.exception.common.NotFoundEntityException;
import youngpeople.aliali.exception.common.NotMatchedEntitiesException;
import youngpeople.aliali.exception.recruitment.RecruitmentAuthorityException;
import youngpeople.aliali.exception.recruitment.RecruitmentStateException;
import youngpeople.aliali.repository.*;

import java.util.List;
import java.util.Optional;

import static youngpeople.aliali.dto.RecruitmentDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RecruitmentService {

    private final RecruitmentRepository recruitmentRepository;
    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final QuestionRepository questionRepository;
    private final BookmarkRepository bookmarkRepository;

    /**
     * 미사용 예정
     */
    public RecruitmentsResDto findRecruitments(String kakaoId, Long clubId) {
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        Club club = clubRepository.findById(clubId).orElseThrow(NotFoundEntityException::new);
        Optional<ClubMember> myClubMember = clubMemberRepository.findByMemberAndClub(member, club);

        if (myClubMember.isEmpty() || myClubMember.get().getMemberRole() == MemberRole.GENERAL) {
            throw new RecruitmentAuthorityException();
        }

        List<Recruitment> recruitments = recruitmentRepository.findByClub(club);

        return new RecruitmentsResDto("successful", recruitments);
    }

    public RecruitmentDetailResDto findRecruitment(String kakaoId, Long clubId,
                                                   Long recruitmentId) {
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        Club club = clubRepository.findById(clubId).orElseThrow(NotFoundEntityException::new);
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElseThrow(NotFoundEntityException::new);

        if (!recruitment.getClub().equals(club)) {
            throw new NotMatchedEntitiesException();
        }

        int currentApply = recruitment.getApplies().size();
        Boolean recruitmentState = recruitment.getRecruitmentState();
        List<Question> questions = recruitment.getQuestions();

        return new RecruitmentDetailResDto("successful",
                club, recruitment, currentApply, recruitmentState, questions);
    }

    @AlarmTargetMethod
    public AlarmInfo registerRecruitment(String kakaoId, Long clubId,
                                         RecruitmentReqDto recruitmentReqDto,
                                         String profileUrl) {
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        Club club = clubRepository.findById(clubId).orElseThrow(NotFoundEntityException::new);
        ClubMember myClubMember = clubMemberRepository.findByMemberAndClub(member, club).orElseThrow(NotFoundEntityException::new);

        if (myClubMember.getMemberRole().equals(MemberRole.GENERAL)) {
            throw new ClubMemberRoleException();
        }

        Recruitment recruitment = toRecruitmentEntity(recruitmentReqDto, club, profileUrl);

        recruitmentRepository.save(recruitment);
        questionRepository.saveAll(toQuestionEntities(recruitmentReqDto, recruitment));

        return AlarmInfoCreatorForAop.createInfoInRegisterRecruitment(bookmarkRepository, recruitment);
    }

    /**
     * 등록과 같은 Dto를 받는 방식으로 수정 (QuestionList는 전부 삭제 후 새로 추가)
     */
    public BasicResDto updateRecruitment(String kakaoId, Long clubId,
                                         Long recruitmentId,
                                         RecruitmentReqDto recruitmentReqDto,
                                         String profileUrl) {
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        Club club = clubRepository.findById(clubId).orElseThrow(NotFoundEntityException::new);
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElseThrow(NotFoundEntityException::new);
        ClubMember myClubMember = clubMemberRepository.findByMemberAndClub(member, club).orElseThrow(NotFoundEntityException::new);

        if (myClubMember.getMemberRole().equals(MemberRole.GENERAL)) {
            throw new ClubMemberRoleException();
        }

        if (!recruitment.getRecruitmentState()) {
            throw new RecruitmentStateException();
        }

        updateRecruitmentEntity(recruitment, recruitmentReqDto, profileUrl);

        return BasicResDto.builder()
                .message("successful").build();
    }

    public BasicResDto deleteRecruitment(String kakaoId, Long clubId,
                                         Long recruitmentId) {
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        Club club = clubRepository.findById(clubId).orElseThrow(NotFoundEntityException::new);
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElseThrow(NotFoundEntityException::new);
        ClubMember myClubMember = clubMemberRepository.findByMemberAndClub(member, club).orElseThrow(NotFoundEntityException::new);

        if (myClubMember.getMemberRole().equals(MemberRole.GENERAL)) {
            throw new ClubMemberRoleException();
        }

        if (!recruitment.getRecruitmentState()) {
            throw new RecruitmentStateException();
        }

        recruitmentRepository.delete(recruitment);

        return BasicResDto.builder()
                .message("successful").build();
    }

}
