package youngpeople.aliali.entity.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 1640222954L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMember member = new QMember("member1");

    public final youngpeople.aliali.entity.QBaseEntity _super = new youngpeople.aliali.entity.QBaseEntity(this);

    //inherited
    public final BooleanPath activated = _super.activated;

    public final ListPath<Alarm, QAlarm> alarms = this.<Alarm, QAlarm>createList("alarms", Alarm.class, QAlarm.class, PathInits.DIRECT2);

    public final ListPath<youngpeople.aliali.entity.club.Apply, youngpeople.aliali.entity.club.QApply> applies = this.<youngpeople.aliali.entity.club.Apply, youngpeople.aliali.entity.club.QApply>createList("applies", youngpeople.aliali.entity.club.Apply.class, youngpeople.aliali.entity.club.QApply.class, PathInits.DIRECT2);

    public final ListPath<Block, QBlock> blockeds = this.<Block, QBlock>createList("blockeds", Block.class, QBlock.class, PathInits.DIRECT2);

    public final ListPath<Block, QBlock> blockings = this.<Block, QBlock>createList("blockings", Block.class, QBlock.class, PathInits.DIRECT2);

    public final ListPath<youngpeople.aliali.entity.clubmember.Bookmark, youngpeople.aliali.entity.clubmember.QBookmark> bookmarks = this.<youngpeople.aliali.entity.clubmember.Bookmark, youngpeople.aliali.entity.clubmember.QBookmark>createList("bookmarks", youngpeople.aliali.entity.clubmember.Bookmark.class, youngpeople.aliali.entity.clubmember.QBookmark.class, PathInits.DIRECT2);

    public final ListPath<youngpeople.aliali.entity.clubmember.ClubMember, youngpeople.aliali.entity.clubmember.QClubMember> clubMembers = this.<youngpeople.aliali.entity.clubmember.ClubMember, youngpeople.aliali.entity.clubmember.QClubMember>createList("clubMembers", youngpeople.aliali.entity.clubmember.ClubMember.class, youngpeople.aliali.entity.clubmember.QClubMember.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath kakaoId = createString("kakaoId");

    public final DateTimePath<java.time.LocalDateTime> lastLogin = createDateTime("lastLogin", java.time.LocalDateTime.class);

    public final StringPath nickname = createString("nickname");

    public final NumberPath<Integer> profile = createNumber("profile", Integer.class);

    public final QSchool school;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public QMember(String variable) {
        this(Member.class, forVariable(variable), INITS);
    }

    public QMember(Path<? extends Member> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMember(PathMetadata metadata, PathInits inits) {
        this(Member.class, metadata, inits);
    }

    public QMember(Class<? extends Member> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.school = inits.isInitialized("school") ? new QSchool(forProperty("school")) : null;
    }

}

