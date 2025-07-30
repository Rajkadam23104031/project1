import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PaymentMethod extends JFrame {

    public PaymentMethod() {
        setTitle("Payment Method Selection");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setLayout(new GridBagLayout());
        backgroundPanel.setBackground(new Color(25, 25, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1;
        gbc.weighty = 1;


        JLabel paymentMethodLabel = new JLabel("Payment Method:");
        paymentMethodLabel.setFont(new Font("Arial", Font.BOLD, 40));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        backgroundPanel.add(paymentMethodLabel, gbc);


        JRadioButton creditCardRadioButton = new JRadioButton("Credit Card");
        JRadioButton debitCardRadioButton = new JRadioButton("Debit Card");


        Font radioButtonFont = new Font("Arial", Font.BOLD, 24);
        creditCardRadioButton.setFont(radioButtonFont);
        debitCardRadioButton.setFont(radioButtonFont);

        gbc.gridx = 0;
        gbc.gridy = 1;
        backgroundPanel.add(creditCardRadioButton, gbc);

        gbc.gridy = 2;
        backgroundPanel.add(debitCardRadioButton, gbc);


        ButtonGroup paymentMethodButtonGroup = new ButtonGroup();
        paymentMethodButtonGroup.add(creditCardRadioButton);
        paymentMethodButtonGroup.add(debitCardRadioButton);

        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 24));
        submitButton.setBackground(Color.GREEN);
        submitButton.setForeground(Color.WHITE);

        gbc.gridy = 3;
        backgroundPanel.add(submitButton, gbc);


        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 24));
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);

        gbc.gridy = 4;
        backgroundPanel.add(backButton, gbc);


        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String selectedMethod = null;
                if (creditCardRadioButton.isSelected()) {
                    selectedMethod = "Credit Card";
                } else if (debitCardRadioButton.isSelected()) {
                    selectedMethod = "Debit Card";
                }


                if (selectedMethod == null) {
                    JOptionPane.showMessageDialog(PaymentMethod.this, "Please select a payment method.");
                    return;
                }


                try {

                    String url = "jdbc:mysql://localhost:3306/customerregister";
                    String user = "root";
                    String password = "9869";


                    Connection connection = DriverManager.getConnection(url, user, password);


                    String sql = "INSERT INTO payment_methods (method) VALUES (?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, selectedMethod);
                    int rowsInserted = preparedStatement.executeUpdate();
                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(PaymentMethod.this, "paymentmethod successfully saved!");
                        dispose();
                        new PaymentDetails().setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(PaymentMethod.this, "Failed to save Courier details");
                    }
                    preparedStatement.close();

                    connection.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(PaymentMethod.this, "Error saving payment method: " + ex.getMessage());
                }


                dispose();
                new PaymentDetails().setVisible(true);
            }
        });


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new DeliveryAddress().setVisible(true);
            }
        });


        add(backgroundPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PaymentMethod::new);
    }
}
