package youngpeople.aliali.exception.post;

public class ModifyingPostAutorityException extends RuntimeException{
    public static final Integer STATUS_CODE = 490;
    public static final String MESSAGE = "There is no permission to modify this entity.";
}
