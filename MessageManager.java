import java.io.*;
import java.util.*;
import java.time.*;

public class MessageManager {

    private ArrayList<HashMap<String, String>> database;
    // Commas are not allowed to be used for usernames or passwords
    // in fact, no special characters are allowed
    // it makes splitting the database complicated
    private final String DATABASE_SPLIT = ";;;";
    private final String BLOCKED_SPLIT_STRING = ",";
    private Random random;

    public MessageManager() {
        database = readFromDatabase();
        random = new Random();
    }

    //blocked arraylist contains all the ids of people who are blocked by this customer
    public void add(String name, String password, Role role, ArrayList<String> blocked) throws InvalidUserException {
        if (get(name) != null) {
            throw new InvalidUserException("That username is already taken");
        }
        if (!validate(name)) {
            throw new InvalidUserException("Your username cannot include special characters");
        }
        if (!validate(password)) {
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
        } while (get(getUsername(id)) != null);
        String line = createFormattedString(id, name, password, role.toString(), Instant.now().toString(), blockedArrayListToString(blocked));
        database.add(getDatabaseEntry(line));
        writeToDatabase();
    }

    public void remove(String name) throws InvalidUserException {
        HashMap<String, String> toBeRemoved = get(name);
        if (toBeRemoved == null) {
            throw new InvalidUserException("That username does not exist");
        }
        if (database.remove(toBeRemoved)) {
            writeToDatabase();
        }
    }

    public void editName(String name, String newName) throws InvalidUserException {
        HashMap<String, String> changeInfo = get(name);
        if (changeInfo == null) {
            throw new InvalidUserException("That username does not exist");
        }
        if (get(newName) != null) {
            throw new InvalidUserException("That username is already taken");
        }
        if (!validate(newName)) {
            throw new InvalidUserException("Your username cannot include special characters");
        }
        changeInfo.put("username", newName);
        writeToDatabase();
    }

    public void editPassword(String name, String oldPassword, String newPassword) throws InvalidUserException {
        if (verify(name, oldPassword)) {
            HashMap<String, String> changeInfo = get(name);
            changeInfo.put("password", newPassword);
            writeToDatabase();
        } else {
            throw new InvalidUserException("The password you entered is not correct");
        }
    }

    public void setLastOnline(String name) throws InvalidUserException {
        HashMap<String, String> changeInfo = get(name);
        if (changeInfo == null) {
            throw new InvalidUserException("That username does not exist");
        }
        changeInfo.put("lastOnline", Instant.now().toString());
        writeToDatabase();
    }

    public void addBlocked(String name, String usernameToBlock) throws InvalidUserException {
        HashMap<String, String> changeInfo = get(name);
        if (changeInfo == null) {
            throw new InvalidUserException("That username does not exist");
        }
        HashMap<String, String> blockedUser = get(usernameToBlock);
        if (blockedUser == null) {
            throw new InvalidUserException("The user you want to block does not exist");
        }
        ArrayList<String> blockedUsers = getBlocked(name);
        blockedUsers.add(blockedUser.get("id"));
        changeInfo.put("blocked", blockedArrayListToString(blockedUsers));
        writeToDatabase();
    }

    public void removeBlocked(String name, String usernameToUnblock) throws InvalidUserException {
        HashMap<String, String> changeInfo = get(name);
        if (changeInfo == null) {
            throw new InvalidUserException("That username does not exist");
        }
        HashMap<String, String> blockedUser = get(usernameToUnblock);
        if (blockedUser == null) {
            throw new InvalidUserException("The user you want to unblock does not exist");
        }
        ArrayList<String> blockedUsers = getBlocked(name);
        if (blockedUsers.remove(blockedUser.get("id"))) {
            writeToDatabase();
        } else {
            throw new InvalidUserException("You were not blocking that user");
        }
    }

    private HashMap<String, String> get(String name) {
        for (HashMap<String, String> user : database) {
            if (user.get("username").equals(name)) {
                return user;
            }
        }
        return null;
    }

    public String getUsername(String id) {
        for (HashMap<String, String> user : database) {
            if (user.get("id").equals(id)) {
                return user.get("username");
            }
        }
        return null;
    }

    //Definitely unnecessary but it completes the set
    public String getName(String username) {
        HashMap<String, String> user = get(username);
        if (user == null) {
            return null;
        }
        return user.get("username");
    }

    private String getPassword(String username) {
        HashMap<String, String> user = get(username);
        if (user == null) {
            return null;
        }
        return user.get("password");
    }

    public String getID(String username) {
        HashMap<String, String> user = get(username);
        if (user == null) {
            return null;
        }
        return user.get("id");
    }

    public String getRole(String username) {
        HashMap<String, String> user = get(username);
        if (user == null) {
            return null;
        }
        return user.get("role");
    }

    public Instant getLastOnline(String username) {
        HashMap<String, String> user = get(username);
        if (user == null) {
            return null;
        }
        return Instant.parse(user.get("lastOnline"));
    }

    public ArrayList<String> getBlocked(String username) {
        HashMap<String, String> user = get(username);
        if (user == null) {
            return null;
        }
        return blockedStringToArrayList(user.get("blocked"));
    }

    public ArrayList<String> blockedStringToArrayList(String blocked) {
        if (blocked == null || blocked.equals("")) {
            return new ArrayList<String>();
        }
        ArrayList<String> blockedList = new ArrayList<String>();
        blockedList.addAll(Arrays.asList(blocked.split(BLOCKED_SPLIT_STRING)));
        return blockedList;
    }

    public String blockedArrayListToString(ArrayList<String> blockedList) {
        String blocked = "";
        if (blockedList == null) {
            return null;
        }
        for (String item : blockedList) {
            blocked += item + BLOCKED_SPLIT_STRING;
        }
        blocked = blocked.substring(0, blocked.lastIndexOf(BLOCKED_SPLIT_STRING));
        return blocked;
    }

    //Returns everything in the user database into an arraylist of HashMaps
    private ArrayList<HashMap<String, String>> readFromDatabase() {
        try (BufferedReader bfr = new BufferedReader(new FileReader(new File("UserDatabase.txt")))) {
            ArrayList<HashMap<String, String>> userList = new ArrayList<HashMap<String, String>>();
            String line;
            while((line = bfr.readLine()) != null) {
                userList.add(getDatabaseEntry(line));
            }
            if (userList.size() == 0) {
                return new ArrayList<HashMap<String, String>>();
            } else {
                return userList;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Returns everything in the user database into a HashMap
    private HashMap<String, String> readFromDatabase(String username) {
        try (BufferedReader bfr = new BufferedReader(new FileReader(new File("UserDatabase.txt")))) {
            String line;
            while((line = bfr.readLine()) != null) {
                HashMap<String, String> userLine = getDatabaseEntry(line);
                if (userLine.get("username").equals(username)) {
                    return userLine;
                }
            }
            return null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String createFormattedString(String id, String username, String password, String role, String lastOnline, String blocked) {
        return id + DATABASE_SPLIT + username + DATABASE_SPLIT + password + DATABASE_SPLIT + role + DATABASE_SPLIT + lastOnline + DATABASE_SPLIT + blocked;
    }

    //Checks to make sure a string that will go to the database has the right characters
    private boolean validate(String str) {
        return str.matches("^[A-Za-z0-9]+$");
    }

    public boolean verify(String name, String password) throws InvalidUserException {
        String databasePassword = getPassword(name);
        if (databasePassword == null) {
            throw new InvalidUserException("That username does not exist");
        }
        return databasePassword.equals(password);
    }

    private HashMap<String, String> getDatabaseEntry(String userString) {
        HashMap<String, String> map = new HashMap<String, String>();
        String[] lineArray = userString.split(DATABASE_SPLIT);
        map.put("id", lineArray[0]);
        map.put("username", lineArray[1]);
        map.put("password", lineArray[2]);
        map.put("role", lineArray[3]);
        map.put("lastOnline", lineArray[4]);
        map.put("blocked", lineArray[5]);
        return map;
    }

    private void writeToDatabase() {
        try {
            File file = new File("UserDatabase.txt");
            file.createNewFile();
            PrintWriter pw = new PrintWriter(file);
            String string = "";
            if (database != null && database.size() > 0){
                string = toString();
            }
            pw.write(string.strip());
            pw.flush();
            pw.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String toString() {
        String string = "";
        for (HashMap<String, String> userInfo : database) {
            string += createFormattedString(userInfo.get("id"), userInfo.get("username"), userInfo.get("password"), userInfo.get("role"), userInfo.get("lastOnline"), userInfo.get("blocked")) + "\n";
        }
        return string;
    }
}