package youngpeople.aliali.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import youngpeople.aliali.entity.club.Club;
import youngpeople.aliali.entity.club.Recruitment;
import youngpeople.aliali.entity.member.School;

import java.time.LocalDateTime;
import java.util.List;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {

    List<Recruitment> findByClub(Club club);

    /**
     * for MainPage
     */
    List<Recruitment> findByEndDateAfter(LocalDateTime localDateTime);

    List<Recruitment> findByClub_SchoolAndEndDateAfter(School school, LocalDateTime localDateTime);

    List<Recruitment> findByClub_SchoolIsNullAndEndDateAfter(LocalDateTime localDateTime);

    List<Recruitment> findByEndDateBetween(LocalDateTime start, LocalDateTime end);
}