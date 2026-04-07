import javax.swing.*;
import java.awt.*;

public class StudentDashboard extends BaseFrame {

    private int studentId;

    public StudentDashboard(int studentId) {

        super("STUDENT DASHBOARD");
        this.studentId = studentId;

        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 🔹 Register Course
        gbc.gridy = 0;
        JButton registerBtn = createButton("Register Course");
        card.add(registerBtn, gbc);

        // 🔹 My Courses
        gbc.gridy = 1;
        JButton myCoursesBtn = createButton("My Courses");
        card.add(myCoursesBtn, gbc);

        // 🔹 Payment History
        gbc.gridy = 2;
        JButton paymentHistoryBtn = createButton("Payment History");
        card.add(paymentHistoryBtn, gbc);

        // 🔹 My Profile
        gbc.gridy = 3;
        JButton profileBtn = createButton("My Profile");
        card.add(profileBtn, gbc);

        // 🔹 Update Password
        gbc.gridy = 4;
        JButton updatePassBtn = createButton("Update Password");
        card.add(updatePassBtn, gbc);

        // 🔹 Logout
        gbc.gridy = 5;
        JButton logoutBtn = createButton("Logout");
        card.add(logoutBtn, gbc);

        // ================= ACTIONS =================

        registerBtn.addActionListener(e -> {
            dispose();
            new RegisterCourseGUI(studentId);
        });

        myCoursesBtn.addActionListener(e -> {
            dispose();
            new ViewRegisteredCoursesGUI(studentId);
        });

        paymentHistoryBtn.addActionListener(e -> {
            dispose();
            new ViewPaymentHistoryGUI(studentId);
        });

        profileBtn.addActionListener(e -> {
            dispose();
            new StudentProfileGUI(studentId);
        });

        updatePassBtn.addActionListener(e -> {
            dispose();
            new UpdatePasswordGUI(studentId);
        });

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginPage();
        });

        setVisible(true);
    }
}