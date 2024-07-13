package youngpeople.aliali.exception.apply;

public class NotPendencyApplyException extends RuntimeException {

    public static final Integer STATUS_CODE = 488;
    public static final String MESSAGE = "State of apply is not PENDENCY.";

}
