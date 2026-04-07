import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class UpdateCourseGUI extends BaseFrame {

    private int courseId;

    public UpdateCourseGUI(int courseId) {

        super("UPDATE COURSE");
        this.courseId = courseId;

        card.setLayout(new GridBagLayout());

        JPanel panel = new JPanel(new GridLayout(20,1,10,10));
        panel.setBackground(new Color(255,255,255,200));
        panel.setBorder(BorderFactory.createEmptyBorder(40,80,40,80));
        panel.setPreferredSize(new Dimension(450,650));

        JTextField nameField = new JTextField();
        JTextField deptField = new JTextField();
        JTextField durationField = new JTextField();
        JTextField creditsField = new JTextField();
        JTextField feeField = new JTextField();
        JTextField capacityField = new JTextField();
        JTextField startField = new JTextField();
        JTextField endField = new JTextField();

        nameField.setPreferredSize(new Dimension(250,35));
        deptField.setPreferredSize(new Dimension(250,35));
        durationField.setPreferredSize(new Dimension(250,35));
        creditsField.setPreferredSize(new Dimension(250,35));
        feeField.setPreferredSize(new Dimension(250,35));
        capacityField.setPreferredSize(new Dimension(250,35));
        startField.setPreferredSize(new Dimension(250,35));
        endField.setPreferredSize(new Dimension(250,35));

        JButton saveBtn = createButton("Save Changes");
        JButton backBtn = createButton("Back");

        saveBtn.setPreferredSize(new Dimension(250,40));
        backBtn.setPreferredSize(new Dimension(250,40));

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

        panel.add(saveBtn);
        panel.add(backBtn);

        card.add(panel);

        saveBtn.addActionListener(e -> {

            try {

                Connection conn = DBConnection.getConnection();

                String durationValue = durationField.getText() + " weeks";

                String sql = "UPDATE courses SET course_name=?, department=?, duration=?, credits=?, fee=?, max_capacity=?, registration_start=?, registration_end=? WHERE course_id=?";

                PreparedStatement pst = conn.prepareStatement(sql);

                pst.setString(1, nameField.getText());
                pst.setString(2, deptField.getText());
                pst.setString(3, durationValue);
                pst.setInt(4, Integer.parseInt(creditsField.getText()));
                pst.setDouble(5, Double.parseDouble(feeField.getText()));
                pst.setInt(6, Integer.parseInt(capacityField.getText()));
                pst.setString(7, startField.getText());
                pst.setString(8, endField.getText());
                pst.setInt(9, courseId);

                pst.executeUpdate();

                JOptionPane.showMessageDialog(this,"Course Updated Successfully");

                conn.close();

                dispose();
                new ViewCoursesGUI();

            }

            catch(Exception ex){

                JOptionPane.showMessageDialog(this,ex.getMessage());

            }

        });

        backBtn.addActionListener(e -> {

            dispose();
            new ViewCoursesGUI();

        });

        setVisible(true);
    }
}