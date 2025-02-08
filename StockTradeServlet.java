import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/pqr")
public class StockTradeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/market";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        int stockId = Integer.parseInt(request.getParameter("stock_id"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        int userId = 1;  // Replace with session user ID

        if (action.equals("buy")) {
            processBuyTransaction(userId, stockId, quantity, response);
        } else if (action.equals("sell")) {
            processSellTransaction(userId, stockId, quantity, response);
        }
    }

    private void processBuyTransaction(int userId, int stockId, int quantity, HttpServletResponse response) throws IOException {
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Get stock details
            String stockQuery = "SELECT stock_name, current_price, available_quantity FROM stocks WHERE stock_id = ?";
            PreparedStatement psStock = con.prepareStatement(stockQuery);
            psStock.setInt(1, stockId);
            ResultSet rsStock = psStock.executeQuery();

            if (rsStock.next()) {
                String stockName = rsStock.getString("stock_name");
                double stockPrice = rsStock.getDouble("current_price");
                int availableQuantity = rsStock.getInt("available_quantity");

                if (quantity > availableQuantity) {
                    response.getWriter().println("Error: Not enough stock available.");
                    return;
                }

                // Get user balance
                String userQuery = "SELECT balance FROM users WHERE user_id = ?";
                PreparedStatement psUser = con.prepareStatement(userQuery);
                psUser.setInt(1, userId);
                ResultSet rsUser = psUser.executeQuery();

                if (rsUser.next()) {
                    double userBalance = rsUser.getDouble("balance");
                    double totalCost = stockPrice * quantity;

                    if (totalCost > userBalance) {
                        response.getWriter().println("Error: Insufficient balance.");
                        return;
                    }

                    // Update user balance
                    String updateBalance = "UPDATE users SET balance = balance - ? WHERE user_id = ?";
                    PreparedStatement psUpdateBalance = con.prepareStatement(updateBalance);
                    psUpdateBalance.setDouble(1, totalCost);
                    psUpdateBalance.setInt(2, userId);
                    psUpdateBalance.executeUpdate();

                    // Update stock quantity
                    String updateStock = "UPDATE stocks SET available_quantity = available_quantity - ? WHERE stock_id = ?";
                    PreparedStatement psUpdateStock = con.prepareStatement(updateStock);
                    psUpdateStock.setInt(1, quantity);
                    psUpdateStock.setInt(2, stockId);
                    psUpdateStock.executeUpdate();

                    // Insert transaction record
                    String insertTransaction = "INSERT INTO transactions (user_id, stock_id, quantity, price, type, timestamp) VALUES (?, ?, ?, ?, 'buy', ?)";
                    PreparedStatement psTransaction = con.prepareStatement(insertTransaction);
                    psTransaction.setInt(1, userId);
                    psTransaction.setInt(2, stockId);
                    psTransaction.setInt(3, quantity);
                    psTransaction.setDouble(4, stockPrice);
                    psTransaction.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                    psTransaction.executeUpdate();

                    response.getWriter().println("Success: Stock purchased!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }

    private void processSellTransaction(int userId, int stockId, int quantity, HttpServletResponse response) throws IOException {
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Get stock details
            String stockQuery = "SELECT stock_name, current_price FROM stocks WHERE stock_id = ?";
            PreparedStatement psStock = con.prepareStatement(stockQuery);
            psStock.setInt(1, stockId);
            ResultSet rsStock = psStock.executeQuery();

            if (rsStock.next()) {
                String stockName = rsStock.getString("stock_name");
                double stockPrice = rsStock.getDouble("current_price");

                // Update stock quantity
                String updateStock = "UPDATE stocks SET available_quantity = available_quantity + ? WHERE stock_id = ?";
                PreparedStatement psUpdateStock = con.prepareStatement(updateStock);
                psUpdateStock.setInt(1, quantity);
                psUpdateStock.setInt(2, stockId);
                psUpdateStock.executeUpdate();

                // Insert transaction record
                String insertTransaction = "INSERT INTO transactions (user_id, stock_id, quantity, price, type, timestamp) VALUES (?, ?, ?, ?, 'sell', ?)";
                PreparedStatement psTransaction = con.prepareStatement(insertTransaction);
                psTransaction.setInt(1, userId);
                psTransaction.setInt(2, stockId);
                psTransaction.setInt(3, quantity);
                psTransaction.setDouble(4, stockPrice);
                psTransaction.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                psTransaction.executeUpdate();

                response.getWriter().println("Success: Stock sold!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}
