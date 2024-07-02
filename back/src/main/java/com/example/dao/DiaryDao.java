package com.example.dao;

import com.example.model.Diary;
import com.example.utils.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class DiaryDao {
    private static final String DB_URL = "jdbc:sqlite:D:\\Aack\\arch\\src\\main\\resources\\sample.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void saveDiary(Diary diary) throws SQLException {
        String sql = "INSERT INTO diary (diary_name, content, author_name, author_id, location, score) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, diary.getDiaryName());
            pstmt.setString(2, diary.getContent());
            pstmt.setString(3, diary.getAuthorName());
            pstmt.setInt(4, diary.getAuthorId());
            pstmt.setString(5, diary.getLocation());
            pstmt.setInt(6, diary.getScore());
            pstmt.executeUpdate();
        }
    }

    public static List<Diary> getAllDiaries() throws SQLException {
        List<Diary> diaries = new ArrayList<>();
        String sql = "SELECT * FROM diary";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Diary diary = new Diary();
                diary.setId(rs.getInt("id"));
                diary.setDiaryName(rs.getString("diary_name"));
                diary.setContent(rs.getString("content"));
                diary.setId(rs.getInt("author_id"));
                diary.setAuthorName(rs.getString("author_name"));
                diary.setLocation(rs.getString("location"));
                diary.setScore(rs.getInt("score"));
                diaries.add(diary);
            }
        }
        return diaries;
    }

    public static List<Diary> getRecommendedDiaries(String authorName) throws SQLException {
        List<Diary> diaries = new ArrayList<>();
        String sql = "SELECT * FROM diary ORDER BY score DESC LIMIT 10";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, authorName);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Diary diary = new Diary();
                    diary.setId(rs.getInt("id"));
                    diary.setDiaryName(rs.getString("diary_name"));
                    diary.setContent(rs.getString("content"));
                    diary.setAuthorName(rs.getString("author_name"));
                    diary.setLocation(rs.getString("location"));
                    diary.setScore(rs.getInt("score"));
                    diaries.add(diary);
                }
            }
        }
        return diaries;
    }

    public static List<Diary> searchDiariesByTitle(String searchQuery) throws SQLException {
        System.out.println(searchQuery);
        List<Diary> diaries = new ArrayList<>();
        String sql = "SELECT * FROM diary";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String title = rs.getString("diary_name");
                if (LogTools.KMPMatcher.kmpMatch(title, searchQuery)) {
                    Diary diary = new Diary();
                    diary.setId(rs.getInt("id"));
                    diary.setDiaryName(rs.getString("diary_name"));
                    diary.setContent(rs.getString("content"));
                    diary.setAuthorName(rs.getString("author_name"));
                    diary.setAuthorId(rs.getInt("author_id"));
                    diary.setLocation(rs.getString("location"));
                    diary.setScore(rs.getInt("score"));
                    diaries.add(diary);
                }
            }
        }
        return diaries;
    }
    public static void updateDiary(Diary diary) throws SQLException {
        String sql = "UPDATE diary SET diary_name = ?, content = ?, author_name = ?, location = ?, score = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, diary.getDiaryName());
            pstmt.setString(2, diary.getContent());
            pstmt.setString(3, diary.getAuthorName());
            pstmt.setString(4, diary.getLocation());
            pstmt.setInt(5, diary.getScore());
            pstmt.setInt(6, diary.getId());
            pstmt.executeUpdate();
        }
    }
}
