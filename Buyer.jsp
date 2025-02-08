<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Stock Seller Dashboard</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
    <header>
        <h2>Stock Seller Dashboard</h2>
    </header>

    <div class="main-container">
        <h3>List Your Stock</h3>
        <form action="sellStock.jsp" method="post">
            <label>Stock Name:</label>
            <input type="text" name="stock_name" required>
            
            <label>Quantity:</label>
            <input type="number" name="quantity" required>
            
            <label>Price per Share:</label>
            <input type="number" step="0.01" name="price" required>
            
            <button type="submit">List Stock</button>
        </form>

        <h3>Your Listed Stocks</h3>
        <table>
            <tr>
                <th>Stock ID</th>
                <th>Stock Name</th>
                <th>Quantity</th>
                <th>Price per Share</th>
                <th>Action</th>
            </tr>

            <%
                Connection con = null;
                PreparedStatement ps = null;
                ResultSet rs = null;

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/stock_market", "root", "password");

                    String query = "SELECT * FROM stocks WHERE seller_id = ?";
                    ps = con.prepareStatement(query);
                    ps.setInt(1, 1); // Assuming seller_id is 1 for demo, replace it with session user ID

                    rs = ps.executeQuery();
                    while (rs.next()) {
            %>
                        <tr>
                            <td><%= rs.getInt("id") %></td>
                            <td><%= rs.getString("name") %></td>
                            <td><%= rs.getInt("quantity") %></td>
                            <td><%= rs.getDouble("price") %></td>
                            <td>
                                <form action="removeStock.jsp" method="post">
                                    <input type="hidden" name="stock_id" value="<%= rs.getInt("id") %>">
                                    <button type="submit">Remove</button>
                                </form>
                            </td>
                        </tr>
            <%
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (rs != null) rs.close();
                    if (ps != null) ps.close();
                    if (con != null) con.close();
                }
            %>
        </table>
    </div>

    <footer>
        <p>&copy; 2025 Stock Trading System</p>
    </footer>
</body>
</html>
