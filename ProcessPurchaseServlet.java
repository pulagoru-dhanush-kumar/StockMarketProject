import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@WebServlet("/processPurchase")
public class ProcessPurchaseServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve data from the form
        int userId = Integer.parseInt(request.getParameter("userId"));
        int productId = Integer.parseInt(request.getParameter("productId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        double price = Double.parseDouble(request.getParameter("price"));
        
        // Assume 'buy' type transaction
        String transactionType = "buy"; 

        // Create a transaction object or directly interact with the database
        try {
            // Create a connection to the database
            Connection connection =Jdbc.jdbcconnection();
            String query = "INSERT INTO transactions (user_id, stock_id, quantity, price, type) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            
            statement.setInt(1, userId);
            statement.setInt(2, productId);
            statement.setInt(3, quantity);
            statement.setDouble(4, price);
            statement.setString(5, transactionType);

            // Execute the insert query
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                response.getWriter().write("Transaction successful!");
            } else {
                response.getWriter().write("Transaction failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("An error occurred while processing your transaction.");
        }
    }
}
