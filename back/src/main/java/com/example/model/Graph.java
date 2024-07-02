package com.example.model;

public class Graph {
    public double[][] Matrix1;//邻接矩阵存边的距离
    public double[][] Matrix2;//存通过每条边的时间
    public Graph(int vertexnum){
        Matrix1=new double[vertexnum][vertexnum];
        Matrix2=new double[vertexnum][vertexnum];
        for(int i=0;i<vertexnum;i++) {
            for (int j = 0; j < vertexnum; j++) {
                Matrix1[i][j] = Integer.MAX_VALUE;
                Matrix2[i][j] = Integer.MAX_VALUE;
            }
        }
    }//创建邻接矩阵,最初每条边设为不可达
}
