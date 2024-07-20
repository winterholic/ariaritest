package youngpeople.aliali.exception.block;

public class BlockedMemberAccessException extends RuntimeException{ // block으로 옮겨가야할듯
    public static final Integer STATUS_CODE = 490;
    public static final String MESSAGE = "A blocked user has accessed the post.";
}
