import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

class CanvasPanel extends JPanel {
    private Image backgroundImage;

    public CanvasPanel(String imagePath) {
        try {
            this.backgroundImage = new ImageIcon(imagePath).getImage();
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
            this.backgroundImage = null;
        }
        setBackground(Color.LIGHT_GRAY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

public class FeedbackForm extends JFrame {
    private JRadioButton q1Excellent, q1Good, q1Poor;
    private JRadioButton q2Excellent, q2Good, q2Poor;
    private JRadioButton q3Excellent, q3Good, q3Poor;
    private JRadioButton q4Excellent, q4Good, q4Poor;
    private JRadioButton q5Excellent, q5Good, q5Poor;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/customerregister";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "9869";

    public FeedbackForm() {
        setTitle("Feedback Form");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        CanvasPanel canvasPanel = new CanvasPanel("background.jpg");
        canvasPanel.setLayout(new BoxLayout(canvasPanel, BoxLayout.Y_AXIS));

        Font questionFont = new Font("Arial", Font.PLAIN, 16);
        Font answerFont = new Font("Arial", Font.PLAIN, 14);


        JPanel q1Panel = createQuestionPanel("1. How would you rate the timeliness of your deliveries?", questionFont, answerFont);
        q1Excellent = new JRadioButton("Excellent");
        q1Good = new JRadioButton("Good");
        q1Poor = new JRadioButton("Poor");
        addRadioButtonsToPanel(q1Panel, q1Excellent, q1Good, q1Poor);


        JPanel q2Panel = createQuestionPanel("2. How satisfied are you with the condition of packages upon delivery?", questionFont, answerFont);
        q2Excellent = new JRadioButton("Excellent");
        q2Good = new JRadioButton("Good");
        q2Poor = new JRadioButton("Poor");
        addRadioButtonsToPanel(q2Panel, q2Excellent, q2Good, q2Poor);


        JPanel q3Panel = createQuestionPanel("3. How effective is the tracking information provided for your shipments?", questionFont, answerFont);
        q3Excellent = new JRadioButton("Excellent");
        q3Good = new JRadioButton("Good");
        q3Poor = new JRadioButton("Poor");
        addRadioButtonsToPanel(q3Panel, q3Excellent, q3Good, q3Poor);


        JPanel q4Panel = createQuestionPanel("4. How would you assess the professionalism of the delivery personnel?", questionFont, answerFont);
        q4Excellent = new JRadioButton("Excellent");
        q4Good = new JRadioButton("Good");
        q4Poor = new JRadioButton("Poor");
        addRadioButtonsToPanel(q4Panel, q4Excellent, q4Good, q4Poor);


        JPanel q5Panel = createQuestionPanel("5. How likely are you to use our courier service again?", questionFont, answerFont);
        q5Excellent = new JRadioButton("Excellent");
        q5Good = new JRadioButton("Good");
        q5Poor = new JRadioButton("Poor");
        addRadioButtonsToPanel(q5Panel, q5Excellent, q5Good, q5Poor);


        canvasPanel.add(q1Panel);
        canvasPanel.add(q2Panel);
        canvasPanel.add(q3Panel);
        canvasPanel.add(q4Panel);
        canvasPanel.add(q5Panel);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));


        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFeedbackToDatabase();
            }
        });
        buttonPanel.add(submitButton);


        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Home().setVisible(true);
                dispose();
            }
        });
        buttonPanel.add(backButton);

        canvasPanel.add(buttonPanel);

        add(canvasPanel);
        setVisible(true);
    }

    private JPanel createQuestionPanel(String question, Font questionFont, Font answerFont) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(question);
        label.setFont(questionFont);
        panel.add(label);

        return panel;
    }

    private void addRadioButtonsToPanel(JPanel panel, JRadioButton... buttons) {
        ButtonGroup group = new ButtonGroup();
        JPanel radioPanel = new JPanel();
        radioPanel.setOpaque(false);
        radioPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        for (JRadioButton button : buttons) {
            group.add(button);
            radioPanel.add(button);
        }
        panel.add(radioPanel);
    }

    private void saveFeedbackToDatabase() {
        String q1Response = getSelectedResponse(q1Excellent, q1Good, q1Poor);
        String q2Response = getSelectedResponse(q2Excellent, q2Good, q2Poor);
        String q3Response = getSelectedResponse(q3Excellent, q3Good, q3Poor);
        String q4Response = getSelectedResponse(q4Excellent, q4Good, q4Poor);
        String q5Response = getSelectedResponse(q5Excellent, q5Good, q5Poor);

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO feedback (timeliness_rating, condition_rating, tracking_effectiveness_rating, professionalism_rating, likelihood_to_use_again_rating) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, q1Response);
                stmt.setString(2, q2Response);
                stmt.setString(3, q3Response);
                stmt.setString(4, q4Response);
                stmt.setString(5, q5Response);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Feedback submitted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving feedback: " + e.getMessage());
        }
    }

    private String getSelectedResponse(JRadioButton... buttons) {
        for (JRadioButton button : buttons) {
            if (button.isSelected()) {
                return button.getText();
            }
        }
        return "Not Answered";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FeedbackForm());
    }
}
