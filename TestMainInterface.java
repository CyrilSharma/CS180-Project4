import java.io.*;
import java.util.*;

public class TestMainInterface {
    // Useful constants.
    private static final String dbPath = "testMainInterface/databases/db.txt";
    private static final String histPath = "testMainInterface/history";
    private static final String inputPath = "testMainInterface/inputs";
    private static final String outputPath = "testMainInterface/outputs.txt";
    private static ArrayList<String> messageIDs = new ArrayList<String>();
    public static void main(String[] args) throws IOException {
        System.out.println("Test " + (test0() ? "Passed" : "Failed"));
        String[] args2 = null;
        if (System.getProperty("os.name").equals("Windows 10")) {
            args2 = new String[] {"cmd.exe", "/c", "java MainInterface < testMainInterface/inputs.txt > testMainInterface/outputs.txt", "with", "args"};
        } else {
            args2 = new String[] {"/bin/bash", "-c", "java MainInterface < testMainInterface/inputs.txt > testMainInterface/outputs.txt", "with", "args"};
        }
        Process proc = new ProcessBuilder(args2).start();
    }
    public static boolean test0() {
        // tests construction.
        return false;
    }
}