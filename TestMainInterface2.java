import java.io.*;
import java.util.*;
/**
 * Project 4 -> TestMainInterface2
 *
 * class handles the testing of main interface
 *
 * @author Atharva Gupta, Cyril Sharma, Josh George, Nitin Murthy, Jacob Choi, L11
 *
 * @version November 13, 2022
 *
 */
public class TestMainInterface2 {
    // Useful constants.
    private static final String dbPath = "UserDatabase.txt";
    private static final String storePath = "Stores.txt";
    private static final String inputPath = "testMainInterface2/inputs.txt";
    private static final String outputPath = "testMainInterface2/outputs.txt";
    private static final String expectedPath = "testMainInterface2/expected.txt";
    private static ArrayList<String> messageIDs = new ArrayList<String>();
    public static void main(String[] args) throws IOException {
        String[] args2 = null;
        if (System.getProperty("os.name").contains("Windows")) {
            args2 = new String[] {"cmd.exe", "/c", "java MainInterface < testMainInterface2/inputs.txt > testMainInterface2/outputs.txt", "with", "args"};
        } else {
            args2 = new String[] {"/bin/bash", "-c", "java MainInterface < testMainInterface2/inputs.txt > testMainInterface2/outputs.txt", "with", "args"};
        }
        Process proc = new ProcessBuilder(args2).start();
        while(proc.isAlive()) {

        }
        System.out.println("Test " + (test0() ? "Passed" : "Failed"));
    }
    public static boolean test0() {
        // tests construction.
        try {
            boolean passedTest = true;
            BufferedReader bfr = new BufferedReader(new FileReader(new File(outputPath)));
            BufferedReader bfr2 = new BufferedReader(new FileReader(new File(expectedPath)));
            String line;
            String line2;
            while ((line = bfr.readLine()) != null && (line2 = bfr2.readLine()) != null) {
                if (!line.strip().equals(line2.strip())) {
                    System.out.println("Comparison Failure: " + "<" + line.strip() + "> " + "<" + line2.strip() + ">");
                    passedTest = false;
                }
            }
            bfr.close();
            bfr2.close();
            File file1 = new File(dbPath);
            File file2 = new File(storePath);
            file1.delete();
            file1.createNewFile();
            file2.delete();
            file2.createNewFile();
            return passedTest;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
}