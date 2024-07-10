package youngpeople.aliali.exception.member;

public class AlreadyAuthException extends RuntimeException {

    public static final Integer STATUS_CODE = 485;
    public static final String MESSAGE = "School auth is already authenticated";

}
