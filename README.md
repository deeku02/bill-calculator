In this code, we create a GUI using the Swing library. The GUI consists of input fields for price and quantity, buttons to add items and calculate the total cost, and a text area to display the bill. The connectToDatabase() method establishes a connection to the database using JDBC.

When the "Add Item" button is clicked, the addItem() method retrieves the price and quantity from the input fields, inserts the item into the database, and clears the input fields.

When the "Calculate Total" button is clicked, the calculateTotal() method retrieves all the items from the database, calculates the total cost, and displays the bill in the text area.

Please note that you need to update the database connection URL, username, and password in the connectToDatabase() method to match your database settings. Additionally, you need to have the MySQL JDBC driver in your classpath to establish the database connection.
