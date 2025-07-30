import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class CustomerTracking extends JFrame {

    private final JTextField deliveryIdField;
    private final JLabel statusLabel;
    private final Map<String, String> deliveryStatusMap;
    private final JPanel statusPanel;
    private final JTextField updateStatusField;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/customerregister";
    private static final String USER = "root";
    private static final String PASS = "9869";

    public CustomerTracking() {
        setTitle("Customer Tracking Update");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());


        deliveryStatusMap = new HashMap<>();
        initializeDeliveryStatusMap();


        ImagePanel trackingPanel = new ImagePanel("C:\\Users\\harsh\\IdeaProjects\\java mini project 1\\src\\couriertrackingimagefor admin.jpeg"); // Load background image
        trackingPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;


        JLabel deliveryIdLabel = new JLabel("Enter Order ID:");
        deliveryIdLabel.setFont(new Font("Arial", Font.BOLD, 24));
        deliveryIdLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        trackingPanel.add(deliveryIdLabel, gbc);

        deliveryIdField = new JTextField(15);
        deliveryIdField.setFont(new Font("Arial", Font.PLAIN, 16));
        deliveryIdField.setForeground(Color.BLACK);
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
        statusPanel.setOpaque(false);


        String[] statusTexts = {"Ordered", "Shipped", "Out for Delivery", "Arriving"};
        for (String text : statusTexts) {
            JPanel stepPanel = new JPanel();
            stepPanel.setLayout(new BorderLayout());
            stepPanel.setOpaque(false);
            JLabel stepLabel = new JLabel(text, SwingConstants.CENTER);
            stepLabel.setFont(new Font("Arial", Font.BOLD, 14));
            stepLabel.setForeground(Color.WHITE);
            stepPanel.add(stepLabel, BorderLayout.CENTER);
            stepPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            statusPanel.add(stepPanel);
        }

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        trackingPanel.add(statusPanel, gbc);


        statusLabel = new JLabel("Enter a Order ID to check status");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setForeground(Color.WHITE);
        gbc.gridy = 3;
        trackingPanel.add(statusLabel, gbc);


        JLabel updateLabel = new JLabel("Update Status:");
        updateLabel.setForeground(Color.WHITE);
        gbc.gridy = 4;
        trackingPanel.add(updateLabel, gbc);

        updateStatusField = new JTextField(15);
        updateStatusField.setForeground(Color.BLACK);
        gbc.gridy = 5;
        trackingPanel.add(updateStatusField, gbc);

        JButton updateButton = new JButton("Update Status");
        updateButton.addActionListener(e -> updateDeliveryStatus());
        updateButton.setForeground(Color.BLACK);
        gbc.gridy = 6;
        trackingPanel.add(updateButton, gbc);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            dispose();
            new AdminHomepage().setVisible(true);
        });
        backButton.setForeground(Color.BLACK);
        gbc.gridy = 7;
        trackingPanel.add(backButton, gbc);


        JScrollPane scrollPane = new JScrollPane(trackingPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);


        add(scrollPane, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeDeliveryStatusMap() {

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, status FROM courierdetails")) {

            while (rs.next()) {
                deliveryStatusMap.put(rs.getString("id"), rs.getString("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching delivery statuses from the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStatusPanel() {
        String deliveryId = deliveryIdField.getText().trim();
        String status = deliveryStatusMap.get(deliveryId);


        for (Component comp : statusPanel.getComponents()) {
            JPanel stepPanel = (JPanel) comp;
            stepPanel.setBackground(null);
            ((JLabel) stepPanel.getComponent(0)).setForeground(Color.BLACK);
        }

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

            if (index >= 0) {
                JPanel stepPanel = (JPanel) statusPanel.getComponent(index);
                stepPanel.setBackground(Color.black);
                ((JLabel) stepPanel.getComponent(0)).setForeground(Color.GREEN);
            }
        }
    }

    private void updateDeliveryStatus() {
        String deliveryId = deliveryIdField.getText().trim();
        String newStatus = updateStatusField.getText().trim();


        if (newStatus.isEmpty() || !isValidStatus(newStatus)) {
            statusLabel.setText("Invalid status! Please enter a valid status (Ordered, Shipped, Out for Delivery, Arriving).");
            return;
        }


        String updateSql = "UPDATE courierdetails SET status = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(updateSql)) {

            pstmt.setString(1, newStatus);
            pstmt.setInt(2, Integer.parseInt(deliveryId));

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                deliveryStatusMap.put(deliveryId, newStatus);
                statusLabel.setText("Status updated successfully!");
                updateStatusField.setText("");


                updateStatusPanelWithColor(newStatus);
            } else {
                statusLabel.setText("Invalid Delivery ID!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("Error updating status in the database.");
        }
    }

    private void updateStatusPanelWithColor(String newStatus) {

        for (Component comp : statusPanel.getComponents()) {
            JPanel stepPanel = (JPanel) comp;
            stepPanel.setBackground(null); // Reset color
            ((JLabel) stepPanel.getComponent(0)).setForeground(Color.BLACK);
        }


        int index = switch (newStatus) {
            case "Ordered" -> 0;
            case "Shipped" -> 1;
            case "Out for Delivery" -> 2;
            case "Arriving" -> 3;
            default -> -1;
        };

        if (index >= 0) {
            JPanel stepPanel = (JPanel) statusPanel.getComponent(index);
            stepPanel.setBackground(Color.GREEN);
            ((JLabel) stepPanel.getComponent(0)).setForeground(Color.black);
        }
    }

    private boolean isValidStatus(String status) {

        return status.equalsIgnoreCase("Ordered") || status.equalsIgnoreCase("Shipped")
                || status.equalsIgnoreCase("Out for Delivery") || status.equalsIgnoreCase("Arriving");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CustomerTracking::new);
    }
}
