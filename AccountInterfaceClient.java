/**
 * Project 5 ->AccountInterfaceClient
 *
 * Interface class that connects with the AccountGUI.
 *
 * @author Atharva Gupta, Cyril Sharma, Josh George, Nitin Murthy, Jacob Choi, L11
 *
 * @version December 10, 2022
 *
 */
public class AccountInterfaceClient {
    private Translator translator;
    public AccountInterfaceClient() {
        translator = new Translator();
    }

    /**
     * deletes the account of the user
     *
     * @param email email of the user
     */
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

    /**
     * modifies the password of the user
     *
     * @param email email of user
     * @param newPassword of the user
     */
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

    /**
     * modifies the username of user
     *
     * @param email of the user
     * @param newUsername of the user
     */
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

    /**
     * logs the user out of the account
     *
     */
    public void logout() throws Exception {
        translator.query(new Query(null, "logout"));
    }
}