package youngpeople.aliali.entity.club;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QClub is a Querydsl query type for Club
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QClub extends EntityPathBase<Club> {

    private static final long serialVersionUID = 340999530L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QClub club = new QClub("club");

    public final youngpeople.aliali.entity.QBaseEntity _super = new youngpeople.aliali.entity.QBaseEntity(this);

    //inherited
    public final BooleanPath activated = _super.activated;

    public final ListPath<youngpeople.aliali.entity.clubmember.ClubMember, youngpeople.aliali.entity.clubmember.QClubMember> clubMembers = this.<youngpeople.aliali.entity.clubmember.ClubMember, youngpeople.aliali.entity.clubmember.QClubMember>createList("clubMembers", youngpeople.aliali.entity.clubmember.ClubMember.class, youngpeople.aliali.entity.clubmember.QClubMember.class, PathInits.DIRECT2);

    public final EnumPath<youngpeople.aliali.entity.enumerated.ClubTypeA> clubTypeA = createEnum("clubTypeA", youngpeople.aliali.entity.enumerated.ClubTypeA.class);

    public final EnumPath<youngpeople.aliali.entity.enumerated.ClubTypeB> clubTypeB = createEnum("clubTypeB", youngpeople.aliali.entity.enumerated.ClubTypeB.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final youngpeople.aliali.entity.QImage image;

    public final StringPath introduction = createString("introduction");

    public final EnumPath<youngpeople.aliali.entity.enumerated.LocationType> locationType = createEnum("locationType", youngpeople.aliali.entity.enumerated.LocationType.class);

    public final StringPath name = createString("name");

    public final ListPath<Notice, QNotice> notices = this.<Notice, QNotice>createList("notices", Notice.class, QNotice.class, PathInits.DIRECT2);

    public final ListPath<Post, QPost> posts = this.<Post, QPost>createList("posts", Post.class, QPost.class, PathInits.DIRECT2);

    public final ListPath<Recruitment, QRecruitment> recruitments = this.<Recruitment, QRecruitment>createList("recruitments", Recruitment.class, QRecruitment.class, PathInits.DIRECT2);

    public final youngpeople.aliali.entity.member.QSchool school;

    public final StringPath typeName = createString("typeName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public QClub(String variable) {
        this(Club.class, forVariable(variable), INITS);
    }

    public QClub(Path<? extends Club> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QClub(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QClub(PathMetadata metadata, PathInits inits) {
        this(Club.class, metadata, inits);
    }

    public QClub(Class<? extends Club> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.image = inits.isInitialized("image") ? new youngpeople.aliali.entity.QImage(forProperty("image"), inits.get("image")) : null;
        this.school = inits.isInitialized("school") ? new youngpeople.aliali.entity.member.QSchool(forProperty("school")) : null;
    }

}

