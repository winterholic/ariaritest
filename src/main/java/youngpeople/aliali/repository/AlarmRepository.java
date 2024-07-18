package youngpeople.aliali.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import youngpeople.aliali.entity.member.Alarm;
import youngpeople.aliali.entity.member.Member;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    Page<Alarm> findTop5ByMemberOrderByCreatedDateDesc(Member member);

}
