package youngpeople.aliali.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import youngpeople.aliali.entity.member.Alarm;
import youngpeople.aliali.entity.member.Member;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    List<Alarm> findTop5ByMemberOrderByCreatedDateDesc(Member member);

    Page<Alarm> findByMember(Member member, Pageable pageable);

}
