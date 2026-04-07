import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class StudentProfileGUI extends BaseFrame {

    private int studentId;

    public StudentProfileGUI(int studentId) {

        super("MY PROFILE");
        this.studentId = studentId;

        card.setLayout(new GridBagLayout());

        // Main Panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7,1,10,10));
        panel.setBackground(new Color(255,255,255,200));
        panel.setBorder(BorderFactory.createEmptyBorder(40,80,40,80));
        panel.setPreferredSize(new Dimension(450,400));

        JLabel nameLabel = new JLabel();
        JLabel emailLabel = new JLabel();
        JLabel deptLabel = new JLabel();
        JLabel courseLabel = new JLabel();
        JLabel paymentLabel = new JLabel();

        nameLabel.setFont(new Font("Arial",Font.BOLD,16));
        emailLabel.setFont(new Font("Arial",Font.BOLD,16));
        deptLabel.setFont(new Font("Arial",Font.BOLD,16));
        courseLabel.setFont(new Font("Arial",Font.BOLD,16));
        paymentLabel.setFont(new Font("Arial",Font.BOLD,16));

        panel.add(nameLabel);
        panel.add(emailLabel);
        panel.add(deptLabel);
        panel.add(courseLabel);
        panel.add(paymentLabel);

        JButton backBtn = createButton("Back");
        panel.add(backBtn);

        card.add(panel);

        loadProfile(nameLabel,emailLabel,deptLabel,courseLabel,paymentLabel);

        backBtn.addActionListener(e -> {

            dispose();
            new StudentDashboard(studentId);

        });

        setVisible(true);
    }

    private void loadProfile(JLabel name, JLabel email, JLabel dept,
                             JLabel courses, JLabel payment) {

        try {

            Connection conn = DBConnection.getConnection();

            // Student details
            String studentSql = "SELECT name,email,department FROM students WHERE student_id=?";
            PreparedStatement pst = conn.prepareStatement(studentSql);
            pst.setInt(1,studentId);
            ResultSet rs = pst.executeQuery();

            if(rs.next()){

                name.setText("Name : " + rs.getString("name"));
                email.setText("Email : " + rs.getString("email"));
                dept.setText("Department : " + rs.getString("department"));

            }

            // Registered courses count
            String courseSql = "SELECT COUNT(*) FROM registrations WHERE student_id=?";
            PreparedStatement pst2 = conn.prepareStatement(courseSql);
            pst2.setInt(1,studentId);
            ResultSet rs2 = pst2.executeQuery();

            if(rs2.next()){
                courses.setText("Registered Courses : " + rs2.getInt(1));
            }

            // Total payment
            String paymentSql = "SELECT SUM(amount) FROM payments WHERE student_id=?";
            PreparedStatement pst3 = conn.prepareStatement(paymentSql);
            pst3.setInt(1,studentId);
            ResultSet rs3 = pst3.executeQuery();

            if(rs3.next()){

                payment.setText("Total Paid Amount : ₹ " + rs3.getDouble(1));

            }

            conn.close();

        } catch(Exception e){

            JOptionPane.showMessageDialog(this,e.getMessage());

        }
    }
}