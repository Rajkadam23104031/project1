import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminLogin extends JFrame implements ActionListener {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel messageLabel;

    public AdminLogin() {
        setTitle("Admin Login Page");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel fieldsPanel = createFormPanel();
        fieldsPanel.setOpaque(false); // Make the panel transparent so the background image shows through

        // Create message label
        messageLabel = new JLabel("");
        messageLabel.setFont(new Font("Arial", Font.BOLD, 30));
        messageLabel.setForeground(Color.RED);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Create background panel
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image backgroundImage = new ImageIcon("C:\\Users\\harsh\\IdeaProjects\\java mini project 1\\src\\Parcel_delivery_vehicle_with_just_yellow_parcels_i_half_trans (1).png").getImage();
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        // Add fields panel to the background
        backgroundPanel.add(fieldsPanel, BorderLayout.CENTER);

        // Add message label to the background
        backgroundPanel.add(messageLabel, BorderLayout.SOUTH);

        // Set the background panel as the content pane of the JFrame
        setContentPane(backgroundPanel);

        setVisible(true);
    }


    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding around components
        gbc.fill = GridBagConstraints.HORIZONTAL; // Expand horizontally

        int row = 0;


        Font labelFont = new Font("Arial", Font.BOLD, 16);
        Font textFieldFont = new Font("Arial", Font.PLAIN, 18);


        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.LINE_END;
        usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.white);
        usernameLabel.setFont(labelFont);
        panel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        usernameField = new JTextField();
        usernameField.setFont(textFieldFont);
        usernameField.setPreferredSize(new Dimension(200, 30));
        panel.add(usernameField, gbc);

        row++;


        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.LINE_END;
        passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.white);
        passwordLabel.setFont(labelFont);
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        passwordField = new JPasswordField();
        passwordField.setFont(textFieldFont);
        passwordField.setPreferredSize(new Dimension(200, 30)); // Increase width and height
        panel.add(passwordField, gbc);

        row++;

        // Login Button
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2; // Span across both columns
        gbc.anchor = GridBagConstraints.CENTER;
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 24));
        loginButton.addActionListener(this);
        panel.add(loginButton, gbc);

        row++;

        // Forgot Password
        JLabel forgetPasswordLabel = new JLabel("<html><a href=\"#\">Forgot Password?</a></html>");
        forgetPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        forgetPasswordLabel.setForeground(Color.BLUE);
        forgetPasswordLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Change cursor to hand
        forgetPasswordLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Action for "Forgot Password?" link
                JOptionPane.showMessageDialog(panel, "Password recovery options will be provided here.");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2; // Span across both columns
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(forgetPasswordLabel, gbc);

        row++;

        // Message Label
        messageLabel = new JLabel("");
        messageLabel.setFont(new Font("Arial", Font.BOLD, 20));
        messageLabel.setForeground(Color.RED);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        panel.add(messageLabel, gbc);

        // Add padding around the panel
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please enter both username and password.");
                return;
            }

            // Hardcoded admin username and password
            if (username.equals("Raj") && password.equals("9869")) {
                messageLabel.setText("Login successful!");
                new AdminHomepage().setVisible(true); // Open admin homepage
                dispose(); // Close the login frame
            } else {
                messageLabel.setText("Invalid username or password.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminLogin::new);
    }
}
