import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.DocumentFilter;
import javax.swing.text.BadLocationException;

public class PaymentDetails extends JFrame {

    private final JTextField cardNumberField;
    private final JTextField expirationDateField;
    private final JTextField cvvField;
    private final JTextField nameOnCardField;
    private final JLabel messageLabel;

    public PaymentDetails() {
        setTitle("Online Payment");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 2, 10, 10));
        inputPanel.setOpaque(false);


        JLabel cardNumberLabel = new JLabel("Card Number (last 4 digits):");
        cardNumberLabel.setFont(new Font("Arial", Font.BOLD, 16));
        cardNumberLabel.setForeground(Color.WHITE);
        cardNumberField = new JTextField(4);
        cardNumberField.setFont(new Font("Arial", Font.PLAIN, 16));
        ((AbstractDocument) cardNumberField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = currentText.substring(0, offset) + string + currentText.substring(offset);
                if (newText.matches("\\d{0,4}")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);
                if (newText.matches("\\d{0,4}")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        JLabel expirationDateLabel = new JLabel("Expiration Date (MM/YY):");
        expirationDateLabel.setFont(new Font("Arial", Font.BOLD, 16));
        expirationDateLabel.setForeground(Color.WHITE);
        expirationDateField = new JTextField(10);
        expirationDateField.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel cvvLabel = new JLabel("CVV:");
        cvvLabel.setFont(new Font("Arial", Font.BOLD, 16));
        cvvLabel.setForeground(Color.WHITE);
        cvvField = new JTextField(3);
        cvvField.setFont(new Font("Arial", Font.PLAIN, 16));
        ((AbstractDocument) cvvField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = currentText.substring(0, offset) + string + currentText.substring(offset);
                if (newText.matches("\\d{0,3}")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);
                if (newText.matches("\\d{0,3}")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        JLabel nameOnCardLabel = new JLabel("Name on Card:");
        nameOnCardLabel.setFont((new Font("Arial", Font.BOLD, 16)));
        nameOnCardLabel.setForeground(Color.WHITE);
        nameOnCardField = new JTextField(20);
        nameOnCardField.setFont(new Font("Arial", Font.PLAIN, 16));

        inputPanel.add(cardNumberLabel);
        inputPanel.add(cardNumberField);
        inputPanel.add(expirationDateLabel);
        inputPanel.add(expirationDateField);
        inputPanel.add(cvvLabel);
        inputPanel.add(cvvField);
        inputPanel.add(nameOnCardLabel);
        inputPanel.add(nameOnCardField);


        JButton payButton = new JButton("Pay");
        payButton.setFont(new Font("Arial", Font.BOLD, 16));
        payButton.setPreferredSize(new Dimension(120, 40));
        payButton.setBackground(Color.GREEN);
        payButton.setForeground(Color.WHITE);
        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cardNumber = cardNumberField.getText();
                String expirationDate = expirationDateField.getText();
                String cvv = cvvField.getText();
                String nameOnCard = nameOnCardField.getText();

                if (validateInput(cardNumber, expirationDate, cvv, nameOnCard)) {

                    dispose();
                    new ConfirmationDialog().setVisible(true);
                } else {
                    messageLabel.setText("Invalid input. Please try again.");
                }
            }
        });


        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PaymentMethod().setVisible(true);
                dispose();

            }
        });


        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(payButton);
        buttonPanel.add(backButton);


        messageLabel = new JLabel("");
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        messageLabel.setForeground(Color.RED);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);


        BackgroundPanel backgroundPanel = new BackgroundPanel("C:\\Users\\harsh\\IdeaProjects\\java mini project 1\\src\\image payment details.jpeg"); // Update with your image path
        backgroundPanel.setLayout(new BorderLayout());


        backgroundPanel.add(inputPanel, BorderLayout.CENTER);
        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);
        backgroundPanel.add(messageLabel, BorderLayout.NORTH);


        add(backgroundPanel);


        setVisible(true);
    }

    private static boolean validateInput(String cardNumber, String expirationDate, String cvv, String nameOnCard) {

        if (cardNumber.length() != 4 || !cardNumber.matches("\\d{4}")) {
            return false;
        }
        if (expirationDate.length() != 5 || !expirationDate.matches("\\d{2}/\\d{2}")) {
            return false;
        }
        if (cvv.length() != 3 || !cvv.matches("\\d{3}")) {
            return false;
        }
        return !nameOnCard.trim().isEmpty();
    }

    static class BackgroundPanel extends JPanel {

        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                backgroundImage = new ImageIcon(imagePath).getImage();
            } catch (Exception e) {
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PaymentDetails::new);
    }
}
