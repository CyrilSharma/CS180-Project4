public class Test {
    public static void main(String[] args) {
        MessageManager mm = new MessageManager();
        try {
            mm.addBlocked("aksex", "thinggy");
            System.out.println(mm.getCustomerNames("thinggy"));
        } catch (InvalidUserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
