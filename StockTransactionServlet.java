package com.stock;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class StockTransactionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	System.out.println("Request has came to this servlet");
        // Get stockId from the form submission
        String stockId = request.getParameter("stockId");
        String action = request.getParameter("action");  // "buy" or "sell"

        // Store stockId and action in session
        HttpSession session = request.getSession();
        session.setAttribute("stockId", stockId);
        session.setAttribute("action", action);
System.out.println(stockId);
        // Redirect to the respective JSP page
        if ("buy".equals(action)) {
            response.sendRedirect("Buyer.jsp");
        } else if ("sell".equals(action)) {
            response.sendRedirect("Seller.jsp");
        }
    }
}
