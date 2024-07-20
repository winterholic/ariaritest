package youngpeople.aliali.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import youngpeople.aliali.entity.club.Club;
import youngpeople.aliali.entity.clubmember.ClubMember;
import youngpeople.aliali.entity.member.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {

    Optional<ClubMember> findByMemberAndClub(Member member, Club club);

    Optional<ClubMember> findByMemberIdAndClubId(Long memberId, Long clubId);

    List<ClubMember> findByMember(Member member);

    boolean existsByMemberAndClub(Member member, Club club);
}