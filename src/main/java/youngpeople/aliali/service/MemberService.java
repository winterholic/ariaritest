package youngpeople.aliali.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import youngpeople.aliali.dto.*;
import youngpeople.aliali.entity.member.Member;
import youngpeople.aliali.entity.member.School;
import youngpeople.aliali.exception.common.NotFoundEntityException;
import youngpeople.aliali.exception.member.WrongEmailException;
import youngpeople.aliali.manager.MailManager;
import youngpeople.aliali.repository.MemberRepository;
import youngpeople.aliali.repository.SchoolRepository;
import youngpeople.aliali.manager.TokenManager;

import java.time.LocalDateTime;
import java.util.Optional;
import static youngpeople.aliali.dto.MemberDto.*;
import static youngpeople.aliali.dto.SchoolDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private static Long sequence = 100L;

    private final MemberRepository memberRepository;
    private final SchoolRepository schoolRepository;
    private final TokenManager tokenManager;
    private final MailManager mailManager;

    private final static String MAIL_SUBJECT = "ALIALI school auth : ";
    private final static String SCHOOL_AUTH_URI = "http://localhost:8888/my/auth/school-certification?authToken=";

    public MemberResDto findMy(String kakaoId) {
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        return new MemberResDto(member);
    }

    public String login(String kakaoId) {
        Optional<Member> findMember = memberRepository.findByKakaoId(kakaoId);

        Member member = null;
        if (findMember.isEmpty()) {
            member = register(kakaoId);
        } else {
            member = findMember.get();
        }

        member.setLastLogin(LocalDateTime.now());

        return tokenManager.issueToken(kakaoId);
    }

    public Member register(String kakaoId) {
        Member member = new Member();
        member.setKakaoId(kakaoId);
        member.setNickname(autoNickname());

        memberRepository.save(member);
        return member;
    }

    public BasicResDto updateMy(String kakoId,
                                MemberUpdateProfileReqDto profileReqDto,
                                MemberUpdateNicknameReqDto nicknameReqDto) {
        Member member = memberRepository.findByKakaoId(kakoId).orElseThrow(NotFoundEntityException::new);

        if (profileReqDto != null) {
            updateProfileEntity(member, profileReqDto);
        }

        if (nicknameReqDto != null) {
            updateNicknameEntity(member, nicknameReqDto);
        }

        return BasicResDto.builder()
                .message("successful")
                .build();
    }

    public BasicResDto registerMySchool(String kakaoId, SchoolAuthReqDto schoolAuthReqDto) {
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);

        Long schoolId = schoolAuthReqDto.getSchoolId();
        String email = schoolAuthReqDto.getEmail();
        School school = schoolRepository.findById(schoolId).orElseThrow(NotFoundEntityException::new);

        if (!email.split("@")[1].equals(school.getEmail())) {
            throw new WrongEmailException();
        }

        String authToken = tokenManager.createAuthToken(kakaoId, schoolId);
        mailManager.send(email, MAIL_SUBJECT + member.getNickname(), SCHOOL_AUTH_URI + authToken);

        return BasicResDto.builder()
                .message("successful")
                .build();
    }

    public void authenticateSchool(String authToken) {
        String kakaoId = tokenManager.getKakaoId(authToken);
        tokenManager.verifyAuthToken(authToken);

        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        if (member.getSchool() != null) {

        }

        Long schoolId = Long.parseLong(tokenManager.getSchoolId(authToken));
        School school = schoolRepository.findById(schoolId).orElseThrow(NotFoundEntityException::new);

        member.setSchool(school);
    }

    /**
     * 삭제 요청 시 일정 기간 동안 보관 후 삭제 처리
     * 삭제할 member_id를 외래키로 갖는 테이블 추가 -> 삭제용 테이블
     * 작동 방식 : 하루에 한 번 날짜 지난 녀석들 삭제처리
     */
    public BasicResDto unregister(String kakaoId) {
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        memberRepository.delete(member);

        return BasicResDto.builder()
                .message("successful")
                .build();
    }

    // 랜덤 닉네임 생성 메서드 필요
    private String autoNickname() {
        return "user" + sequence++;
    }

}
