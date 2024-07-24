package youngpeople.aliali.entity.club;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import youngpeople.aliali.entity.BaseEntity;
import youngpeople.aliali.entity.member.Member;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
//@Where(clause = "activated = true") // 비활성화된 댓글도 조회해야하는 경우가 있음
@SQLDelete(sql = "UPDATE post SET activated = false WHERE comment_id = ?")
@Getter
public class Comment extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String text;
    private Boolean secret;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @OneToMany(fetch = LAZY)
    private List<Comment> childrenComments = new ArrayList<>();

    public Comment(Post post, Member member, String text, Boolean secret) {
        this.post = post;
        this.member = member;
        this.text = text;
        this.secret = secret;
    }

    public Comment(Post post, Member member, String text, Boolean secret, Comment parentComment) {
        this.post = post;
        this.member = member;
        this.text = text;
        this.secret = secret;
        this.parentComment = parentComment;
    }

    public void addChildComment(Comment childComment) {
        this.childrenComments.add(childComment);
    }
}

