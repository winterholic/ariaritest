package youngpeople.aliali.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QImage is a Querydsl query type for Image
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QImage extends EntityPathBase<Image> {

    private static final long serialVersionUID = 1536578967L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QImage image = new QImage("image");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final BooleanPath activated = _super.activated;

    public final youngpeople.aliali.entity.club.QClub club;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<youngpeople.aliali.entity.enumerated.ImageTargetType> imageTargetType = createEnum("imageTargetType", youngpeople.aliali.entity.enumerated.ImageTargetType.class);

    public final StringPath imageUri = createString("imageUri");

    public final youngpeople.aliali.entity.club.QPost post;

    public final youngpeople.aliali.entity.club.QRecruitment recruitment;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public QImage(String variable) {
        this(Image.class, forVariable(variable), INITS);
    }

    public QImage(Path<? extends Image> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QImage(PathMetadata metadata, PathInits inits) {
        this(Image.class, metadata, inits);
    }

    public QImage(Class<? extends Image> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.club = inits.isInitialized("club") ? new youngpeople.aliali.entity.club.QClub(forProperty("club"), inits.get("club")) : null;
        this.post = inits.isInitialized("post") ? new youngpeople.aliali.entity.club.QPost(forProperty("post"), inits.get("post")) : null;
        this.recruitment = inits.isInitialized("recruitment") ? new youngpeople.aliali.entity.club.QRecruitment(forProperty("recruitment"), inits.get("recruitment")) : null;
    }

}

