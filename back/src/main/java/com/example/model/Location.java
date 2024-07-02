package com.example.model;

public class Location {
    int id;
    String name;//名字
    String key1;
    int convenience;
    int qo;
    int flow;
    int ratio;
    int humanity;
    int nature;
    int hot;

    public static int value11;
    public static int value22;
    public static int value33;
    public static int value44;
    public static int value55;
    public static int value66;
    //分别代表用户对每一项的重视程度
    int rank;//代表每个对象根据用户重视程度得到的评级

    public Location() {
    }
    public Location(int a,int b,int c,int d,int e, int f, int g,int h,String a1,String key1) {
        this.id=a;
        this.convenience=b;
        this.qo=c;
        this.flow=d;
        this.ratio=e;
        this.humanity=f;
        this.nature=g;
        this.hot = h;
        this.name=a1;
        this.key1=key1;
    }//构造方法

    public void setRank(int value11,int value22,int value33,int value44,int value55,int value66) {
        this.rank = (this.convenience*value11+this.qo*value22+this.humanity*value66+this.ratio*value44+this.nature*value55+this.flow*value33)/100;
    }

    public String getName(){
        return this.name;
    }



    public int getConvenience(){
        return convenience;
    }
    public int getQo(){
        return  qo;
    }

    public int getFlow() {
        return flow;
    }

    public int getHot() {
        return hot;
    }

    public int getNature() {
        return nature;
    }

    public int getHumanity() {
        return humanity;
    }

    public int getId() {
        return id;
    }
    public int getRatio() {
        return ratio;
    }

    public String getKey1() {
        return key1;
    }

    public int getrank(){
        return rank;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setKey1(String key1) {
        this.key1 = key1;
    }

    public void setConvenience(int convenience) {
        this.convenience = convenience;
    }

    public void setQo(int qo) {
        this.qo = qo;
    }

    public void setFlow(int flow) {
        this.flow = flow;
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    public void setHumanity(int humanity) {
        this.humanity = humanity;
    }

    public void setNature(int nature) {
        this.nature = nature;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public void setName(String name) {
        this.name = name;
    }


}
