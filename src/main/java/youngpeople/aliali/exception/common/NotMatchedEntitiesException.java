package youngpeople.aliali.exception.common;

public class NotMatchedEntitiesException extends RuntimeException {

    /**
     * NotMatchedEntitesException.MESSAGE + message는 어떰?
     */
//    public NotMatchedEntitiesException(String message) {
//        super(message);
//    }

    public static final Integer STATUS_CODE = 472;
    public static final String MESSAGE = "Not matched entities.";

}
