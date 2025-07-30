import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class TrackDeliveryStatus extends JFrame {

    private final JTextField deliveryIdField;
    private final JLabel statusLabel;
    private final Map<String, String> deliveryStatusMap;
    private final JPanel statusPanel;


    private static final String DB_URL = "jdbc:mysql://localhost:3306/customerregister";
    private static final String USER = "root";
    private static final String PASS = "9869";

    public TrackDeliveryStatus() {
        setTitle("Track Delivery Status");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        deliveryStatusMap = new HashMap<>();
        initializeDeliveryStatusMap();


        BackgroundPanel backgroundPanel = new BackgroundPanel("C:\\Users\\harsh\\IdeaProjects\\java mini project 1\\src\\couriertrackingimageforuser.jpeg");
        backgroundPanel.setLayout(new BorderLayout());


        JPanel trackingPanel = new JPanel();
        trackingPanel.setLayout(new GridBagLayout());
        trackingPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;


        JLabel deliveryIdLabel = new JLabel("Enter Order ID:");
        deliveryIdLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST; // Align right
        trackingPanel.add(deliveryIdLabel, gbc);

        deliveryIdField = new JTextField(15);
        deliveryIdField.setFont(new Font("Arial", Font.PLAIN, 16));
        deliveryIdField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        trackingPanel.add(deliveryIdField, gbc);


        JButton checkStatusButton = new JButton("Check Status");
        checkStatusButton.setFont(new Font("Arial", Font.BOLD, 18));
        checkStatusButton.setBackground(Color.BLUE);
        checkStatusButton.setForeground(Color.WHITE);
        checkStatusButton.addActionListener(e -> updateStatusPanel());
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        trackingPanel.add(checkStatusButton, gbc);


        statusPanel = new JPanel();
        statusPanel.setLayout(new GridLayout(1, 4, 10, 10));
        statusPanel.setBackground(new Color(0, 0, 0, 0));


        String[] statusTexts = {"Ordered", "Shipped", "Out for Delivery", "Arriving"};
        for (String text : statusTexts) {
            JPanel stepPanel = new JPanel();
            stepPanel.setLayout(new BorderLayout());
            stepPanel.setPreferredSize(new Dimension(120, 80));
            JLabel stepLabel = new JLabel(text, SwingConstants.CENTER);
            stepLabel.setFont(new Font("Arial", Font.BOLD, 14));
            stepPanel.add(stepLabel, BorderLayout.CENTER);
            stepPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            statusPanel.add(stepPanel);
        }

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        trackingPanel.add(statusPanel, gbc);


        statusLabel = new JLabel("Enter a Delivery ID to check status");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 3;
        trackingPanel.add(statusLabel, gbc);


        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.setBackground(Color.black);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> {
            dispose();
            new UserHomepage().setVisible(true);
        });
        gbc.gridy = 4;
        trackingPanel.add(backButton, gbc);


        JScrollPane scrollPane = new JScrollPane(trackingPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);


        backgroundPanel.add(scrollPane, BorderLayout.CENTER);


        add(backgroundPanel);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeDeliveryStatusMap() {
        // Fetch delivery statuses from the database
        String query = "SELECT id, status FROM courierdetails";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String deliveryId = rs.getString("id");
                String status = rs.getString("status");
                deliveryStatusMap.put(deliveryId.toLowerCase(), status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching delivery statuses from the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStatusPanel() {
        String deliveryId = deliveryIdField.getText().trim().toLowerCase();
        String status = deliveryStatusMap.get(deliveryId);

        if (status == null) {
            statusLabel.setText("Delivery ID not found");
        } else {
            statusLabel.setText("Status: " + status);


            int index = switch (status) {
                case "Ordered" -> 0;
                case "Shipped" -> 1;
                case "Out for Delivery" -> 2;
                case "Arriving" -> 3;
                default -> -1;
            };

            // Update only the relevant step panel
            if (index >= 0) {
                JPanel stepPanel = (JPanel) statusPanel.getComponent(index);
                stepPanel.setBackground(Color.GREEN);
                ((JLabel) stepPanel.getComponent(0)).setForeground(Color.BLACK);
            }
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TrackDeliveryStatus().setVisible(true));
    }


    static class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {

            backgroundImage = new ImageIcon(imagePath).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
