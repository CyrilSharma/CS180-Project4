import java.util.*;
import java.io.*;
import java.net.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Server implements Runnable {
    
    private Socket socket;
    private MessageManager mm;
    private Database db;
    private User user;
    private Dashboard dashboard;
    private Filter filter;
    private boolean loggedIn;

    private String typeSeperator = "\\*\\*\\*";
    private String elementSeperator = ",,,";

    public Server(Socket socket, MessageManager mm, Database db) {
        this.socket = socket;
        this.mm = mm;
        this.db = db;
        this.dashboard = null;
        this.filter = null;
        this.loggedIn = false;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while (true) {
                line = bfr.readLine();
                System.out.println(line);
                String[] parameters = line.split(typeSeperator);
                Object o = null;
                String function = null;
                String[] args = null;
                if (parameters[0].equals("Database")) {
                    o = db;
                } else if (parameters[0].equals("MessageManager")) {
                    o = mm;
                } else if (loggedIn) {
                    if (parameters[0].equals("User")) {
                        o = user;
                    } else if (parameters[0].equals("Dashboard")) {
                        o = dashboard;
                    } else if (parameters[0].equals("Filter")) {
                        o = filter;
                    }
                } else {
                    continue;
                }
                function = parameters[1];
                if (parameters[2] != null) {
                    args = parameters[2].split(elementSeperator);
                }
                Object result = executeMethod(o, function, args);
                //TODO: Add logout function
                if (function.equals("verify") && (Boolean) result) {
                    loggedIn = true;
                    user = new User(args[0], args[1], db.get("email", args[0]).get("role"), mm, db);
                    dashboard = new Dashboard(args[0], mm.getHistoryLocation(db.get("email", args[0]).get("id")), db);
                    filter = new Filter(args[0], db);
                }
                oos.writeObject(result);
                oos.flush();
            }
        } catch (IOException e) {
            // TODO: Change this
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(7000);
            Database db = new Database();
            MessageManager mm = new MessageManager(db);
            while (true) {
                Socket s = ss.accept();
                Server server = new Server(s, mm, db);
                new Thread(server).start();
            }
        } catch (IOException e) {
            //TODO: Change this
            e.printStackTrace();
        }
    }

    private <T> Method generateClasses(Object obj, String function, String[] args) {
        Method[] methods = obj.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equals(function) && method.getParameterTypes().length == args.length) {
                return method;
            }
        }
        return null;
    }

    private Object executeMethod(Object o, String function, String[] args) {
        Method method = generateClasses(o, function, args);
        if (method == null) {
            return null;
        }
        Object result = null;
        if (method != null) {
            Class[] methodParameterTypes = method.getParameterTypes();
            Object[] params = null;
            if (args != null && args.length == methodParameterTypes.length) {
                params = new Object[args.length];
                for (int i = 0; i < args.length; i++) {
                    try {
                        params[i] = methodParameterTypes[i].cast(args[i]);
                    } catch (ClassCastException e) {
                        //Add all the things that can't be cast as parameters here with if statements or something
                        params[i] = Role.valueOf(args[i]);
                    }
                }
            }
            try {
                result = method.invoke(o, params);
            } catch (IllegalAccessException | InvocationTargetException e) {
                result = e;
            }
        }
        return result;
    }

}
