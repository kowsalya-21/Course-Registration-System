import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class UpdateStudentGUI extends BaseFrame {

    private final int studentId;

    public UpdateStudentGUI(int studentId) {

        super("UPDATE STUDENT");
        this.studentId = studentId;

        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 🔥 Better label font
        Font labelFont = new Font("Arial", Font.BOLD, 18);

        // Fields
        JTextField nameField = createTextField();
        JTextField deptField = createTextField();
        JTextField emailField = createTextField();

        // Optional: better field visibility
        nameField.setBackground(Color.WHITE);
        deptField.setBackground(Color.WHITE);
        emailField.setBackground(Color.WHITE);

        // 🔥 Load Existing Data
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM students WHERE student_id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, this.studentId);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                nameField.setText(rs.getString("name"));
                deptField.setText(rs.getString("department"));
                emailField.setText(rs.getString("email"));
            }

            conn.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }

        // ✅ FINAL FIX: WHITE labels for dark background
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(labelFont);

        JLabel deptLabel = new JLabel("Department:");
        deptLabel.setForeground(Color.WHITE);
        deptLabel.setFont(labelFont);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setFont(labelFont);

        // Layout (unchanged)
        gbc.gridy = 0;
        gbc.gridx = 0;
        card.add(nameLabel, gbc);
        gbc.gridx = 1;
        card.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        card.add(deptLabel, gbc);
        gbc.gridx = 1;
        card.add(deptField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        card.add(emailLabel, gbc);
        gbc.gridx = 1;
        card.add(emailField, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;

        JButton updateBtn = createButton("Update Student");
        card.add(updateBtn, gbc);

        gbc.gridy = 4;
        JButton backBtn = createButton("Back");
        card.add(backBtn, gbc);

        // 🔥 Update Action
        updateBtn.addActionListener(e -> {

            try {
                Connection conn = DBConnection.getConnection();

                String sql = "UPDATE students SET name=?, department=?, email=? WHERE student_id=?";
                PreparedStatement pst = conn.prepareStatement(sql);

                pst.setString(1, nameField.getText());
                pst.setString(2, deptField.getText());
                pst.setString(3, emailField.getText());
                pst.setInt(4, this.studentId);

                pst.executeUpdate();
                conn.close();

                JOptionPane.showMessageDialog(this, "Student Updated Successfully!");

                dispose();
                new ViewStudentsGUI();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        backBtn.addActionListener(e -> {
            dispose();
            new ViewStudentsGUI();
        });

        setVisible(true);
    }
}