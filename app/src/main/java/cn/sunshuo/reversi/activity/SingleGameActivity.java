package cn.sunshuo.reversi.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import cn.sunshuo.reversi.R;
import cn.sunshuo.reversi.bean.Move;
import cn.sunshuo.reversi.bean.Statistic;
import cn.sunshuo.reversi.game.Algorithm;
import cn.sunshuo.reversi.game.Constant;
import cn.sunshuo.reversi.game.ReversiView;
import cn.sunshuo.reversi.game.Rule;
import cn.sunshuo.reversi.widget.MessageDialog;
import cn.sunshuo.reversi.widget.StartNewGameDialog;
import cn.sunshuo.reversi.widget.SetNewGameDialog;
import cn.sunshuo.reversi.widget.UndoDialog;

public class SingleGameActivity extends Activity {

    private static final byte NULL = Constant.NULL;
    private static final byte BLACK = Constant.BLACK;
    private static final byte WHITE = Constant.WHITE;
//定义棋盘的三个状态分别为：1玩家走 2AI走 3游戏结束
    private static final int STATE_PLAYER_MOVE = 0;
    private static final int STATE_AI_MOVE = 1;
    private static final int STATE_GAME_OVER = 2;

    private ReversiView reversiView = null;
    private LinearLayout playerLayout;
    private LinearLayout aiLayout;
    private TextView playerChesses;
    private TextView aiChesses;
    private ImageView playerImage;
    private ImageView aiImage;
    private TextView nameOfAI;
    private Button newGame;
    private Button tip;
    private Button single_undo;

    private byte playerColor;
    private byte aiColor;
    private int difficulty;



    private static final int M = 8;
    private static final int depth[] = new int[] { 0, 1, 2, 3, 7, 3, 5, 2, 4 };

    private byte[][] chessBoard = new byte[M][M];
    private int gameState;

    private static final String MULTIPLY = " × ";
    private static final String NAME_OF_AI[] = new String[]{"菜鸟", "新手", "入门", "棋手", "棋士", "大师", "宗师", "棋圣"};

    private SetNewGameDialog dialog;
    private MessageDialog msgDialog;
    private UndoDialog undoDialog;
    private MessageDialog notundoDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_single_game);
        reversiView = (ReversiView) findViewById(R.id.reversiView);
        playerLayout = (LinearLayout) findViewById(R.id.player);
        aiLayout = (LinearLayout) findViewById(R.id.ai);
        playerChesses = (TextView) findViewById(R.id.player_chesses);
        aiChesses = (TextView) findViewById(R.id.aiChesses);
        playerImage = (ImageView) findViewById(R.id.player_image);
        aiImage = (ImageView) findViewById(R.id.aiImage);
        nameOfAI = (TextView) findViewById(R.id.name_of_ai);
        newGame = (Button) findViewById(R.id.new_game);
        single_undo=(Button)findViewById(R.id.single_undo);
        tip = (Button) findViewById(R.id.tip);

        Bundle bundle = getIntent().getExtras();
        playerColor = bundle.getByte("playerColor");
        aiColor = (byte) -playerColor;
        difficulty = bundle.getInt("difficulty");

        nameOfAI.setText(NAME_OF_AI[difficulty - 1]);

        initialChessboard();


        reversiView.setOnTouchListener(new OnTouchListener() {

            boolean down = false;
            int downRow;
            int downCol;



            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (gameState != STATE_PLAYER_MOVE) {
                    return false;
                }
                float x = event.getX();
                float y = event.getY();
                if (!reversiView.inChessBoard(x, y)) {
                    return false;
                }
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
                            if (!Rule.isLegalMove(chessBoard, new Move(row, col), playerColor)) {
                                return true;
                            }

                            /**
                             * 玩家走步
                             */
                            Move move = new Move(row, col);
                            List<Move> moves = Rule.move(chessBoard, move, playerColor);
                            reversiView.move(chessBoard, moves, move, playerColor);
                            aiTurn();

                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        down = false;
                        break;
                }
                return true;
            }
        });

        single_undo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                undoDialog=new UndoDialog(SingleGameActivity.this);
                undoDialog.setOnUndoListener(new OnClickListener() {
                    @Override
                    //问题是所有的chessBoardState中的元素都被更新为最新的棋盘状态
                    public void onClick(View v) {
                    //    byte[][]chessBoard=reversiView.chessBoardState.get(reversiView.chessBoardState.size()-2);
                    if(reversiView.chessBoardState.size()<3){
                        notundoDialog=new MessageDialog(SingleGameActivity.this,"无法悔棋");
                        notundoDialog.show();
                        undoDialog.dismiss();
                    } else {
                        byte[][] chessBoard1 = reversiView.chessBoardState.get(reversiView.chessBoardState.size() - 3);
                        byte[][] chessBoard2 = reversiView.chessBoardState.get(reversiView.chessBoardState.size() - 2);
                        byte[][] chessBoard3 = reversiView.chessBoardState.get(reversiView.chessBoardState.size() - 1);
                        for (int i = 0; i < M; i++) {
                            for (int j = 0; j < M; j++) {
                                if (chessBoard2[i][j] != chessBoard3[i][j]) {
                                    if (chessBoard2[i][j] == NULL) {
                                        reversiView.chessBoard[i][j] = NULL;
                                        chessBoard[i][j] = NULL;
                                        reversiView.index[i][j] = 0;
                                    } else {
                                        reversiView.chessBoard[i][j] = chessBoard2[i][j];
                                        chessBoard[i][j] = chessBoard2[i][j];
                                        reversiView.index[i][j] = (reversiView.index[i][j] + 11) % 22;


                                    }
                                }
                                if (chessBoard1[i][j] != chessBoard2[i][j]) {
                                    if (chessBoard1[i][j] == NULL) {
                                        reversiView.chessBoard[i][j] = NULL;
                                        chessBoard[i][j] = NULL;
                                        reversiView.index[i][j] = 0;
                                    } else {
                                        reversiView.chessBoard[i][j] = chessBoard1[i][j];
                                        chessBoard[i][j] = chessBoard1[i][j];
                                        // reversiView.index[i][j]+=1;
                                     /*if(chessBoard1[i][j]==WHITE)
                                      reversiView.index[i][j]=11;
                                      else{
                                         reversiView.index[i][j]=0;
                                     }*/
                                        reversiView.index[i][j] = (reversiView.index[i][j] + 11) % 22;

                                    }

                                }
                                //  if(chessBoard1[i][j]==chessBoard3[i][j]&&chessBoard1[i][j]!=NULL)
                                //    reversiView.index[i][j]=(reversiView.index[i][j]+11)%22;

                            }
                        }
                        reversiView.chessBoardState.remove(reversiView.chessBoardState.size() - 1);
                        reversiView.chessBoardState.remove(reversiView.chessBoardState.size() - 1);
                        undoDialog.dismiss();
                        Statistic statistic = Rule.analyse(chessBoard, playerColor);
                        playerChesses.setText(MULTIPLY + statistic.PLAYER);
                        aiChesses.setText(MULTIPLY + statistic.AI);
                        // reversiView.thread.setRunning(true);
                    }
                    }
                });
                undoDialog.show();
            }
        });

        newGame.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                byte _playerColor = (byte)preferences.getInt("playerColor", Constant.BLACK);
                int _difficulty = preferences.getInt("difficulty", 1);

                dialog = new SetNewGameDialog(SingleGameActivity.this, _playerColor, _difficulty);

                dialog.setOnStartNewGameListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playerColor = dialog.getPlayerColor();
                        aiColor = (byte) -playerColor;
                        difficulty = dialog.getDifficulty();

                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        preferences.edit().putInt("playerColor", playerColor).commit();
                        preferences.edit().putInt("difficulty", difficulty).commit();

                        nameOfAI.setText(NAME_OF_AI[difficulty - 1]);

                        initialChessboard();
                        if(playerColor == BLACK){
                            playerImage.setImageResource(R.drawable.black1);
                            aiImage.setImageResource(R.drawable.white1);
                            playerTurn();
                        }else{
                            playerImage.setImageResource(R.drawable.white1);
                            aiImage.setImageResource(R.drawable.black1);
                            aiTurn();
                        }
                        reversiView.initialChessBoard();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        tip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gameState != STATE_PLAYER_MOVE){
                    return;
                }
                new ThinkingThread(playerColor).start();
            }
        });

        if(playerColor == BLACK){
            playerImage.setImageResource(R.drawable.black1);
            aiImage.setImageResource(R.drawable.white1);
            playerTurn();
        }else{
            playerImage.setImageResource(R.drawable.white1);
            aiImage.setImageResource(R.drawable.black1);
            aiTurn();
        }


    }

    private void initialChessboard(){
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {
                chessBoard[i][j] = NULL;
            }
        }
        chessBoard[3][3] = WHITE;
        chessBoard[3][4] = BLACK;
        chessBoard[4][3] = BLACK;
        chessBoard[4][4] = WHITE;
    }


    class ThinkingThread extends Thread {

        private byte thinkingColor;

        public ThinkingThread(byte thinkingColor) {
            this.thinkingColor = thinkingColor;
        }

        public void run() {
            try {
                sleep(20 * 100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int legalMoves = Rule.getLegalMoves(chessBoard, thinkingColor).size();
            if (legalMoves > 0) {
                Move move = Algorithm.getGoodMove(chessBoard, depth[difficulty], thinkingColor, difficulty);
                List<Move> moves = Rule.move(chessBoard, move, thinkingColor);
                reversiView.move(chessBoard, moves, move, thinkingColor);
            }
            updateUI.handle(0, legalMoves, thinkingColor);
        }
    }

    private UpdateUIHandler updateUI = new UpdateUIHandler();

    class UpdateUIHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            /**
             * 更新游戏状态
             */
            int legalMoves = msg.what;
            int thinkingColor = msg.arg1;
            int legalMovesOfAI, legalMovesOfPlayer;
            if(thinkingColor == aiColor){
                legalMovesOfAI = legalMoves;
                legalMovesOfPlayer = Rule.getLegalMoves(chessBoard, playerColor).size();

                Statistic statistic = Rule.analyse(chessBoard, playerColor);
                if (legalMovesOfAI > 0 && legalMovesOfPlayer > 0) {
                    playerTurn();
                } else if (legalMovesOfAI == 0 && legalMovesOfPlayer > 0) {
                    playerTurn();
                } else if (legalMovesOfAI == 0 && legalMovesOfPlayer == 0) {
                    gameState = STATE_GAME_OVER;
                    gameOver(statistic.PLAYER - statistic.AI);
                } else if (legalMovesOfAI > 0 && legalMovesOfPlayer == 0) {
                    aiTurn();
                    return;
                }
            }else{
                legalMovesOfPlayer = legalMoves;
                legalMovesOfAI = Rule.getLegalMoves(chessBoard, aiColor).size();
                Statistic statistic = Rule.analyse(chessBoard, playerColor);
                if (legalMovesOfPlayer > 0 && legalMovesOfAI > 0) {
                    aiTurn();
                }else if(legalMovesOfPlayer == 0 && legalMovesOfAI > 0){
                    aiTurn();
                }else if(legalMovesOfPlayer == 0 && legalMovesOfAI == 0){
                    gameState = STATE_GAME_OVER;
                    gameOver(statistic.PLAYER - statistic.AI);
                }else if (legalMovesOfPlayer > 0 && legalMovesOfAI == 0) {
                    playerTurn();
                }
            }

        }

        public void handle(long delayMillis, int legalMoves, int thinkingColor) {
            removeMessages(0);
            sendMessageDelayed(Message.obtain(updateUI, legalMoves, thinkingColor, 0), delayMillis);
        }
    };

    private void playerTurn(){
        Statistic statistic = Rule.analyse(chessBoard, playerColor);
        playerChesses.setText(MULTIPLY + statistic.PLAYER);
        aiChesses.setText(MULTIPLY + statistic.AI);
        playerLayout.setBackgroundResource(R.drawable.rect);
        aiLayout.setBackgroundResource(R.drawable.rect_normal);
        gameState = STATE_PLAYER_MOVE;
    }

    private void aiTurn(){
        Statistic statistic = Rule.analyse(chessBoard, playerColor);
        playerChesses.setText(MULTIPLY + statistic.PLAYER);
        aiChesses.setText(MULTIPLY + statistic.AI);
        playerLayout.setBackgroundResource(R.drawable.rect_normal);
        aiLayout.setBackgroundResource(R.drawable.rect);
        gameState = STATE_AI_MOVE;
        new ThinkingThread(aiColor).start();
    }

    private void gameOver(int winOrLoseOrDraw){

        String msg = "";
        if(winOrLoseOrDraw > 0){
            msg = "你击败了" + NAME_OF_AI[difficulty - 1];
        }else if(winOrLoseOrDraw == 0){
            msg = "平局";
        }else if(winOrLoseOrDraw < 0){
            msg = "你被" + NAME_OF_AI[difficulty - 1] + "击败了";
        }
        msgDialog = new MessageDialog(SingleGameActivity.this, msg);
        msgDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent intent = new Intent(SingleGameActivity.this, MainActivity.class);
            setResult(RESULT_CANCELED, intent);
            SingleGameActivity.this.finish();
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
