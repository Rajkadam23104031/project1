import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

public class CourierDetailApp extends JFrame {

    private static final String URL = "jdbc:mysql://localhost:3306/customerregister";
    private static final String USER = "root";
    private static final String PASSWORD = "9869";
    private final JTextField recipientNameField;
    private final JTextArea packageDetailsField;
    private final JTextField valueField;
    private final JTextField priceField;
    private final JComboBox<String> weightCategoryCombo;
    private final HashMap<String, Double> priceMap = new HashMap<>();

    public CourierDetailApp() {
        setTitle("Courier Details");
        setSize(900, 700);
        setMinimumSize(new Dimension(700, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize price map based on weight categories
        priceMap.put("10 kg", 100.0);
        priceMap.put("50 kg", 400.0);
        priceMap.put("100 kg", 900.0);
        priceMap.put("500 kg", 3000.0);
        priceMap.put("1000 kg", 5000.0);
        priceMap.put("1000+ kg", 10000.0);

        BackgroundImagePanel backgroundPanel = new BackgroundImagePanel("C:\\Users\\harsh\\IdeaProjects\\java mini project 1\\src\\courierdetailsimage.jpeg");
        backgroundPanel.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        Font labelFont = new Font("Arial", Font.BOLD, 24);
        Font textFieldFont = new Font("Arial", Font.PLAIN, 22);

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel recipientLabel = new JLabel("Recipient Name:");
        recipientLabel.setFont(labelFont);
        recipientLabel.setForeground(Color.BLACK);
        formPanel.add(recipientLabel, gbc);
        recipientNameField = new JTextField(25);
        recipientNameField.setFont(textFieldFont);
        addTextFilter(recipientNameField, "[a-zA-Z ]+");
        gbc.gridx = 1;
        formPanel.add(recipientNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel packageDetailsLabel = new JLabel("Description of Contents:");
        packageDetailsLabel.setFont(labelFont);
        packageDetailsLabel.setForeground(Color.BLACK);
        formPanel.add(packageDetailsLabel, gbc);
        packageDetailsField = new JTextArea(3, 25);
        packageDetailsField.setFont(textFieldFont);
        gbc.gridx = 1;
        formPanel.add(new JScrollPane(packageDetailsField), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel weightLabel = new JLabel("Weight Category:");
        weightLabel.setFont(labelFont);
        weightLabel.setForeground(Color.BLACK);
        formPanel.add(weightLabel, gbc);
        weightCategoryCombo = new JComboBox<>(new String[]{"10 kg", "50 kg", "100 kg", "500 kg", "1000 kg", "1000+ kg"});
        weightCategoryCombo.setFont(textFieldFont);
        weightCategoryCombo.addActionListener(e -> updatePriceField());
        gbc.gridx = 1;
        formPanel.add(weightCategoryCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel valueLabel = new JLabel("Value of Contents:");
        valueLabel.setFont(labelFont);
        valueLabel.setForeground(Color.BLACK);
        formPanel.add(valueLabel, gbc);
        valueField = new JTextField(10);
        valueField.setFont(textFieldFont);
        addTextFilter(valueField, "\\d*");
        gbc.gridx = 1;
        formPanel.add(valueField, gbc);


        valueField.addCaretListener(e -> updatePriceField());

        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setFont(labelFont);
        priceLabel.setForeground(Color.BLACK);
        formPanel.add(priceLabel, gbc);
        priceField = new JTextField(10);
        priceField.setFont(textFieldFont);
        priceField.setEditable(false);
        gbc.gridx = 1;
        formPanel.add(priceField, gbc);

        JButton sendCourierButton = new JButton("Send Courier");
        sendCourierButton.setFont(new Font("Arial", Font.BOLD, 28));
        sendCourierButton.setBackground(Color.black);
        sendCourierButton.setForeground(Color.WHITE);
        sendCourierButton.addActionListener(e -> sendCourier());
        gbc.gridy = 5;
        formPanel.add(sendCourierButton, gbc);

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 28));
        backButton.setBackground(Color.black);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> {
            dispose();
            new UserHomepage().setVisible(true);
        });
        gbc.gridy = 6;
        formPanel.add(backButton, gbc);

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        backgroundPanel.add(scrollPane, BorderLayout.CENTER);
        add(backgroundPanel);
        setLocationRelativeTo(null);
        updatePriceField();
    }

    class BackgroundImagePanel extends JPanel {
        private BufferedImage backgroundImage;

        public BackgroundImagePanel(String filePath) {
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

    private void updatePriceField() {
        String selectedWeightCategory = (String) weightCategoryCombo.getSelectedItem();
        Double basePrice = priceMap.get(selectedWeightCategory);


        String valueText = valueField.getText().trim();
        double value = 1.0;


        try {
            value = Double.parseDouble(valueText);
        } catch (NumberFormatException e) {

            value = 1.0;
        }


        double finalPrice = basePrice * value;
        priceField.setText(String.format("%.2f", finalPrice));
    }

    private void sendCourier() {
        String recipientName = recipientNameField.getText().trim();
        String packageDetails = packageDetailsField.getText().trim();
        String selectedWeightCategory = (String) weightCategoryCombo.getSelectedItem();
        double value;
        try {
            value = Double.parseDouble(valueField.getText().trim());
        } catch (NumberFormatException e) {
            value = 0;
        }
        String price = priceField.getText().trim();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO courierdetails (RECIPENT_NAME, DESCRIPTION_OF_CONTENTS, WEIGHT, VALUE_OF_CONTENTS, PRICE) VALUES (?, ?, ?, ?, ?)")) {

            stmt.setString(1, recipientName);
            stmt.setString(2, packageDetails);
            stmt.setString(3, selectedWeightCategory);
            stmt.setDouble(4, value);
            stmt.setDouble(5, Double.parseDouble(price));

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Courier details successfully saved!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new PickupAddress().setVisible(true);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving courier details: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addTextFilter(JTextField textField, String regex) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text,
                                AttributeSet attrs) throws BadLocationException {
                if (text.matches(regex
                )) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            @Override
            public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
                if (text.matches(regex)) {
                    super.insertString(fb, offset, text, attr);
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CourierDetailApp().setVisible(true);
        });
    }
}
