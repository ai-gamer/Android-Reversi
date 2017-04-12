package cn.sunshuo.reversi.bean;

/**
 * ai模式下用于棋盘黑白子状态的讨论
 */

public class Statistic {

    public int PLAYER;
    public int AI;

    public Statistic(int PLAYER,int AI){
        this.PLAYER=PLAYER;
        this.AI=AI;
    }
}
