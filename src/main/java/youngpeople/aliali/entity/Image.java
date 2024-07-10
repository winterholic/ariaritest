package youngpeople.aliali.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import youngpeople.aliali.entity.club.Club;
import youngpeople.aliali.entity.club.Post;
import youngpeople.aliali.entity.club.Recruitment;
import youngpeople.aliali.entity.enumerated.ImageTargetType;

@Entity
@Getter @Setter
public class Image extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(name = "image_target_type")
    private ImageTargetType imageTargetType;

    @Column(name = "image_uri")
    private String imageUri;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_id")
    private Recruitment recruitment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public Image(ImageTargetType imageTargetType, String imageUri) {
        this.imageTargetType = imageTargetType;
        this.imageUri = imageUri;
    }
}
