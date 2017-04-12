package cn.sunshuo.reversi.bean;

/**
 * Move类包含row和col属性，即下棋子的位置。定义了hashCode和equals函数
 */

public class Move {

    public int row;
    public int col;
    public Move(int row,int col){
        this.row=row;
        this.col=col;
    }

    @Override
    public int hashCode(){
        final int prime=31;
        int result=1;
        result=prime*result+col;
        result=prime*result+row;
        return result;
    }

    @Override
    public boolean equals(Object obj){
        if(this==obj)
            return true;
        if(obj==null)
            return false;
        if(this.getClass()!=obj.getClass())
            return false;
        Move other=(Move) obj;
        if(this.col!=other.col)
            return false;
        if(this.row!=other.row)
            return false;
        return true;

    }
}
