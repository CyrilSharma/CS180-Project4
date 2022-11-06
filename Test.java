public class Test {
    public static void main(String[] args) {
        Database db = new Database("TestDatabase.txt");
        try {
            db.add("Cyril", "eriuhefur", Role.Customer);
            System.out.println("Test 1 Failed.");
        } catch (InvalidUserException e) {}
        try {
            db.add("Joniel", "eriuhefur", Role.Customer);
        } catch (InvalidUserException e) {
            System.out.println("Test 2 Failed.");
        }

        System.out.println(db);
        
        try {
            // NOTE. This will eventually have to be replaced with IDs since names are not unique.
            db.remove("Joniel");
        } catch (InvalidUserException e) {
            System.out.println("Test 3 Failed.");
        }

        System.out.println(db);

        try {
            // NOTE. This will eventually have to be replaced with IDs since names are not unique.
            db.modify("Cyril", "username", "Qyril");
        } catch (Exception e) {
            System.out.println("Test 3 Failed.");
        }

        // NOTE. This will eventually have to be replaced with IDs since names are not unique.
        if (!db.verify("Qyril", "password")) {
            System.out.println("Test 4 Failed.");
        }

        try {
            // NOTE. This will eventually have to be replaced with IDs since names are not unique.
            db.modify("Cyril", "name", "Qyril");
            System.out.println("Test 5 Failed.");
        } catch (Exception e) {}
    
        System.out.println(db);
    }
}
