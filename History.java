import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class History extends JFrame {

    private static final String  DB_URL  = "jdbc :mysql://localhost:3306/customerregister";
    private static final String USER = "root";
    private static final String PASS = "9869";

    private JLabel lblRecipientName, lblDescription, lblWeight, lblValue, lblUsername;
    private JLabel lblDeliveryDetails, lblPaymentMethod;
    private JTextField txtRecipientName, txtDescription, txtWeight, txtValue, txtUsername;
    private JTextField txtDeliveryDetails, txtPaymentMethod;

    // Add a Back button
    private JButton btnBack;

    public History(int orderID) {
    }

    public History() {

    }

    public void History(int orderID) {
        setTitle("Order Status");
        setSize(400, 600); // Adjusted size for additional fields
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize components
        lblRecipientName = new JLabel("Recipient Name:");
        lblDescription = new JLabel("Description of Contents:");
        lblWeight = new JLabel("Weight:");
        lblValue = new JLabel("Value of Contents:");
        lblUsername = new JLabel("Username:");

        lblDeliveryDetails = new JLabel("Delivery Details:");
        lblPaymentMethod = new JLabel("Payment Method:");

        txtRecipientName = new JTextField(20);
        txtDescription = new JTextField(20);
        txtWeight = new JTextField(20);
        txtValue = new JTextField(20);
        txtUsername = new JTextField(20);

        txtDeliveryDetails = new JTextField(20);
        txtPaymentMethod = new JTextField(20);

        // Set fields to non-editable
        txtRecipientName.setEditable(false);
        txtDescription.setEditable(false);
        txtWeight.setEditable(false);
        txtValue.setEditable(false);
        txtUsername.setEditable(false);

        txtDeliveryDetails.setEditable(false);
        txtPaymentMethod.setEditable(false);

        // Layout manager
        setLayout(new GridLayout(9, 2, 10, 10)); // Adjusted rows to accommodate Back button

        // Add components to the panel
        add(lblRecipientName);
        add(txtRecipientName);
        add(lblDescription);
        add(txtDescription);
        add(lblWeight);
        add(txtWeight);
        add(lblValue);
        add(txtValue);
        add(lblUsername);
        add(txtUsername);

        add(lblDeliveryDetails);
        add(txtDeliveryDetails);
        add(lblPaymentMethod);
        add(txtPaymentMethod);

        // Initialize Back button
        btnBack = new JButton("Back");
        btnBack.addActionListener(e -> {
            dispose();
            new AdminHomepage().setVisible(true); // Close the current window and open AdminHomepage
        });

        // Add Back button to the layout
        add(new JLabel()); // Empty label for spacing
        add(btnBack); // Add Back button to the last row

        // Fetch and display data from the database
        fetchOrderDetails(orderID);
    }

    private void fetchOrderDetails(int orderID) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Establish the database connection
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // SQL query to fetch order, delivery, and payment details
            String sql = "SELECT c.RECIPENT_NAME, c.DESCRIPTION_OF_CONTENTS, c.WEIGHT, c.VALUE_OF_CONTENTS, " +
                    "c.USERNAME, " +
                    "d.delivery_address AS DELIVERY_ADDRESS, d.delivery_city AS DELIVERY_CITY, d.delivery_state AS DELIVERY_STATE, d.delivery_pincode AS DELIVERY_PINCODE, " +
                    "pm.method " +
                    "FROM courierdetails c " +
                    "JOIN delivery_addresses d ON c.id = d.id " + // Adjust if ORDER_ID is the foreign key
                    "JOIN payment_methods pm ON c.id = pm.id " + // Adjust if ORDER_ID is the foreign key
                    "WHERE c.id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderID); // Use orderID

            // Execute the query
            rs = stmt.executeQuery();

            // Populate the fields with the retrieved data
            if (rs.next()) {
                txtRecipientName.setText(rs.getString("RECIPENT_NAME"));
                txtDescription.setText(rs.getString("DESCRIPTION_OF_CONTENTS"));
                txtWeight.setText(rs.getString("WEIGHT"));
                txtValue.setText(rs.getString("VALUE_OF_CONTENTS"));
                txtUsername.setText(rs.getString("USERNAME"));

                txtDeliveryDetails.setText(rs.getString("DELIVERY_ADDRESS") + ", " + rs.getString("DELIVERY_CITY") + ", " +
                        rs.getString("DELIVERY_STATE") + " - " + rs.getString("DELIVERY_PINCODE"));
                txtPaymentMethod.setText(rs.getString("method"));
            } else {
                JOptionPane.showMessageDialog(this, "No order found with ID: " + orderID);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching order details from the database.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        int orderID = 1; // Example order ID

        // Display the order status window
        SwingUtilities.invokeLater(() -> {
            OrderStatus orderStatus = new OrderStatus(orderID);
            orderStatus.setVisible(true);
        });
    }
}
