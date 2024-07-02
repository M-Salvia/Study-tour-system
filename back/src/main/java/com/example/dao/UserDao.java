package com.example.dao;

import com.example.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    private static final String DB_URL = "jdbc:sqlite:D:\\Aack\\arch\\src\\main\\resources\\sample.db";

    // 数据库连接URL，请根据你的实际情况修改
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    // 保存用户到数据库
    public void saveUser(User user) throws SQLException {
        String sql = "INSERT INTO user (username, password, email, age, traffic_convenience, service_quality, visitor_flow_rate, cultural_atmosphere, natural_landscape, price_performance_ratio) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.setInt(4, user.getAge());
            pstmt.setInt(5, user.getTraffic_convenience());
            pstmt.setInt(6, user.getService_quality());
            pstmt.setInt(7, user.getVisitor_flow_rate());
            pstmt.setInt(8, user.getCultural_atmosphere());
            pstmt.setInt(9, user.getNatural_landscape());
            pstmt.setInt(10, user.getPrice_performance_ratio());
            pstmt.executeUpdate();
        }
    }
    public void updateUserPreferences(User user) throws SQLException {
        String sql = "UPDATE user SET traffic_convenience = ?, service_quality = ?, visitor_flow_rate = ?, cultural_atmosphere = ?, natural_landscape = ?, price_performance_ratio = ? WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, user.getTraffic_convenience());
            pstmt.setInt(2, user.getService_quality());
            pstmt.setInt(3, user.getVisitor_flow_rate());
            pstmt.setInt(4, user.getCultural_atmosphere());
            pstmt.setInt(5, user.getNatural_landscape());
            pstmt.setInt(6, user.getPrice_performance_ratio());
            pstmt.setString(7, user.getUsername());
            pstmt.executeUpdate();
        }
    }


    // 根据用户名获取用户
    public static User getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM user WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("email"),
                            rs.getInt("age"),
                            rs.getInt("traffic_convenience"),
                            rs.getInt("service_quality"),
                            rs.getInt("visitor_flow_rate"),
                            rs.getInt("cultural_atmosphere"),
                            rs.getInt("natural_landscape"),
                            rs.getInt("price_performance_ratio")
                    );
                }
            }
        }
        return null;
    }
}
