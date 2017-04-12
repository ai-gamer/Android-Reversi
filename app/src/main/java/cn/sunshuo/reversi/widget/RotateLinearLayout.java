package cn.sunshuo.reversi.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by admin on 2017/3/28.
 */

public class RotateLinearLayout extends LinearLayout {
    public RotateLinearLayout(Context context){
        super(context);
    }

    public RotateLinearLayout(Context context, AttributeSet attrs){
        super(context,attrs);
    }

    @Override
    protected void onDraw(Canvas canvas){
        //倾斜180度，上下左右居中
        canvas.rotate(180,getMeasuredWidth()/2,getMeasuredHeight()/2);
        super.onDraw(canvas);
    }
}
