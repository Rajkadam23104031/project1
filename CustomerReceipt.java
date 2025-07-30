import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerReceipt extends JFrame {

    private final JTextField nameField;
    private final JTextField stateField;
    private final JTextField cityField;
    private final JTextField itemTypeField;
    private final JTextField priceField;
    private final JTextField trackingIdField;
    private JLabel messageLabel = null;

    public CustomerReceipt() {
        setTitle("Generate Customer Receipt");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the panel with a background image
        ImagePanel imagePanel = new ImagePanel("C:\\Users\\harsh\\IdeaProjects\\java mini project 1\\src\\customerreciept.jpg");
        imagePanel.setLayout(new BorderLayout());

        // Create panel for input fields
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 2, 10, 10));
        inputPanel.setOpaque(false);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Labels and fields
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel stateLabel = new JLabel("State:");
        stateLabel.setFont(new Font("Arial", Font.BOLD, 16));
        stateField = new JTextField();
        stateField.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel cityLabel = new JLabel("City:");
        cityLabel.setFont(new Font("Arial", Font.BOLD, 16));
        cityField = new JTextField();
        cityField.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel itemTypeLabel = new JLabel("Item Type:");
        itemTypeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        itemTypeField = new JTextField();
        itemTypeField.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        priceField = new JTextField();
        priceField.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel trackingIdLabel = new JLabel("Tracking ID:");
        trackingIdLabel.setFont(new Font("Arial", Font.BOLD, 16));
        trackingIdField = new JTextField();
        trackingIdField.setFont(new Font("Arial", Font.PLAIN, 16));

        // Add labels and fields to the panel
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(stateLabel);
        inputPanel.add(stateField);
        inputPanel.add(cityLabel);
        inputPanel.add(cityField);
        inputPanel.add(itemTypeLabel);
        inputPanel.add(itemTypeField);
        inputPanel.add(priceLabel);
        inputPanel.add(priceField);
        inputPanel.add(trackingIdLabel);
        inputPanel.add(trackingIdField);

        // Create and configure the Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        submitButton.setPreferredSize(new Dimension(120, 40));
        submitButton.setBackground(Color.GREEN);
        submitButton.setForeground(Color.WHITE);
        submitButton.addActionListener(e -> {
            String name = nameField.getText();
            String state = stateField.getText();
            String city = cityField.getText();
            String itemType = itemTypeField.getText();
            String price = priceField.getText();
            String trackingId = trackingIdField.getText();

            if (validateInput(name, state, city, itemType, price, trackingId)) {
                String receipt = "Receipt:\n" +
                        "Name: " + name + "\n" +
                        "State: " + state + "\n" +
                        "City: " + city + "\n" +
                        "Item Type: " + itemType + "\n" +
                        "Price: " + price + "\n" +
                        "Tracking ID: " + trackingId;
                JOptionPane.showMessageDialog(CustomerReceipt.this, receipt, "Receipt", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
            } else {
                messageLabel.setText("Invalid input. Please try again.");
            }
        });

        // Create and configure the Back button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> {
            new AdminHomepage().setVisible(true);// Logic to go back to the previous screen or close the current frame
            dispose(); // Closes the current frame
        });

        // Create and configure the message label
        messageLabel = new JLabel("");
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        messageLabel.setForeground(Color.RED);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Create a panel for buttons and add them
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Keep the panel transparent
        buttonPanel.add(submitButton);
        buttonPanel.add(backButton);

        // Add components to the image panel
        imagePanel.add(inputPanel, BorderLayout.CENTER);
        imagePanel.add(messageLabel, BorderLayout.NORTH);
        imagePanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the image panel to the frame
        add(imagePanel);

        // Display the frame
        setVisible(true);
    }

    private boolean validateInput(String name, String state, String city, String itemType, String price, String trackingId) {
        return !name.trim().isEmpty() && !state.trim().isEmpty() && !city.trim().isEmpty() &&
                !itemType.trim().isEmpty() && !price.trim().isEmpty() && !trackingId.trim().isEmpty();
    }

    private void clearFields() {
        nameField.setText("");
        stateField.setText("");
        cityField.setText("");
        itemTypeField.setText("");
        priceField.setText("");
        trackingIdField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CustomerReceipt::new);
    }

    // Custom JPanel class to display a background image
    class ImagePanel extends JPanel {
        private final Image backgroundImage;

        public ImagePanel(String imagePath) {
            backgroundImage = new ImageIcon(imagePath).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

