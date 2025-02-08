<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.stock.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.sql.SQLException" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Stock Market</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; text-align: center; }
        .header { background-color: #333; color: white; padding: 15px; text-align: center; }
        .header a { color: white; text-decoration: none; margin: 0 15px; font-weight: bold; }
        table { width: 80%; border-collapse: collapse; margin: 20px auto; }
        th, td { border: 1px solid black; padding: 10px; text-align: center; }
        th { background-color: #4CAF50; color: white; }
        button { padding: 5px 10px; border: none; cursor: pointer; border-radius: 5px; }
        .buy-btn { background-color: #008CBA; color: white; }
        .sell-btn { background-color: #f44336; color: white; }
        .loan-btn { background-color: #ff9800; color: white; }
    </style>
</head>
<body>

    <!-- Header Section -->
    <div class="header">
        <a href="">Login</a>
        <a href="">Register</a>
        <a href="">Transactions</a>
        <a href="">Apply for Loan</a>
    </div>

    <h2>Available Stocks</h2>

    <table>
        <tr>
            <th>Stock ID</th>
            <th>Stock Name</th>
            <th>Current Price</th>
            <th>Available Quantity</th>
            <th>Actions</th>
        </tr>

        <%
            try {
                ArrayList<Stock> stockList = com.stock.Stock.getStocks();
                for (Stock stock : stockList) {
        %>
                    <tr>
                        <td><%= stock.getStockid() %></td>
                        <td><%= stock.getStockname() %></td>
                        <td>₹<%= stock.getCurrentprice() %></td>
                        <td><%= stock.getAvailablequantity() %></td>
                        <td>
                            <form action="abc" method="post">
                                <input type="hidden" name="stockId" value="<%= stock.getStockid() %>">
                                <button type="submit" name="action" value="buy" class="buy-btn">Buy</button>
                                <button type="submit" name="action" value="sell" class="sell-btn">Sell</button>
                            </form>
                        </td>
                    </tr>
        <%
                }
            } catch (Exception e) {
                out.println("<tr><td colspan='5' style='color: red;'>Error: " + e.getMessage() + "</td></tr>");
            }
        %>
    </table>

</body>
</html>
