package cn.sunshuo.reversi.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import cn.sunshuo.reversi.R;

import static cn.sunshuo.reversi.R.id.yes;

/**
 * Created by admin on 2017/3/24.
 */

public class UndoDialog extends Dialog {
    private Button yes;
    private Button no;

    public UndoDialog(Context context){
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view= LayoutInflater.from(getContext()).inflate(R.layout.undo_dialog,null);

        yes=(Button)view.findViewById(R.id.yes);
        no=(Button)view.findViewById(R.id.no);
        no.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dismiss();
            }
        });

        super.setContentView(view);

    }
    public void setOnUndoListener(View.OnClickListener onClickListener){
        yes.setOnClickListener(onClickListener);
    }
}
