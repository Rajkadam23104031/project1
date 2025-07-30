import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class PickupAddress extends JFrame {

    private final JLabel pickupAddressLabel;
    private final JTextField pickupAddressField;
    private final JLabel pickupStateLabel;
    private final JComboBox<String> pickupStateField;
    private final JLabel pickupCityLabel;
    private final JComboBox<String> pickupCityField;
    private final JLabel pickupPincodeLabel;
    private final JTextField pickupPincodeField;
    private final JButton submitButton;
    private final JButton backButton;

    private final Map<String, Map<String, String>> stateCityPincodeMap;

    public PickupAddress() {
        setTitle("Pickup Address Details");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        stateCityPincodeMap = new HashMap<>();
        initializeCityPincodeMap();


        JPanel pickupAddressPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("C:\\Users\\harsh\\IdeaProjects\\java mini project 1\\src\\pickupaddressimage.jpeg"); // Update the path to your image
                Image img = backgroundImage.getImage();
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };
        pickupAddressPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;


        JLabel courierImage = new JLabel(new ImageIcon("path/to/courier-image.png"));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        pickupAddressPanel.add(courierImage, gbc);
        gbc.gridwidth = 1;


        JPanel addressPanel = new JPanel();
        addressPanel.setLayout(new GridBagLayout());
        addressPanel.setOpaque(false);

        pickupAddressLabel = new JLabel("Pickup Address:");
        pickupAddressLabel.setFont(new Font("Arial", Font.BOLD, 18));
        pickupAddressLabel.setForeground(Color.WHITE);
        GridBagConstraints addressGbc = new GridBagConstraints();
        addressGbc.insets = new Insets(10, 10, 10, 10);
        addressGbc.anchor = GridBagConstraints.WEST;
        addressGbc.gridx = 0;
        addressGbc.gridy = 0;
        addressPanel.add(pickupAddressLabel, addressGbc);


        pickupAddressField = new JTextField(20);
        pickupAddressField.setFont(new Font("Arial", Font.PLAIN, 16));
        pickupAddressField.setForeground(Color.WHITE);
        pickupAddressField.setBackground(Color.BLACK);
        addressGbc.gridx = 1;
        addressGbc.gridy = 0;
        addressPanel.add(pickupAddressField, addressGbc);


        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        pickupAddressPanel.add(addressPanel, gbc);
        gbc.gridwidth = 1;


        pickupStateLabel = new JLabel("State:");
        pickupStateLabel.setFont(new Font("Arial", Font.BOLD, 18));
        pickupStateLabel.setForeground(Color.WHITE); // Set text color to white
        gbc.gridx = 0;
        gbc.gridy = 2;
        pickupAddressPanel.add(pickupStateLabel, gbc);

        String[] states = stateCityPincodeMap.keySet().toArray(new String[0]);
        pickupStateField = new JComboBox<>(states);
        pickupStateField.setFont(new Font("Arial", Font.PLAIN, 16));
        pickupStateField.setForeground(Color.WHITE);
        pickupStateField.setBackground(Color.BLACK);
        pickupStateField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCityDropdown();
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 2;

        pickupAddressPanel.add(pickupStateField, gbc);


        pickupCityLabel = new JLabel("City:");
        pickupCityLabel.setFont(new Font("Arial", Font.BOLD, 18));
        pickupCityLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        pickupAddressPanel.add(pickupCityLabel, gbc);

        pickupCityField = new JComboBox<>();
        pickupCityField.setFont(new Font("Arial", Font.PLAIN, 16));
        pickupCityField.setForeground(Color.WHITE);
        pickupCityField.setBackground(Color.BLACK);
        pickupCityField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCity = (String) pickupCityField.getSelectedItem();
                String pincode = getPincode(selectedCity);
                pickupPincodeField.setText(pincode);
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 3;
        pickupAddressPanel.add(pickupCityField, gbc);


        pickupPincodeLabel = new JLabel("Pincode:");
        pickupPincodeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        pickupPincodeLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 4;
        pickupAddressPanel.add(pickupPincodeLabel, gbc);

        pickupPincodeField = new JTextField(15);
        pickupPincodeField.setFont(new Font("Arial", Font.PLAIN, 16));
        pickupPincodeField.setForeground(Color.WHITE);
        pickupPincodeField.setBackground(Color.BLACK);
        gbc.gridx = 1;
        gbc.gridy = 4;
        pickupAddressPanel.add(pickupPincodeField, gbc);

        submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 18));
        submitButton.setBackground(Color.GREEN);
        submitButton.setForeground(Color.WHITE);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pickupAddress = pickupAddressField.getText();
                String pickupCity = (String) pickupCityField.getSelectedItem();
                String pickupState = (String) pickupStateField.getSelectedItem();
                String pickupPincode = pickupPincodeField.getText();

                if (pickupAddress.isEmpty() || pickupPincode.isEmpty()) {
                    JOptionPane.showMessageDialog(PickupAddress.this, "Please fill all fields");
                } else if (pickupPincode.length() != 6) {
                    JOptionPane.showMessageDialog(PickupAddress.this, "Pincode should be 6 digits");
                } else {

                    try {

                        String url = "jdbc:mysql://localhost:3306/customerregister";
                        String user = "root";
                        String password = "9869";

                        Connection connection = DriverManager.getConnection(url, user, password);

                        String sql = "INSERT INTO pickup_addresses (address, city, state, pincode) VALUES (?, ?, ?, ?)";
                        PreparedStatement preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setString(1, pickupAddress);
                        preparedStatement.setString(2, pickupCity);
                        preparedStatement.setString(3, pickupState);
                        preparedStatement.setString(4, pickupPincode);
                        int rowsInserted = preparedStatement.executeUpdate();
                        if (rowsInserted > 0) {
                            JOptionPane.showMessageDialog(PickupAddress.this, "Pickupaddress successfully saved!");
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    dispose();
                                    new DeliveryAddress().setVisible(true);
                                }
                            });
                        } else {
                            JOptionPane.showMessageDialog(PickupAddress.this, "Failed to save Courier details");
                        }

                        preparedStatement.close();
                        connection.close();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(PickupAddress.this, "Error saving pickup address: " + ex.getMessage());
                    }
                }
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 5;
        pickupAddressPanel.add(submitButton, gbc);


        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.setBackground(Color.RED); // Red button for "Back"
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new CourierDetailApp().setVisible(true);
                dispose();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 5;
        pickupAddressPanel.add(backButton, gbc);

        add(pickupAddressPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);


        updateCityDropdown();
    }

    private void initializeCityPincodeMap() {
        Map<String, String> goaCities = new HashMap<>();
        goaCities.put("Panaji", "403001");
        goaCities.put("Margao", "403601");
        goaCities.put("Vasco da Gama", "403802");
        goaCities.put("Mapusa", "403507");
        goaCities.put("Pernem", "403512");
        goaCities.put("Bicholim", "403504");
        goaCities.put("Sanguem", "403704");
        goaCities.put("Quepem", "403705");
        goaCities.put("Canacona", "403702");
        goaCities.put("Dona Paula", "403004");
        goaCities.put("Candolim", "403515");
        goaCities.put("Calangute", "403516");
        goaCities.put("Anjuna", "403509");
        goaCities.put("Arambol", "403524");
        goaCities.put("Saligao", "403511");
        goaCities.put("Assagao", "403507");

        Map<String, String> gujaratCities = new HashMap<>();
        gujaratCities.put("Ahmedabad", "380001");
        gujaratCities.put("Surat", "395001");
        gujaratCities.put("Vadodara", "390001");
        gujaratCities.put("Rajkot", "360001");
        gujaratCities.put("Gandhinagar", "382010");
        gujaratCities.put("Bhavnagar", "364001");
        gujaratCities.put("Junagadh", "362001");
        gujaratCities.put("Anand", "388001");
        gujaratCities.put("Nadiad", "387001");
        gujaratCities.put("Palanpur", "385001");
        gujaratCities.put("Mehsana", "384001");
        gujaratCities.put("Dahod", "389151");
        gujaratCities.put("Surendranagar", "363001");
        gujaratCities.put("Valsad", "396001");
        gujaratCities.put("Navsari", "396445");
        gujaratCities.put("Kutch", "370001");
        gujaratCities.put("Bharuch", "392001");
        gujaratCities.put("Sabarmati", "380019");
        gujaratCities.put("Godhra", "389001");
        gujaratCities.put("Dahod", "389151");
        gujaratCities.put("Bhuj", "370001");


        Map<String, String> karnatakaCities = new HashMap<>();
        karnatakaCities.put("Bangalore", "560001");
        karnatakaCities.put("Mysore", "570001");
        karnatakaCities.put("Mangalore", "575001");
        karnatakaCities.put("Hubli", "580020");
        karnatakaCities.put("Dharwad", "580001");
        karnatakaCities.put("Belgaum", "590001");
        karnatakaCities.put("Bijapur", "586101");
        karnatakaCities.put("Gulbarga", "585101");
        karnatakaCities.put("Tumkur", "572101");
        karnatakaCities.put("Shimoga", "577201");
        karnatakaCities.put("Chitradurga", "577501");
        karnatakaCities.put("Kolar", "563101");
        karnatakaCities.put("Udupi", "576101");
        karnatakaCities.put("Hassan", "573201");
        karnatakaCities.put("Mandya", "571401");
        karnatakaCities.put("Kodagu", "571201");
        karnatakaCities.put("Raichur", "584101");
        karnatakaCities.put("Bagalkot", "587101");
        karnatakaCities.put("Chikmagalur", "577101");
        karnatakaCities.put("Yadgir", "585201");
        karnatakaCities.put("Belagavi", "590002");


        Map<String, String> mpCities = new HashMap<>();
        mpCities.put("Indore", "452001");
        mpCities.put("Gwalior", "474001");
        mpCities.put("Bhopal", "462001");
        mpCities.put("Jabalpur", "482001");
        mpCities.put("Ujjain", "456001");
        mpCities.put("Sagar", "470002");
        mpCities.put("Satna", "485001");
        mpCities.put("Khandwa", "450001");
        mpCities.put("Mandsaur", "458001");
        mpCities.put("Dewas", "455001");
        mpCities.put("Ratlam", "457001");
        mpCities.put("Burhanpur", "450331");
        mpCities.put("Shivpuri", "473551");
        mpCities.put("Chhindwara", "480001");
        mpCities.put("Balaghat", "481001");
        mpCities.put("Khargone", "451001");
        mpCities.put("Damoh", "470661");
        mpCities.put("Hoshangabad", "461001");
        mpCities.put("Neemuch", "458441");
        mpCities.put("Sidhi", "486661");
        mpCities.put("Singrauli", "486889");


        Map<String, String> maharashtraCities = new HashMap<>();
        maharashtraCities.put("Mumbai", "400001");
        maharashtraCities.put("Thane", "400601");
        maharashtraCities.put("Pune", "411001");
        maharashtraCities.put("Nagpur", "440001");
        maharashtraCities.put("Nashik", "422001");
        maharashtraCities.put("Aurangabad", "431001");
        maharashtraCities.put("Solapur", "413001");
        maharashtraCities.put("Kalyan", "421301");
        maharashtraCities.put("Vasai", "401201");
        maharashtraCities.put("Dombivli", "421201");
        maharashtraCities.put("Kolhapur", "416001");
        maharashtraCities.put("Akola", "444001");
        maharashtraCities.put("Jalna", "431203");
        maharashtraCities.put("Latur", "413512");
        maharashtraCities.put("Ahmednagar", "414001");
        maharashtraCities.put("Ratnagiri", "415612");
        maharashtraCities.put("Bhiwandi", "421302");
        maharashtraCities.put("Ulhasnagar", "421003");
        maharashtraCities.put("Sangli", "416416");
        maharashtraCities.put("Ichalkaranji", "416115");

        Map<String, String> apCities = new HashMap<>();
        apCities.put("Amaravati", "522001");
        apCities.put("Visakhapatnam", "530001");
        apCities.put("Vijayawada", "520001");
        apCities.put("Guntur", "522002");
        apCities.put("Nellore", "524001");
        apCities.put("Tirupati", "517501");
        apCities.put("Kakinada", "533001");
        apCities.put("Rajahmundry", "533101");
        apCities.put("Eluru", "534001");
        apCities.put("Chirala", "523155");
        apCities.put("Anantapur", "515001");
        apCities.put("Kurnool", "518001");
        apCities.put("Srikakulam", "532001");
        apCities.put("Vizianagaram", "535001");
        apCities.put("Kadapa", "516001");
        apCities.put("Proddatur", "516360");

        Map<String, String> upCities = new HashMap<>();
        upCities.put("Lucknow", "226001");
        upCities.put("Kanpur", "208001");
        upCities.put("Varanasi", "221001");
        upCities.put("Agra", "282001");
        upCities.put("Allahabad", "211001");
        upCities.put("Ghaziabad", "201001");
        upCities.put("Noida", "201301");
        upCities.put("Meerut", "250001");
        upCities.put("Aligarh", "202001");
        upCities.put("Bareilly", "243001");
        upCities.put("Moradabad", "244001");
        upCities.put("Jhansi", "284001");
        upCities.put("Saharanpur", "247001");
        upCities.put("Firozabad", "283203");
        upCities.put("Bijnor", "246701");
        upCities.put("Rampur", "244901");
        upCities.put("Budaun", "243601");
        upCities.put("Sitapur", "261001");
        upCities.put("Shahjahanpur", "242001");
        upCities.put("Lakhimpur Kheri", "262701");
        upCities.put("Deoria", "274001");

        Map<String, String> arunachalCities = new HashMap<>();
        arunachalCities.put("Itanagar", "791111");
        arunachalCities.put("Naharlagun", "791110");
        arunachalCities.put("Pasighat", "791102");
        arunachalCities.put("Aalo", "791001");
        arunachalCities.put("Tezu", "792001");
        arunachalCities.put("Tawang", "790104");
        arunachalCities.put("Bomdila", "790201");
        arunachalCities.put("Ziro", "791120");
        arunachalCities.put("Seppa", "791115");
        arunachalCities.put("Changlang", "792120");
        arunachalCities.put("Daporijo", "791122");
        arunachalCities.put("Yupia", "791112");

        Map<String, String> assamCities = new HashMap<>();
        assamCities.put("Guwahati", "781001");
        assamCities.put("Dibrugarh", "786001");
        assamCities.put("Silchar", "788001");
        assamCities.put("Jorhat", "785001");
        assamCities.put("Nagaon", "782001");
        assamCities.put("Tezpur", "784001");
        assamCities.put("Tinsukia", "786125");
        assamCities.put("Sivasagar", "785640");
        assamCities.put("Karimganj", "788710");
        assamCities.put("Diphu", "782460");
        assamCities.put("Lakhimpur", "787001");
        assamCities.put("Barpeta", "781301");
        assamCities.put("Bongaigaon", "783380");

        Map<String, String> biharCities = new HashMap<>();
        biharCities.put("Patna", "800001");
        biharCities.put("Gaya", "823001");
        biharCities.put("Bhagalpur", "812001");
        biharCities.put("Muzaffarpur", "842001");
        biharCities.put("Darbhanga", "846004");
        biharCities.put("Purnia", "854301");
        biharCities.put("Bhagalpur", "812001");
        biharCities.put("Sasaram", "821115");
        biharCities.put("Arrah", "802301");
        biharCities.put("Bihar Sharif", "803101");
        biharCities.put("Munger", "811201");
        biharCities.put("Samastipur", "848101");
        biharCities.put("Sitamarhi", "843302");
        biharCities.put("Kishanganj", "855108");
        biharCities.put("Jamui", "811307");

        Map<String, String> cgCities = new HashMap<>();
        cgCities.put("Raipur", "492001");
        cgCities.put("Bilaspur", "495001");
        cgCities.put("Durg", "491001");
        cgCities.put("Korba", "495677");
        cgCities.put("Jagdalpur", "494001");
        cgCities.put("Dantewada", "494449");
        cgCities.put("Raigarh", "496001");
        cgCities.put("Kanker", "494334");
        cgCities.put("Mahasamund", "493445");
        cgCities.put("Bijapur", "494200");
        cgCities.put("Surguja", "497001");
        cgCities.put("Jashpur", "496331");

        Map<String, String> haryanaCities = new HashMap<>();
        haryanaCities.put("Gurgaon", "122001");
        haryanaCities.put("Faridabad", "121001");
        haryanaCities.put("Ambala", "134001");
        haryanaCities.put("Hisar", "125001");
        haryanaCities.put("Panipat", "132103");
        haryanaCities.put("Karnal", "132001");
        haryanaCities.put("Sonipat", "131001");
        haryanaCities.put("Rohtak", "124001");
        haryanaCities.put("Fatehabad", "125050");
        haryanaCities.put("Sirsa", "125055");
        haryanaCities.put("Jhajjar", "124103");
        haryanaCities.put("Nuh", "122107");
        haryanaCities.put("Panchkula", "134109");

        Map<String, String> hpCities = new HashMap<>();
        hpCities.put("Shimla", "171001");
        hpCities.put("Dharamshala", "176215");
        hpCities.put("Solan", "173212");
        hpCities.put("Mandi", "175001");
        hpCities.put("Kullu", "175101");
        hpCities.put("Hamirpur", "177001");
        hpCities.put("Bilaspur", "174001");
        hpCities.put("Una", "174303");
        hpCities.put("Nahan", "173001");
        hpCities.put("Palampur", "176061");
        hpCities.put("Rampur", "172001");
        hpCities.put("Reckong Peo", "172107");

        Map<String, String> jharkhandCities = new HashMap<>();
        jharkhandCities.put("Ranchi", "834001");
        jharkhandCities.put("Jamshedpur", "831001");
        jharkhandCities.put("Dhanbad", "826001");
        jharkhandCities.put("Bokaro", "827001");
        jharkhandCities.put("Deoghar", "814112");
        jharkhandCities.put("Giridih", "815301");
        jharkhandCities.put("Hazaribagh", "825301");
        jharkhandCities.put("Ramgarh", "829122");
        jharkhandCities.put("Koderma", "825410");
        jharkhandCities.put("Palamu", "822101");
        jharkhandCities.put("Chatra", "825401");
        jharkhandCities.put("Godda", "814133");
        jharkhandCities.put("Sahibganj", "816109");

        Map<String, String> keralaCities = new HashMap<>();
        keralaCities.put("Thiruvananthapuram", "695001");
        keralaCities.put("Kochi", "682001");
        keralaCities.put("Kozhikode", "673001");
        keralaCities.put("Kollam", "691001");
        keralaCities.put("Thrissur", "680001");
        keralaCities.put("Malappuram", "676505");
        keralaCities.put("Palakkad", "678001");
        keralaCities.put("Kottayam", "686001");
        keralaCities.put("Idukki", "685601");
        keralaCities.put("Pathanamthitta", "689645");
        keralaCities.put("Wayanad", "673121");
        keralaCities.put("Kasargod", "671121");
        keralaCities.put("Alappuzha", "688001");
        keralaCities.put("Ernakulam", "682011");

        Map<String, String> manipurCities = new HashMap<>();
        manipurCities.put("Imphal", "795001");
        manipurCities.put("Churachandpur", "795128");
        manipurCities.put("Kakching", "795103");
        manipurCities.put("Thoubal", "795138");
        manipurCities.put("Bishnupur", "795126");
        manipurCities.put("Tamenglong", "795141");
        manipurCities.put("Senapati", "795106");
        manipurCities.put("Ukhrul", "795142");
        manipurCities.put("Tengnoupal", "795148");
        manipurCities.put("Jiribam", "795134");

        Map<String, String> meghalayaCities = new HashMap<>();
        meghalayaCities.put("Shillong", "793001");
        meghalayaCities.put("Tura", "794001");
        meghalayaCities.put("Nongpoh", "793102");
        meghalayaCities.put("Jowai", "793150");
        meghalayaCities.put("Williamnagar", "794111");
        meghalayaCities.put("Baghmara", "794201");
        meghalayaCities.put("Mawkyrwat", "793114");
        meghalayaCities.put("Nongstoin", "793119");
        meghalayaCities.put("Resubelpara", "794102");

        Map<String, String> mizoramCities = new HashMap<>();
        mizoramCities.put("Aizawl", "796001");
        mizoramCities.put("Lunglei", "796201");
        mizoramCities.put("Saiha", "796901");
        mizoramCities.put("Champhai", "796321");
        mizoramCities.put("Serchhip", "796181");
        mizoramCities.put("Mamit", "796441");
        mizoramCities.put("Kolasib", "796081");
        mizoramCities.put("Lawngtlai", "796891");
        mizoramCities.put("Hnahthial", "796551");

        Map<String, String> nagalandCities = new HashMap<>();
        nagalandCities.put("Kohima", "797001");
        nagalandCities.put("Dimapur", "797112");
        nagalandCities.put("Mokokchung", "798601");
        nagalandCities.put("Wokha", "797111");
        nagalandCities.put("Zunheboto", "798620");
        nagalandCities.put("Tuensang", "798612");
        nagalandCities.put("Mon", "798601");
        nagalandCities.put("Phek", "797108");

        Map<String, String> odishaCities = new HashMap<>();
        odishaCities.put("Bhubaneswar", "751001");
        odishaCities.put("Cuttack", "753001");
        odishaCities.put("Sambalpur", "768001");
        odishaCities.put("Berhampur", "760001");
        odishaCities.put("Rourkela", "769001");
        odishaCities.put("Balasore", "756001");
        odishaCities.put("Koraput", "764020");
        odishaCities.put("Rayagada", "765001");
        odishaCities.put("Kendrapara", "754211");
        odishaCities.put("Puri", "752001");
        odishaCities.put("Khurda", "752055");
        odishaCities.put("Jagatsinghpur", "754103");
        odishaCities.put("Dhenkanal", "759001");
        odishaCities.put("Ganjam", "760001");

        Map<String, String> punjabCities = new HashMap<>();
        punjabCities.put("Chandigarh", "160001");
        punjabCities.put("Amritsar", "143001");
        punjabCities.put("Ludhiana", "141001");
        punjabCities.put("Jalandhar", "144001");
        punjabCities.put("Patiala", "147001");
        punjabCities.put("Mohali", "160055");
        punjabCities.put("Batala", "143505");
        punjabCities.put("Hoshiarpur", "146001");
        punjabCities.put("Rupnagar", "140001");
        punjabCities.put("Fatehgarh Sahib", "140406");
        punjabCities.put("Faridkot", "151203");
        punjabCities.put("Moga", "142001");
        punjabCities.put("Tarn Taran", "143401");
        punjabCities.put("Barnala", "148101");
        punjabCities.put("Sangrur", "148001");

        Map<String, String> rajasthanCities = new HashMap<>();
        rajasthanCities.put("Jaipur", "302001");
        rajasthanCities.put("Udaipur", "313001");
        rajasthanCities.put("Jodhpur", "342001");
        rajasthanCities.put("Ajmer", "305001");
        rajasthanCities.put("Bikaner", "334001");
        rajasthanCities.put("Kota", "324001");
        rajasthanCities.put("Alwar", "301001");
        rajasthanCities.put("Tonk", "304001");
        rajasthanCities.put("Sikar", "332001");
        rajasthanCities.put("Churu", "331001");
        rajasthanCities.put("Jhunjhunu", "333001");
        rajasthanCities.put("Pali", "306401");
        rajasthanCities.put("Dausa", "303303");
        rajasthanCities.put("Banswara", "327001");
        rajasthanCities.put("Barmer", "344001");
        rajasthanCities.put("Dholpur", "328001");
        rajasthanCities.put("Sawai Madhopur", "322001");
        rajasthanCities.put("Nagaur", "341001");

        Map<String, String> sikkimCities = new HashMap<>();
        sikkimCities.put("Gangtok", "737101");
        sikkimCities.put("Namchi", "737126");
        sikkimCities.put("Pakyong", "737106");
        sikkimCities.put("Mangan", "737113");
        sikkimCities.put("Gyalshing", "737111");
        sikkimCities.put("Jorethang", "737121");
        sikkimCities.put("Rangpo", "737138");

        Map<String, String> tnCities = new HashMap<>();
        tnCities.put("Chennai", "600001");
        tnCities.put("Coimbatore", "641001");
        tnCities.put("Madurai", "625001");
        tnCities.put("Tiruchirappalli", "620001");
        tnCities.put("Salem", "636001");
        tnCities.put("Tirunelveli", "627001");
        tnCities.put("Erode", "638001");
        tnCities.put("Vellore", "632001");
        tnCities.put("Dindigul", "624001");
        tnCities.put("Kanyakumari", "629001");
        tnCities.put("Thanjavur", "613001");
        tnCities.put("Pudukkottai", "622001");
        tnCities.put("Nagercoil", "629001");
        tnCities.put("Karur", "639001");
        tnCities.put("Kumbakonam", "612001");

        Map<String, String> telanganaCities = new HashMap<>();
        telanganaCities.put("Hyderabad", "500001");
        telanganaCities.put("Warangal", "506001");
        telanganaCities.put("Nizamabad", "503001");
        telanganaCities.put("Karimnagar", "505001");
        telanganaCities.put("Khammam", "507001");
        telanganaCities.put("Mahbubnagar", "509001");
        telanganaCities.put("Suryapet", "508001");
        telanganaCities.put("Ranga Reddy", "500070");
        telanganaCities.put("Medak", "502001");
        telanganaCities.put("Adilabad", "504001");
        telanganaCities.put("Medchal", "500088");
        telanganaCities.put("Jagtial", "505327");
        telanganaCities.put("Kamareddy", "503111");
        telanganaCities.put("Siddipet", "502103");

        Map<String, String> tripuraCities = new HashMap<>();
        tripuraCities.put("Agartala", "799001");
        tripuraCities.put("Ambassa", "799201");
        tripuraCities.put("Dharmanagar", "799250");
        tripuraCities.put("Kailasahar", "799277");
        tripuraCities.put("Khowai", "799202");
        tripuraCities.put("Sabroom", "799145");
        tripuraCities.put("Udaipur", "799102");
        tripuraCities.put("Belonia", "799155");
        tripuraCities.put("Badharghat", "799003");
        tripuraCities.put("Jirania", "799045");

        Map<String, String> ukCities = new HashMap<>();
        ukCities.put("Dehradun", "248001");
        ukCities.put("Haridwar", "249401");
        ukCities.put("Rudrapur", "263153");
        ukCities.put("Nainital", "263001");
        ukCities.put("Roorkee", "247667");
        ukCities.put("Haldwani", "263139");
        ukCities.put("Pithoragarh", "262501");
        ukCities.put("Tehri", "249001");
        ukCities.put("Kashipur", "244713");
        ukCities.put("Karanprayag", "246161");
        ukCities.put("Ranikhet", "263645");
        ukCities.put("Almora", "263601");
        ukCities.put("Mussoorie", "248179");
        ukCities.put("Champawat", "262523");
        ukCities.put("Bageshwar", "263642");

        Map<String, String> wbCities = new HashMap<>();
        wbCities.put("Kolkata", "700001");
        wbCities.put("Siliguri", "734001");
        wbCities.put("Howrah", "711101");
        wbCities.put("Durgapur", "713201");
        wbCities.put("Asansol", "713301");
        wbCities.put("Bardhaman", "713101");
        wbCities.put("Malda", "732101");
        wbCities.put("Purulia", "723101");
        wbCities.put("Kharagpur", "721301");
        wbCities.put("Jalpaiguri", "735101");
        wbCities.put("Birbhum", "731101");
        wbCities.put("Bankura", "722101");
        wbCities.put("Hooghly", "712101");
        wbCities.put("Nadia", "741101");
        wbCities.put("Cooch Behar", "736101");
        wbCities.put("South 24 Parganas", "743368");
        wbCities.put("North 24 Parganas", "743701");
        wbCities.put("Howrah", "711106");


        stateCityPincodeMap.put("Goa", goaCities);
        stateCityPincodeMap.put("Gujarat", gujaratCities);
        stateCityPincodeMap.put("Karnataka", karnatakaCities);
        stateCityPincodeMap.put("Madhya Pradesh", mpCities);
        stateCityPincodeMap.put("Maharashtra", maharashtraCities);
        stateCityPincodeMap.put("Andhra Pradesh", apCities);
        stateCityPincodeMap.put("Uttar Pradesh", upCities);
        stateCityPincodeMap.put("Arunachal Pradesh", arunachalCities);
        stateCityPincodeMap.put("Assam", assamCities);
        stateCityPincodeMap.put("Bihar", biharCities);
        stateCityPincodeMap.put("Chhattisgarh", cgCities);
        stateCityPincodeMap.put("Haryana", haryanaCities);
        stateCityPincodeMap.put("Himachal Pradesh", hpCities);
        stateCityPincodeMap.put("Jharkhand", jharkhandCities);
        stateCityPincodeMap.put("Kerala", keralaCities);
        stateCityPincodeMap.put("Manipur", manipurCities);
        stateCityPincodeMap.put("Meghalaya", meghalayaCities);
        stateCityPincodeMap.put("Mizoram", mizoramCities);
        stateCityPincodeMap.put("Nagaland", nagalandCities);
        stateCityPincodeMap.put("Odisha", odishaCities);
        stateCityPincodeMap.put("Punjab", punjabCities);
        stateCityPincodeMap.put("Rajasthan", rajasthanCities);
        stateCityPincodeMap.put("Sikkim", sikkimCities);
        stateCityPincodeMap.put("Tamil Nadu", tnCities);
        stateCityPincodeMap.put("Telangana", telanganaCities);
        stateCityPincodeMap.put("Tripura", tripuraCities);
        stateCityPincodeMap.put("Uttarakhand", ukCities);
        stateCityPincodeMap.put("West Bengal", wbCities);


}

    private void updateCityDropdown() {
        String selectedState = (String) pickupStateField.getSelectedItem();
        if (selectedState != null) {
            Map<String, String> cities = stateCityPincodeMap.get(selectedState);
            if (cities != null) {
                pickupCityField.removeAllItems();
                for (String city : cities.keySet())

                {pickupCityField.addItem(city);
                }
                pickupCityField.setSelectedIndex(0);
                pickupPincodeField.setText(getPincode((String) pickupCityField.getSelectedItem()));
            }
        }
    }

    private String getPincode(String city) {
        for (Map<String, String> cities : stateCityPincodeMap.values()) {
            if (cities.containsKey(city)) {
                return cities.get(city);
            }
        }
        return "";
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(PickupAddress::new);
    }
}
