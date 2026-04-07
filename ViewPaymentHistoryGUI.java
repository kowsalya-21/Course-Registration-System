import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ViewPaymentHistoryGUI extends BaseFrame {

    private int studentId;
    private JTable table;

    public ViewPaymentHistoryGUI(int studentId) {

        super("PAYMENT HISTORY");
        this.studentId = studentId;

        loadPayments();
        setVisible(true);
    }

    private void loadPayments() {

        String[] columns = {
                "Transaction ID",
                "Course Name",
                "Amount",
                "Payment Date",
                "Status"
        };

        String[][] data = {};

        try {
            Connection conn = DBConnection.getConnection();

            String sql = """
                    SELECT p.transaction_id,
                           c.course_name,
                           p.amount,
                           p.payment_date,
                           p.status
                    FROM payments p
                    JOIN courses c ON p.course_id = c.course_id
                    WHERE p.student_id = ?
                    """;

            PreparedStatement pst = conn.prepareStatement(
                    sql,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            pst.setInt(1, studentId);

            ResultSet rs = pst.executeQuery();

            rs.last();
            int rows = rs.getRow();
            rs.beforeFirst();

            data = new String[rows][5];

            int i = 0;
            while (rs.next()) {

                data[i][0] = rs.getString("transaction_id"); // can be null
                data[i][1] = rs.getString("course_name");
                data[i][2] = "₹ " + rs.getString("amount");
                data[i][3] = rs.getString("payment_date");
                data[i][4] = rs.getString("status");

                i++;
            }

            conn.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

        table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton deleteBtn = createButton("Delete Selected");
        JButton backBtn = createButton("Back");

        deleteBtn.addActionListener(e -> deletePayment());
        backBtn.addActionListener(e -> {
            dispose();
            new StudentDashboard(studentId);
        });

        card.setLayout(new BorderLayout());
        card.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(deleteBtn);
        bottomPanel.add(backBtn);

        card.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void deletePayment() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a payment first!");
            return;
        }

        String transactionId = (String) table.getValueAt(selectedRow, 0);
        String courseName = (String) table.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this payment?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst;

            if (transactionId == null || transactionId.trim().isEmpty()) {

                // 🔥 If transaction ID is null, delete using student + course
                String sql = """
                        DELETE FROM payments
                        WHERE student_id = ?
                        AND course_id = (
                            SELECT course_id FROM courses WHERE course_name = ?
                        )
                        LIMIT 1
                        """;

                pst = conn.prepareStatement(sql);
                pst.setInt(1, studentId);
                pst.setString(2, courseName);

            } else {

                String sql = "DELETE FROM payments WHERE transaction_id=?";
                pst = conn.prepareStatement(sql);
                pst.setString(1, transactionId);
            }

            pst.executeUpdate();
            conn.close();

            JOptionPane.showMessageDialog(this, "Payment Deleted Successfully!");

            dispose();
            new ViewPaymentHistoryGUI(studentId); // refresh table

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}