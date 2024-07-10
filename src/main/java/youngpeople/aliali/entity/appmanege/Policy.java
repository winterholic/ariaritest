package youngpeople.aliali.entity.appmanege;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import youngpeople.aliali.entity.BaseEntity;

@Entity
@Where(clause = "activated = true")
@SQLDelete(sql = "UPDATE policy SET activated = false WHERE policy_id = ?")
@NoArgsConstructor
public class Policy extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "policy_id")
    private Long id;

    private String title;
    private String text;
    private Boolean fixed;

}
