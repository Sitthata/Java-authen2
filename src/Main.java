import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {

        UserManager userManager = new UserManager();
        userManager.registerUser(new User("FirstTest", "ayo123"));

    }
}