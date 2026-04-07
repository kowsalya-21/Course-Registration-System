import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AddStudentGUI extends BaseFrame {

    public AddStudentGUI() {

        super("ADD STUDENT");

        card.setLayout(new GridBagLayout());

        // Main Panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(255,255,255,200));

        // Wider panel
        panel.setBorder(BorderFactory.createEmptyBorder(40,80,40,80));
        panel.setPreferredSize(new Dimension(420,420));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12,12,12,12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Name
        gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Name:");
        panel.add(nameLabel, gbc);

        gbc.gridy = 1;
        JTextField nameField = createTextField();
        panel.add(nameField, gbc);

        // Email
        gbc.gridy = 2;
        JLabel emailLabel = new JLabel("Email:");
        panel.add(emailLabel, gbc);

        gbc.gridy = 3;
        JTextField emailField = createTextField();
        panel.add(emailField, gbc);

        // Department
        gbc.gridy = 4;
        JLabel deptLabel = new JLabel("Department:");
        panel.add(deptLabel, gbc);

        gbc.gridy = 5;
        JTextField deptField = createTextField();
        panel.add(deptField, gbc);

        // Password
        gbc.gridy = 6;
        JLabel passLabel = new JLabel("Password:");
        panel.add(passLabel, gbc);

        gbc.gridy = 7;
        JPasswordField passField = createPasswordField();
        panel.add(passField, gbc);

        // Add Button
        gbc.gridy = 8;
        JButton addBtn = createButton("Add Student");
        panel.add(addBtn, gbc);

        // Back Button
        gbc.gridy = 9;
        JButton backBtn = createButton("Back");
        panel.add(backBtn, gbc);

        card.add(panel);

        // ADD STUDENT LOGIC
        addBtn.addActionListener(e -> {

            String name = nameField.getText();
            String email = emailField.getText();
            String dept = deptField.getText();
            String pass = new String(passField.getPassword());

            if(name.isEmpty() || email.isEmpty() || dept.isEmpty() || pass.isEmpty()) {

                JOptionPane.showMessageDialog(this,"Please fill all fields");
                return;

            }

            try {

                Connection conn = DBConnection.getConnection();

                String sql = "INSERT INTO students(name,email,department,password) VALUES(?,?,?,?)";

                PreparedStatement pst = conn.prepareStatement(sql);

                pst.setString(1,name);
                pst.setString(2,email);
                pst.setString(3,dept);
                pst.setString(4,pass);

                pst.executeUpdate();

                JOptionPane.showMessageDialog(this,"Student Added Successfully");

                conn.close();

                nameField.setText("");
                emailField.setText("");
                deptField.setText("");
                passField.setText("");

            }
            catch(Exception ex){

                JOptionPane.showMessageDialog(this,ex.getMessage());

            }

        });

        // BACK BUTTON
        backBtn.addActionListener(e -> {

            dispose();
            new AdminDashboard();

        });

        setVisible(true);
    }
}