import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AddCourseGUI extends BaseFrame {

    public AddCourseGUI() {

        super("ADD COURSE");

        card.setLayout(new GridBagLayout());

        JPanel panel = new JPanel(new GridLayout(20,1,10,10));
        panel.setBackground(new Color(255,255,255,200));
        panel.setBorder(BorderFactory.createEmptyBorder(30,60,30,60));
        panel.setPreferredSize(new Dimension(420,650));

        JTextField nameField = new JTextField();
        JTextField deptField = new JTextField();
        JTextField durationField = new JTextField();
        JTextField creditsField = new JTextField();
        JTextField feeField = new JTextField();
        JTextField capacityField = new JTextField();
        JTextField startField = new JTextField();
        JTextField endField = new JTextField();

        JButton addBtn = createButton("Add Course");
        JButton backBtn = createButton("Back");

        nameField.setPreferredSize(new Dimension(250,35));
        deptField.setPreferredSize(new Dimension(250,35));
        durationField.setPreferredSize(new Dimension(250,35));
        creditsField.setPreferredSize(new Dimension(250,35));
        feeField.setPreferredSize(new Dimension(250,35));
        capacityField.setPreferredSize(new Dimension(250,35));
        startField.setPreferredSize(new Dimension(250,35));
        endField.setPreferredSize(new Dimension(250,35));

        panel.add(new JLabel("Course Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Department:"));
        panel.add(deptField);

        panel.add(new JLabel("Duration (weeks):"));
        panel.add(durationField);

        panel.add(new JLabel("Credits:"));
        panel.add(creditsField);

        panel.add(new JLabel("Fee:"));
        panel.add(feeField);

        panel.add(new JLabel("Max Capacity:"));
        panel.add(capacityField);

        panel.add(new JLabel("Registration Start (YYYY-MM-DD):"));
        panel.add(startField);

        panel.add(new JLabel("Registration End (YYYY-MM-DD):"));
        panel.add(endField);

        panel.add(addBtn);
        panel.add(backBtn);

        card.add(panel);

        addBtn.addActionListener(e -> {

            try {

                Connection conn = DBConnection.getConnection();

                String sql = "INSERT INTO courses(course_name,department,duration,credits,fee,max_capacity,registration_start,registration_end) VALUES(?,?,?,?,?,?,?,?)";

                PreparedStatement pst = conn.prepareStatement(sql);

                pst.setString(1,nameField.getText());
                pst.setString(2,deptField.getText());

                // FIXED: do NOT add "weeks" again
                pst.setString(3,durationField.getText());

                pst.setInt(4,Integer.parseInt(creditsField.getText()));
                pst.setDouble(5,Double.parseDouble(feeField.getText()));
                pst.setInt(6,Integer.parseInt(capacityField.getText()));
                pst.setString(7,startField.getText());
                pst.setString(8,endField.getText());

                pst.executeUpdate();

                JOptionPane.showMessageDialog(this,"Course Added Successfully");

                conn.close();

                dispose();
                new AdminDashboard();

            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(this,ex.getMessage());
            }

        });

        backBtn.addActionListener(e -> {

            dispose();
            new AdminDashboard();

        });

        setVisible(true);
    }
}