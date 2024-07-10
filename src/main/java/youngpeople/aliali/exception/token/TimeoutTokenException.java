package youngpeople.aliali.exception.token;

public class TimeoutTokenException extends RuntimeException {

    public static final Integer STATUS_CODE = 461;
    public static final String MESSAGE = "The token is expired.";

}
