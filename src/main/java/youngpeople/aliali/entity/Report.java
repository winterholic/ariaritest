//package youngpeople.aliali.entity;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import youngpeople.aliali.entity.club.Club;
//import youngpeople.aliali.entity.club.Notice;
//import youngpeople.aliali.entity.club.Post;
//import youngpeople.aliali.entity.club.Recruitment;
//import youngpeople.aliali.entity.enumerated.ContentType;
//import youngpeople.aliali.entity.enumerated.ReasonType;
//import youngpeople.aliali.entity.member.Member;
//
//import static jakarta.persistence.FetchType.*;
//
//
///**
// * 개발 및 테스트 시 주의
// */
//@Entity
//@NoArgsConstructor
//@Getter @Setter
//public class Report extends BaseEntity {
//
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "report_id")
//    private Long id;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "reason_type", nullable = false)
//    private ReasonType reasonType;
//
//    private Boolean handled = false;
//
//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "member_id")
//    private Member reporter;
//
//    /**
//     * reportable contents
//     *  → comment / post / notice / club / recruitment / member
//     */
//    @Enumerated(EnumType.STRING)
//    @Column(name = "content_type", nullable = false)
//    private ContentType contentType;
//
//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "comment_id")
//    private Comment comment;
//
//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "post_id")
//    private Post post;
//
//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "notice_id")
//    private Notice notice;
//
//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "club_id")
//    private Club club;
//
//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "recruitment_id")
//    private Recruitment recruitment;
//
//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "member_id")
//    private Member member;
//}
