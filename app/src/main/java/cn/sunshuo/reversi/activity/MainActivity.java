package cn.sunshuo.reversi.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import cn.sunshuo.reversi.R;
import cn.sunshuo.reversi.game.Constant;

public class MainActivity extends Activity {

    private Button button_singlegame;
    private Button button_doublegame;
    private Button button_gamesettings;
    private Button button_gamerule;
    private TextView textView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //设置 “黑白棋” 字体
        textView=(TextView) findViewById(R.id.reversititle);
        AssetManager mgr=getAssets();
        Typeface tf=Typeface.createFromAsset(mgr,"fonts/华文行楷.ttf");
        Typeface tf2=Typeface.createFromAsset(mgr,"fonts/华文新魏.ttf");
        textView.setTypeface(tf);
        button_singlegame=(Button)findViewById(R.id.button_singlegame);
        button_singlegame.setTypeface(tf2);
        button_singlegame.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                byte playColor=(byte)preferences.getInt("playerColor", Constant.BLACK);
                int difficulty=preferences.getInt("difficulty",1);
                Intent intent =new Intent(MainActivity.this,SingleGameActivity.class);
                Bundle bundle=new Bundle();
                bundle.putByte("playerColor",playColor);
                bundle.putInt("difficulty",difficulty);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

            }
        });

        button_doublegame=(Button) findViewById(R.id.button_doublegame);
        button_doublegame.setTypeface(tf2);
        button_doublegame.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this,DoubleGameActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        button_gamesettings=(Button) findViewById(R.id.button_gamesettings);
        button_gamesettings.setTypeface(tf2);

        button_gamerule=(Button) findViewById(R.id.button_gamerule);
        button_gamerule.setTypeface(tf2);
    }
}
