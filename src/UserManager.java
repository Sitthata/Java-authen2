import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserManager {

    private final Connection userConnection;

    public UserManager() throws SQLException {
        this.userConnection = DataConnection.getConnection();
    }

    public void registerUser(User user) {
        String insertSQL = "INSERT INTO db_authen.users VALUES (?, ?)";
        String sql = "SELECT * FROM db_authen.users WHERE username = ?";

        try (Connection connection = userConnection) {
            // Check if User exist
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // User already exist, throw exception
                throw new SQLException("This username is already exist");
            }

            // If user doesn't exist, then insert new
            PreparedStatement insertStatement = connection.prepareStatement(insertSQL);
            insertStatement.setString(1, user.getUsername());
            insertStatement.setString(2, user.getPassword());
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean loginUser(String username, String password) {
        String sql = "SELECT * FROM db_authen.users WHERE username = ? AND password = ?";
        try {
            Connection connection = userConnection;
            // Fetch data from database
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            // Username doesn't exists
            if (!resultSet.next()) {
                throw new SQLException("Invalid username or password");
            }

            String db_password = resultSet.getString(2);
            if (db_password.equals(password)) return true;
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
