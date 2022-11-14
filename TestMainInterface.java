import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Project 4 -> TestMainInterface
 *
 * class handles the testing of main interface
 *
 * @author Atharva Gupta, Cyril Sharma, Josh George, Nitin Murthy, Jacob Choi, L11
 *
 * @version November 13, 2022
 *
 */
public class TestMainInterface {
    public static void main(String[] args) throws IOException {
        File dir = new File("testMainInterface/inputs");
        File[] files = dir.listFiles();
        String[] filenames = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            filenames[i] = files[i].getName();
        }

        for (String name: filenames) {
            String[] commandArgs = null;
            if (System.getProperty("os.name").equals("Windows 10")) {
                commandArgs = new String[] {"cmd.exe", "/c", "java MainInterface < testMainInterface/inputs/%s > testMainInterface/outputs/%s", "with", "args"};
            } else {
                commandArgs = new String[] {"/bin/bash", "-c", String.format("java MainInterface < testMainInterface/inputs/%s > testMainInterface/outputs/%s", name, name), "with", "args"};
            }
            Process proc = new ProcessBuilder(commandArgs).start();
            Path inputPath = new File(String.format("testMainInterface/outputs/%s", name)).toPath();
            Path expectedPath = new File(String.format("testMainInterface/expected/%s", name)).toPath();
            boolean success = Files.mismatch(inputPath, expectedPath) == -1;
            System.out.printf("Test %s %s\n", name.substring(0, name.length() - 4),
                success ? "passed." : "failed.");
        }
    }
}