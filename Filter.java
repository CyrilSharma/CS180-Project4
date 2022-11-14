import java.io.*;
import java.util.*;
/**
 * Project 4 -> Filter
 *
 * Allows user to filter out certain words in their conversations and replace with auto-generated asterisks or
 * user-defined phrase(s)
 *
 * @author Atharva Gupta, Cyril Sharma, Josh George, Nitin Murthy, Jacob Choi, L11
 *
 * @version November 13, 2022
 *
 */
public class Filter {
    private File f;
    private Database db;
    private HashMap<String, String> map;
    private ArrayList<String[]> userWordList;

    //using one txt file that has all users' blocked/filtered words
    public Filter(String email) {
        db = new Database("UserDatabase.txt");
        map = db.get("email", email);
        f = new File("UserFilter.txt");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (Exception e) {
                System.out.println("Error occured while trying to create a file!");
                e.printStackTrace();
            }
        }
        userWordList = new ArrayList<>();
        read();
    }
    /**
     * read UserFilter.txt and bring the content into the list
     */
    public void read() {
        String splitter = "::";
        FileReader fr;
        BufferedReader bfr;
        try {
            fr = new FileReader(f);
            bfr = new BufferedReader(fr);
            while(true) {
                String line = bfr.readLine();
                if (line == null) {
                    break;
                }
                String[] users = line.split(splitter);
                userWordList.add(users);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Database Error!");
        }
    }
    /**
     * get list of filtered words
     * @return result, arraylist of filtered words
     */
    //
    public ArrayList<String> get() {
        String[] words = new String[0];
        for (String[] userData: userWordList) {
            if (userData[0].equals(map.get("id"))) {
                words = userData[1].split(";");
            }
        }
        ArrayList<String> result = new ArrayList<>(Arrays.asList(words));
        return result;
    }

    //
    /**
     * remove special characters from word
     * @param word
     * @return
     */
    public String removeSpecialChar(String word) {
        String result = "";
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) > 64 && word.charAt(i) <= 122) {
                result = result + word.charAt(i);
            }
        }
        return result;
    }

    /**
     * checks the line and if the line has the filtered word, replace it with * and return the modified line
     * @param line
     * @return line, modified
     */
    public String filter(String line) {
        ArrayList<String> list = get();
        for (String word: list) {
            String temp = "";
            while(line.toLowerCase().indexOf(word) != -1) {
                temp = line.substring(0, line.toLowerCase().indexOf(word));
                temp += createFilteredWord(word.length());
                temp += line.substring(line.toLowerCase().indexOf(word) + word.length());
                line = temp;
            }
        }
        return line;
    }

    /**
     * get str composed of * based on length
     * @param size (int)
     * @return word, filtered & replaced w/ *'s
     */
    public String createFilteredWord(int size) {
        String word = "";
        for (int i = 0; i < size; i++) {
            word += "*";
        }
        return word;
    }

    /**
     * turn filtered list into format in txt file
     * @param words (arraylist of strings)
     * @return str
     */
    public String toTxtFileFormat(ArrayList<String> words) {
        String str = "";
        for (int i = 0; i < words.size(); i++) {
            str += words.get(i);
            if (i != words.size() - 1) {
                str += ";";
            }
        }
        return str;
    }

    /**
     * remove word from filter and txt file
     * @param word (string)
     */
    public void remove(String word) throws InvalidWordException{
        ArrayList<String> words = get();
        if (!words.contains(word)) {
            throw new InvalidWordException("The word does not exist in the current filter!");
        }
        words.remove(word);
        String id = map.get("id");
        String newLine = toTxtFileFormat(words);
        int index = 0;
        for (String[] user: userWordList) {
            if (user[0].equals(id)) {
                user[1] = newLine;
                userWordList.set(index, user);
            }
            index++;
        }
        write();
    }

    /**
     * add word to the filter and txt file
     * @param word
    */
    public void add(String word) throws InvalidWordException {
        ArrayList<String> words = get();
        if (words.contains(word)) {
            throw new InvalidWordException("The word already exists in the filter");
        }
        words.add(word);
        String id = map.get("id");
        String newLine = toTxtFileFormat(words);
        int index = 0;
        for (String[] user: userWordList) {
            if (user[0].equals(id)) {
                user[1] = newLine;
                userWordList.set(index, user);
            }
            index++;
        }
        write();
    }

    /**
     * write file
     */
    public void write() {
        FileOutputStream fos;
        PrintWriter pw;
        try {
            fos = new FileOutputStream(f, false);
            pw = new PrintWriter(fos);
            for (String[] data: userWordList) {
                ArrayList<String> list = new ArrayList<String>(Arrays.asList(data[1]));
                String line = data[0] + "::" + toTxtFileFormat(list);
                pw.println(line);
            }
            pw.close();

        } catch (Exception e) {
            System.out.println("Exception occurred!!");
            e.printStackTrace();
        }
    }
    /**
     * toString format
     */
    public String toString() {
        String format = "words: ";
        ArrayList<String> list = get();
        int index = 0;
        for (String word: list) {
            format += word;
            if (index != list.size() - 1) {
                format +=  ", ";
            } else {
                format += "\n";
            }
            index++;
        }
        return format;
    }
    /**
     * UI for user to utilize Filter class
     */
    public void presentFilterMenu(Scanner sc) {
        String menu = "what do you want to do?\n" +
                    "1. see filtered words\n" +
                    "2. add word to the filter\n" +
                    "3. remove the word from filter\n" +
                    "4. quit";
        System.out.println(menu);
        boolean ongoing = true;
        while(ongoing) {
            String option = sc.nextLine();
            switch (option) {
                case "1":
                    //TODO:FIX THIS
                    System.out.println(toString());
                    break;
                case "2":
                    System.out.println("please enter a word to be filtered");
                    String word = sc.nextLine();
                    try {
                        add(word);
                        System.out.println("Successfully added a word!");
                        System.out.println(toString());
                    } catch (Exception e) {
                        System.out.println("Error while trying to add a word");
                        e.printStackTrace();

                    }
                    break;
                case "3":
                    System.out.println("please enter a word to remove from filter");
                    String wor = sc.nextLine();
                    try {
                        remove(wor);
                        System.out.println("Successfully removed a word!");
                        System.out.println(toString());
                    } catch (Exception e) {
                        System.out.println("Error while trying to remove a word");
                        e.printStackTrace();
                    }
                    break;
                case "4":
                    ongoing = false;
                    break;
                default:
                    System.out.println("Invalid input");
                    break;
            }
        }
    }
}
