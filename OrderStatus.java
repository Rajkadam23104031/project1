import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class OrderStatus extends JFrame {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/customerregister";
    private static final String USER = "root";
    private static final String PASS = "9869";

    private JLabel lblRecipientName, lblDescription, lblWeight, lblValue, lblPrice;
    private JLabel lblPickupDetails, lblDeliveryDetails, lblPaymentMethod;
    private JTextField txtRecipientName, txtDescription, txtWeight, txtValue, txtPrice;
    private JTextField txtPickupDetails, txtDeliveryDetails, txtPaymentMethod;


    private JButton btnBack;

    public OrderStatus(int orderID) {
        setTitle("Order Status");
        setSize(400, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        lblRecipientName = new JLabel("Recipient Name:");
        lblDescription = new JLabel("Description of Contents:");
        lblWeight = new JLabel("Weight:");
        lblValue = new JLabel("Value of Contents:");
        lblPrice = new JLabel("Price:");

        lblPickupDetails = new JLabel("Pickup Details:");
        lblDeliveryDetails = new JLabel("Delivery Details:");
        lblPaymentMethod = new JLabel("Payment Method:");

        txtRecipientName = new JTextField(20);
        txtDescription = new JTextField(20);
        txtWeight = new JTextField(20);
        txtValue = new JTextField(20);
        txtPrice = new JTextField(20);

        txtPickupDetails = new JTextField(20);
        txtDeliveryDetails = new JTextField(20);
        txtPaymentMethod = new JTextField(20);

        // Set fields to non-editable
        txtRecipientName.setEditable(false);
        txtDescription.setEditable(false);
        txtWeight.setEditable(false);
        txtValue.setEditable(false);
        txtPrice.setEditable(false);

        txtPickupDetails.setEditable(false);
        txtDeliveryDetails.setEditable(false);
        txtPaymentMethod.setEditable(false);


        setLayout(new GridLayout(11, 2, 10, 10));


        add(lblRecipientName);
        add(txtRecipientName);
        add(lblDescription);
        add(txtDescription);
        add(lblWeight);
        add(txtWeight);
        add(lblValue);
        add(txtValue);
        add(lblPrice);
        add(txtPrice);

        add(lblPickupDetails);
        add(txtPickupDetails);
        add(lblDeliveryDetails);
        add(txtDeliveryDetails);
        add(lblPaymentMethod);
        add(txtPaymentMethod);


        btnBack = new JButton("Back");
        btnBack.addActionListener(e -> {
            dispose();
            new Home().setVisible(true);
        });


        add(new JLabel());
        add(btnBack);


        fetchOrderDetails(orderID);
    }

    private void fetchOrderDetails(int orderID) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            conn = DriverManager.getConnection(DB_URL, USER, PASS);


            String sql = "SELECT c.RECIPENT_NAME, c.DESCRIPTION_OF_CONTENTS, c.WEIGHT, c.VALUE_OF_CONTENTS, c.PRICE, " +
                    "p.address AS PICKUP_ADDRESS, p.city AS PICKUP_CITY, p.state AS PICKUP_STATE, p.pincode AS PICKUP_PINCODE, " +
                    "d.delivery_address AS DELIVERY_ADDRESS, d.delivery_city AS DELIVERY_CITY, d.delivery_state AS DELIVERY_STATE, d.delivery_pincode AS DELIVERY_PINCODE, " +
                    "pm.method " +
                    "FROM courierdetails c " +
                    "JOIN pickup_addresses p ON c.id = p.id " +
                    "JOIN delivery_addresses d ON c.id = d.id " +
                    "JOIN payment_methods pm ON c.id = pm.id " +
                    "WHERE c.id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderID); // Use orderID


            rs = stmt.executeQuery();


            if (rs.next()) {
                txtRecipientName.setText(rs.getString("RECIPENT_NAME"));
                txtDescription.setText(rs.getString("DESCRIPTION_OF_CONTENTS"));
                txtWeight.setText(rs.getString("WEIGHT"));
                txtValue.setText(rs.getString("VALUE_OF_CONTENTS"));
                txtPrice.setText(rs.getString("PRICE"));

                txtPickupDetails.setText(rs.getString("PICKUP_ADDRESS") + ", " + rs.getString("PICKUP_CITY") + ", " +
                        rs.getString("PICKUP_STATE") + " - " + rs.getString("PICKUP_PINCODE"));
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
        int orderID = 1;


        SwingUtilities.invokeLater(() -> {
            OrderStatus orderStatus = new OrderStatus(orderID);
            orderStatus.setVisible(true);
        });
    }
}
