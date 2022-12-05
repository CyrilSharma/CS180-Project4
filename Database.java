import java.io.*;
import java.time.Instant;
import java.util.*;
import java.lang.reflect.*;

/**
 * Project 4 -> Database
 *
 * class handles all the database manipulations
 *
 * @author Atharva Gupta, Cyril Sharma, Josh George, Nitin Murthy, Jacob Choi, L11
 *
 * @version November 13, 2022
 *
 */
public class Database {
    private String databasePath;
    private ArrayList<HashMap<String, String>> database;
    private final String databaseSplit = "###";
    private final String[] keys = {"id", "email", "password", "role", "lastOnline", "blocked", "invisible"};
    private Random random;

    /**
     * initializes the instance field variables
     *
     * @param path of the database
     * @throws Exception
     */
    public Database() throws Exception {
        databasePath = String.format("%s/Database.txt", PathManager.storeDir);
        database = readFromDatabase(databasePath);
        random = new Random();
    }

    /**
     * returns everything in the user database into an arraylist of HashMaps
     *
     * @param path of the fileName
     * @return ArrayList<HashMap<String, String>> userlist
     */
    private synchronized ArrayList<HashMap<String, String>> readFromDatabase(String path) throws Exception {
        File f = new File(path);
        try {
            f.createNewFile();
        } catch (Exception e) {
            throw new Exception("An error has occurred reading from the database");
        }
        try (BufferedReader bfr = new BufferedReader(new FileReader(f))) {
            ArrayList<HashMap<String, String>> userList = new ArrayList<HashMap<String, String>>();
            String line;
            while ((line = bfr.readLine()) != null) {
                userList.add(getDatabaseEntry(line));
            }
            if (userList.isEmpty()) {
                return new ArrayList<HashMap<String, String>>();
            } else {
                return userList;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * returns a HashMap object by splitting the string from the database
     *
     * @param userString
     * @return a HashMap<String, String></String,>
     */
    private HashMap<String, String> getDatabaseEntry(String userString) {
        HashMap<String, String> map = new HashMap<String, String>();
        String[] lineArray = userString.split(databaseSplit);
        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i], lineArray[i]);
        }
        return map;
    }

    /**
     * returns the ArrayList from the database where the key equals the criteria
     *
     * @param key email of the user
     * @param criteria object passed in
     * @return ArrayList<HashMap<String, String>>
     */
    public ArrayList<HashMap<String, String>> getSelection(String key, String criteria) {
        ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
        for (HashMap<String, String> entry: database) {
            if (entry.get(key) == criteria || entry.get(key).equals(criteria) || criteria.equals("all")) {
                results.add(entry);
            }
        }
        return results;
    }

    /**
     * adds a newly created user to the database arraylist
     *
     * @param name
     * @param password
     * @param role
     */
    public synchronized void add(String name, String password, Role role) throws InvalidUserException {
        if (get("email", name) != null) {
            throw new InvalidUserException("That email is already registered");
        } else if (!validate(name, keys[1])) {
            throw new InvalidUserException("You must enter a valid email");
        } else if (!validate(password, keys[2])) {
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
        String line = String.join(databaseSplit, tokens);
        database.add(getDatabaseEntry(line));
        save();
    }

    /**
     * removes the user from the database arraylist
     *
     * @param name
     */
    public synchronized void remove(String name) throws InvalidUserException, InvalidKeyException {
        HashMap<String, String> toBeRemoved = get("email", name);
        if (toBeRemoved == null) {
            throw new InvalidUserException("That email does not exist");
        }
        try {
            modify(name, "role", Role.Deleted.toString());
        } catch (InvalidKeyException e) {
            throw new InvalidKeyException("Something went wrong trying to delete your account");
        }
        save();
    }

    /**
     * blocks the user and sets the user as invisible in the
     * database arraylis
     *
     * @param name
     * @param emailToBlock
     * @param invisible
     */
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
                Arrays.asList(changeInfo.get("invisible").split(","))
            );
        } else {
            blockedUsers = new ArrayList<String>(
                Arrays.asList(changeInfo.get("blocked").split(","))
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
        save();
    }

    /**
     * unblocks the user from the arraylist database
     *
     * @param name
     * @param emailToUnblock
     * @param invisible
     */
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
            if (blockedUsers.isEmpty()) {
                blockedUsers.add("null");
            }
            if (invisible) {
                blocker.put("invisible", String.join(",", blockedUsers));
            } else {
                blocker.put("blocked", String.join(",", blockedUsers));
            }
        } else {
            throw new InvalidUserException("You are not blocking that user");
        }
        save();
    }

    // Returns whether the password given for a user was the correct password
    /**
     * returns whether the password given for a user was the correct password
     *
     * @param name
     * @param password
     * @return true or false
     */
    public boolean verify(String name, String password) {
        HashMap<String, String> user = get("email", name);
        if (user != null && !user.get("role").equals(Role.Deleted.toString())) {
            String databasePassword = user.get("password");
            return databasePassword.equals(password);
        } else {
            return false;
        }
    }

    /**
     * Searches through the database and pulls out a user's information
     * represented in a HashMap
     *
     * @param key
     * @param val
     * @return HashMap<String, String>
     */
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

    /**
     * modifies the account
     *
     * @param email
     * @param key
     * @param val
     */
    public synchronized void modify(String email, String key, String val) throws InvalidUserException,
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
                save();
                return;
            }
        }
        throw new InvalidUserException("User does not exist!");
    }

    /**
     * Checks to make sure a string that will go to the database has NO SPECIAL CHARACTERS
     *
     * @param str
     * @param key
     * @return if the string is valid
     */
    public boolean validate(String str, String key) {
        if (key == keys[1]) {
            return str.matches("^[A-Za-z0-9\\-\\._]{1,64}[^.]@[A-Za-z0-9]+\\.[a-z]{3}$");
        } else if (key == keys[2]) {
            return str.matches("^[A-Za-z0-9]+$");
        }
        return true;
    }

    /**
     * Checks to see if the key exists
     *
     * @param key
     * @return true or false is the key exists
     */
    private boolean validateKey(String key) {
        boolean keyExists = false;
        for (String k: keys) {
            if (k.equals(key)) {
                keyExists = true;
                break;
            }
        }
        return keyExists;
    }

    /**
     * saves the file
     */
    public synchronized void save() {
        try {
            File file = new File(databasePath);
            file.createNewFile();
            PrintWriter pw = new PrintWriter(file);
            String output = toString();
            pw.write(output.strip());
            pw.flush();
            pw.close();
        } catch (Exception e) {
            return;
        }
    }

    /**
     * formats the string based on the toString
     *
     * @param key
     * @return String
     */
    public String toString() {
        String output = "";
        for (HashMap<String, String> user : database) {
            ArrayList<String> strings = new ArrayList<String>();
            for (String key : keys) {
                strings.add(user.get(key));
            }
            output += "\n" + String.join(databaseSplit, strings);
        }
        return output;
    }
}