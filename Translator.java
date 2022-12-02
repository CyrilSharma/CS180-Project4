import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.JOptionPane;

public class Translator {
    private static Socket socket;
    private String typeSeperator = "***";
    private String elementSeperator = ",,,";
    private String ansSeperator = "&&&";
    //TODO: use this for... something
    private static boolean connected;
    private static PrintWriter writer;
    private static ObjectInputStream ois;
    private static ObjectOutputStream oos;

    public Translator() {
        // There is only socket active at any time.
        if (socket == null) {
            try {
                socket = new Socket("localhost", 7000);
                writer = new PrintWriter(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
                oos = new ObjectOutputStream(socket.getOutputStream());
                connected = true;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "We can't connect to the server right now. Please try again later", "Error", JOptionPane.ERROR_MESSAGE);
                connected = false;
            }
        }
    }

    public Object query(Query q) throws Exception {
        try {/*
            String args = String.join(elementSeperator, q.getArgs());
            if (q.getArgs() == null || q.getArgs().length == 0) {
                args = null;
            }
            String message = q.getObject() + typeSeperator + q.getFunction() + typeSeperator + args;
            */
            Object result = null;
            oos.writeObject(q);
            oos.flush();
            result = ois.readObject();
            return result;
        } catch (Exception e) {
            throw new Exception("We are having trouble connecting to the server");
        }
    }

    public void respond(String[] data) {
        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            String message = String.join(ansSeperator, data);
            writer.write(message);
            writer.println();
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Query receive() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String result = null;
            try {
                result = reader.readLine();
            } catch (Exception e) {
                // CONNECTION ERROR. Custom exception?
                e.printStackTrace();
                return null;
            }
            String[] response = result.split(typeSeperator);
            if (response.length != 3) {
                // Throw an exception too probably.
                return null;
            }
            String object = response[0];
            String function = response[1];
            String[] args = response[2].split(elementSeperator);
            return new Query(object, function, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * Rewritten methods for convenience
     */
    public HashMap<String, String> get(String key, String value) throws Exception {
        Object o = query(new Query("Database", "get", new String[]{key, value}));
        if (o == null) {
            return null;
        }
        return (HashMap<String, String>) o;
    }
}