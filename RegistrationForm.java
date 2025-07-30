import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class RegistrationForm extends JFrame implements ActionListener {

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField middleNameField;
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private JLabel messageLabel;
    private JLabel loginLinkLabel;

    private static final String URL = "jdbc:mysql://localhost:3306/customerregister";
    private static final String USER = "root";
    private static final String PASSWORD = "9869";
    private static final int PASSWORD_MIN_LENGTH = 8;
    private static final int PASSWORD_MAX_LENGTH = 15;

    public RegistrationForm() {
        setTitle("Registration Page");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel fieldsPanel = createFormPanel();
        fieldsPanel.setOpaque(false);

        messageLabel = new JLabel("");
        messageLabel.setFont(new Font("Arial", Font.BOLD, 30));
        messageLabel.setForeground(Color.RED);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image backgroundImage = new ImageIcon("C:\\Users\\harsh\\IdeaProjects\\java mini project 1\\src\\COURIER_REGISTRATIION_IMAGE_half_trans.png").getImage();
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        backgroundPanel.add(fieldsPanel, BorderLayout.CENTER);
        backgroundPanel.add(messageLabel, BorderLayout.SOUTH);

        setContentPane(backgroundPanel);
        setVisible(true);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        int row = 0;

        Font labelFont = new Font("Arial", Font.BOLD, 16);
        Font textFieldFont = new Font("Arial", Font.PLAIN, 18);

        addLabeledField(panel, gbc, "First Name:", 0, row++, labelFont, textFieldFont, true);
        addLabeledField(panel, gbc, "Middle Name:", 0, row++, labelFont, textFieldFont, true);
        addLabeledField(panel, gbc, "Last Name:", 0, row++, labelFont, textFieldFont, true);
        addLabeledField(panel, gbc, "Username:", 0, row++, labelFont, textFieldFont, true);
        addLabeledField(panel, gbc, "Email ID:", 0, row++, labelFont, textFieldFont, true);
        addLabeledField(panel, gbc, "Password:", 0, row++, labelFont, textFieldFont, false);
        addLabeledField(panel, gbc, "Confirm Password:", 0, row++, labelFont, textFieldFont, false);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.CENTER;
        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 24));
        registerButton.setBackground(Color.decode("#157100"));
        registerButton.setForeground(Color.WHITE);
        registerButton.setOpaque(true);
        registerButton.setBorderPainted(false);
        registerButton.addActionListener(this);
        panel.add(registerButton, gbc);


        loginLinkLabel = new JLabel("Already have an account? Log in");
        loginLinkLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        loginLinkLabel.setForeground(Color.BLUE);
        loginLinkLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridy = row + 1;
        panel.add(loginLinkLabel, gbc);


        loginLinkLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new Login().setVisible(true);
            }
        });

        panel.setBackground(Color.decode("#F0F8FF"));

        return panel;
    }



    private void addLabeledField(JPanel panel, GridBagConstraints gbc, String labelText, int gridx, int gridy, Font labelFont, Font fieldFont, boolean isTextField) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        panel.add(label, gbc);

        gbc.gridx = gridx + 1;
        gbc.gridy = gridy;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        if (isTextField) {
            JTextField textField = new JTextField(15);
            textField.setFont(fieldFont);
            textField.setPreferredSize(new Dimension(200, 40));
            panel.add(textField, gbc);

            switch (labelText) {
                case "First Name:": firstNameField = textField; restrictToLetters(firstNameField); break;
                case "Last Name:": lastNameField = textField; restrictToLetters(lastNameField); break;
                case "Middle Name:": middleNameField = textField; restrictToLetters(middleNameField); break;
                case "Username:": usernameField = textField; break;
                case "Email ID:": emailField = textField; break;
            }
        } else {
            JPasswordField passwordField = new JPasswordField(20);
            passwordField.setFont(fieldFont);
            passwordField.setPreferredSize(new Dimension(300, 40));
            panel.add(passwordField, gbc);
            if (labelText.equals("Password:")) {
                this.passwordField = passwordField;
                setPasswordLengthLimit(passwordField);
            } else if (labelText.equals("Confirm Password:")) {
                this.confirmPasswordField = passwordField;
                setPasswordLengthLimit(confirmPasswordField);
            }
        }
    }

    private void restrictToLetters(JTextField textField) {
        textField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isDigit(c)) {
                    e.consume();
                }
            }
        });
    }

    private void setPasswordLengthLimit(JPasswordField passwordField) {
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (passwordField.getPassword().length >= PASSWORD_MAX_LENGTH) {
                    e.consume();
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String middleName = middleNameField.getText().trim();
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (isAnyFieldEmpty(firstName, lastName, middleName, username, email, password, confirmPassword)) {
                messageLabel.setText("Please enter all fields.");
                return;
            }

            if (password.length() < PASSWORD_MIN_LENGTH || password.length() > PASSWORD_MAX_LENGTH) {
                messageLabel.setText("Password must be between 8 and 15 characters.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                messageLabel.setText("Passwords do not match.");
                return;
            }

            if (!isValidEmail(email)) {
                messageLabel.setText("Invalid email format.");
                return;
            }

            if (isUsernameTaken(username)) {
                messageLabel.setText("Username is already exists!");
                return;
            }

            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement statement = connection.prepareStatement(
                         "INSERT INTO customerdata (FIRSTNAME, LASTNAME, MIDDLENAME, USERNAME, EMAIL, PASSWORD) VALUES (?, ?, ?, ?, ?, ?)")) {

                statement.setString
                        (1, firstName);
                statement.setString(2, lastName);
                statement.setString(3, middleName);
                statement.setString(4, username);
                statement.setString(5, email);
                statement.setString(6, password);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted >
                        0) {
                    messageLabel.setText("Registration successful!");
                    new Login().setVisible(true);
                    dispose();
                } else {
                    messageLabel.setText
                            ("Registration failed. Please try again.");
                }

            } catch (SQLException sqlException) {
                messageLabel.setText("Database error: " + sqlException.getMessage());
            }
        }
    }
    private boolean isUsernameTaken(String username) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM customerdata WHERE USERNAME = ?")) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            messageLabel.setText("Database error: " + e.getMessage());
        }
        return false;
    }


    private boolean isAnyFieldEmpty(String... fields) {
        for (String field : fields) {
            if (field.isEmpty()) return true;
        }
        return false;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[  a-zA-Z0-9._%+-]+@gmail\\.com$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegistrationForm::new);
    }
}


