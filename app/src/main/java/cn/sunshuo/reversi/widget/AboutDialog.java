package cn.sunshuo.reversi.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import cn.sunshuo.reversi.R;


public class AboutDialog extends Dialog {
    /**
     * 显示一条消息的对话框
     */
    public AboutDialog(Context context) {

        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.about_dialog, null);
        Button ok = (Button) view.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        super.setContentView(view);
    }
    
    
}
