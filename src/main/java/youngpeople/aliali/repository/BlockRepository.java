package youngpeople.aliali.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import youngpeople.aliali.entity.member.Block;
import youngpeople.aliali.entity.member.Member;

import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block, Long> {

    Optional<Block> findByMemberAndTarget(Member member, Member target);

}
