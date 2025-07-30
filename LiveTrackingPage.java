import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LiveTrackingPage extends JFrame {

    private final JTextField orderIdField;
    private final JTextArea orderDetailsArea;
    private final JTextArea addressArea;

    public LiveTrackingPage() {
        setTitle("Order Tracking");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create panel for input and buttons
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Order ID label and field
        JLabel orderIdLabel = new JLabel("Enter Order ID:");
        orderIdLabel.setFont(new Font("Arial", Font.BOLD, 14));
        orderIdField = new JTextField();
        orderIdField.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add components to input panel
        inputPanel.add(orderIdLabel);
        inputPanel.add(orderIdField);

        // Create and configure the View Details button
        JButton viewDetailsButton = new JButton("View Details");
        viewDetailsButton.setFont(new Font("Arial", Font.BOLD, 14));
        viewDetailsButton.setPreferredSize(new Dimension(150, 40));
        viewDetailsButton.setBackground(Color.BLUE);
        viewDetailsButton.setForeground(Color.WHITE);
        viewDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String orderId = orderIdField.getText();
                // Fetch order details and address based on orderId
                String orderDetails = getOrderDetails(orderId);
                String address = getAddress(orderId);

                // Display the fetched details in the text areas
                orderDetailsArea.setText(orderDetails);
                addressArea.setText(address);
            }
        });

        // Create and configure the text areas
        orderDetailsArea = new JTextArea();
        orderDetailsArea.setFont(new Font("Arial", Font.PLAIN, 14));
        orderDetailsArea.setEditable(false);
        orderDetailsArea.setBorder(BorderFactory.createTitledBorder("Order Details"));

        addressArea = new JTextArea();
        addressArea.setFont(new Font("Arial", Font.PLAIN, 14));
        addressArea.setEditable(false);
        addressArea.setBorder(BorderFactory.createTitledBorder("Address"));

        // Add components to frame
        add(inputPanel, BorderLayout.NORTH);
        add(viewDetailsButton, BorderLayout.SOUTH);

        // Create a panel to hold the text areas and add it to the frame
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridLayout(2, 1, 10, 10));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        detailsPanel.add(new JScrollPane(orderDetailsArea));
        detailsPanel.add(new JScrollPane(addressArea));

        add(detailsPanel, BorderLayout.CENTER);

        // Display the frame
        setVisible(true);
    }

    private String getOrderDetails(String orderId) {
        // Dummy implementation for demonstration purposes
        // Replace this with actual logic to get order details based on orderId
        if (orderId.trim().isEmpty()) {
            return "Please enter an order ID.";
        }
        return "Order ID: " + orderId + "\nItem: Sample Product\nQuantity: 1\nStatus: Shipped"; // Replace with actual order details
    }

    private String getAddress(String orderId) {
        // Dummy implementation for demonstration purposes
        // Replace this with actual logic to get address based on orderId
        if (orderId.trim().isEmpty()) {
            return "Please enter an order ID.";
        }
        return "123 Main Street, Anytown, USA"; // Replace with actual address
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LiveTrackingPage::new);
    }
}
