package youngpeople.aliali.entity.club;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import youngpeople.aliali.entity.BaseEntity;
import youngpeople.aliali.entity.enumerated.ResultType;
import youngpeople.aliali.entity.member.Member;
import youngpeople.aliali.entity.club.Recruitment;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static youngpeople.aliali.entity.enumerated.ResultType.*;

@Entity
@NoArgsConstructor
@Where(clause = "activated = true")
@SQLDelete(sql = "UPDATE apply SET activated = false WHERE apply_id = ?")
@Getter @Setter
public class Apply extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apply_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Setter
    private ResultType resultType = PENDENCY;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "recruitment_id")
    private Recruitment recruitment;

    @OneToMany(mappedBy = "apply", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    public Apply(Member member, Recruitment recruitment) {
        this.member = member;
        this.recruitment = recruitment;
    }

    /**
     * Test Data Init
     */
    public Apply(Member member, Recruitment recruitment, List<Answer> answers) {
        this.member = member;
        this.recruitment = recruitment;
        this.answers = answers;
    }

}
