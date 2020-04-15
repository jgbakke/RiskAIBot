package com.sillysoft.lux.agent.utils;

import com.sillysoft.lux.Board;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.Date;

public class Testing {

    public static String httpRequest(String json) throws IOException{
        URL url = new URL("http://localhost:8080/request-params");
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        OutputStream os = con.getOutputStream();
        try {
            byte[] input = json.getBytes("utf-8");
            os.write(input, 0, input.length);
        } finally {
            os.close();
        }

        BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8")
        );

        try {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        } finally {
            br.close();
        }
    }

    public static void Test(Board board) {
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("org.sqlite.JDBC");
            System.out.println("Driver loaded");
        } catch (Exception e){
            e.printStackTrace();
            return;
        }

        Connection conn = connect();
        insert(conn);
        selectAll(conn, board);
        try {
            conn.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }

    }

    private static void insert(Connection conn){
        String name = new Date().toString();
        int capacity = new Date().getMinutes();

        String sql = "INSERT INTO warehouses(name,capacity) VALUES(?,?)";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setDouble(2, capacity);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:/Users/jordan/Library/Application Support/Lux/test.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void selectAll(Connection conn, Board board){
        String sql = "SELECT id, name, capacity FROM warehouses";

        try {
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql);
            // loop through the result set
            while (rs.next()) {
                board.sendChat(rs.getInt("id") +  "\t" +
                        rs.getString("name") + "\t" +
                        rs.getDouble("capacity"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
