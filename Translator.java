import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import javax.swing.JOptionPane;
/**
 * Project 5 -> Translator
 *
 * utilizes Query and the networking/concurrency.
 *
 * @author Atharva Gupta, Cyril Sharma, Josh George, Nitin Murthy, Jacob Choi, L11
 *
 * @version December 10, 2022
 *
 */
public class Translator {
    private static Socket socket;
    private static ObjectInputStream ois;
    private static ObjectOutputStream oos;
    private static boolean inLoop;
    private static HashMap<String, String> loggedInUser;

    public Translator() {
        // There is only socket active at any time.
        if (socket == null) {
            try {
                socket = new Socket(Constants.hostname, Constants.port);
                ois = new ObjectInputStream(socket.getInputStream());
                oos = new ObjectOutputStream(socket.getOutputStream());
                loggedInUser = null;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "We can't connect to the server right now. Please try again later", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    /**
     * returns a object query
     * @param q
     * @throws Exception
     * @return Object
     */
    public Object query(Query q) throws Exception {
        if (!inLoop) {
            try {
                Object result = null;
                oos.writeObject(q);
                oos.flush();
                result = ois.readObject();
                if (result instanceof Exception) {
                    throw new Exception(((Exception) result).getMessage());
                } else if (q.getFunction().equals("verify") && (Boolean) result) {
                    setUser(get("email", (String) q.getArgs()[0]));
                } else if (q.getFunction().equals("logout")) {
                    setUser(null);
                }
                return result;
            } catch (IOException e) {
                inLoop = true;
                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        int i = 0;
                        while (true) {
                            System.out.println(i);
                            i++;
                            try {
                                refresh();
                                inLoop = false;
                                if (loggedInUser != null) {
                                    query(new Query("Database", "verify", new String[]{loggedInUser.get("email"), loggedInUser.get("password")}));
                                }
                                break;
                            } catch (Exception e) {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e1) {
                                    
                                }
                            }
                        }
                    }
                });
                thread.start();
                throw new Exception("We can't connect to the server right now. Please try again later");
            }
            catch (Exception e) {
                throw new Exception(e.getMessage());
            }
        }
        return new Exception("We can't connect to the server right now. Please try again later");
    }

    /*
     * Rewritten methods for convenience
     */
    @SuppressWarnings("unchecked")
    public HashMap<String, String> get(String key, String value) throws Exception {
        Object o = query(new Query("Database", "get", new String[]{key, value}));
        if (o == null) {
            return null;
        }
        return (HashMap<String, String>) o;
    }

    /*
     * Refreshes to ake sure the server is connected
     */
    public void refresh() throws Exception {
        try {
            socket = new Socket(Constants.hostname, Constants.port);
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            throw new Exception("We can't connect to the server right now.");
        }
    }
    /*
     * sets the user from loggedInUser to the parameter
     * @param user
     */
    private void setUser(HashMap<String, String> user) {
        loggedInUser = user;
    }
}