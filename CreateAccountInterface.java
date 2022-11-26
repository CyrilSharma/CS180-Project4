import java.lang.reflect.InvocationTargetException;

public class CreateAccountInterface {
    
    Translator translator;

    public CreateAccountInterface() {
        translator = new Translator();
    }

    public boolean add(String email, String password, String role) throws InvalidUserException {
        Object o = translator.query(new Query("Database", "add", new String[]{email, password, role}));
        if (!(o instanceof Exception)) {
            return true;
        } else {
            throw new InvalidUserException(((Exception) o).getMessage());
        }
    }
}
