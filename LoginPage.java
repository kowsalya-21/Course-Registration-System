import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginPage extends BaseFrame {

    public LoginPage() {

        super("LOGIN");

        card.setLayout(new GridBagLayout());

        // Transparent Login Panel
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBackground(new Color(255, 255, 255, 200)); // transparent
        loginPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Email
        gbc.gridy = 0;
        JLabel emailLabel = new JLabel("Email:");
        loginPanel.add(emailLabel, gbc);

        gbc.gridy = 1;
        JTextField emailField = createTextField();
        loginPanel.add(emailField, gbc);

        // Password
        gbc.gridy = 2;
        JLabel passLabel = new JLabel("Password:");
        loginPanel.add(passLabel, gbc);

        gbc.gridy = 3;
        JPasswordField passwordField = createPasswordField();
        loginPanel.add(passwordField, gbc);

        // Login As
        gbc.gridy = 4;
        JLabel roleLabel = new JLabel("Login As:");
        loginPanel.add(roleLabel, gbc);

        gbc.gridy = 5;
        String[] roles = {"Admin", "Student"};
        JComboBox<String> roleBox = new JComboBox<>(roles);
        roleBox.setPreferredSize(new Dimension(200, 35));
        loginPanel.add(roleBox, gbc);

        // Login Button
        gbc.gridy = 6;
        JButton loginBtn = createButton("Login");
        loginPanel.add(loginBtn, gbc);

        // Forgot Password Button
        gbc.gridy = 7;
        JButton forgotBtn = createButton("Forgot Password");
        loginPanel.add(forgotBtn, gbc);

        // Add login panel to background card
        card.add(loginPanel);

        // LOGIN LOGIC
        loginBtn.addActionListener(e -> {

            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String role = roleBox.getSelectedItem().toString();

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter all fields!");
                return;
            }

            try {

                Connection conn = DBConnection.getConnection();

                if (role.equals("Admin")) {

                    String sql = "SELECT * FROM admins WHERE email=? AND password=?";
                    PreparedStatement pst = conn.prepareStatement(sql);

                    pst.setString(1, email);
                    pst.setString(2, password);

                    ResultSet rs = pst.executeQuery();

                    if (rs.next()) {

                        dispose();
                        new AdminDashboard();

                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid Admin Credentials!");
                    }

                } else {

                    String sql = "SELECT * FROM students WHERE email=? AND password=?";
                    PreparedStatement pst = conn.prepareStatement(sql);

                    pst.setString(1, email);
                    pst.setString(2, password);

                    ResultSet rs = pst.executeQuery();

                    if (rs.next()) {

                        int studentId = rs.getInt("student_id");

                        dispose();
                        new StudentDashboard(studentId);

                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid Student Credentials!");
                    }
                }

                conn.close();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());

            }
        });

        // Forgot Password
        forgotBtn.addActionListener(e -> {

            dispose();
            new ForgotPasswordGUI();

        });

        setVisible(true);
    }

    public static void main(String[] args) {

        new LoginPage();

    }
}