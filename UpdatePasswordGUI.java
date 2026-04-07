import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class UpdatePasswordGUI extends BaseFrame {

    private int studentId;

    public UpdatePasswordGUI(int studentId) {

        super("UPDATE PASSWORD");
        this.studentId = studentId;

        card.setLayout(new GridBagLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(255,255,255,200));
        panel.setBorder(BorderFactory.createEmptyBorder(40,80,40,80));
        panel.setPreferredSize(new Dimension(420,320));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Old Password
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Old Password:"), gbc);

        gbc.gridy = 1;
        JPasswordField oldPass = new JPasswordField();
        oldPass.setPreferredSize(new Dimension(200,25));
        panel.add(oldPass, gbc);

        // New Password
        gbc.gridy = 2;
        panel.add(new JLabel("New Password:"), gbc);

        gbc.gridy = 3;
        JPasswordField newPass = new JPasswordField();
        newPass.setPreferredSize(new Dimension(200,25));
        panel.add(newPass, gbc);

        // Confirm Password
        gbc.gridy = 4;
        panel.add(new JLabel("Confirm Password:"), gbc);

        gbc.gridy = 5;
        JPasswordField confirmPass = new JPasswordField();
        confirmPass.setPreferredSize(new Dimension(200,25));
        panel.add(confirmPass, gbc);

        // Update Button
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton updateBtn = createButton("Update");
        panel.add(updateBtn, gbc);

        // Back Button (Below Update)
        gbc.gridy = 7;

        JButton backBtn = createButton("Back");
        panel.add(backBtn, gbc);

        card.add(panel);

        // Update Password Logic
        updateBtn.addActionListener(e -> {

            try {

                Connection conn = DBConnection.getConnection();

                String oldPassword = new String(oldPass.getPassword());
                String newPassword = new String(newPass.getPassword());
                String confirmPassword = new String(confirmPass.getPassword());

                if(!newPassword.equals(confirmPassword)){

                    JOptionPane.showMessageDialog(this,"New passwords do not match");
                    return;

                }

                String checkSql = "SELECT password FROM students WHERE student_id=?";
                PreparedStatement pst1 = conn.prepareStatement(checkSql);
                pst1.setInt(1,studentId);

                ResultSet rs = pst1.executeQuery();

                if(rs.next()){

                    if(!rs.getString("password").equals(oldPassword)){

                        JOptionPane.showMessageDialog(this,"Old password incorrect");
                        return;

                    }

                }

                String updateSql = "UPDATE students SET password=? WHERE student_id=?";
                PreparedStatement pst2 = conn.prepareStatement(updateSql);
                pst2.setString(1,newPassword);
                pst2.setInt(2,studentId);

                pst2.executeUpdate();

                JOptionPane.showMessageDialog(this,"Password Updated Successfully");

                conn.close();

            }
            catch(Exception ex){

                JOptionPane.showMessageDialog(this,ex.getMessage());

            }

        });

        // Back Button Logic
        backBtn.addActionListener(e -> {

            dispose();
            new StudentDashboard(studentId);

        });

        setVisible(true);
    }
}