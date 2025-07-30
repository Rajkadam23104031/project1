import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class Statement extends JFrame {

    private final JTextField nameField;
    private final JTextField stateField;
    private final JTextField cityField;
    private final JComboBox<String> itemTypeComboBox;
    private final JTextField priceField;
    private JLabel messageLabel = null;


    private final HashMap<String, String> itemPriceMap = new HashMap<>();

    public Statement() {
        setTitle("Customer Receipt");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        itemPriceMap.put("Standard", "500");
        itemPriceMap.put("Express", "1000");
        itemPriceMap.put("Overnight", "5000");


        ImagePanel imagePanel = new ImagePanel("C:\\Users\\harsh\\IdeaProjects\\java mini project 1\\src\\customerreciept.jpg");
        imagePanel.setLayout(new BorderLayout());


        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 2, 10, 10));
        inputPanel.setOpaque(false);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        nameField = createInputField(inputPanel, "Name:");
        stateField = createInputField(inputPanel, "State:");
        cityField = createInputField(inputPanel, "City:");


        JLabel itemTypeLabel = new JLabel("Courier Type:");
        itemTypeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        itemTypeComboBox = new JComboBox<>(new String[]{"Standard", "Express", "Overnight"});
        itemTypeComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        itemTypeComboBox.addActionListener(e -> updatePriceField());
        inputPanel.add(itemTypeLabel);
        inputPanel.add(itemTypeComboBox);


        priceField = createInputField(inputPanel, "Price:");
        priceField.setEditable(false); // User cannot manually edit price


        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        submitButton.setPreferredSize(new Dimension(120, 40));
        submitButton.setBackground(Color.GREEN);
        submitButton.setForeground(Color.WHITE);
        submitButton.addActionListener(e -> {
            String name = nameField.getText();
            String state = stateField.getText();
            String city = cityField.getText();
            String itemType = (String) itemTypeComboBox.getSelectedItem();
            String price = priceField.getText();

            if (validateInput(name, state, city, itemType, price)) {
                String receipt = "Receipt:\n" +
                        "Name: " + name + "\n" +
                        "State: " + state + "\n" +
                        "City: " + city + "\n" +
                        "Courier Type: " + itemType + "\n" +
                        "Price: " + price;
                JOptionPane.showMessageDialog(Statement.this, receipt, "Receipt", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
            } else {
                messageLabel.setText("Invalid input. Please try again.");
            }
        });


        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> {
            new UserHomepage().setVisible(true);
            dispose();
        });


        messageLabel = new JLabel("");
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        messageLabel.setForeground(Color.RED);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Keep the panel transparent
        buttonPanel.add(submitButton);
        buttonPanel.add(backButton);


        imagePanel.add(inputPanel, BorderLayout.CENTER);
        imagePanel.add(messageLabel, BorderLayout.NORTH);
        imagePanel.add(buttonPanel, BorderLayout.SOUTH);


        add(imagePanel);


        setVisible(true);
    }

    private JTextField createInputField(JPanel panel, String labelText) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        JTextField textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(label);
        panel.add(textField);
        return textField;
    }

    private boolean validateInput(String name, String state, String city, String itemType, String price) {
        return !name.trim().isEmpty() && !state.trim().isEmpty() && !city.trim().isEmpty() &&
                !itemType.trim().isEmpty() && !price.trim().isEmpty();
    }

    private void clearFields() {
        nameField.setText("");
        stateField.setText("");
        cityField.setText("");
        itemTypeComboBox.setSelectedIndex(0);
        priceField.setText(itemPriceMap.get("Standard"));
    }


    private void updatePriceField() {
        String selectedItem = (String) itemTypeComboBox.getSelectedItem();
        String price = itemPriceMap.get(selectedItem);
        priceField.setText(price);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Statement::new);
    }


    class ImagePanel extends JPanel {
        private final Image backgroundImage;

        public ImagePanel(String imagePath) {
            backgroundImage = new ImageIcon(imagePath).getImage();
            if (backgroundImage == null) {
                System.err.println("Image not found at " + imagePath);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
