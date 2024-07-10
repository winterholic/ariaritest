package youngpeople.aliali.entity.clubmember;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import youngpeople.aliali.entity.club.Club;
import youngpeople.aliali.entity.member.Member;

@Entity
@NoArgsConstructor
@Getter @Setter
public class Bookmark {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private Long id;

    // FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    public Bookmark(Member member, Club club) {
        this.member = member;
        this.club = club;
    }

}
