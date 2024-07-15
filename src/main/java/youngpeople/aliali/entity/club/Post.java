package youngpeople.aliali.entity.club;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import youngpeople.aliali.entity.BaseEntity;
import youngpeople.aliali.entity.Image;
import youngpeople.aliali.entity.enumerated.PostType;
import youngpeople.aliali.entity.member.Member;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
@Where(clause = "activated = true")
@SQLDelete(sql = "UPDATE post SET activated = false WHERE post_id = ?")
@Getter
@Setter
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;
    private String text;
    private Boolean fixed;

    private PostType postType;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @OneToMany(mappedBy = "post")
    private List<Image> images = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    
    // 댓글기능 추가

    public Post(String title, String text, Boolean fixed, PostType postType, Club club, Member member) {
        this.title = title;
        this.text = text;
        this.fixed = fixed;
        this.postType = postType;
        this.club = club;
        this.member = member;
    }
}