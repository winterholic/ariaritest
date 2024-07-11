package youngpeople.aliali.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import youngpeople.aliali.entity.club.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
