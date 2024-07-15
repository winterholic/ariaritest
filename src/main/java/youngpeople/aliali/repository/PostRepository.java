package youngpeople.aliali.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import youngpeople.aliali.entity.club.Post;
import youngpeople.aliali.entity.enumerated.PostType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByClubIdAndPostTypeAndFixedOrderByCreatedDateDesc(Long clubId, PostType postType, boolean Fixed, Pageable pageable);
    List<Post> findByClubIdAndFixedAndPostType(Long clubId, boolean Fixed, PostType postType);
}
