package youngpeople.aliali.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import youngpeople.aliali.entity.member.Alarm;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {


}
