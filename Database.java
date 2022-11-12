import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidAlgorithmParameterException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * This class handles all database manipulations.
 */
public class Database {
    private String databasePath;
    private ArrayList<HashMap<String, String>> database;
    private final String DATABASE_SPLIT = "###";
    private final String[] KEYS = {"id", "email", "password", "role", "lastOnline", "blocked"};
    private final String BLOCKED_SPLIT_STRING = ",";
    private Random random;

    public Database(String path) {
        databasePath = path;
        database = readFromDatabase(path);
        random = new Random();
    }

    //Returns everything in the user database into an arraylist of HashMaps
    private ArrayList<HashMap<String, String>> readFromDatabase(String path) {
        File f = new File(path);
        try {
            f.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (BufferedReader bfr = new BufferedReader(new FileReader(f))) {
            ArrayList<HashMap<String, String>> userList = new ArrayList<HashMap<String, String>>();
            String line;
            while((line = bfr.readLine()) != null) {
                userList.add(getDatabaseEntry(line));
            }
            if (userList.isEmpty()) {
                return new ArrayList<HashMap<String, String>>();
            } else {
                return userList;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Returns a HashMap object by splitting the string from the database
    private HashMap<String, String> getDatabaseEntry(String userString) {
        HashMap<String, String> map = new HashMap<String, String>();
        String[] lineArray = userString.split(DATABASE_SPLIT);
        for (int i = 0; i < KEYS.length; i++) {
            map.put(KEYS[i], lineArray[i]);
        }
        return map;
    }

    public ArrayList<HashMap<String, String>> getSelection(String key, String criteria) {
        ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
        for (HashMap<String, String> entry: database) {
            if (entry.get(key) == criteria || criteria.equals("all")) {
                results.add(entry);
            }
        }
        return results;
    }

    public void add(String name, String password, Role role) throws InvalidUserException {
        if (get("email", name) != null) {
            throw new InvalidUserException("That email is already registered");
        } else if (!validate(name, KEYS[1])) {
            throw new InvalidUserException("Your email cannot include special characters");
        } else if (!validate(password, KEYS[2])) {
            throw new InvalidUserException("Your password cannot include special characters");
        }
        String id = "";
        do {
            for (int i = 0; i < 14; i++) {
                int num = random.nextInt(62);
                if (num < 26) {
                    id += (char)(num + 'A');
                } else if (num < 52) {
                    id += (char)(num - 26 + 'a');
                } else {
                    id += (char)(num - 52 + '0');
                }
            }
        } while (get("id", id) != null);
        String[] tokens = {id, name, password, role.toString(), Instant.now().toString(), "null", "null"};
        String line = String.join(DATABASE_SPLIT, tokens);
        database.add(getDatabaseEntry(line));
    }

    public void remove(String name) throws InvalidUserException {
        HashMap<String, String> toBeRemoved = get("email", name);
        if (toBeRemoved == null) {
            throw new InvalidUserException("That email does not exist");
        }
        database.remove(toBeRemoved);
    }

    public void block(String name, String emailToBlock, boolean invisible) throws InvalidUserException {
        HashMap<String, String> changeInfo = get("email", name);
        if (changeInfo == null) {
            throw new InvalidUserException("That email does not exist");
        }
        HashMap<String, String> blockedUser = get("email", emailToBlock);
        if (blockedUser == null) {
            throw new InvalidUserException("The user you want to block does not exist");
        }
        ArrayList<String> blockedUsers;
        if (invisible) {
            blockedUsers = new ArrayList<String>(
                Arrays.asList(blockedUser.get("invisible").split(","))
            );
        } else {
            blockedUsers = new ArrayList<String>(
                Arrays.asList(blockedUser.get("blocked").split(","))
            );
        }

        if (blockedUsers.size() == 1 && blockedUsers.get(0).equals("null")) {
            blockedUsers = new ArrayList<String>();
        }
        if (blockedUsers.contains(blockedUser.get("id"))) {
            throw new InvalidUserException("You already blocked that user");
        }
        blockedUsers.add(blockedUser.get("id"));
        if (invisible) {
            changeInfo.put("invisible", String.join(",", blockedUsers));
        } else {
            changeInfo.put("blocked", String.join(",", blockedUsers));
        }
    }

    public void unblock(String name, String emailToUnblock, boolean invisible) throws InvalidUserException {
        HashMap<String, String> blocker = get("email", name);
        HashMap<String, String> blocked = get("email", emailToUnblock);
        if (blocker == null || blocked == null) {
            throw new InvalidUserException("That email does not exist");
        }
        ArrayList<String> blockedUsers;
        if (invisible) {
            blockedUsers = new ArrayList<String>(
                Arrays.asList(blocker.get("invisible").split(","))
            );
        } else {
            blockedUsers = new ArrayList<String>(
                Arrays.asList(blocker.get("blocked").split(","))
            );
        }
        boolean removed = blockedUsers.remove(blocked.get("id"));
        if (removed) {
            try {
                if (blockedUsers.isEmpty()) {
                    blockedUsers = null;
                }
                if (invisible) {
                    modify(name, "invisible", String.join(",", blockedUsers));
                } else {
                    modify(name, "blocked", String.join(",", blockedUsers));
                }
            } catch (InvalidKeyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else {
            throw new InvalidUserException("You are not blocking that user");
        }
    }

    // Returns whether the password given for a user was the correct password
    public boolean verify(String name, String password) {
        HashMap<String, String> user = get("email", name);
        if (user != null) {
            String databasePassword = user.get("password");
            return databasePassword.equals(password);
        } else {
            return false;
        }
    }
    // Searches through the database and pulls out a user's information, represented in a HashMap
    public HashMap<String, String> get(String key, String val) {
        if (!validateKey(key)) {
            return null;
        }
        for (HashMap<String, String> user : database) {
            if (user.get(key) != null && user.get(key).equals(val)) {
                return user;
            }
        }
        return null;
    }

    public void modify(String email, String key, String val) throws InvalidUserException,
        InvalidKeyException {
        if (!validateKey(key)) {
            throw new InvalidKeyException(String.format("Invalid Key: {%s}", key));
        }
        if (!validate(val, key)) {
            throw new InvalidUserException("That is not a valid email");
        }
        for (int i = 0; i < database.size(); i++) {
            HashMap<String, String> user = database.get(i);
            if (user.get("email").equals(email)) {
                user.put(key, val);
                database.set(i, user);
                return;
            }
        }
        throw new InvalidUserException("User does not exist!");
    }

    //Checks to make sure a string that will go to the database has NO SPECIAL CHARACTERS
    private boolean validate(String str, String key) {
        if (key == KEYS[1]) {
            return str.matches("^[A-Za-z0-9\\-\\._]{1,64}[^.]@[A-Za-z0-9]+\\.[a-z]{3}$");
        } else if (key == KEYS[2]) {
            return str.matches("^[A-Za-z0-9]+$");
        }
        return true;
    }

    private boolean validateKey(String key) {
        boolean keyExists = false;
        for (String k: KEYS) {
            if (k == key) {
                keyExists = true;
                break;
            }
        }
        return keyExists;
    }

    public void save() {
        try {
            File file = new File(databasePath);
            file.createNewFile();
            PrintWriter pw = new PrintWriter(file);
            String output = toString();
            pw.write(output.strip());
            pw.flush();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        String output = "";
        for (HashMap<String, String> user : database) {
            ArrayList<String> strings = new ArrayList<String>();
            for (String key : KEYS) {
                strings.add(user.get(key));
            }
            output += "\n" + String.join(DATABASE_SPLIT, strings);
        }
        return output;
    }
}