import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TestMainInterface {
    // Useful constants.
    private static final String dbPath = "testMainInterface/databases/db.txt";
    private static final String histPath = "testMainInterface/history";
    private static final String inputPath = "testMainInterface/inputs";
    private static final String outputPath = "testMainInterface/outputs";
    private static ArrayList<String> messageIDs = new ArrayList<String>();
    public static void main(String[] args) throws IOException {
        System.out.println("Test " + (test0() ? "Passed" : "Failed"));
        String[] args2 = new String[] {"/bin/bash", "-c", "java MainInterface < testcase.txt", "with", "args"};
        Process proc = new ProcessBuilder(args2).start();
    }
    public static boolean test0() {
        // tests construction.
        return false;
    }
}