package youngpeople.aliali.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import youngpeople.aliali.entity.club.Club;
import youngpeople.aliali.entity.enumerated.ClubTypeB;
import youngpeople.aliali.entity.member.Member;
import youngpeople.aliali.entity.member.School;

import java.util.List;
import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long> {

    List<Club> findBySchool(School school);

    List<Club> findBySchoolAndClubTypeB(School school, ClubTypeB clubTypeB);

    Optional<Club> findBySchoolAndName(School school, String name);

}
