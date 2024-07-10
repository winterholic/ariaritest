package youngpeople.aliali.entity.club;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import youngpeople.aliali.entity.BaseEntity;
import youngpeople.aliali.entity.Image;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
@Where(clause = "activated = true")
@SQLDelete(sql = "UPDATE recruitment SET activated = false WHERE recruitment_id = ?")
@Getter @Setter
public class Recruitment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Recruitment_id")
    private Long id;

    private String title;
    private String text;
    private String posterAddress;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer limitPeople;

    private Long views = 0L;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @OneToOne(mappedBy = "recruitment")
    private Image image;

    @OneToMany(mappedBy = "recruitment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "recruitment")
    private List<Apply> applies = new ArrayList<>();

    public Recruitment(String title, String text,
                       LocalDateTime startDate, LocalDateTime endDate,
                       Integer limitPeople, String profileUrl, Club club) {
        this.title = title;
        this.text = text;
        this.startDate = startDate;
        this.endDate = endDate;
        this.limitPeople = limitPeople;
        this.club = club;
        this.posterAddress = profileUrl;
    }

    public Boolean getRecruitmentState() {
        return this.getStartDate().isBefore(LocalDateTime.now()) && this.getEndDate().isAfter(LocalDateTime.now());
    }

    /**
     * 수정 !! LocalDateTime 반환
     */
    public long getLimitedDateDays() {
        return ChronoUnit.DAYS.between(LocalDateTime.now(), endDate);
    }
    public long getLimitedDateHours() {
        long totalHours = ChronoUnit.HOURS.between(LocalDateTime.now(), endDate);
        long days = getLimitedDateDays();
        return totalHours - (days * 24);
    }
    public String getLimitedDateString() {
        return getLimitedDateDays() + "일"+ getLimitedDateHours() + "시간";
    }

}