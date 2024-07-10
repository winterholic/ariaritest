package youngpeople.aliali.exception.token;

public class NotAuthenticatedException extends RuntimeException {

    public static final Integer STATUS_CODE = 401;
    public static final String MESSAGE = "The path requires token. There is on token.";

}
