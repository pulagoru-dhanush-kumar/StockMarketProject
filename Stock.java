package com.stock;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Stock {

    private int stockid;
    private String stockname;
    private int currentprice;
    private int availablequantity;

    public int getStockid() {
        return stockid;
    }

    public void setStockid(int stockid) {
        this.stockid = stockid;
    }

    public String getStockname() {
        return stockname;
    }

    public void setStockname(String stockname) {
        this.stockname = stockname;
    }

    public int getCurrentprice() {
        return currentprice;
    }

    public void setCurrentprice(int currentprice) {
        this.currentprice = currentprice;
    }

    public int getAvailablequantity() {
        return availablequantity;
    }

    public void setAvailablequantity(int availablequantity) {
        this.availablequantity = availablequantity;
    }

  public  static ArrayList<Stock> getStocks() throws ClassNotFoundException, SQLException, IOException {
        ArrayList<Stock> stocks = new ArrayList<Stock>();
        Connection con = Jdbc.jdbcconnection();  // Ensure this connects to the correct database
        Statement st = con.createStatement();
        st.execute("USE market");  // Ensure the correct database is selected
        ResultSet rs = st.executeQuery("SELECT * FROM stocks");  // Query to fetch stock data from the 'stocks' table
        while (rs.next()) {
            Stock stock = new Stock();
            stock.setStockid(rs.getInt("stock_id"));
            stock.setStockname(rs.getString("stock_name"));
            stock.setCurrentprice(rs.getInt("current_price"));
            stock.setAvailablequantity(rs.getInt("available_quantity"));
            stocks.add(stock);
        }
        return stocks;
    }

    static void updateStockPrices() throws SQLException, ClassNotFoundException, IOException {
    	System.out.println("Price Updated");
        ArrayList<Stock> stocks = getStocks();
        Random random = new Random();
        for (Stock stock : stocks) {
            int newPrice = random.nextInt(100) + 1;
            stock.setCurrentprice(newPrice);
            String updateSQL = "UPDATE stocks SET current_price = ? WHERE stock_id = ?";
            Connection con = Jdbc.jdbcconnection();  // Ensure this connects to the correct database
            Statement st=con.createStatement();
            st.execute("use market");
            PreparedStatement ps = con.prepareStatement(updateSQL);
            ps.setInt(1, newPrice);
            ps.setInt(2, stock.getStockid());
            ps.executeUpdate();
            System.out.println("Stock ID " + stock.getStockid() + " updated to new price: " + newPrice);
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
        ArrayList<Stock> stocks = getStocks();
        System.out.println("Initial Stock Prices:");
        for (Stock stock : stocks) {
            System.out.println("Stock ID: " + stock.getStockid() + ", Name: " + stock.getStockname() + ", Price: " + stock.getCurrentprice() + " Available Quantity: " + stock.getAvailablequantity());
        }

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            try {
                updateStockPrices();
            } catch (SQLException | ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }, 0, 5, TimeUnit.MINUTES);

        try {
            Thread.sleep(30 * 60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       scheduler.shutdown();
    }
}
