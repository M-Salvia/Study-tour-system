package com.example.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:D:/Aack/arch/src/main/resources/sample.db";  // 使用项目中的数据库文件

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        // 数据库已经提前创建和初始化，这里可以不用做什么
    }
}
