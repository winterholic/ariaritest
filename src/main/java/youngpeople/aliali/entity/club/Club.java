package youngpeople.aliali.entity.club;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import youngpeople.aliali.entity.BaseEntity;
import youngpeople.aliali.entity.Image;
import youngpeople.aliali.entity.clubmember.ClubMember;
import youngpeople.aliali.entity.enumerated.ClubTypeA;
import youngpeople.aliali.entity.enumerated.ClubTypeB;
import youngpeople.aliali.entity.enumerated.LocationType;
import youngpeople.aliali.entity.member.School;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "activated = true")
@SQLDelete(sql = "UPDATE club SET activated = false WHERE club_id = ?")
@Getter @Setter
public class Club extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String introduction;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ClubTypeA clubTypeA;

    @Column
    @Enumerated(EnumType.STRING)
    private ClubTypeB clubTypeB;

    private String typeName;

    @Column
    @Enumerated(EnumType.STRING)
    private LocationType locationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    @OneToOne(mappedBy = "club")
    private Image image;

    @OneToMany(mappedBy = "club")
    private List<ClubMember> clubMembers = new ArrayList<>();

    @OneToMany(mappedBy = "club")
    private List<Recruitment> recruitments = new ArrayList<>();

    @OneToMany(mappedBy = "club")
    private List<Notice> notices = new ArrayList<>();

    @OneToMany(mappedBy = "club")
    private List<Post> posts = new ArrayList<>();


    /**
     * Test Data Init 전용
     */
    public Club(String name, String introduction, ClubTypeA clubTypeA, ClubTypeB clubTypeB, String typeName, Image image, School school) {
        this.name = name;
        this.introduction = introduction;
        this.clubTypeA = clubTypeA;
        this.clubTypeB = clubTypeB;
        this.typeName = typeName;
        this.image = image;
        this.school = school;
    }
}
