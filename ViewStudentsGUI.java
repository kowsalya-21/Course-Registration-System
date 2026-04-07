import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ViewStudentsGUI extends BaseFrame {

    private JTable table;
    private JTextField searchField;

    public ViewStudentsGUI() {

        super("VIEW STUDENTS");

        card.setLayout(new BorderLayout());

        // 🔹 Top Panel
        JPanel topPanel = new JPanel();

        topPanel.add(new JLabel("Search by Name:"));

        searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(250, 35));
        topPanel.add(searchField);

        JButton searchBtn = createButton("Search");
        JButton showAllBtn = createButton("Show All");

        topPanel.add(searchBtn);
        topPanel.add(showAllBtn);

        card.add(topPanel, BorderLayout.NORTH);

        // 🔹 Table
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        card.add(scrollPane, BorderLayout.CENTER);

        // 🔹 Bottom Panel
        JPanel bottomPanel = new JPanel();

        JButton updateBtn = createButton("Update");
        JButton deleteBtn = createButton("Delete");
        JButton backBtn = createButton("Back");

        bottomPanel.add(updateBtn);
        bottomPanel.add(deleteBtn);
        bottomPanel.add(backBtn);

        card.add(bottomPanel, BorderLayout.SOUTH);

        // 🔥 Button Actions
        searchBtn.addActionListener(e -> searchStudents());
        showAllBtn.addActionListener(e -> loadStudents());

        deleteBtn.addActionListener(e -> deleteStudent());
        updateBtn.addActionListener(e -> updateStudent());

        backBtn.addActionListener(e -> {
            dispose();
            new AdminDashboard();
        });

        loadStudents();
        setVisible(true);
    }

    private void loadStudents() {

        String[] columns = {"S.No", "ID", "Name", "Department", "Email"};
        String[][] data;

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM students";
            Statement st = conn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            ResultSet rs = st.executeQuery(sql);

            rs.last();
            int rows = rs.getRow();
            rs.beforeFirst();

            data = new String[rows][5];

            int i = 0;
            while (rs.next()) {

                data[i][0] = String.valueOf(i + 1);
                data[i][1] = rs.getString("student_id");
                data[i][2] = rs.getString("name");
                data[i][3] = rs.getString("department");
                data[i][4] = rs.getString("email");

                i++;
            }

            table.setModel(new javax.swing.table.DefaultTableModel(data, columns));
            conn.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void searchStudents() {

        String name = searchField.getText();

        String[] columns = {"S.No", "ID", "Name", "Department", "Email"};
        String[][] data;

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM students WHERE name LIKE ?";
            PreparedStatement pst = conn.prepareStatement(
                    sql,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            pst.setString(1, "%" + name + "%");

            ResultSet rs = pst.executeQuery();

            rs.last();
            int rows = rs.getRow();
            rs.beforeFirst();

            data = new String[rows][5];

            int i = 0;
            while (rs.next()) {

                data[i][0] = String.valueOf(i + 1);
                data[i][1] = rs.getString("student_id");
                data[i][2] = rs.getString("name");
                data[i][3] = rs.getString("department");
                data[i][4] = rs.getString("email");

                i++;
            }

            table.setModel(new javax.swing.table.DefaultTableModel(data, columns));
            conn.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void deleteStudent() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a student first!");
            return;
        }

        int studentId = Integer.parseInt(table.getValueAt(selectedRow, 1).toString());

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete?",
                "Confirm",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {

            try {
                Connection conn = DBConnection.getConnection();
                String sql = "DELETE FROM students WHERE student_id=?";
                PreparedStatement pst = conn.prepareStatement(sql);

                pst.setInt(1, studentId);
                pst.executeUpdate();
                conn.close();

                JOptionPane.showMessageDialog(this, "Student Deleted!");

                loadStudents();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void updateStudent() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a student first!");
            return;
        }

        int studentId = Integer.parseInt(table.getValueAt(selectedRow, 1).toString());

        dispose();
        new UpdateStudentGUI(studentId);  // We'll create this next
    }
}