package youngpeople.aliali.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import youngpeople.aliali.entity.club.Apply;
import youngpeople.aliali.entity.club.Recruitment;
import youngpeople.aliali.entity.member.Member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ApplyRepository extends JpaRepository<Apply, Long> {

    @Query("SELECT a FROM Apply a WHERE a.member = :member AND a.updatedDate <= :dateLimit")
    List<Apply> findByMemberAndUpdatedDateAfter(@Param("member") Member member, @Param("dateLimit") LocalDateTime dateLimit);

    Optional<Apply> findByMember(Member member);

    List<Apply> findByRecruitment(Recruitment recruitment);


}
