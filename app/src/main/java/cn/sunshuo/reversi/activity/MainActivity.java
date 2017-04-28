package cn.sunshuo.reversi.activity;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.provider.Contacts.SettingsColumns;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import cn.sunshuo.reversi.R;
import cn.sunshuo.reversi.game.Constant;
import cn.sunshuo.reversi.util.Setting;
import cn.sunshuo.reversi.widget.AboutDialog;
import cn.sunshuo.reversi.widget.SettingDialog;

public class MainActivity extends Activity implements Setting.OnSettingChangedCallback{

    private Button button_singlegame;
    private Button button_doublegame;
    private Button button_gamesettings;
    private Button button_gamerule;
    private TextView textView;
    private AboutDialog aboutDialog;
    private SettingDialog settingDialog;
    private MediaPlayer mediaPlayer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Setting.init(this);

    	mediaPlayer = new MediaPlayer();
    	try {
    		AssetFileDescriptor assetFileDescriptor=getAssets().openFd("music/bonus_stage.mp3");
    		if(assetFileDescriptor!=null){
    			mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
    				assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength() );
    			mediaPlayer.setLooping(true);
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
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //设置 “黑白棋” 字体
        textView=(TextView) findViewById(R.id.reversititle);
        AssetManager mgr=getAssets();
        //华文行楷  
        Typeface tf=Typeface.createFromAsset(mgr,"fonts/hwxk.ttf");
        //华文新魏 
        Typeface tf2=Typeface.createFromAsset(mgr,"fonts/hwxw.ttf");
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
        button_gamesettings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
            	settingDialog = new SettingDialog(MainActivity.this);
            	settingDialog.show();
            }
        });

        button_gamerule=(Button) findViewById(R.id.button_gamerule);
        button_gamerule.setTypeface(tf2);
        button_gamerule.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
            	aboutDialog = new AboutDialog(MainActivity.this);
            	aboutDialog.show();
            }
        });
        Setting.getInstance().addSettingChangedCallback(this);
    }
    

    @Override
    protected void onResume() {
    	if(mediaPlayer!=null) {
    		if(Setting.getInstance().isMusicEnabled())
    			mediaPlayer.start();
    	}
    	super.onResume();
    }
    
    @Override
    protected void onPause() {
    	if(mediaPlayer!=null) {
    		mediaPlayer.pause();
    	}
    	super.onPause();
    }
    
    @Override
    protected void onDestroy() {
    	Setting.getInstance().removeSettingChangeCallback(this);
    	if(mediaPlayer!=null){
        	mediaPlayer.pause();
        	mediaPlayer.stop();
        	mediaPlayer.release();
        	mediaPlayer = null;
    	}
    	super.onDestroy();
    }


	@Override
	public boolean onSettingChange(Setting setting) {
		return false;
	}


	@Override
	public boolean onMusicStateChange(Setting setting) {
    	if(mediaPlayer!=null) {
    		if(Setting.getInstance().isMusicEnabled())
    			mediaPlayer.start();
    		else
    			mediaPlayer.pause();
    	}
		return true;
	}


	@Override
	public boolean onBackgroundStateChange(Setting setting) {
		return false;
	}

}
