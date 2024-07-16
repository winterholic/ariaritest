package youngpeople.aliali.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import youngpeople.aliali.entity.Image;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query("SELECT i FROM Image i WHERE i.post.club.id = :clubId ORDER BY i.createdDate DESC")
    List<Image> findByClubIdOrderByCreatedDateDesc(@Param("clubId") Long clubId, Pageable pageable);
}
