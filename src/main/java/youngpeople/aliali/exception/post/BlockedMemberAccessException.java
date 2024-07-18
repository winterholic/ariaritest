package youngpeople.aliali.exception.post;

public class BlockedMemberAccessException extends RuntimeException{
    public static final Integer STATUS_CODE = 490;
    public static final String MESSAGE = "A blocked user has accessed the post.";
}
