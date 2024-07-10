package youngpeople.aliali.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import youngpeople.aliali.entity.club.*;
import youngpeople.aliali.entity.clubmember.Bookmark;
import youngpeople.aliali.entity.clubmember.ClubMember;
import youngpeople.aliali.entity.enumerated.ClubTypeA;
import youngpeople.aliali.entity.enumerated.ClubTypeB;
import youngpeople.aliali.entity.enumerated.MemberRole;
import youngpeople.aliali.entity.member.Member;
import youngpeople.aliali.entity.member.School;
import youngpeople.aliali.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestDataConfig {

    private final SchoolRepository schoolRepository;
    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final BookmarkRepository bookmarkRepository;
    private final QuestionRepository questionRepository;
    private final ApplyRepository applyRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void testDataInit() {

        /**
         * school : 4
         */
        List<School> schoolArr = new ArrayList<>();
        School school1 = new School("세종대학교", "@sju.ac.kr");
        School school2 = new School("가톨릭대학교", "@ktu.ac.kr");
        School school3 = new School("홍익대학교", "@hiu.ac.kr");
        School school4 = new School("국민대학교", "@kmu.ac.kr");
        schoolArr.add(school1);
        schoolArr.add(school2);
        schoolArr.add(school3);
        schoolArr.add(school4);
        schoolRepository.saveAll(schoolArr);
        schoolArr.add(null);

        /**
         * member : 20
         */
        List<Member> memberArr = new ArrayList<>();
        Member member1 = new Member("3456718320", "원순재", school1);
        Member member2 = new Member("3457570799", "유한석", school1);
        Member member3 = new Member("3458126487", "김대선", school1);
        Member member4 = new Member("3459895644", "변성은", school1);
        memberArr.add(member1);
        memberArr.add(member2);
        memberArr.add(member3);
        memberArr.add(member4);
        for (int i = 1; i <= 4; i++) {
            for (int j = i*4; j <= i*4 + 3; j++) {
                Member member = new Member("00000000" + j, "member" + j, schoolArr.get(i));
                memberArr.add(member);
            }
        }
        memberRepository.saveAll(memberArr);

        /**
         * club : 6
         */
        Club club1 = new Club("아리아리", "개발하는 동아리입니다.",
                ClubTypeA.BUSINESS, ClubTypeB.COLLEGE, "경영대학", null, school1);
        Club club2 = new Club("람머스", "앞구르기 하는 동아리입니다.",
                ClubTypeA.CULTURE, ClubTypeB.COLLEGE, "소프트웨어융합대학", null, school1);
        Club club3 = new Club("club3", "club3 introduction",
                ClubTypeA.CULTURE, ClubTypeB.COLLEGE, "공과대학", null, school2);
        Club club4 = new Club("club4", "club4 introduction",
                ClubTypeA.CULTURE, ClubTypeB.MAJOR, "컴퓨터공학과", null, school2);
        Club club5 = new Club("club5", "club5 introduction",
                ClubTypeA.CULTURE, null, null, null, null);
        Club club6 = new Club("club6", "club6 introduction",
                ClubTypeA.CULTURE, null, null, null, null);
        clubRepository.save(club1);
        clubRepository.save(club2);
        clubRepository.save(club3);
        clubRepository.save(club4);
        clubRepository.save(club5);
        clubRepository.save(club6);

        /**
         * clubMember
         */
        ClubMember clubMember1_1 = new ClubMember(member1, club1, MemberRole.ADMIN, "회장");
        ClubMember clubMember1_2 = new ClubMember(member2, club1, MemberRole.MANAGER, "관리인");
        ClubMember clubMember1_3 = new ClubMember(member3, club1, MemberRole.GENERAL, "일반");
        ClubMember clubMember1_4 = new ClubMember(member4, club1, MemberRole.GENERAL, "일반");
        ClubMember clubMember2_2 = new ClubMember(member2, club2, MemberRole.ADMIN, "회장");
        ClubMember clubMember2_1 = new ClubMember(member1, club2, MemberRole.MANAGER, "관리자");
        ClubMember clubMember3_5 = new ClubMember(memberArr.get(4), club3, MemberRole.ADMIN, "회장");
        ClubMember clubMember4_6 = new ClubMember(memberArr.get(5), club4, MemberRole.ADMIN, "회장");
        ClubMember clubMember5_1 = new ClubMember(member1, club5, MemberRole.ADMIN, "회장");
        ClubMember clubMember5_5 = new ClubMember(memberArr.get(4), club5, MemberRole.GENERAL, "일반");
        ClubMember clubMember6_2 = new ClubMember(member2, club6, MemberRole.ADMIN, "회장");
        clubMemberRepository.save(clubMember1_1);
        clubMemberRepository.save(clubMember1_2);
        clubMemberRepository.save(clubMember1_3);
        clubMemberRepository.save(clubMember1_4);
        clubMemberRepository.save(clubMember2_2);
        clubMemberRepository.save(clubMember2_1);
        clubMemberRepository.save(clubMember3_5);
        clubMemberRepository.save(clubMember4_6);
        clubMemberRepository.save(clubMember5_1);
        clubMemberRepository.save(clubMember5_5);
        clubMemberRepository.save(clubMember6_2);

        /**
         * recruitment
         */
        Recruitment recruitment1 = new Recruitment("아리아리 1기 모집",
                "아리아리의 첫 모집입니다.",
                LocalDateTime.of(2024, 1, 1, 0, 0, 0),
                LocalDateTime.of(2024, 3, 18, 12, 0, 0),
                10, null, club1);
        Recruitment recruitment2 = new Recruitment("아리아리 2기 모집",
                "아리아리의 두 번째 모집입니다.",
                LocalDateTime.of(2024,5, 1, 0, 0, 0),
                LocalDateTime.of(2024, 10, 18, 5, 0, 0),
                30, null, club1);
        Recruitment recruitment3 = new Recruitment("람머스 1기 모집",
                "람머스의 첫 모집입니다.",
                LocalDateTime.of(2024, 1, 1, 0, 0, 0),
                LocalDateTime.of(2024, 3, 18, 18, 0, 0),
                10, null, club2);
        Recruitment recruitment4 = new Recruitment("람머스 2기 모집",
                "람머스의 두 번째 모집입니다.",
                LocalDateTime.of(2024, 5, 1, 0, 0, 0),
                LocalDateTime.of(2024, 10, 18, 4, 0, 0),
                40, null, club2);
        Recruitment recruitment5 = new Recruitment("club5 모집",
                "text",
                LocalDateTime.of(2024, 5, 1, 0, 0, 0),
                LocalDateTime.of(2024, 8, 14, 4, 0, 0),
                40, null, club5);
        Recruitment recruitment6 = new Recruitment("club6 모집",
                "text",
                LocalDateTime.of(2024, 5, 1, 0, 0, 0),
                LocalDateTime.of(2024, 7, 14, 5, 0, 0),
                40, null, club6);
        recruitmentRepository.save(recruitment1);
        recruitmentRepository.save(recruitment2);
        recruitmentRepository.save(recruitment3);
        recruitmentRepository.save(recruitment4);
        recruitmentRepository.save(recruitment5);
        recruitmentRepository.save(recruitment6);

        /**
         * bookmark
         */
        Bookmark bookmark1 = new Bookmark(member1, club1);
        Bookmark bookmark2 = new Bookmark(member2, club2);
        bookmarkRepository.save(bookmark1);
        bookmarkRepository.save(bookmark2);

        /**
         * question
         */
        Question question1 = new Question("넌 누구냐", recruitment4);
        Question question2 = new Question("넌 뭐냐", recruitment4);
        questionRepository.save(question1);
        questionRepository.save(question2);

        /**
         * apply ( + apply )
         */
        List<Answer> answers1 = new ArrayList<>(Arrays.asList(new Answer("난 한석", question1), new Answer("나는 한석", question2)));
        List<Answer> answers2 = new ArrayList<>(Arrays.asList(new Answer("난 대선", question1), new Answer("나는 대선", question2)));
        Apply apply1 = new Apply(member2, recruitment4, answers1);
        Apply apply2 = new Apply(member3, recruitment4, answers2);
        for (Answer answer : answers1) {
            answer.setApply(apply1);
        }
        for (Answer answer : answers2) {
            answer.setApply(apply2);
        }
        applyRepository.save(apply1);
        applyRepository.save(apply2);

        log.info("Initiating test data is complete");
    }

}
