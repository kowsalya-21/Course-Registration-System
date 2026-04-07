import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ViewRegisteredCoursesGUI extends BaseFrame {

    private JTable table;
    private int studentId;

    public ViewRegisteredCoursesGUI(int studentId) {

        super("MY REGISTERED COURSES");
        this.studentId = studentId;

        loadRegisteredCourses();

        setVisible(true);
    }

    private void loadRegisteredCourses() {

        String[] columns = {
                "Course ID",
                "Course Name",
                "Department",
                "Duration",
                "Credits",
                "Fee",
                "Status"
        };

        String[][] data = {};

        try {
            Connection conn = DBConnection.getConnection();

            String sql = "SELECT c.course_id, c.course_name, c.department, " +
                    "c.duration, c.credits, c.fee, r.status " +
                    "FROM registrations r " +
                    "JOIN courses c ON r.course_id = c.course_id " +
                    "WHERE r.student_id=?";

            PreparedStatement pst = conn.prepareStatement(
                    sql,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            );

            pst.setInt(1, studentId);
            ResultSet rs = pst.executeQuery();

            rs.last();
            int rows = rs.getRow();
            rs.beforeFirst();

            data = new String[rows][7];
            int i = 0;

            while (rs.next()) {

                data[i][0] = rs.getString("course_id");
                data[i][1] = rs.getString("course_name");
                data[i][2] = rs.getString("department");
                data[i][3] = rs.getString("duration") + " weeks";
                data[i][4] = rs.getString("credits");
                data[i][5] = "₹ " + rs.getString("fee");
                data[i][6] = rs.getString("status");

                i++;
            }

            conn.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

        table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton dropBtn = createButton("Drop Selected Course");
        JButton backBtn = createButton("Back");

        dropBtn.addActionListener(e -> dropCourse());
        backBtn.addActionListener(e -> {
            dispose();
            new StudentDashboard(studentId);
        });

        card.setLayout(new BorderLayout());
        card.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(dropBtn);
        bottomPanel.add(backBtn);

        card.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void dropCourse() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a course first!");
            return;
        }

        int courseId = Integer.parseInt(table.getValueAt(row, 0).toString());

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to drop this course?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            Connection conn = DBConnection.getConnection();

            String sql = "UPDATE registrations SET status='Dropped' " +
                    "WHERE student_id=? AND course_id=?";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, studentId);
            pst.setInt(2, courseId);
            pst.executeUpdate();

            // 🔥 Get student email
            String emailSql = "SELECT email FROM students WHERE student_id=?";
            PreparedStatement pst2 = conn.prepareStatement(emailSql);
            pst2.setInt(1, studentId);
            ResultSet rs = pst2.executeQuery();
            rs.next();
            String studentEmail = rs.getString("email");

            conn.close();

            JOptionPane.showMessageDialog(this, "Course Dropped Successfully!");

            // 🔥 Simulated Email
            EmailService.sendEmail(
                    studentEmail,
                    "Course Dropped Successfully",
                    "Dear Student,\n\nYou have successfully dropped the course.\n\nThank you."
            );

            dispose();
            new ViewRegisteredCoursesGUI(studentId);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}