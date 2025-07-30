import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Display extends JPanel {
    private Image backgroundImage;


    public Display(String imagePath) {
        this.backgroundImage = new ImageIcon(imagePath).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}

public class contactline extends JFrame {
    public contactline() {
        setTitle("Helpline");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        Display canvasPanel = new Display("background.jpg");
        canvasPanel.setLayout(new BorderLayout()); // Use BorderLayout for positioning


        JTextArea helplineText = new JTextArea();
        helplineText.setText("We prioritize customer support to ensure a smooth experience. Here are the various ways you can reach us for assistance:\n" +
                "\n" +
                "☎:+91 8591763637\n" +
                "Call our dedicated helpline for immediate assistance. Our team is available 24/7 to answer your queries.\n" +
                "\n" +
                "\uD83D\uDCE7: courierService@gmail.com\n" +
                "For detailed inquiries, please email us.\n" +
                "\n" +
                "\uD83D\uDCAC: +91 8591763637\n" +
                "Receive support via SMS. Simply text your inquiry to the above number for quick assistance.\n" +
                "\n" +
                "\uD83D\uDCF1: +91 8591763637\n" +
                "Connect with us on WhatsApp for instant messaging support. We respond promptly during our service hours.");


        helplineText.setFont(new Font("Serif", Font.PLAIN, 16));


        helplineText.setEditable(false);
        helplineText.setLineWrap(true);
        helplineText.setWrapStyleWord(true);
        helplineText.setOpaque(false);


        helplineText.setMargin(new Insets(10, 10, 10, 10));


        JScrollPane scrollPane = new JScrollPane(helplineText,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);


        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Home().setVisible(true);
                dispose();
            }
        });


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);


        buttonPanel.add(backButton);


        canvasPanel.add(scrollPane, BorderLayout.CENTER);
        canvasPanel.add(buttonPanel, BorderLayout.SOUTH);


        setContentPane(canvasPanel);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new contactline().setVisible(true));
    }
}
