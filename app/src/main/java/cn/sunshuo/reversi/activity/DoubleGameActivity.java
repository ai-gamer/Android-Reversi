package cn.sunshuo.reversi.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import java.io.IOException;
import java.util.List;

import cn.sunshuo.reversi.R;
import cn.sunshuo.reversi.bean.Move;
import cn.sunshuo.reversi.bean.Statistic;
import cn.sunshuo.reversi.game.Constant;
import cn.sunshuo.reversi.game.ReversiView;
import cn.sunshuo.reversi.game.Rule;
import cn.sunshuo.reversi.util.Setting;
import cn.sunshuo.reversi.widget.GiveUpDialog;
import cn.sunshuo.reversi.widget.RotateGiveUpDialog;
import cn.sunshuo.reversi.widget.StartNewGameDialog;

/**
 * Created by admin on 2017/3/25.
 */

public class DoubleGameActivity extends Activity {

    public static DoubleGameActivity doubleGameActivity=null;
    private static final byte NULL= Constant.NULL;
    private static final byte BLACK=Constant.BLACK;
    private static final byte WHITE=Constant.WHITE;

    //游戏状态的变量
    private static final int STATE_PLAYER1_MOVE=0;
    private static final int STATE_PLAYER2_MOVE=1;
    private static final int STATE_GAME_OVER=2;

    private ReversiView reversiView=null;
    private StartNewGameDialog msgDialog;
    private GiveUpDialog giveUpDialog_black;
    private RotateGiveUpDialog giveUpDialog_white;
    private Button double_giveup_white;
    private Button double_giveup_black;

    //双方棋手的颜色
    private byte player1Color;
    private byte player2Color;

    private MediaPlayer mediaPlayer;
    
    private static final int M=8;

    private byte[][]chessBoard=new byte[M][M];
    private int gameState=0;


    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_double_game);

        mediaPlayer = new MediaPlayer();
    	try {
    		AssetFileDescriptor assetFileDescriptor=getAssets().openFd("music/down.mp3");
    		if(assetFileDescriptor!=null){
    			mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
    				assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength() );
    			mediaPlayer.setLooping(false);
    			mediaPlayer.prepare();
    		}
    		else {
    			mediaPlayer = null;
    		}
		} catch (IllegalArgumentException e) {
			mediaPlayer = null;
			e.printStackTrace();
		} catch (IllegalStateException e) {
			mediaPlayer = null;
			e.printStackTrace();
		} catch (IOException e) {
			mediaPlayer = null;
			e.printStackTrace();
		}
        
        reversiView=(ReversiView) findViewById(R.id.double_reversiView);
        double_giveup_black=(Button) findViewById(R.id.double_giveup_black);
        double_giveup_white=(Button)findViewById(R.id.double_giveup_white);
        double_giveup_black.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                giveUpDialog_black=new GiveUpDialog(DoubleGameActivity.this);
                giveUpDialog_black.show();
            }
        });
        double_giveup_white.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                giveUpDialog_white=new RotateGiveUpDialog(DoubleGameActivity.this);
                giveUpDialog_white.show();
            }

        });
        player1Color=BLACK;
        player2Color=(byte)-player1Color;

        initialChessboard();

        reversiView.setOnTouchListener(new View.OnTouchListener() {

            boolean down=false;
            int downRow;
            int downCol;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //去除了ai状态是 非玩家走是都等于false的代码

                float x=event.getX();
                float y=event.getY();
                if(!reversiView.inChessBoard(x,y)){
                    return false;
                }
                if(gameState==STATE_PLAYER1_MOVE) {
                    int row = reversiView.getRow(y);
                    int col = reversiView.getCol(x);
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            down = true;
                            downRow = row;
                            downCol = col;
                            break;
                        case MotionEvent.ACTION_UP:

                            if (down && downRow == row && downCol == col) {
                                down = false;
                                if (!Rule.isLegalMove(chessBoard, new Move(row, col), player1Color))
                                {
                                    return true;
                                }
                                //轮到的玩家走步
                                palySound();
                                Move move = new Move(row, col);
                                List<Move> moves = Rule.move(chessBoard, move, player1Color);
                                reversiView.move(chessBoard, moves, move, player1Color);
                                updateUI.handle(0, Rule.getLegalMoves(chessBoard, player1Color).size(), player1Color);
                                player2Turn();
                                //去掉了aiTurn()的函数

                            }
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            down = false;
                            break;
                    }
                    return true;
                }
                //没有讨论state==gameover的情况，可能会出现问题
                else {
                    if(gameState==STATE_PLAYER2_MOVE) {
                        int row = reversiView.getRow(y);
                        int col = reversiView.getCol(x);
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                down = true;
                                downRow = row;
                                downCol = col;
                                break;
                            case MotionEvent.ACTION_UP:

                                if (down && downRow == row && downCol == col) {
                                    down = false;
                                    if (!Rule.isLegalMove(chessBoard, new Move(row, col), player2Color))
                                    {
                                        return true;
                                    }

                                    //轮到的玩家2走步
                                    palySound();
                                    Move move = new Move(row, col);
                                    List<Move> moves = Rule.move(chessBoard, move, player2Color);
                                    reversiView.move(chessBoard, moves, move, player2Color);
                                    updateUI.handle(0, Rule.getLegalMoves(chessBoard, player2Color).size(), player2Color);
                                    player1Turn();
                                    //去掉了aiTurn()的函数

                                }
                                break;
                            case MotionEvent.ACTION_CANCEL:
                                down = false;
                                break;
                        }
                        return true;
                    }
                }
                return true;
            }
        });


        doubleGameActivity=this;
    }

    private void initialChessboard(){
        for(int i=0;i<M;i++){
            for(int j=0;j<M;j++){
                chessBoard[i][j]=NULL;
            }
        }
        chessBoard[3][3]=WHITE;
        chessBoard[3][4]=BLACK;
        chessBoard[4][3]=BLACK;
        chessBoard[4][4]=WHITE;
    }

    private UpdateUIHandler updateUI=new UpdateUIHandler();
    class UpdateUIHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            /**
             * 更新游戏状态
             */
            int legalMoves = msg.what;
            int thinkingColor = msg.arg1;
            int legalMovesOfPlayer1, legalMovesOfPlayer2;
            if(thinkingColor == player2Color){
                legalMovesOfPlayer2 = legalMoves;
                legalMovesOfPlayer1 = Rule.getLegalMoves(chessBoard, player1Color).size();

                Statistic statistic = Rule.analyse(chessBoard, player1Color);
                if (legalMovesOfPlayer2 > 0 && legalMovesOfPlayer1 > 0) {
                    player1Turn();
                } else if (legalMovesOfPlayer2 == 0 && legalMovesOfPlayer1 > 0) {
                    player1Turn();
                } else if (legalMovesOfPlayer2 == 0 && legalMovesOfPlayer1 == 0) {
                    gameState = STATE_GAME_OVER;
                    gameOver(statistic.PLAYER - statistic.AI);
                } else if (legalMovesOfPlayer2 > 0 && legalMovesOfPlayer1 == 0) {
                    player2Turn();
                    return;
                }
            }else{
                legalMovesOfPlayer1 = legalMoves;
                legalMovesOfPlayer2 = Rule.getLegalMoves(chessBoard, player2Color).size();
                Statistic statistic = Rule.analyse(chessBoard, player1Color);
                if (legalMovesOfPlayer1 > 0 && legalMovesOfPlayer2 > 0) {
                    player2Turn();
                }else if(legalMovesOfPlayer1 == 0 && legalMovesOfPlayer2 > 0){
                    player2Turn();
                }else if(legalMovesOfPlayer1 == 0 && legalMovesOfPlayer2 == 0){
                    gameState = STATE_GAME_OVER;
                    gameOver(statistic.PLAYER - statistic.AI);
                }else if (legalMovesOfPlayer1 > 0 && legalMovesOfPlayer2 == 0) {
                    player1Turn();
                }
            }

        }

        public void handle(long delayMillis, int legalMoves, int thinkingColor) {
            removeMessages(0);
            sendMessageDelayed(Message.obtain(updateUI, legalMoves, thinkingColor, 0), delayMillis);
        }
    };

    private void player1Turn(){
        gameState=STATE_PLAYER1_MOVE;
    }
    private void player2Turn(){
        gameState=STATE_PLAYER2_MOVE;
    }

    private void gameOver(int winOrLoseOrDraw){

        String msg = "";
        if(winOrLoseOrDraw > 0){
            msg = "黑棋胜" ;
        }else if(winOrLoseOrDraw == 0){
            msg = "平局";
        }else if(winOrLoseOrDraw < 0){
            msg = "白棋胜";
        }
        msgDialog = new StartNewGameDialog(DoubleGameActivity.this, msg);
        msgDialog.show();


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent intent = new Intent(DoubleGameActivity.this, MainActivity.class);
            setResult(RESULT_CANCELED, intent);
            DoubleGameActivity.this.finish();
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void palySound()
    {
    	if(Setting.getInstance().isMusicEnabled()){
    		mediaPlayer.seekTo(200);
    		mediaPlayer.start();
    	}
    }
    
    @Override
    protected void onDestroy() {
    	if(mediaPlayer!=null){
    		mediaPlayer.pause();
    		mediaPlayer.stop();
    		mediaPlayer.release();
    	}
    	super.onDestroy();
    }
}
