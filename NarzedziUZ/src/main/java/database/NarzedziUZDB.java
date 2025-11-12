package database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class NarzedziUZDB {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/NARZEDZIUZDB?useSSL=false&serverTimezone=UTC";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection connect() throws SQLException {
        String user = "root";
        String password = "root123";
        return DriverManager.getConnection(DB_URL, user, password);
    }

    public static void createTables() {
        try (Connection conn = NarzedziUZDB.connect(); Statement stmt = conn.createStatement()) {
            stmt.execute("USE NARZEDZIUZDB");
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS Category (
                category_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(80) NOT NULL
            )
            """);

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS Manufacturer (
                manufacturer_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(80) NOT NULL,
                address VARCHAR(255) NOT NULL
            )
            """);

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS UserAddress (
                address_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                address VARCHAR(255) NOT NULL,
                city VARCHAR(80) NOT NULL,
                zip_code VARCHAR(15) NOT NULL
            );
            """);

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS User (
                user_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                address_id INT UNSIGNED,
                email VARCHAR(255) NOT NULL UNIQUE,
                password VARCHAR(255) NOT NULL,
                first_name VARCHAR(80) NOT NULL,
                last_name VARCHAR(80) NOT NULL,
                role ENUM('Admin','Użytkownik','Pracownik') NOT NULL,
                created_at DATETIME NOT NULL,/* 'YYYY-MM-DD HH:MM:SS' */
                FOREIGN KEY (address_id) REFERENCES UserAddress(address_id)
            );
            """);

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS Product (
                product_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(120) NOT NULL,
                price DECIMAL(10,2) NOT NULL,
                description TEXT,
                quantity INT UNSIGNED NOT NULL,
                category_id INT UNSIGNED,
                manufacturer_id INT UNSIGNED,
                photo BLOB,
                FOREIGN KEY (category_id) REFERENCES Category(category_id),
                FOREIGN KEY (manufacturer_id) REFERENCES Manufacturer(manufacturer_id)
            );
            """);

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS Review (
                review_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                product_id INT UNSIGNED,
                user_id INT UNSIGNED,
                grade TINYINT UNSIGNED,
                comment TEXT,
                rate_date DATETIME,
                FOREIGN KEY (product_id) REFERENCES Product(product_id),
                FOREIGN KEY (user_id) REFERENCES User(user_id)
            );
            """);

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS Cart (
                cart_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                user_id INT UNSIGNED,
                created_at DATETIME,
                FOREIGN KEY (user_id) REFERENCES User(user_id)
            );
            """);

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS CartItem (
                cart_item_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                cart_id INT UNSIGNED,
                product_id INT UNSIGNED,
                quantity INT UNSIGNED,
                FOREIGN KEY (cart_id) REFERENCES Cart(cart_id),
                FOREIGN KEY (product_id) REFERENCES Product(product_id)
            );
            """);

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS Orders (
                order_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                user_id INT UNSIGNED,
                order_date DATETIME,
                order_status VARCHAR(64),
                price_sum DECIMAL(10,2),
                deliver_address VARCHAR(255),
                billing_address VARCHAR(255),
                payment_method VARCHAR(64),
                FOREIGN KEY (user_id) REFERENCES User(user_id)
            );
            """);

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS OrderItem (
                order_item_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                order_id INT UNSIGNED,
                product_id INT UNSIGNED,
                quantity INT UNSIGNED,
                price DECIMAL(10,2),
                FOREIGN KEY (order_id) REFERENCES Orders(order_id),
                FOREIGN KEY (product_id) REFERENCES Product(product_id)
            )
            """);

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS Invoice (
                invoice_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                order_id INT UNSIGNED,
                PDF_path VARCHAR(255),
                exposure_date DATETIME,
                FOREIGN KEY (order_id) REFERENCES Orders(order_id)
            )
            """);

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS EmailHistory (
                email_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                user_id INT UNSIGNED,
                mail_type VARCHAR(64),
                send_date DATETIME,
                FOREIGN KEY (user_id) REFERENCES User(user_id)
            )
            """);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Insert do Category
    public static void insertCategory(String name) {
        String sql = "INSERT INTO Category(name) VALUES (?);";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Insert do Manufacturer
    public static void insertManufacturer(String name, String address) {
        String sql = "INSERT INTO Manufacturer(name, address) VALUES (?, ?);";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Insert do UserAddress
    public static void insertUserAddress(String address, String city, String zipCode) {
        String sql = "INSERT INTO UserAddress(address, city, zip_code) VALUES (?, ?, ?);";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, address);
            pstmt.setString(2, city);
            pstmt.setString(3, zipCode);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Insert do User
    public static void insertUser(
            Integer addressId, String email, String password, String firstName,
            String lastName, String role) {

        StringBuilder columns = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();
        List<Object> params = new ArrayList<>();
        String createdAt = String.valueOf(LocalDate.now());

        if (addressId != null) {
            columns.append("address_id, ");
            placeholders.append("?, ");
            params.add(addressId);
        }
        if (email != null) {
            columns.append("email, ");
            placeholders.append("?, ");
            params.add(email);
        }
        if (password != null) {
            columns.append("password, ");
            placeholders.append("?, ");
            params.add(password);
        }
        if (firstName != null) {
            columns.append("first_name, ");
            placeholders.append("?, ");
            params.add(firstName);
        }
        if (lastName != null) {
            columns.append("last_name, ");
            placeholders.append("?, ");
            params.add(lastName);
        }
        if (role != null) {
            columns.append("role, ");
            placeholders.append("?, ");
            params.add(role);
        }
        if (createdAt != null) {
            columns.append("created_at, ");
            placeholders.append("?, ");
            params.add(createdAt);
        }

        // Usuwamy ostatnie przecinki i spacje
        if (columns.length() > 0) {
            columns.setLength(columns.length() - 2);
            placeholders.setLength(placeholders.length() - 2);
        } else {
            throw new IllegalArgumentException("At least one field must be provided.");
        }

        String sql = "INSERT INTO User (" + columns.toString() + ") VALUES (" + placeholders.toString() + ")";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Insert do Product
    public static void insertProduct(String name, double price, String description, int quantity, int categoryId, int manufacturerId, byte[] photo) {
        String sql = "INSERT INTO Product(name, price, description, quantity, category_id, manufacturer_id, photo) VALUES (?, ?, ?, ?, ?, ?, ?);";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setString(3, description);
            pstmt.setInt(4, quantity);
            pstmt.setInt(5, categoryId);
            pstmt.setInt(6, manufacturerId);
            pstmt.setBytes(7, photo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Insert do Review
    public static void insertReview(int productId, int userId, int grade, String comment, String rateDate) {
        String sql = "INSERT INTO Review(product_id, user_id, grade, comment, rate_date) VALUES (?, ?, ?, ?, ?);";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            pstmt.setInt(2, userId);
            pstmt.setInt(3, grade);
            pstmt.setString(4, comment);
            pstmt.setString(5, rateDate);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Insert do Cart
    public static void insertCart(int userId, String createdAt) {
        String sql = "INSERT INTO Cart(user_id, created_at) VALUES (?, ?);";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, createdAt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Insert do CartItem
    public static void insertCartItem(int cartId, int productId, int quantity) {
        String sql = "INSERT INTO CartItem(cart_id, product_id, quantity) VALUES (?, ?, ?);";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cartId);
            pstmt.setInt(2, productId);
            pstmt.setInt(3, quantity);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Insert do Orders
    public static void insertOrder(int userId, String orderDate, String orderStatus, double priceSum, String deliverAddress, String billingAddress, String paymentMethod) {
        String sql = "INSERT INTO Orders(user_id, order_date, order_status, price_sum, deliver_address, billing_address, payment_method) VALUES (?, ?, ?, ?, ?, ?, ?);";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, orderDate);
            pstmt.setString(3, orderStatus);
            pstmt.setDouble(4, priceSum);
            pstmt.setString(5, deliverAddress);
            pstmt.setString(6, billingAddress);
            pstmt.setString(7, paymentMethod);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Insert do OrderItem
    public static void insertOrderItem(int orderId, int productId, int quantity, double price) {
        String sql = "INSERT INTO OrderItem(order_id, product_id, quantity, price) VALUES (?, ?, ?, ?);";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            pstmt.setInt(2, productId);
            pstmt.setInt(3, quantity);
            pstmt.setDouble(4, price);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Insert do Invoice
    public static void insertInvoice(int orderId, String pdfPath, String exposureDate) {
        String sql = "INSERT INTO Invoice(order_id, PDF_path, exposure_date) VALUES (?, ?, ?);";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            pstmt.setString(2, pdfPath);
            pstmt.setString(3, exposureDate);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Insert do EmailHistory
    public static void insertEmailHistory(int userId, String mailType, String sendDate) {
        String sql = "INSERT INTO EmailHistory(user_id, mail_type, send_date) VALUES (?, ?, ?);";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, mailType);
            pstmt.setString(3, sendDate);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Aktualizacja i usuwanie w tabeli Category
    public static void updateCategory(int categoryId, String newName) {
        String sql = "UPDATE Category SET name = ? WHERE category_id = ?;";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, categoryId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteCategory(int categoryId) {
        String sql = "DELETE FROM Category WHERE category_id = ?;";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, categoryId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Aktualizacja i usuwanie w tabeli Manufacturer
    public static void updateManufacturer(int manufacturerId, String newName, String newAddress) {
        String sql = "UPDATE Manufacturer SET name = ?, address = ? WHERE manufacturer_id = ?;";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setString(2, newAddress);
            pstmt.setInt(3, manufacturerId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteManufacturer(int manufacturerId) {
        String sql = "DELETE FROM Manufacturer WHERE manufacturer_id = ?;";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, manufacturerId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Aktualizacja i usuwanie w tabeli UserAddress
    public static void updateUserAddress(int addressId, String newAddress, String newCity, String newZipCode) {
        String sql = "UPDATE UserAddress SET address = ?, city = ?, zip_code = ? WHERE address_id = ?;";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newAddress);
            pstmt.setString(2, newCity);
            pstmt.setString(3, newZipCode);
            pstmt.setInt(4, addressId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteUserAddress(int addressId) {
        String sql = "DELETE FROM UserAddress WHERE address_id = ?;";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, addressId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Aktualizacja i usuwanie w tabeli User
    public static void updateUser(int userId, int newAddressId, String newEmail, String newPassword, String newFirstName, String newLastName, String newRole, String newCreatedAt) {
        String sql = "UPDATE User SET address_id = ?, email = ?, password = ?, first_name = ?, last_name = ?, role = ?, created_at = ? WHERE user_id = ?;";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newAddressId);
            pstmt.setString(2, newEmail);
            pstmt.setString(3, newPassword);
            pstmt.setString(4, newFirstName);
            pstmt.setString(5, newLastName);
            pstmt.setString(6, newRole);
            pstmt.setString(7, newCreatedAt);
            pstmt.setInt(8, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteUser(int userId) {
        String sql = "DELETE FROM User WHERE user_id = ?;";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Aktualizacja i usuwanie w tabeli Product
    public static void updateProduct(int productId, String newName, double newPrice, String newDescription, int newQuantity, int newCategoryId, int newManufacturerId, byte[] newPhoto) {
        String sql = "UPDATE Product SET name = ?, price = ?, description = ?, quantity = ?, category_id = ?, manufacturer_id = ?, photo = ? WHERE product_id = ?;";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setDouble(2, newPrice);
            pstmt.setString(3, newDescription);
            pstmt.setInt(4, newQuantity);
            pstmt.setInt(5, newCategoryId);
            pstmt.setInt(6, newManufacturerId);
            pstmt.setBytes(7, newPhoto);
            pstmt.setInt(8, productId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteProduct(int productId) {
        String sql = "DELETE FROM Product WHERE product_id = ?;";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Aktualizacja i usuwanie w tabeli Review
    public static void updateReview(int reviewId, int newProductId, int newUserId, int newGrade, String newComment, String newRateDate) {
        String sql = "UPDATE Review SET product_id = ?, user_id = ?, grade = ?, comment = ?, rate_date = ? WHERE review_id = ?;";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newProductId);
            pstmt.setInt(2, newUserId);
            pstmt.setInt(3, newGrade);
            pstmt.setString(4, newComment);
            pstmt.setString(5, newRateDate);
            pstmt.setInt(6, reviewId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteReview(int reviewId) {
        String sql = "DELETE FROM Review WHERE review_id = ?;";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reviewId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Aktualizacja i usuwanie w tabeli Cart
    public static void updateCart(int cartId, int newUserId, String newCreatedAt) {
        String sql = "UPDATE Cart SET user_id = ?, created_at = ? WHERE cart_id = ?;";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newUserId);
            pstmt.setString(2, newCreatedAt);
            pstmt.setInt(3, cartId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteCart(int cartId) {
        String sql = "DELETE FROM Cart WHERE cart_id = ?;";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cartId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Aktualizacja i usuwanie w tabeli CartItem
    public static void updateCartItem(int cartItemId, int newCartId, int newProductId, int newQuantity) {
        String sql = "UPDATE CartItem SET cart_id = ?, product_id = ?, quantity = ? WHERE cart_item_id = ?;";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newCartId);
            pstmt.setInt(2, newProductId);
            pstmt.setInt(3, newQuantity);
            pstmt.setInt(4, cartItemId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteCartItem(int cartItemId) {
        String sql = "DELETE FROM CartItem WHERE cart_item_id = ?;";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cartItemId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Aktualizacja i usuwanie w tabeli Orders
    public static void updateOrder(int orderId, int newUserId, String newOrderDate, String newOrderStatus, double newPriceSum, String newDeliverAddress, String newBillingAddress, String newPaymentMethod) {
        String sql = "UPDATE Orders SET user_id = ?, order_date = ?, order_status = ?, price_sum = ?, deliver_address = ?, billing_address = ?, payment_method = ? WHERE order_id = ?;";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newUserId);
            pstmt.setString(2, newOrderDate);
            pstmt.setString(3, newOrderStatus);
            pstmt.setDouble(4, newPriceSum);
            pstmt.setString(5, newDeliverAddress);
            pstmt.setString(6, newBillingAddress);
            pstmt.setString(7, newPaymentMethod);
            pstmt.setInt(8, orderId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteOrder(int orderId) {
        String sql = "DELETE FROM Orders WHERE order_id = ?;";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Aktualizacja i usuwanie w tabeli OrderItem
    public static void updateOrderItem(int orderItemId, int newOrderId, int newProductId, int newQuantity, double newPrice) {
        String sql = "UPDATE OrderItem SET order_id = ?, product_id = ?, quantity = ?, price = ? WHERE order_item_id = ?;";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newOrderId);
            pstmt.setInt(2, newProductId);
            pstmt.setInt(3, newQuantity);
            pstmt.setDouble(4, newPrice);
            pstmt.setInt(5, orderItemId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteOrderItem(int orderItemId) {
        String sql = "DELETE FROM OrderItem WHERE order_item_id = ?;";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderItemId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Aktualizacja i usuwanie w tabeli Invoice
    public static void updateInvoice(int invoiceId, int newOrderId, String newPdfPath, String newExposureDate) {
        String sql = "UPDATE Invoice SET order_id = ?, PDF_path = ?, exposure_date = ? WHERE invoice_id = ?;";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newOrderId);
            pstmt.setString(2, newPdfPath);
            pstmt.setString(3, newExposureDate);
            pstmt.setInt(4, invoiceId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteInvoice(int invoiceId) {
        String sql = "DELETE FROM Invoice WHERE invoice_id = ?;";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, invoiceId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Aktualizacja i usuwanie w tabeli EmailHistory
    public static void updateEmailHistory(int emailId, int newUserId, String newMailType, String newSendDate) {
        String sql = "UPDATE EmailHistory SET user_id = ?, mail_type = ?, send_date = ? WHERE email_id = ?;";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newUserId);
            pstmt.setString(2, newMailType);
            pstmt.setString(3, newSendDate);
            pstmt.setInt(4, emailId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteEmailHistory(int emailId) {
        String sql = "DELETE FROM EmailHistory WHERE email_id = ?;";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, emailId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> selectSmth(String table, String column, String conditionColumn, String conditionValue) throws SQLException {
        List<String> results = new ArrayList<>();
        String sql = "SELECT " + column + " FROM " + table;

        boolean hasCondition = conditionColumn != null && !conditionColumn.isEmpty() && conditionValue != null && !conditionValue.isEmpty();
        if (hasCondition) {
            sql += " WHERE " + conditionColumn + " = ?";
        }

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (hasCondition) {
                pstmt.setString(1, conditionValue);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    results.add(rs.getString(column));
                }
            }
        }
        return results;
    }

    public static void main(String[] args) {
        createTables();
    }
}
