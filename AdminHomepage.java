import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class AdminHomepage extends JFrame {
    public AdminHomepage() {
        setTitle("Admin Home Page");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        BackgroundPanel backgroundPanel = new BackgroundPanel("C:\\Users\\harsh\\IdeaProjects\\java mini project 1\\src\\courierhoomepageimage.jpeg");
        backgroundPanel.setLayout(new BorderLayout());


        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.decode("#4682B4"));
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel headerLabel = new JLabel("Admin Home Page");
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


        JButton[] buttons = new JButton[3];
        String[] buttonNames = {"Booking Status",  "Customer Tracking Update", "Log Out"};
        Color[] buttonColors = {Color.darkGray, Color.darkGray, Color.darkGray};

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

            String orderIDStr = JOptionPane.showInputDialog(AdminHomepage.this, "Enter Order ID:", "Order ID Input", JOptionPane.QUESTION_MESSAGE);

            if (orderIDStr != null && !orderIDStr.trim().isEmpty()) {
                try {
                    int orderID = Integer.parseInt(orderIDStr.trim());
                    dispose();
                    new OrderStatus(orderID).setVisible(true);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(AdminHomepage.this, "Invalid Order ID. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        buttons[1].addActionListener(e -> {
            dispose();
            new CustomerTracking().setVisible(true);
        });
        buttons[2].addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(AdminHomepage.this,
                    "Are you sure you want to log out?", "Log Out",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new AdminLogin().setVisible(true);
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
        SwingUtilities.invokeLater(() -> new AdminHomepage().setVisible(true));
    }
}


class backgroundImagePanel extends JPanel {
    private BufferedImage backgroundImage;

    public backgroundImagePanel(String filePath) {
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


class BookingStatus extends JFrame {
    public BookingStatus() {
        setTitle("Booking Status");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
    }
}

class GenerateCustomerReceipt extends JFrame {
    public GenerateCustomerReceipt() {
        setTitle("Customer Receipt");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
    }
}

class CustomerTrackingUpdate extends JFrame {
    public CustomerTrackingUpdate() {
        setTitle("Customer Tracking Update");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
    }
}