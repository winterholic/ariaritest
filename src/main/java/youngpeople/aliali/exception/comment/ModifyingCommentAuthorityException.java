package youngpeople.aliali.exception.comment;

public class ModifyingCommentAuthorityException extends RuntimeException {
    public static final Integer STATUS_CODE = 490;
    public static final String MESSAGE = "There is no permission to modify this entity.";
}
