package youngpeople.aliali.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import youngpeople.aliali.entity.club.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {

}
