package youngpeople.aliali.entity.member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import static jakarta.persistence.FetchType.*;

/**
 * 개발 및 테스트 시 주의
 */
@Entity
@NoArgsConstructor
@Where(clause = "activated = true")
@SQLDelete(sql = "UPDATE block SET activated = false WHERE block_id = ?")
@Getter
public class Block {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn
    private Member blockingMember;

    @ManyToOne(fetch = LAZY)
    @JoinColumn
    private Member blockedMember;

    public Block(Member blockingMember, Member blockedMember) {
        this.blockingMember = blockingMember;
        this.blockedMember = blockedMember;
    }
}

/**
 * DROP TABLE tmember;
 * DROP TABLE tblock;
 *
 * CREATE TABLE tmember
 * (
 *   tmember_id BIGINT NOT NULL AUTO_INCREMENT,
 *   username VARCHAR(20)
 * );
 *
 * CREATE TABLE tblock
 * (
 *   tblock_id BIGINT NOT NULL AUTO_INCREMENT,
 *   tmember_id1 BIGINT NOT NULL,
 *   tmember_id2 BIGINT NOT NULL
 * );
 *
 * INSERT INTO tmember(username) VALUES('순재');
 * INSERT INTO tmember(username) VALUES('혜정');
 * INSERT INTO tmember(username) VALUES('지원');
 *
 * INSERT INTO tblock(tmember_id1, tmember_id2) VALUES(1, 3);
 *
 * SELECT m1.username AS blocking, m2.username AS blocked
 * FROM tblock AS b, tmember AS m1, tmember AS m2
 * WHERE b.tmember_id1 = m1.tmember_id and b.tmember_id2 = m2.tmember_id;
 *
 * SELECT * FROM tmember;
 * SELECT * FROM tblock;
 */