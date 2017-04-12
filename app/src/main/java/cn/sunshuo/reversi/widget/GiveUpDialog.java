package cn.sunshuo.reversi.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import cn.sunshuo.reversi.R;
import cn.sunshuo.reversi.activity.DoubleGameActivity;
import cn.sunshuo.reversi.activity.MainActivity;

/**
 * Created by admin on 2017/3/27.
 */

public class GiveUpDialog extends Dialog {

    public GiveUpDialog(final Context context){
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view= LayoutInflater.from(getContext()).inflate(R.layout.giveup_dialog,null);

        Button yes=(Button)view.findViewById(R.id.yes);
        yes.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(context,DoubleGameActivity.class);
                DoubleGameActivity.doubleGameActivity.finish();
                context.startActivity(intent);

                //overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

            }
        });
        Button no=(Button)view.findViewById(R.id.no);
        no.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dismiss();
            }
        });


        super.setContentView(view);
    }
}