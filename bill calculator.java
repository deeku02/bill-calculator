import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class BillCalculatorGUI extends JFrame implements ActionListener {
    private JLabel lblPrice, lblQuantity;
    private JTextField txtPrice, txtQuantity;
    private JButton btnAdd, btnCalculate;
    private JTextArea txtBill;
    private Connection conn;

    public BillCalculatorGUI() {
        // Set up the GUI
        setTitle("Bill Calculator");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the input fields
        lblPrice = new JLabel("Price:");
        txtPrice = new JTextField(10);
        lblQuantity = new JLabel("Quantity:");
        txtQuantity = new JTextField(10);

        // Create the buttons
        btnAdd = new JButton("Add Item");
        btnAdd.addActionListener(this);
        btnCalculate = new JButton("Calculate Total");
        btnCalculate.addActionListener(this);

        // Create the bill text area
        txtBill = new JTextArea();
        txtBill.setEditable(false);

        // Create a panel and add components
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));
        panel.add(lblPrice);
        panel.add(txtPrice);
        panel.add(lblQuantity);
        panel.add(txtQuantity);
        panel.add(btnAdd);
        panel.add(btnCalculate);
        panel.add(new JScrollPane(txtBill));

        // Add the panel to the frame
        add(panel);

        // Connect to the database
        connectToDatabase();
    }

    private void connectToDatabase() {
        try {
            // Update the connection URL, username, and password based on your database settings
            String url = "jdbc:mysql://localhost:3306/billdb";
            String username = "your_username";
            String password = "your_password";

            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd) {
            addItem();
        } else if (e.getSource() == btnCalculate) {
            calculateTotal();
        }
    }

    private void addItem() {
        try {
            double price = Double.parseDouble(txtPrice.getText());
            int quantity = Integer.parseInt(txtQuantity.getText());

            // Insert the item into the database
            String query = "INSERT INTO items (price, quantity) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setDouble(1, price);
            statement.setInt(2, quantity);
            statement.executeUpdate();

            System.out.println("Item added to the database");

            // Clear the input fields
            txtPrice.setText("");
            txtQuantity.setText("");
        } catch (NumberFormatException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void calculateTotal() {
        try {
            // Retrieve the items from the database
            String query = "SELECT price, quantity FROM items";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            double totalCost = 0.0;
            StringBuilder billText = new StringBuilder();

            while (resultSet.next()) {
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");

                double itemCost = price * quantity;
                totalCost += itemCost;

                billText.append("Price: ").append(price).append("\tQuantity: ").append(quantity)
                        .append("\tItem Cost: ").append(itemCost).append("\n");
            }

            billText.append("\nTotal cost: ").append(totalCost);

            // Display the bill in the text area
            txtBill.setText(billText.toString());

            // Close the result set, statement, and connection
            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BillCalculatorGUI billCalculator = new BillCalculatorGUI();
            billCalculator.setVisible(true);
        });
    }
}

