package youngpeople.aliali.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import youngpeople.aliali.entity.club.Post;
import youngpeople.aliali.entity.enumerated.PostType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByClubIdAndPostTypeAndFixedOrderByCreatedDateDesc(Long clubId, PostType postType, boolean Fixed, Pageable pageable);
    Page<Post> findByClubId(Long clubId, Pageable pageable);
    Page<Post> findByPostType(PostType postType, Pageable pageable);
    Page<Post> findByFixed(boolean Fixed, Pageable pageable);
}
