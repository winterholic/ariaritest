package youngpeople.aliali.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import youngpeople.aliali.entity.club.Club;
import youngpeople.aliali.entity.clubmember.Bookmark;
import youngpeople.aliali.entity.member.Member;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findByMember(Member member);

    Optional<Bookmark> findByMemberAndClub(Member member, Club club);

    List<Bookmark> findByClub(Club club);

}
