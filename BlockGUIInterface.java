import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BlockGUIInterface {
    
    HashMap<String, String> user;
    Translator translator;
    
    public BlockGUIInterface() throws Exception {
        translator = new Translator();
        user = translator.get("id", (String) translator.query(new Query("User", "getID")));
    }

    public Translator geTranslator() {
        return translator;
    }

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

    public ArrayList<String> getAllUsers() throws Exception {
        ArrayList<String> blockedUsers = getUsers("blocked");
        ArrayList<String> invisibleUsers = getUsers("invisible");
        invisibleUsers.removeAll(blockedUsers);
        blockedUsers.addAll(invisibleUsers);
        return blockedUsers;
    }

    public void blockUser(String email, boolean invisible) throws Exception {
        translator.query(new Query("Database", "block", new Object[]{user.get("email"), email, invisible}));
    }

    public void unblockUser(String email, boolean invisible) throws Exception {
        translator.query(new Query("Database", "unblock", new Object[]{user.get("email"), email, invisible}));
    }
}
