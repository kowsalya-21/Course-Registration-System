import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ViewAllRegistrationsGUI extends BaseFrame {

    public ViewAllRegistrationsGUI() {

        super("All Registrations");

        try {
            Connection conn = DBConnection.getConnection();

            String sql = "SELECT s.name AS student_name, " +
                         "c.course_name, c.department, " +
                         "r.status, r.registration_date " +
                         "FROM registrations r " +
                         "JOIN students s ON r.student_id = s.student_id " +
                         "JOIN courses c ON r.course_id = c.course_id";

            Statement st = conn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            );

            ResultSet rs = st.executeQuery(sql);

            rs.last();
            int rows = rs.getRow();
            rs.beforeFirst();

            String[][] data = new String[rows][5];
            int i = 0;

            while (rs.next()) {
                data[i][0] = rs.getString("student_name");
                data[i][1] = rs.getString("course_name");
                data[i][2] = rs.getString("department");
                data[i][3] = rs.getString("status");
                data[i][4] = rs.getString("registration_date");
                i++;
            }

            String[] columns = {
                    "Student Name",
                    "Course Name",
                    "Department",
                    "Status",
                    "Registration Date"
            };

            JTable table = new JTable(data, columns);
            JScrollPane scrollPane = new JScrollPane(table);

            card.setLayout(new BorderLayout());
            card.add(scrollPane, BorderLayout.CENTER);

            conn.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }

        JButton backBtn = createButton("Back");
        card.add(backBtn, BorderLayout.SOUTH);

        backBtn.addActionListener(e -> {
            dispose();
            new AdminDashboard();
        });

        setVisible(true);
    }
}
