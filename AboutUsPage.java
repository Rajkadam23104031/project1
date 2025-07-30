import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class AboutUsPage extends JFrame {


    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;


        public BackgroundPanel(String imagePath) {
            try {
                backgroundImage = ImageIO.read(new File(imagePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            setBackground(Color.DARK_GRAY);
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

    public AboutUsPage()  {
        setTitle("About Us");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());


        BackgroundPanel backgroundPanel = new BackgroundPanel("background.jpg");
        backgroundPanel.setLayout(new BorderLayout());


        JTextArea aboutText = new JTextArea();
        aboutText.setText("We are dedicated to providing reliable and efficient courier services tailored to meet your needs. "
                + "We have built a strong reputation for timely deliveries and exceptional customer service. Our team of experienced professionals ensures that every package is handled with care and delivered on schedule.\n\n"
                + "We offer the best services, shipping, and specialized handling for fragile item)s. Utilizing tracking technology, we keep you informed at every step of the delivery process. "
                + "Our commitment to transparency means you can always check the status of your shipment in real-time.\n\n"
                + "Customer satisfaction is our top priority, and we strive to exceed expectations with every interaction. Our flexible pricing options cater to businesses of all sizes, providing cost-effective solutions without compromising quality.\n\n"
                + "We believe in creating a positive impact through eco-friendly practices. Our dedicated support team is available 24/7 to address any questions or concerns you may have.\n\n"
                + "Join the countless satisfied customers who trust us for their courier needs. At [Your Company Name], your package is our priority, and we take pride in delivering excellence every time.\n\n"
                 + "If the courier is lost by any chance during the delivery then the delivery boy will be responsible for it");

        aboutText.setFont(new Font("Serif", Font.PLAIN, 16));


        aboutText.setEditable(false);
        aboutText.setLineWrap(true);
        aboutText.setWrapStyleWord(true);


        JScrollPane scrollPane = new JScrollPane(aboutText,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


        aboutText.setMargin(new Insets(10, 10, 10, 10));


        backgroundPanel.add(scrollPane, BorderLayout.CENTER);


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
        buttonPanel.add(backButton);


        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);


        add(backgroundPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new AboutUsPage().setVisible(true));
    }
}
