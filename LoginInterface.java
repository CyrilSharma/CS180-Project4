public class LoginInterface {
    
    Translator translator;

    public LoginInterface() {
        translator = new Translator();
    }

    public boolean verify(String email, String password) {
        return ((Boolean) translator.query(new Query("Database", "verify", new String[]{email, password}))).booleanValue();
    }

}
