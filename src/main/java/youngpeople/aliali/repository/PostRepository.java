package youngpeople.aliali.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import youngpeople.aliali.entity.club.Post;
import youngpeople.aliali.entity.enumerated.PostType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import youngpeople.aliali.entity.member.Member;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByClubIdAndPostTypeAndFixedOrderByCreatedDateDesc(Long clubId, PostType postType, boolean Fixed, Pageable pageable);
    List<Post> findByClubIdAndFixedAndPostType(Long clubId, boolean Fixed, PostType postType);
    Page<Post> findByMemberNotInAndClubIdAndPostTypeOrderByCreatedDateDesc(List<Member> members, Long clubId, PostType postType, Pageable pageable);
    int countByClubIdAndFixedAndPostType(Long clubId, boolean Fixed, PostType postType);
}

// 객체 그래프 탐색이 잘 이루어지고 있는지에 대해서 확인해봐야할듯...?