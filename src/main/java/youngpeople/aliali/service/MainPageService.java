package youngpeople.aliali.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import youngpeople.aliali.entity.club.Club;
import youngpeople.aliali.entity.club.Recruitment;
import youngpeople.aliali.entity.clubmember.ClubMember;
import youngpeople.aliali.entity.enumerated.ClubTypeB;
import youngpeople.aliali.entity.member.Member;
import youngpeople.aliali.entity.member.School;
import youngpeople.aliali.repository.ClubRepository;
import youngpeople.aliali.repository.MemberRepository;
import youngpeople.aliali.repository.RecruitmentRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static youngpeople.aliali.dto.MainPageDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MainPageService {

    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;
    private final RecruitmentRepository recruitmentRepository;

    // 정렬하는 부분들을 JPA적으로 개선할 수 있는 부분들이 있어서 개선이 필요해 보임
    public ClubAllDto findClubsAll(String kakaoId) {
        List<Club> clubs = clubRepository.findBySchool(null);

//        if (kakaoId != null) {
//            Member member = memberRepository.findByKakaoId(kakaoId).get();
//            List<ClubMember> clubMembers = member.getClubMembers();
//            for (ClubMember clubMember : clubMembers) {
//                clubs.add(clubMember.getClub());
//            }
//        }

        if (kakaoId != null) {
            Member member = memberRepository.findByKakaoId(kakaoId).get();
            if (member == null) {
                // 익셉션
            }
            School school = member.getSchool();
            if (school != null) {
                clubs.addAll(clubRepository.findBySchool(school));
            }
        }

        Collections.sort(clubs, new Comparator<Club>() {
            @Override
            public int compare(Club c1, Club c2) {
                return c2.getCreatedDate().compareTo(c1.getCreatedDate());
            }
        });

        return new ClubAllDto("successful", clubs);
    }

    public ClubSchoolDto findClubsSchool(String kakaoId) {
        Member member = memberRepository.findByKakaoId(kakaoId).get();
        School school = member.getSchool();
        if (school == null) {
            // 익셉션
        }

        List<Club> clubs = clubRepository.findBySchool(school);

        Collections.sort(clubs, new Comparator<Club>() {
            @Override
            public int compare(Club c1, Club c2) {
                return c2.getCreatedDate().compareTo(c1.getCreatedDate());
            }
        });

        return new ClubSchoolDto("successful", clubs);
    }

    public ClubUnionDto findClubsUnion() {
        List<Club> clubs = clubRepository.findBySchool(null);

        Collections.sort(clubs, new Comparator<Club>() {
            @Override
            public int compare(Club c1, Club c2) {
                return c2.getCreatedDate().compareTo(c1.getCreatedDate());
            }
        });
        return new ClubUnionDto("successful", clubs);
    }

    public ClubSchoolCategoryDto findClubsSchoolCategory(String kakaoId) {
        Member member = memberRepository.findByKakaoId(kakaoId).get();
        School school = member.getSchool();
        if (school == null) {
            // 익셉션
        }

        List<Club> collegeSchool = clubRepository.findBySchoolAndClubTypeB(school, ClubTypeB.COLLEGE);
        List<Club> majorSchool = clubRepository.findBySchoolAndClubTypeB(school, ClubTypeB.MAJOR);

        return new ClubSchoolCategoryDto("successful", collegeSchool, majorSchool);
    }

    public RecruitmentsDto findRecentRecruitmentsAll(String kakaoId) {
        LocalDateTime now = LocalDateTime.now();

        // 현재 모집중인 연합 클럽 모집 찾기
        List<Recruitment> recruitmentsUnion = recruitmentRepository.findByClub_SchoolAndEndDateAfter(null, now);

        if (kakaoId != null) {
            Member member = memberRepository.findByKakaoId(kakaoId).get();
            if (member == null) {
                // 익셉션
            }
            School school = member.getSchool();
            if (school != null) {
                recruitmentsUnion.addAll(recruitmentRepository.findByClub_SchoolAndEndDateAfter(school, now));

            }
        }

        Collections.sort(recruitmentsUnion, new Comparator<Recruitment>() {
            @Override
            public int compare(Recruitment r1, Recruitment r2) {
                return r2.getCreatedDate().compareTo(r1.getCreatedDate());
            }
        });

        return new RecruitmentsDto("successful", recruitmentsUnion);
    }

    public RecruitmentsDto findHotRecruitmentsAll(String kakaoId) {
        LocalDateTime now = LocalDateTime.now();

        // 현재 모집중인 연합 클럽 모집 찾기
        List<Recruitment> recruitmentsUnion = recruitmentRepository.findByClub_SchoolAndEndDateAfter(null, now);

        if (kakaoId != null) {
            Member member = memberRepository.findByKakaoId(kakaoId).get();
            if (member == null) {
                // 익셉션
            }
            School school = member.getSchool();
            if (school != null) {
                recruitmentsUnion.addAll(recruitmentRepository.findByClub_SchoolAndEndDateAfter(school, now));

            }
        }

        Collections.sort(recruitmentsUnion, new Comparator<Recruitment>() {
            @Override
            public int compare(Recruitment r1, Recruitment r2) {
                return Long.compare(r1.getViews(), r2.getViews());
            }
        });

        return new RecruitmentsDto("successful", recruitmentsUnion);
    }

    public RecruitmentsDto findCloseDeadlineRecruitmentsAll(String kakaoId) {
        LocalDateTime now = LocalDateTime.now();

        // 현재 모집중인 연합 클럽 모집 찾기
        List<Recruitment> recruitmentsUnion = recruitmentRepository.findByClub_SchoolAndEndDateAfter(null, now);

        if (kakaoId != null) {
            Member member = memberRepository.findByKakaoId(kakaoId).get();
            if (member == null) {
                // 익셉션
            }
            School school = member.getSchool();
            if (school != null) {
                recruitmentsUnion.addAll(recruitmentRepository.findByClub_SchoolAndEndDateAfter(school, now));

            }
        }

        Collections.sort(recruitmentsUnion, new Comparator<Recruitment>() {
            @Override
            public int compare(Recruitment r1, Recruitment r2) {
                LocalDateTime now = LocalDateTime.now();
                long secondsToEnd1 = ChronoUnit.SECONDS.between(now, r1.getEndDate());
                long secondsToEnd2 = ChronoUnit.SECONDS.between(now, r2.getEndDate());

                return Long.compare(secondsToEnd1, secondsToEnd2);
            }
        });

        return new RecruitmentsDto("successful", recruitmentsUnion);
    }

    public RecruitmentsDto findRecentRecruitmentsSchool(String kakaoId) {
        LocalDateTime now = LocalDateTime.now();
        Member member = memberRepository.findByKakaoId(kakaoId).get();
        if (member == null) {
            // 익셉션
        }
        School school = member.getSchool();
        if (school == null) {
            // 익셉션
        }
        // 현재 모집중인 학교 클럽 모집 찾기
        List<Recruitment> recruitmentsSchool = recruitmentRepository.findByClub_SchoolAndEndDateAfter(school, now);

        Collections.sort(recruitmentsSchool, new Comparator<Recruitment>() {
            @Override
            public int compare(Recruitment r1, Recruitment r2) {
                return r2.getCreatedDate().compareTo(r1.getCreatedDate());
            }
        });

        return new RecruitmentsDto("successful", recruitmentsSchool);
    }

    public RecruitmentsDto findHotRecruitmentsSchool(String kakaoId) {
        LocalDateTime now = LocalDateTime.now();
        Member member = memberRepository.findByKakaoId(kakaoId).get();
        if (member == null) {
            // 익셉션
        }
        School school = member.getSchool();
        if (school == null) {
            // 익셉션
        }
        // 현재 모집중인 학교 클럽 모집 찾기
        List<Recruitment> recruitmentsSchool = recruitmentRepository.findByClub_SchoolAndEndDateAfter(school, now);

        Collections.sort(recruitmentsSchool, new Comparator<Recruitment>() {
            @Override
            public int compare(Recruitment r1, Recruitment r2) {
                return Long.compare(r1.getViews(), r2.getViews());
            }
        });

        return new RecruitmentsDto("successful", recruitmentsSchool);
    }

    public RecruitmentsDto findCloseDeadlineRecruitmentsSchool(String kakaoId) {
        LocalDateTime now = LocalDateTime.now();
        Member member = memberRepository.findByKakaoId(kakaoId).get();
        if (member == null) {
            // 익셉션
        }
        School school = member.getSchool();
        if (school == null) {
            // 익셉션
        }
        // 현재 모집중인 학교 클럽 모집 찾기
        List<Recruitment> recruitmentsSchool = recruitmentRepository.findByClub_SchoolAndEndDateAfter(school, now);

        Collections.sort(recruitmentsSchool, new Comparator<Recruitment>() {
            @Override
            public int compare(Recruitment r1, Recruitment r2) {
                LocalDateTime now = LocalDateTime.now();
                long secondsToEnd1 = ChronoUnit.SECONDS.between(now, r1.getEndDate());
                long secondsToEnd2 = ChronoUnit.SECONDS.between(now, r2.getEndDate());

                return Long.compare(secondsToEnd1, secondsToEnd2);
            }
        });

        return new RecruitmentsDto("successful", recruitmentsSchool);
    }

    public RecruitmentsDto findRecentRecruitmentsUnion() {
        LocalDateTime now = LocalDateTime.now();

        // 현재 모집중인 연합 클럽 모집 찾기
        List<Recruitment> recruitmentsUnion = recruitmentRepository.findByClub_SchoolAndEndDateAfter(null, now);

        Collections.sort(recruitmentsUnion, new Comparator<Recruitment>() {
            @Override
            public int compare(Recruitment r1, Recruitment r2) {
                return r2.getCreatedDate().compareTo(r1.getCreatedDate());
            }
        });
        return new RecruitmentsDto("successful", recruitmentsUnion);
    }

    public RecruitmentsDto findHotRecruitmentsUnion() {
        LocalDateTime now = LocalDateTime.now();

        // 현재 모집중인 연합 클럽 모집 찾기
        List<Recruitment> recruitmentsUnion = recruitmentRepository.findByClub_SchoolAndEndDateAfter(null, now);

        Collections.sort(recruitmentsUnion, new Comparator<Recruitment>() {
            @Override
            public int compare(Recruitment r1, Recruitment r2) {
                return Long.compare(r1.getViews(), r2.getViews());
            }
        });

        return new RecruitmentsDto("successful", recruitmentsUnion);
    }

    public RecruitmentsDto findCloseDeadlineRecruitmentsUnion() {
        LocalDateTime now = LocalDateTime.now();

        // 현재 모집중인 연합 클럽 모집 찾기
        List<Recruitment> recruitmentsUnion = recruitmentRepository.findByClub_SchoolAndEndDateAfter(null, now);

        Collections.sort(recruitmentsUnion, new Comparator<Recruitment>() {
            @Override
            public int compare(Recruitment r1, Recruitment r2) {
                LocalDateTime now = LocalDateTime.now();
                long secondsToEnd1 = ChronoUnit.SECONDS.between(now, r1.getEndDate());
                long secondsToEnd2 = ChronoUnit.SECONDS.between(now, r2.getEndDate());

                return Long.compare(secondsToEnd1, secondsToEnd2);
            }
        });

        return new RecruitmentsDto("successful", recruitmentsUnion);
    }

}
