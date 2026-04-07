import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Random;

public class PaymentGUI extends BaseFrame {

    int studentId;
    int courseId;
    String generatedOTP;

    public PaymentGUI(int studentId, int courseId) {

        super("PAYMENT");

        this.studentId = studentId;
        this.courseId = courseId;

        card.setLayout(new GridBagLayout());

        JPanel panel = new JPanel(new GridLayout(12,1,10,10));
        panel.setBackground(new Color(255,255,255,210));
        panel.setBorder(BorderFactory.createEmptyBorder(30,60,30,60));
        panel.setPreferredSize(new Dimension(420,520));

        JLabel courseLabel = new JLabel();
        JLabel amountLabel = new JLabel();

        JComboBox<String> paymentMethod = new JComboBox<>(
                new String[]{"UPI","Debit Card","Credit Card","Net Banking"}
        );

        JTextField paymentField = new JTextField();
        JTextField otpField = new JTextField();

        JButton payBtn = createButton("Pay Now");
        JButton verifyBtn = createButton("Verify OTP");
        JButton backBtn = createButton("Back");

        try {

            Connection conn = DBConnection.getConnection();

            String sql = "SELECT course_name, fee FROM courses WHERE course_id=?";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setInt(1,courseId);

            ResultSet rs = pst.executeQuery();

            if(rs.next()){
                courseLabel.setText("Course : " + rs.getString("course_name"));
                amountLabel.setText("Amount : ₹" + rs.getDouble("fee"));
            }

            conn.close();

        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(this,ex.getMessage());
        }

        panel.add(courseLabel);
        panel.add(amountLabel);

        panel.add(new JLabel("Select Payment Method"));
        panel.add(paymentMethod);

        panel.add(new JLabel("Enter Payment Details"));
        panel.add(paymentField);

        panel.add(payBtn);

        panel.add(new JLabel("Enter OTP"));
        panel.add(otpField);

        panel.add(verifyBtn);
        panel.add(backBtn);

        card.add(panel);

        payBtn.addActionListener(e -> {

            Random rand = new Random();
            generatedOTP = String.valueOf(100000 + rand.nextInt(900000));

            JOptionPane.showMessageDialog(this,
                    "Simulated OTP : " + generatedOTP +
                            "\n(For demo purposes only)");

        });

        verifyBtn.addActionListener(e -> {

            if(!otpField.getText().equals(generatedOTP)){

                JOptionPane.showMessageDialog(this,"Invalid OTP");
                return;

            }

            try{

                Connection conn = DBConnection.getConnection();

                // Generate transaction id
                String transactionId = "TXN" + System.currentTimeMillis();

                double amount = 0;

                String feeQuery = "SELECT fee FROM courses WHERE course_id=?";
                PreparedStatement feeStmt = conn.prepareStatement(feeQuery);
                feeStmt.setInt(1,courseId);

                ResultSet rs = feeStmt.executeQuery();

                if(rs.next()){
                    amount = rs.getDouble("fee");
                }

                // Insert payment record
                String paymentSql =
                        "INSERT INTO payments(transaction_id,student_id,course_id,amount,payment_date,status) VALUES(?,?,?,?,NOW(),'SUCCESS')";

                PreparedStatement paymentStmt = conn.prepareStatement(paymentSql);

                paymentStmt.setString(1,transactionId);
                paymentStmt.setInt(2,studentId);
                paymentStmt.setInt(3,courseId);
                paymentStmt.setDouble(4,amount);

                paymentStmt.executeUpdate();

                // Insert registration record
                String regSql =
                        "INSERT INTO registrations(student_id,course_id) VALUES(?,?)";

                PreparedStatement regStmt = conn.prepareStatement(regSql);

                regStmt.setInt(1,studentId);
                regStmt.setInt(2,courseId);

                regStmt.executeUpdate();

                JOptionPane.showMessageDialog(this,
                        "Payment Successful!\nTransaction ID: " + transactionId);

                conn.close();

                dispose();
                new StudentDashboard(studentId);

            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(this,ex.getMessage());
            }

        });

        backBtn.addActionListener(e -> {

            dispose();
            new RegisterCourseGUI(studentId);

        });

        setVisible(true);
    }
}