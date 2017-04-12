package cn.sunshuo.reversi.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import cn.sunshuo.reversi.R;
import cn.sunshuo.reversi.activity.DoubleGameActivity;

public class StartNewGameDialog extends Dialog {

    /**
     * 显示一条消息的对话框
     */
    public StartNewGameDialog(final Context context, String msg) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.message_dialog, null);
        TextView textView = (TextView)view.findViewById(R.id.msg);
        textView.setText(msg);
        Button ok = (Button)view.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Intent intent=new Intent(context,DoubleGameActivity.class);
                context.startActivity(intent);
                ((Activity)context).finish();
                //activity.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

            }
        });

        super.setContentView(view);
    }




}
