package cn.sunshuo.reversi.game;

import android.graphics.Canvas;
import android.provider.Settings;
import android.view.SurfaceHolder;

/**
 * Created by admin on 2017/3/22.
 * UI棋盘页面的画图，update棋牌状态的线程
 */

public class RenderThread extends Thread{
    private SurfaceHolder surfaceHolder;
    private ReversiView reversiView;
    private boolean running;

    public RenderThread(SurfaceHolder surfaceHolder,ReversiView reversiView){
        this.surfaceHolder=surfaceHolder;
        this.reversiView=reversiView;
    }

    @Override
    public void run(){
        Canvas canvas;
        while(running){
            canvas=null;
            long startTime=System.currentTimeMillis();
            this.reversiView.update();
            long endTime= System.currentTimeMillis();

            try{
                canvas=this.surfaceHolder.lockCanvas();
                //当synchronized用来修饰一个方法或者一个代码块时，能够保证在同一时刻最多只有一个线程执行该段代码
                synchronized (surfaceHolder){
                    this.reversiView.render(canvas);
                }
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                if(canvas!=null){
                    this.surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
            try{
                if((endTime-startTime)<=100){
                    sleep(100-(endTime-startTime));
                }
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
//设置线程状态的函数
    public void setRunning(boolean running){this.running=running;}
}
