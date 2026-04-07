import javax.swing.*;

public class EmailService {

    public static void sendEmail(String toEmail, String subject, String messageText) {

        // 🔹 Simulate sending email (Console Output)
        System.out.println("==================================");
        System.out.println("📧 Simulated Email Sent");
        System.out.println("To      : " + toEmail);
        System.out.println("Subject : " + subject);
        System.out.println("Message : ");
        System.out.println(messageText);
        System.out.println("==================================");

        // 🔹 Optional popup (for demo purpose)
        JOptionPane.showMessageDialog(
                null,
                "Email sent successfully (Simulated)",
                "Email Notification",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}