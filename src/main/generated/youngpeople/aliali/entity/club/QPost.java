package youngpeople.aliali.entity.club;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = 341389652L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final youngpeople.aliali.entity.QBaseEntity _super = new youngpeople.aliali.entity.QBaseEntity(this);

    //inherited
    public final BooleanPath activated = _super.activated;

    public final QClub club;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final BooleanPath fixed = createBoolean("fixed");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<youngpeople.aliali.entity.Image, youngpeople.aliali.entity.QImage> images = this.<youngpeople.aliali.entity.Image, youngpeople.aliali.entity.QImage>createList("images", youngpeople.aliali.entity.Image.class, youngpeople.aliali.entity.QImage.class, PathInits.DIRECT2);

    public final youngpeople.aliali.entity.member.QMember member;

    public final EnumPath<youngpeople.aliali.entity.enumerated.PostType> postType = createEnum("postType", youngpeople.aliali.entity.enumerated.PostType.class);

    public final StringPath text = createString("text");

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.club = inits.isInitialized("club") ? new QClub(forProperty("club"), inits.get("club")) : null;
        this.member = inits.isInitialized("member") ? new youngpeople.aliali.entity.member.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

