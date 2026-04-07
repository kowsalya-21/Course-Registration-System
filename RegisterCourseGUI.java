import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class RegisterCourseGUI extends BaseFrame {

    JTable table;
    DefaultTableModel model;
    int studentId;

    public RegisterCourseGUI(int studentId) {

        super("REGISTER COURSE");

        this.studentId = studentId;

        String[] columns = {
                "ID",
                "Course Name",
                "Department",
                "Duration",
                "Credits",
                "Fee"
        };

        model = new DefaultTableModel(columns,0);
        table = new JTable(model);

        table.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(120,120,1000,420);

        card.setLayout(null);
        card.add(scrollPane);

        JButton registerBtn = createButton("Register Course");
        JButton backBtn = createButton("Back");

        registerBtn.setBounds(420,580,200,40);
        backBtn.setBounds(650,580,200,40);

        card.add(registerBtn);
        card.add(backBtn);

        loadCourses();

        registerBtn.addActionListener(e -> {

            int row = table.getSelectedRow();

            if(row == -1){
                JOptionPane.showMessageDialog(this,"Select a course first");
                return;
            }

            int courseId = (int) model.getValueAt(row,0);

            dispose();

            // Redirect to Payment Page
            new PaymentGUI(studentId, courseId);

        });

        backBtn.addActionListener(e -> {

            dispose();
            new StudentDashboard(studentId);

        });

        setVisible(true);
    }

    private void loadCourses(){

        try{

            Connection conn = DBConnection.getConnection();

            String sql = "SELECT * FROM courses";

            PreparedStatement pst = conn.prepareStatement(sql);

            ResultSet rs = pst.executeQuery();

            model.setRowCount(0);

            while(rs.next()){

                model.addRow(new Object[]{
                        rs.getInt("course_id"),
                        rs.getString("course_name"),
                        rs.getString("department"),
                        rs.getString("duration"),
                        rs.getInt("credits"),
                        rs.getDouble("fee")
                });

            }

            conn.close();

        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(this,ex.getMessage());
        }

    }
}