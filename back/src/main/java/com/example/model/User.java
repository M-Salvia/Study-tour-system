package com.example.model;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private int age;

    private int traffic_convenience;
    private int service_quality;
    private int visitor_flow_rate;
    private int cultural_atmosphere;
    private int natural_landscape;
    private int price_performance_ratio;

    public User() {
    }

    public User(int id, String username, String password, String email, int age,
                int traffic_convenience, int service_quality, int visitor_flow_rate,
                int cultural_atmosphere, int natural_landscape, int price_performance_ratio) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.age = age;
        this.traffic_convenience = traffic_convenience;
        this.service_quality = service_quality;
        this.visitor_flow_rate = visitor_flow_rate;
        this.cultural_atmosphere = cultural_atmosphere;
        this.natural_landscape = natural_landscape;
        this.price_performance_ratio = price_performance_ratio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getTraffic_convenience() {
        return traffic_convenience;
    }

    public void setTraffic_convenience(int traffic_convenience) {
        this.traffic_convenience = traffic_convenience;
    }

    public int getService_quality() {
        return service_quality;
    }

    public void setService_quality(int service_quality) {
        this.service_quality = service_quality;
    }

    public int getVisitor_flow_rate() {
        return visitor_flow_rate;
    }

    public void setVisitor_flow_rate(int visitor_flow_rate) {
        this.visitor_flow_rate = visitor_flow_rate;
    }

    public int getCultural_atmosphere() {
        return cultural_atmosphere;
    }

    public void setCultural_atmosphere(int cultural_atmosphere) {
        this.cultural_atmosphere = cultural_atmosphere;
    }

    public int getNatural_landscape() {
        return natural_landscape;
    }

    public void setNatural_landscape(int natural_landscape) {
        this.natural_landscape = natural_landscape;
    }

    public int getPrice_performance_ratio() {
        return price_performance_ratio;
    }

    public void setPrice_performance_ratio(int price_performance_ratio) {
        this.price_performance_ratio = price_performance_ratio;
    }
}
// Getters and setters

