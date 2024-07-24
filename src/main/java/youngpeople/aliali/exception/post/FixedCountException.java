package youngpeople.aliali.exception.post;

public class FixedCountException extends RuntimeException{
    public static final Integer STATUS_CODE = 490;
    public static final String MESSAGE = "The number of fixed posts is the maximum, so processing is impossible";
}
