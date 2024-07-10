package youngpeople.aliali.entity.member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import youngpeople.aliali.entity.BaseEntity;
import youngpeople.aliali.entity.club.Apply;
import youngpeople.aliali.entity.clubmember.Bookmark;
import youngpeople.aliali.entity.clubmember.ClubMember;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
@Where(clause = "activated = true")
@SQLDelete(sql = "UPDATE member SET activated = false WHERE member_id = ?")
@Getter @Setter
public class Member extends BaseEntity  {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "kakao_id", nullable = false, unique = true)
    private String kakaoId;

    private Integer profile = 1;

    @Column(nullable = false)
    private String nickname;

    private LocalDateTime lastLogin;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    @OneToMany(mappedBy = "member")
    private List<ClubMember> clubMembers = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Apply> applies = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Bookmark> bookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Alarm> alarms = new ArrayList<>();

    /**
     * for test
     */
    public Member(String kakaoId, String nickname, School school) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.school = school;
    }

    public String updateNickname(String nickname) {
        this.nickname = nickname;
        return nickname;
    }

}