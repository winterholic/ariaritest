package youngpeople.aliali.exception.club;

public class ClubDeleteException extends RuntimeException {

    public static final Integer STATUS_CODE = 477;
    public static final String MESSAGE = "This club isn't able to be deleted.";

}
