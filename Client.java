import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
public class Client {
    private static Socket socket;
    private String typeSeperator = "***";
    private String elementSeperator = ",,,";
    private String ansSeperator = "&&&";

    public Client() {
        // There is only socket active at any time.
        if (socket == null) {
            try {
                socket = new Socket("localhost", 7000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Object query(Query q) {
        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            String args = String.join(elementSeperator, q.getArgs());
            String message = q.getObject() + typeSeperator + q.getFunction() + typeSeperator + args;
            writer.write(message);
            writer.println();
            writer.flush();
            
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Object result = null;
            try {
                result = ois.readObject();
            } catch (Exception e) {
                // CONNECTION ERROR. Custom exception?
                e.printStackTrace();
                return null;
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
    public HashMap<String, String> get(String key, String value) {
        Object o = query(new Query("database", "get", new String[]{key, value}));
        if (o == null) {
            return null;
        }
        return (HashMap<String, String>) o;
    }
}