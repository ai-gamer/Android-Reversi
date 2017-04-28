package cn.sunshuo.reversi.util;

import java.util.Iterator;
import java.util.LinkedList;

import cn.sunshuo.reversi.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Setting {
	private int mBackgroundBitmapIndex = 0;
	private boolean mMusicEnabled = true;
	private Context mContext;
	private LinkedList<OnSettingChangedCallback> mCallbacks = new LinkedList<OnSettingChangedCallback>();
	
	private static Setting mSetting = null;
	private Setting(Context context) {
		mContext = context;
		SharedPreferences preferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
		mBackgroundBitmapIndex = preferences.getInt("mBackgroundBitmapIndex", 0);
		mMusicEnabled = preferences.getBoolean("mMusicEnabled", true);
	}
	
	public static Setting getInstance() {
		return mSetting;
	}
	
	public static void init(Context context) {
		if(mSetting==null)
			mSetting = new Setting(context);
	}
	
	public void save() {
		SharedPreferences preferences = mContext.getSharedPreferences("setting", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putInt("mBackgroundBitmapIndex", mBackgroundBitmapIndex);
		editor.putBoolean("mMusicEnabled", mMusicEnabled);
		editor.commit();
	}
	
	public int getBackgroundResId() {
		switch (mBackgroundBitmapIndex) {
		case 0:
			break;
		case 1:
			return R.drawable.congruent_pentagon;
		case 2:
			return R.drawable.ceramic;
		default:
			break;
		}
		return R.drawable.wood;
	}

	public int getBackgroundIndex() {
		return mBackgroundBitmapIndex;
	}
	
	public int getBackgroundNumber() {
		return 3;
	}
	
	public boolean isMusicEnabled() {
		return mMusicEnabled;
	}
	
	public void enableMusic() {
		if(mMusicEnabled==true)
			return;
		mMusicEnabled = true;
		Iterator<OnSettingChangedCallback> iterator = mCallbacks.iterator();
		while(iterator.hasNext()){
			OnSettingChangedCallback callback = iterator.next();
			callback.onSettingChange(this);
			callback.onMusicStateChange(this);
		}
		save();
	}
	
	public void disableMusic() {
		if(mMusicEnabled==false)
			return;
		mMusicEnabled = false;
		Iterator<OnSettingChangedCallback> iterator = mCallbacks.iterator();
		while(iterator.hasNext()){
			OnSettingChangedCallback callback = iterator.next();
			callback.onSettingChange(this);
			callback.onMusicStateChange(this);
		}
		save();
	}
	
	public void selectBackground(int index){
		if(mBackgroundBitmapIndex==index)
			return;
		mBackgroundBitmapIndex = index;
		Iterator<OnSettingChangedCallback> iterator = mCallbacks.iterator();
		while(iterator.hasNext()){
			OnSettingChangedCallback callback = iterator.next();
			callback.onSettingChange(this);
			callback.onBackgroundStateChange(this);
		}
		save();
	}
	
	public void addSettingChangedCallback(OnSettingChangedCallback callback){
		mCallbacks.add(callback);
	}
	
	public void removeSettingChangeCallback(OnSettingChangedCallback callback){
		mCallbacks.remove(callback);
	}
	
	public interface OnSettingChangedCallback{
		public boolean onSettingChange(Setting setting);
		public boolean onMusicStateChange(Setting setting);
		public boolean onBackgroundStateChange(Setting setting);
	}
}
