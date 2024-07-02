package com.example.model;

public class Diary {
    private int id;
    private String diaryName;
    private String content;
    private String authorName;
    private int authorId;
    private String location;

    private int score;

    public Diary() {
    }

    public Diary(int id, String diaryName, String content, String authorName, int authorId, String location, int score) {
        this.id = id;
        this.diaryName = diaryName;
        this.content = content;
        this.authorName = authorName;
        this.authorId = authorId;
        this.location = location;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiaryName() {
        return diaryName;
    }

    public void setDiaryName(String diaryName) {
        this.diaryName = diaryName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
