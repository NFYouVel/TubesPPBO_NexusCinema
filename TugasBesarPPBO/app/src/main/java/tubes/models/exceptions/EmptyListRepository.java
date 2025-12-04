package tubes.models.exceptions;

public class EmptyListRepository extends Exception{
    public EmptyListRepository(String message) {
        super("The list of " + message + " is empty!");
    }
}
