import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AdminLogin {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Email: ");
        String email = sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        try {
            Connection conn = DBConnection.getConnection();

            String query = "SELECT * FROM admins WHERE email = ? AND password = ?";
            PreparedStatement pst = conn.prepareStatement(query);

            pst.setString(1, email);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                System.out.println("Login Successful!");
            } else {
                System.out.println("Invalid Credentials!");
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        sc.close();
    }
}
