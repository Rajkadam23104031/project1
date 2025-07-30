import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Home extends JFrame {

    public Home() {
        setTitle("Courier App Home");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        BackgroundPanel backgroundPanel = new BackgroundPanel("C:\\Users\\harsh\\IdeaProjects\\java mini project 1\\src\\courierhoomepageimage.jpeg");
        backgroundPanel.setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.decode("#4682B4"));
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel headerLabel = new JLabel("Courier App Home");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.BLACK);
        headerPanel.add(headerLabel);
        backgroundPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton[] buttons = new JButton[6];
        String[] buttonNames = {"Admin", "User", "Feedback", "About Us", "Helpline", "Log Out"};
        Color[] buttonColors = {Color.darkGray, Color.darkGray, Color.darkGray, Color.darkGray, Color.darkGray, Color.darkGray};

        Dimension buttonSize = new Dimension(200, 50);

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(buttonNames[i]);
            buttons[i].setFont(new Font("Arial", Font.BOLD, 16));
            buttons[i].setPreferredSize(buttonSize);
            buttons[i].setBackground(buttonColors[i]);
            buttons[i].setForeground(Color.WHITE);

            gbc.gridx = 0;
            gbc.gridy = i;
            contentPanel.add(buttons[i], gbc);
        }

        buttons[0].addActionListener(e -> {
            dispose();
            new Admin().setVisible(false);
        });
        buttons[1].addActionListener(e -> {
            dispose();
            new RegistrationForm().setVisible(true);
        });
        buttons[2].addActionListener(e -> {
            dispose();
            new Feedback().setVisible(false);
        });
        buttons[3].addActionListener(e -> {
            dispose();
            new AboutUs().setVisible(false);
        });
        buttons[4].addActionListener(e -> {
            dispose();
            new Helpline().setVisible(false);
        });
        buttons[5].addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(Home.this,
                    "Are you sure you want to log out?", "Log Out",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new Login().setVisible(true);
            }
        });

        backgroundPanel.add(contentPanel, BorderLayout.CENTER);
        add(backgroundPanel);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Home().setVisible(true));
    }
}

class ImagePanel extends JPanel {
    private BufferedImage backgroundImage;

    public ImagePanel(String filePath) {
        try {
            backgroundImage = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

class Admin extends JFrame {
    public Admin() {
        setTitle("Admin Home");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        new AdminLogin().setVisible(true);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

class User extends JFrame {
    public User() {
        setTitle("User Mode");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

class Feedback extends JFrame {
    public Feedback() {
        setTitle("Feedback");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        new FeedbackForm().setVisible(true);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

class AboutUs extends JFrame {
    public AboutUs() {
        setTitle("About Us");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        new AboutUsPage().setVisible(true);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

class Helpline extends JFrame {
    public Helpline() {
        setTitle("Helpline");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        new contactline().setVisible(true);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
