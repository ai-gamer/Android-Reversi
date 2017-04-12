package cn.sunshuo.reversi.bean;

/**
 * 包含move信息和对应的mark
 */

public class MinimaxResult {

    public int mark;
    public Move move;

    public MinimaxResult(int mark,Move move){
        this.mark=mark;
        this.move=move;
    }
}
