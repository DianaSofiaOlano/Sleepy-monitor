package exceptions;

public class TutorAlreadyAwakeException extends RuntimeException{
    public TutorAlreadyAwakeException(){
        super("Tutor is already awake!");
    }
}
