package youngpeople.aliali.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import youngpeople.aliali.entity.member.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByKakaoId(String kakaoId);

}
