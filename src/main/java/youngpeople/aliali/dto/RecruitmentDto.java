package youngpeople.aliali.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import youngpeople.aliali.entity.club.Club;
import youngpeople.aliali.entity.club.Question;
import youngpeople.aliali.entity.club.Recruitment;
import youngpeople.aliali.entity.enumerated.ClubTypeA;
import youngpeople.aliali.entity.enumerated.ClubTypeB;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RecruitmentDto {

    @Data
    @NoArgsConstructor
    public static class RecruitmentsResDto {
        private String message;
        private List<RecruitmentResDto> recruitmentList = new ArrayList<>();

        public RecruitmentsResDto(String message, List<Recruitment> recruitments) {
            this.message = message;
            for (Recruitment recruitment : recruitments) {
                this.recruitmentList.add(new RecruitmentResDto(recruitment));
            }
        }
    }

    @Data
    public static class RecruitmentResDto {
        private Long recruitmentId;
        private String title;

        public RecruitmentResDto(Recruitment recruitment) {
            this.recruitmentId = recruitment.getId();
            this.title = recruitment.getTitle();
        }
    }

    @Data
    public static class RecruitmentDetailResDto {
        private String message;
        // Club contents
        private Long clubId;
        private String clubName;
        private String clubIntroduction;
        private ClubTypeA clubTypeA;
        private ClubTypeB clubTypeB;
        private String typeName;
        // Recruitment contents
        private Long recruitmentId;
        private String posterAddress;
        private String title;
        private String text;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private Integer limitPeople;
        private Integer currentApply; // Apply
        private Boolean recruitmentState;
        private List<QuestionDto> questionList = new ArrayList<>(); // Question

        public RecruitmentDetailResDto(String message, Club club,
                                       Recruitment recruitment,
                                       Integer currentApply,
                                       Boolean recruitmentState,
                                       List<Question> questions) {
            this.message = message;
            this.clubId = club.getId();
            this.clubName = club.getName();
            this.clubIntroduction = club.getIntroduction();
            this.clubTypeA = club.getClubTypeA();
            this.clubTypeB = club.getClubTypeB();
            this.typeName = club.getTypeName();
            this.recruitmentId = recruitment.getId();
            this.posterAddress = recruitment.getPosterAddress();
            this.title = recruitment.getTitle();
            this.text = recruitment.getText();
            this.startDate = recruitment.getStartDate();
            this.endDate = recruitment.getEndDate();
            this.limitPeople = recruitment.getLimitPeople();
            this.currentApply = currentApply;
            this.recruitmentState = recruitmentState;
            for (Question question: questions) {
                questionList.add(new QuestionDto(question));
            }
        }
    }

    @Data
    @NoArgsConstructor
    public static class RecruitmentReqDto {
        private String title;
        private String text;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private Integer limitPeople;
        private List<String> questionList = new ArrayList<>(); // Question
    }

    public static Recruitment toRecruitmentEntity(RecruitmentReqDto recruitmentReqDto,
                                                  Club club,
                                                  String profileUrl) {
        return new Recruitment(recruitmentReqDto.getTitle(),
                recruitmentReqDto.getText(),
                recruitmentReqDto.getStartDate(),
                recruitmentReqDto.getEndDate(),
                recruitmentReqDto.getLimitPeople(),
                profileUrl,
                club);
    }

    public static List<Question> toQuestionEntities(RecruitmentReqDto recruitmentReqDto,
                                                    Recruitment recruitment) {
        List<Question> entities = new ArrayList<>();
        List<String> questionList = recruitmentReqDto.getQuestionList();
        for (String question : questionList) {
            entities.add(new Question(question, recruitment));
        }

        return entities;
    }

    public static void updateRecruitmentEntity(Recruitment recruitment,
                                               RecruitmentReqDto recruitmentReqDto,
                                               String profileUrl) {
        recruitment.setTitle(recruitmentReqDto.getTitle());
        recruitment.setText(recruitmentReqDto.getText());
        recruitment.setStartDate(recruitmentReqDto.getStartDate());
        recruitment.setEndDate(recruitmentReqDto.getEndDate());
        recruitment.setLimitPeople(recruitmentReqDto.getLimitPeople());
        recruitment.setPosterAddress(profileUrl);

        recruitment.getQuestions().clear();
        recruitment.getQuestions().addAll(toQuestionEntities(recruitmentReqDto, recruitment));
    }




}
