<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Stock Buyer Dashboard</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
    <header>
        <h2>Stock Buyer Dashboard</h2>
    </header>

    <div class="main-container">
        <h3>Available Stocks</h3>
        <table>
            <tr>
                <th>Stock ID</th>
                <th>Stock Name</th>
                <th>Quantity Available</th>
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

                    String query = "SELECT * FROM stocks WHERE quantity > 0";
                    ps = con.prepareStatement(query);
                    rs = ps.executeQuery();

                    while (rs.next()) {
            %>
                        <tr>
                            <td><%= rs.getInt("id") %></td>
                            <td><%= rs.getString("name") %></td>
                            <td><%= rs.getInt("quantity") %></td>
                            <td><%= rs.getDouble("price") %></td>
                            <td>
                                <form action="buyStock.jsp" method="post">
                                    <input type="hidden" name="stock_id" value="<%= rs.getInt("id") %>">
                                    <input type="number" name="buy_quantity" min="1" max="<%= rs.getInt("quantity") %>" required>
                                    <button type="submit">Buy</button>
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
