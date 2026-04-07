import javax.swing.*;
import java.awt.*;

class BackgroundPanel extends JPanel {

    private Image backgroundImage;

    public BackgroundPanel() {
        backgroundImage = new ImageIcon("images/background.jpg").getImage();
        setLayout(new GridBagLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(backgroundImage != null){
            g.drawImage(backgroundImage,0,0,getWidth(),getHeight(),this);
        }
    }
}

public class BaseFrame extends JFrame {

    protected JPanel card;

    public BaseFrame(String title){

        setTitle("COURSE REGISTRATION MANAGEMENT SYSTEM");
        setSize(900,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JLabel header = new JLabel(title,JLabel.CENTER);
        header.setFont(new Font("Arial",Font.BOLD,24));
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(44,62,80));
        header.setOpaque(true);
        header.setPreferredSize(new Dimension(100,60));

        add(header,BorderLayout.NORTH);

        // Background Panel
        card = new BackgroundPanel();
        add(card,BorderLayout.CENTER);
    }

    protected JTextField createTextField(){

        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(200,35));
        return field;
    }

    protected JPasswordField createPasswordField(){

        JPasswordField field = new JPasswordField();
        field.setPreferredSize(new Dimension(200,35));
        return field;
    }

    protected JButton createButton(String text){

        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(200,40));
        btn.setFocusPainted(false);

        btn.setBackground(new Color(52,152,219));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial",Font.BOLD,14));

        return btn;
    }
}