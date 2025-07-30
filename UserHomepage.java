import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class UserHomepage extends JFrame {
    public UserHomepage() {
        setTitle("User Home Page");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        BackgroundPanel backgroundPanel = new BackgroundPanel("C:\\Users\\harsh\\IdeaProjects\\java mini project 1\\src\\courierhoomepageimage.jpeg");
        backgroundPanel.setLayout(new BorderLayout());


        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.decode("#4682B4"));
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel headerLabel = new JLabel("User Home Page");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        backgroundPanel.add(headerPanel, BorderLayout.NORTH);


        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;


        JButton[] buttons = new JButton[4];
        String[] buttonNames = {"Courier Details", "Track Your Parcel", "Receipt", "Log Out"};
        Color[] buttonColors = {Color.darkGray, Color.darkGray};
        Dimension buttonSize = new Dimension(200, 50);


        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(buttonNames[i]);
            buttons[i].setFont(new Font("Arial", Font.BOLD, 16));
            buttons[i].setPreferredSize(buttonSize);
            buttons[i].setBackground(buttonColors[i % buttonColors.length]);
            buttons[i].setForeground(Color.WHITE);

            gbc.gridx = 0;
            gbc.gridy = i;
            contentPanel.add(buttons[i], gbc);
        }


        buttons[0].addActionListener(e -> {
            dispose();
            new CourierDetailApp().setVisible(true);
        });
        buttons[1].addActionListener(e -> {
            dispose();
            new TrackDeliveryStatus().setVisible(true);
        });


        buttons[2].addActionListener(e -> {

            String orderIDStr = JOptionPane.showInputDialog(UserHomepage.this, "Enter Order ID:", "Order ID Input", JOptionPane.QUESTION_MESSAGE);

            if (orderIDStr != null && !orderIDStr.trim().isEmpty()) {
                try {
                    int orderID = Integer.parseInt(orderIDStr.trim());
                    dispose();
                    new OrderStatus(orderID).setVisible(true);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(UserHomepage.this, "Invalid Order ID. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        buttons[3].addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(UserHomepage.this,
                    "Are you sure you want to log out?", "Log Out",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new Login().setVisible(true);
            }
        });

        backgroundPanel.add(contentPanel, BorderLayout.CENTER);


        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(100, 30));
        backButton.addActionListener(e -> {
            dispose();
            new Home().setVisible(true);
        });
        bottomPanel.add(backButton);
        backgroundPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(backgroundPanel);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserHomepage().setVisible(true));
    }
}


class BackgroundPanel extends JPanel {
    private BufferedImage backgroundImage;

    public BackgroundPanel(String filePath) {
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


class CourierDetailsApp extends JFrame {
    public CourierDetailsApp() {
        setTitle("Courier Details");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setVisible(true);
    }
}


class Tracking extends JFrame {
    public Tracking() {
        setTitle("Tracking");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setVisible(true);
    }
}


class Receipt extends JFrame {
    public Receipt() {
        setTitle("Receipt");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
