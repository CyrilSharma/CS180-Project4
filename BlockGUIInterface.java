import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
/**
 * Project 5 -> BlockGUIInterface
 *
 * Interface class that connects with the AccountGUI.
 *
 * @author Atharva Gupta, Cyril Sharma, Josh George, Nitin Murthy, Jacob Choi, L11
 *
 * @version December 10, 2022
 *
 */
public class BlockGUIInterface {
    
    HashMap<String, String> user;
    Translator translator;
    
    public BlockGUIInterface() throws Exception {
        translator = new Translator();
        user = translator.get("id", (String) translator.query(new Query("User", "getID")));
    }
    /**
     * returns the translator instance field
     *
     * @return Translator object
     */
    public Translator geTranslator() {
        return translator;
    }

    /**
     * returns the list of users that will be stores in an arrayList
     *
     * @param key
     * @return ArrayList<String></String>
     */
    public ArrayList<String> getUsers(String key) throws Exception {
        if (user.get(key).equals("null")) {
            return new ArrayList<>();
        } else {
            ArrayList<String> ids = new ArrayList<>(
                Arrays.asList(user.get(key).split(",")));
            for (int i = ids.size() - 1; i >= 0; i--) {
                String id = ids.get(i);
                HashMap<String, String> person = translator.get("id", id);
                if (person.get("invisible").contains(user.get("id"))) {
                    ids.remove(i);
                } else {
                    ids.set(i, person.get("email"));
                }
            }
            return ids;
        }
    }

    /**
     * returns the users that are unblocked
     *
     * @return ArrayList<String></String>
     */
    public ArrayList<String> getUnblockedUsers() throws Exception {
        ArrayList<String> blockedUsers = getUsers("blocked");
        ArrayList<String> invisibleUsers = getUsers("invisible");
        ArrayList<String> allUsers = (ArrayList<String>) translator.query(new Query("User", "getUsers"));
        allUsers.removeAll(blockedUsers);
        allUsers.removeAll(invisibleUsers);
        return allUsers;
    }

    /**
     * returns the list of all active users that will be stores in an arrayList
     *
     * @return ArrayList<String></String>
     */
    public ArrayList<String> getAllUsers() throws Exception {
        ArrayList<String> allUsers = (ArrayList<String>) translator.query(new Query("User", "getUsers"));
        return allUsers;
    }

    /**
     * blocks the user
     *
     * @param email, invisible
     */
    public void blockUser(String email, boolean invisible) throws Exception {
        translator.query(new Query("Database", "block", new Object[]{user.get("email"), email, invisible}));
        user = translator.get("id", user.get("id"));
    }

    /**
     * unblocks the user
     *
     * @param email, invisible
     */
    public void unblockUser(String email, boolean invisible) throws Exception {
        translator.query(new Query("Database", "unblock", new Object[]{user.get("email"), email, invisible}));
        user = translator.get("id", user.get("id"));
    }
}
