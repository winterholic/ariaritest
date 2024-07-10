package youngpeople.aliali.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import youngpeople.aliali.entity.club.Answer;
import youngpeople.aliali.entity.club.Apply;
import youngpeople.aliali.entity.club.Question;
import youngpeople.aliali.entity.club.Recruitment;
import youngpeople.aliali.entity.enumerated.ResultType;
import youngpeople.aliali.entity.member.Member;

import java.util.ArrayList;
import java.util.List;


@Slf4j
public class ApplyDto {

    @Data
    public static class AppliesResDto {
        private String message;
        private List<ApplyResDto> applyList = new ArrayList<>();

        public AppliesResDto(String message, List<Apply> applies) {
            this.message = message;
            for (Apply apply : applies) {
                applyList.add(new ApplyResDto(apply));
            }
        }
    }

    @Data
    @AllArgsConstructor
    public static class ApplyResDto {
        private Long applyId;
        private String memberName;
        private ResultType resultType;

        public ApplyResDto(Apply apply) {
            this.applyId = apply.getId();
            this.memberName = apply.getMember().getNickname();
            this.resultType = apply.getResultType();
        }
    }

    @Data
    public static class ApplyDetailResDto {
        private String message;
        private Long applyId;
        private ResultType resultType;
        private String memberName;
        private List<AnswerResDto> answerList = new ArrayList<>();

        public ApplyDetailResDto(String message, Apply apply) {
            this.message = message;
            this.applyId = apply.getId();
            this.resultType = apply.getResultType();
            this.memberName = apply.getMember().getNickname();
            List<Answer> answers = apply.getAnswers();
            for (Answer answer : answers) {
                this.answerList.add(new AnswerResDto(answer));
            }
        }
    }

    @Data
    public static class AnswerResDto {
        private Long answerId;
        private String question;
        private String answer;

        public AnswerResDto(Answer answer) {
            this.answerId = answer.getId();
            this.question = answer.getQuestion().getText();
            this.answer = answer.getText();
        }
    }

    @Data
    @NoArgsConstructor
    public static class ApplyReqDto {
        private List<AnswerReqDto> answerList = new ArrayList<>();

        public static Apply toEntity(Member member, Recruitment recruitment) {
            return new Apply(member, recruitment);
        }
    }

    @Data
    @NoArgsConstructor
    public static class AnswerReqDto {
        private Long questionId;
        private String answer;

        public static Answer toEntity(AnswerReqDto answerReqDto, Apply apply, Question question) {
            return new Answer(answerReqDto.getAnswer(), apply, question);
        }
    }

    @Data
    @NoArgsConstructor
    public static class ApplyProcessingDto {
        private List<Long> applyProcessingList = new ArrayList<>();
    }

    @Data
    public static class MyAppliesDto {
        private String message;
        private List<MyApplyListDto> applyList = new ArrayList<>();

        public MyAppliesDto(String message, List<Apply> applies) {
            this.message = message;
            for (Apply apply : applies) {
                applyList.add(new MyApplyListDto(apply));
            }
        }

    }

    @Data
    public static class MyApplyListDto {
        private Long applyId;
        private String recruitmentName;
        private ResultType resultType;

        public MyApplyListDto(Apply apply) {
            this.applyId = apply.getId();
            this.recruitmentName = apply.getRecruitment().getTitle();
//            this.recruitmentName = apply.getMember().getNickname();
            this.resultType = apply.getResultType();
        }
    }



}
