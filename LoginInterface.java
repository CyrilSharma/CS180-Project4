public class LoginInterface extends Translator {
    
    public LoginInterface() {
        super();
    }

    public boolean verify(String email, String password) {
        return ((Boolean) query(new Query("Database", "verify", new String[]{email, password}))).booleanValue();
    }

}
