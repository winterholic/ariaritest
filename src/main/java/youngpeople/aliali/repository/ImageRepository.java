package youngpeople.aliali.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import youngpeople.aliali.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
