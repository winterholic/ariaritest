package youngpeople.aliali.dto;

import lombok.Getter;
import youngpeople.aliali.entity.club.Club;
import youngpeople.aliali.entity.club.Recruitment;
import youngpeople.aliali.entity.enumerated.ClubTypeA;
import youngpeople.aliali.entity.enumerated.ClubTypeB;
import youngpeople.aliali.entity.enumerated.LocationType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */

/**
 * ClubAllDto - ClubAllContent
 * ClubSchoolDto - ClubSchoolContent
 * ClubUnionDto - ClubUnionContent
 * ClubSchoolCategoryDto
 *
 * recruitmentsAllDto - RecruitmentContent
 * recruitmentsSchoolDto - RecruitmentContent
 * recruitmentsUnionDto - RecruitmentContent
 */
public class MainPageDto {

    /**
     * /club/list/all
     */
    @Getter
    public static class ClubAllDto {
        private String message;
        private List<ClubAllContent> clubs = new ArrayList<>();

        public ClubAllDto(String message, List<Club> clubs) {
            this.message = message;
            for (Club club : clubs) {
                this.clubs.add(new ClubAllContent(club));
            }
        }
    }

    @Getter
    public static class ClubAllContent {
        private Long clubId;
        private String clubName;
        private String profile;
        private String introduction;
        private ClubTypeA clubTypeA;

        public ClubAllContent(Club club) {
            this.clubId = club.getId();
            this.clubName = club.getName();
            this.introduction = club.getIntroduction();
            this.clubTypeA = club.getClubTypeA();
        }
    }

    /**
     * /club/list/school
     */
    @Getter
    public static class ClubSchoolDto {
        private String message;
        private List<ClubSchoolContent> clubs = new ArrayList<>();

        public ClubSchoolDto(String message, List<Club> clubs) {
            this.message = message;
            for (Club club : clubs) {
                this.clubs.add(new ClubSchoolContent(club));
            }
        }
    }

    // 학교 club 데이터들을 정리할 리스트
    @Getter
    public static class ClubSchoolContent {
        private Long clubId;
        private String clubName;
        private String introduction;
        private ClubTypeA clubTypeA;
        private ClubTypeB clubTypeB;
        private String typeName;

        public ClubSchoolContent(Club club) {
            this.clubId = club.getId();
            this.clubName = club.getName();
            this.introduction = club.getIntroduction();
            this.clubTypeA = club.getClubTypeA();
            this.clubTypeB = club.getClubTypeB();
            this.typeName = club.getTypeName();
        }
    }

    /**
     * /club/list/union
     */
    @Getter
    public static class ClubUnionDto {
        private String message;
        private List<ClubUnionContent> clubs = new ArrayList<>();

        public ClubUnionDto(String message, List<Club> clubs) {
            this.message = message;
            for (Club club : clubs) {
                this.clubs.add(new ClubUnionContent(club));
            }
        }
    }

    @Getter
    public static class ClubUnionContent {
        private Long clubId;
        private String clubName;
        private String introduction;
        private ClubTypeA clubTypeA;
        private LocationType locationType;

        public ClubUnionContent(Club club) {
            this.clubId = club.getId();
            this.clubName = club.getName();
            this.introduction = club.getIntroduction();
            this.clubTypeA = club.getClubTypeA();
            this.locationType = club.getLocationType();
        }
    }

    /**
     * /club/list/school-category
     * 성능 리팩토링 -> native SQL
     * ★ 해시로 바꾸기
     */
    @Getter
    public static class ClubSchoolCategoryDto {
        private String message;
        private List<String> colleges;
        private List<String> majors;
        public ClubSchoolCategoryDto(String message, List<Club> collegeClubs, List<Club> majorClubs) {
            this.message = message;
            ArrayList<String> collegeTmpList = new ArrayList<>();
            ArrayList<String> majorTmpList = new ArrayList<>();

            for (Club collegeClub : collegeClubs) {
                collegeTmpList.add(collegeClub.getTypeName());
            }
            for (Club majorClub : majorClubs) {
                majorTmpList.add(majorClub.getTypeName());
            }

            colleges = collegeTmpList.stream().distinct().collect(Collectors.toList());
            majors = majorTmpList.stream().distinct().collect(Collectors.toList());
        }
    }

    /**
     * /recruitments/all
     */
    @Getter
    public static class RecruitmentsDto {
        private String message;
        private List<RecruitmentContent> recruitments = new ArrayList<>();


        public RecruitmentsDto(String message, List<Recruitment> recruitments){
            this.message = message;
            for (Recruitment recruitment : recruitments) {
                this.recruitments.add(new RecruitmentContent(recruitment));
            }
        }
    }

    @Getter
    public static class RecruitmentContent{
        private Long clubId;
        private String posterAddress;
        private String title;
        private String recruitText;
        private LocalDateTime startDate;
        private String limitedDate;
        private Long views;
        private Integer maxPeople;
        private Integer currentPeople;

        public RecruitmentContent(Recruitment recruitment) {
            this.clubId = recruitment.getClub().getId();
            this.posterAddress = recruitment.getPosterAddress();
            this.title = recruitment.getTitle();
            this.recruitText = recruitment.getText();
            this.startDate = recruitment.getStartDate();
            this.limitedDate = recruitment.getLimitedDateString();
            this.views = recruitment.getViews();
            this.maxPeople = recruitment.getLimitPeople();
            this.currentPeople = recruitment.getApplies().size();
        }
    }


}