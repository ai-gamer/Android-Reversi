package cn.sunshuo.reversi.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import cn.sunshuo.reversi.R;
import cn.sunshuo.reversi.util.Setting;

public class SettingDialog  extends Dialog {

	private RadioButton musicEnabled;
    private RadioButton musicDisabled;
    private final RadioButton[] radioButtons = new RadioButton[3];
    private Button ok;

    @SuppressLint("InflateParams") 
    public SettingDialog(Context context) {

        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        View view = LayoutInflater.from(getContext()).inflate(R.layout.setting_dialog, null);

        musicEnabled = (RadioButton)view.findViewById(R.id.music_enable);
        musicDisabled = (RadioButton)view.findViewById(R.id.music_disable);
        if(Setting.getInstance().isMusicEnabled()){
        	musicEnabled.setChecked(true);
        }else{
        	musicDisabled.setChecked(true);
        }

        radioButtons[0] = (RadioButton)view.findViewById(R.id.background1);
        radioButtons[1] = (RadioButton)view.findViewById(R.id.background2);
        radioButtons[2] = (RadioButton)view.findViewById(R.id.background3);
        for(int i = 0 ;i < radioButtons.length; i++){
            final int k = i;
            radioButtons[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        for(int index = 0; index < radioButtons.length; index++){
                            if(index != k){
                                radioButtons[index].setChecked(false);
                            }
                        }
                    }
                }
            });
        }
        int bkindex = Setting.getInstance().getBackgroundIndex();
        if(bkindex<0||bkindex>=radioButtons.length)
        	bkindex = 0;
        radioButtons[bkindex].setChecked(true);
        
        ok = (Button)view.findViewById(R.id.ok);
        
        ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isMusicEnable())
					Setting.getInstance().enableMusic();
				else
					Setting.getInstance().disableMusic();
				Setting.getInstance().selectBackground(getBackgroundIndex());
				dismiss();
			}
		});
    	
        super.setContentView(view);
    }
    
    private boolean isMusicEnable() {
    	return musicEnabled.isChecked();
    }

    private int getBackgroundIndex() {
        for(int index = 0; index < radioButtons.length; index++){
            if(radioButtons[index].isChecked())
            	return index;
        }
    	return 0;
    }
    
    
}
