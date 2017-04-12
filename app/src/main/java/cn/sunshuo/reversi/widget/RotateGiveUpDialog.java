package cn.sunshuo.reversi.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import cn.sunshuo.reversi.R;
import cn.sunshuo.reversi.activity.DoubleGameActivity;

/**
 * Created by admin on 2017/3/28.
 */

public class RotateGiveUpDialog extends Dialog {

    public RotateGiveUpDialog(final Context context){
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view= LayoutInflater.from(getContext()).inflate(R.layout.rotate_giveup_dialog,null);
        super.setContentView(view);

        Button yes=(Button)view.findViewById(R.id.yes2);
        Button no=(Button)view.findViewById(R.id.no2);
        yes.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(context,DoubleGameActivity.class);
                DoubleGameActivity.doubleGameActivity.finish();
                context.startActivity(intent);
            }
        });
        no.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dismiss();
            }
        });

    }
}
