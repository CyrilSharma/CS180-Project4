public class AccountInterfaceClient {
    private Translator translator;
    public AccountInterfaceClient() {
        translator = new Translator();
    }
    public String deleteAccount(String email) throws Exception {
        String object = "Database";
        String function = "remove";
        String[] args = {email};
        Object o = translator.query(new Query(object, function, args));
        if (o == null) {
            return "INVALID";
        }
        return "SUCCESS";
    }

    public String modifyPassword(String email, String newPassword) throws Exception {
        String object = "Database";
        String function = "modify";
        String[] args = {email, "password", newPassword};
        Object o = translator.query(new Query(object, function, args));
        if (o == null) {
            return "INVALID";
        }
        return "SUCCESS";
    }

    public String modifyUsername(String email, String newUsername) throws Exception {
        String object = "Database";
        String function = "modify";
        String[] args = {email, "email", newUsername};
        Object o = translator.query(new Query(object, function, args));
        if (o == null) {
            return "INVALID";
        }
        return "SUCCESS";
    }

    public void logout() throws Exception {
        translator.query(new Query(null, "logout"));
    }
}