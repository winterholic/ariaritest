package youngpeople.aliali.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import youngpeople.aliali.entity.club.Question;

@Data
public class QuestionDto {

    private Long questionId;
    private String text;

    public QuestionDto(Question question) {
        this.questionId = question.getId();
        this.text = question.getText();
    }
}
