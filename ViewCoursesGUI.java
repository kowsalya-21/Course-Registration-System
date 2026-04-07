import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewCoursesGUI extends BaseFrame {

    JTable table;
    DefaultTableModel model;

    public ViewCoursesGUI() {

        super("VIEW COURSES");

        String[] columns = {
                "ID",
                "Course Name",
                "Department",
                "Duration",
                "Credits",
                "Fee",
                "Capacity",
                "Reg Start",
                "Reg End"
        };

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        table.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 120, 1100, 420);

        card.setLayout(null);
        card.add(scrollPane);

        JButton updateBtn = createButton("Update");
        JButton deleteBtn = createButton("Delete");
        JButton backBtn = createButton("Back");

        updateBtn.setBounds(350, 560, 180, 40);
        deleteBtn.setBounds(550, 560, 180, 40);
        backBtn.setBounds(750, 560, 180, 40);

        card.add(updateBtn);
        card.add(deleteBtn);
        card.add(backBtn);

        loadCourses();

        // ================= UPDATE =================
        updateBtn.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a course first");
                return;
            }

            int courseId = (int) model.getValueAt(row, 0);

            dispose();
            new UpdateCourseGUI(courseId);

        });

        // ================= DELETE =================
        deleteBtn.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a course first");
                return;
            }

            int courseId = (int) model.getValueAt(row, 0);

            // 🔥 Confirmation popup
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this course?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) return;

            try {

                Connection conn = DBConnection.getConnection();

                // STEP 1: delete from payments
                PreparedStatement p1 = conn.prepareStatement(
                        "DELETE FROM payments WHERE course_id=?"
                );
                p1.setInt(1, courseId);
                p1.executeUpdate();

                // STEP 2: delete from registrations
                PreparedStatement p2 = conn.prepareStatement(
                        "DELETE FROM registrations WHERE course_id=?"
                );
                p2.setInt(1, courseId);
                p2.executeUpdate();

                // STEP 3: delete from courses
                PreparedStatement p3 = conn.prepareStatement(
                        "DELETE FROM courses WHERE course_id=?"
                );
                p3.setInt(1, courseId);
                p3.executeUpdate();

                JOptionPane.showMessageDialog(this, "Course Deleted Successfully");

                conn.close();

                // 🔄 Refresh table
                model.setRowCount(0);
                loadCourses();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }

        });

        // ================= BACK =================
        backBtn.addActionListener(e -> {
            dispose();
            new AdminDashboard();
        });

        setVisible(true);
    }

    // ================= LOAD COURSES =================
    private void loadCourses() {

        try {

            Connection conn = DBConnection.getConnection();

            String sql = "SELECT * FROM courses";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            model.setRowCount(0);

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("course_id"),
                        rs.getString("course_name"),
                        rs.getString("department"),
                        rs.getString("duration"),
                        rs.getInt("credits"),
                        rs.getDouble("fee"),
                        rs.getInt("max_capacity"),
                        rs.getDate("registration_start"),
                        rs.getDate("registration_end")
                });

            }

            conn.close();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}