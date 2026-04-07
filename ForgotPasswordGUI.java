import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ForgotPasswordGUI extends BaseFrame {

    private String generatedOTP = "";

    public ForgotPasswordGUI() {

        super("FORGOT PASSWORD");

        card.setLayout(new GridBagLayout());

        JPanel panel = new JPanel(new GridLayout(12,1,12,12));
        panel.setBackground(new Color(255,255,255,200));
        panel.setBorder(BorderFactory.createEmptyBorder(40,80,40,80));
        panel.setPreferredSize(new Dimension(420,420));

        JTextField emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(250,35));

        JTextField otpField = new JTextField();
        otpField.setPreferredSize(new Dimension(250,35));

        JPasswordField newPassField = new JPasswordField();
        newPassField.setPreferredSize(new Dimension(250,35));

        JButton sendOTP = createButton("Send OTP");
        sendOTP.setPreferredSize(new Dimension(250,40));

        JButton resetBtn = createButton("Reset Password");
        resetBtn.setPreferredSize(new Dimension(250,40));

        JButton backBtn = createButton("Back");
        backBtn.setPreferredSize(new Dimension(250,40));

        panel.add(new JLabel("Enter Email:"));
        panel.add(emailField);

        panel.add(sendOTP);

        panel.add(new JLabel("Enter OTP:"));
        panel.add(otpField);

        panel.add(new JLabel("New Password:"));
        panel.add(newPassField);

        panel.add(resetBtn);
        panel.add(backBtn);

        card.add(panel);

        // SEND OTP
        sendOTP.addActionListener(e -> {

            try {

                generatedOTP = OTPService.generateOTP();

                String email = emailField.getText();

                EmailService.sendEmail(
                        email,
                        "Password Reset OTP",
                        "Your OTP is: " + generatedOTP
                );

                JOptionPane.showMessageDialog(this,"OTP sent to email.");

            }
            catch(Exception ex){

                JOptionPane.showMessageDialog(this,ex.getMessage());

            }

        });

        // RESET PASSWORD
        resetBtn.addActionListener(e -> {

            try {

                String email = emailField.getText();
                String enteredOTP = otpField.getText();
                String newPassword = new String(newPassField.getPassword());

                if(!enteredOTP.equals(generatedOTP)){

                    JOptionPane.showMessageDialog(this,"Invalid OTP");
                    return;

                }

                Connection conn = DBConnection.getConnection();

                String sql = "UPDATE students SET password=? WHERE email=?";
                PreparedStatement pst = conn.prepareStatement(sql);

                pst.setString(1,newPassword);
                pst.setString(2,email);

                pst.executeUpdate();

                JOptionPane.showMessageDialog(this,"Password Reset Successful");

                conn.close();

                dispose();
                new LoginPage();

            }
            catch(Exception ex){

                JOptionPane.showMessageDialog(this,ex.getMessage());

            }

        });

        // BACK
        backBtn.addActionListener(e -> {

            dispose();
            new LoginPage();

        });

        setVisible(true);
    }
}