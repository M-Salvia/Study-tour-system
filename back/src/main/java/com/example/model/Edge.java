package com.example.model;

public class Edge {
    int num1; // 边序号
    public double length; // 边的长度
    public int speed; // 速度
    public int start; // 起始点
    public int end; // 终点
    public double congestion; // 拥挤度

    public Edge(int num, int speed, int start, int end, double length) {
        this.num1 = num;
        this.speed = speed;
        this.start = start;
        this.end = end;
        this.length = length;
    }

    public void setCongestion(double congestion) {
        this.congestion = congestion;
    }
}
