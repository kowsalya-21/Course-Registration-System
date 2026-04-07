import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AdminDashboard extends BaseFrame {

    public AdminDashboard() {

        super("Admin Dashboard");

        card.setLayout(new GridBagLayout());

        // Main dashboard panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(255,255,255,200)); // transparent white

        // 🔹 Make dashboard wider
        panel.setBorder(BorderFactory.createEmptyBorder(40,80,40,80));
        panel.setPreferredSize(new Dimension(420,520));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12,12,12,12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        int totalStudents = 0;
        int totalCourses = 0;
        int totalRegistrations = 0;

        try {

            Connection conn = DBConnection.getConnection();
            Statement st = conn.createStatement();

            ResultSet rs1 = st.executeQuery("SELECT COUNT(*) FROM students");
            if(rs1.next()) totalStudents = rs1.getInt(1);

            ResultSet rs2 = st.executeQuery("SELECT COUNT(*) FROM courses");
            if(rs2.next()) totalCourses = rs2.getInt(1);

            ResultSet rs3 = st.executeQuery("SELECT COUNT(*) FROM registrations");
            if(rs3.next()) totalRegistrations = rs3.getInt(1);

            conn.close();

        } catch(Exception e){
            JOptionPane.showMessageDialog(this,e.getMessage());
        }

        // 🔹 Title
        gbc.gridy = 0;
        JLabel title = new JLabel("COURSE STATISTICS", JLabel.CENTER);
        title.setFont(new Font("Arial",Font.BOLD,24));
        panel.add(title,gbc);

        // 🔹 Statistics
        gbc.gridy = 1;
        JLabel students = new JLabel("Total Students Enrolled : " + totalStudents);
        students.setFont(new Font("Arial",Font.PLAIN,15));
        panel.add(students,gbc);

        gbc.gridy = 2;
        JLabel courses = new JLabel("Total Courses Available : " + totalCourses);
        courses.setFont(new Font("Arial",Font.PLAIN,15));
        panel.add(courses,gbc);

        gbc.gridy = 3;
        JLabel regs = new JLabel("Total Course Registrations : " + totalRegistrations);
        regs.setFont(new Font("Arial",Font.PLAIN,15));
        panel.add(regs,gbc);

        // 🔹 Buttons
        gbc.gridy = 4;
        JButton addStudent = createButton("Add Student");
        panel.add(addStudent,gbc);

        gbc.gridy = 5;
        JButton viewStudents = createButton("View Students");
        panel.add(viewStudents,gbc);

        gbc.gridy = 6;
        JButton addCourse = createButton("Add Course");
        panel.add(addCourse,gbc);

        gbc.gridy = 7;
        JButton viewCourses = createButton("View Courses");
        panel.add(viewCourses,gbc);

        gbc.gridy = 8;
        JButton viewRegs = createButton("View All Registrations");
        panel.add(viewRegs,gbc);

        gbc.gridy = 9;
        JButton logout = createButton("Logout");
        panel.add(logout,gbc);

        card.add(panel);

        // 🔹 Button Actions
        addStudent.addActionListener(e -> {
            dispose();
            new AddStudentGUI();
        });

        viewStudents.addActionListener(e -> {
            dispose();
            new ViewStudentsGUI();
        });

        addCourse.addActionListener(e -> {
            dispose();
            new AddCourseGUI();
        });

        viewCourses.addActionListener(e -> {
            dispose();
            new ViewCoursesGUI();
        });

        viewRegs.addActionListener(e -> {
            dispose();
            new ViewAllRegistrationsGUI();
        });

        logout.addActionListener(e -> {
            dispose();
            new LoginPage();
        });

        setVisible(true);
    }
}