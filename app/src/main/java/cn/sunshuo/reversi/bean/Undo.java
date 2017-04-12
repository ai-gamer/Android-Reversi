package cn.sunshuo.reversi.bean;

import java.util.List;

/**
 * Created by admin on 2017/3/31.
 * 记录处理悔棋功能需要的数据
 */

public class Undo {
    private Move move;
    private List<Move> moves;
    private byte color;
    public Undo(Move move,List<Move>moves,byte color){
        this.move=move;
        this.moves=moves;
        this.color=color;

    }
}
