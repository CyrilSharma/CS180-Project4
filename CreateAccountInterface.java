public class CreateAccountInterface {
    
    Translator translator;

    public CreateAccountInterface() {
        translator = new Translator();
    }

    public void add(String email, String password, String role) throws InvalidUserException {
        translator.query(new Query("Database", "add", new String[]{email, password, role}));
    }
}
