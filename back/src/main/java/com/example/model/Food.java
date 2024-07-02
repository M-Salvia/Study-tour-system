package com.example.model;

public class Food {
    int num;
    public String name;
    public int category;//0北京菜 1川菜 2徽菜 3地方菜 4西式餐点
    public int hot2;//热度
    public int rank2;//评分
    int locate;//属于哪个食堂,0综合食堂，1学生食堂，2教工餐厅，3麦当劳，4学院超市
    public Food(int num, String name, int category, int hot2, int rank2, int locate){
        this.name=name;
        this.num=num;
        this.category=category;
        this.hot2=hot2;
        this.rank2=rank2;
        this.locate=locate;
    }
    public String getlocate1(){
        switch(this.locate){
            case 0:
                return "综合食堂";
            case 1:
                return "学生食堂";
            case 2:
                return "教工餐厅";
            case 3:
                return "麦当劳";
            case 4:
                return "学院超市";
        }
        return null;
    }

    public String getName(){
        return this.name;
    }

    public String getcategory1(){
        switch(this.category){
            case 0:
                return "北京菜";
            case 1:
                return "川菜";
            case 2:
                return "徽菜";
            case 3:
                return "地方菜";
            case 4:
                return "西式餐点";
        }
        return null;
    }
}

