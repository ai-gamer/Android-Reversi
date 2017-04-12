package cn.sunshuo.reversi.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import cn.sunshuo.reversi.R;

/**
 * Created by admin on 2017/3/26.
 */

public class MessageDialog extends Dialog {
    /**
     * 显示一条消息的对话框
     */
    public MessageDialog(Context context, String msg) {

        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.message_dialog, null);
        TextView textView = (TextView) view.findViewById(R.id.msg);
        textView.setText(msg);
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
