package com.stufeed.holder;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stufeed.R;

/**
 * Created by sowmitras on 6/11/17.
 */

public class JoinBoardViewHolder extends RecyclerView.ViewHolder {
    public TextView username;
    public TextView boardName;
    public TextView requestedTime;
    public ImageView userImg;
    public Button accepted;
    public Button rejected;
    public RelativeLayout boardR;


    @SuppressLint("WrongViewCast")
    public JoinBoardViewHolder(View v) {
        super(v);
        username = (TextView) itemView.findViewById(R.id.userName);
        boardName = (TextView) itemView.findViewById(R.id.boardname);
        requestedTime = (TextView) itemView.findViewById(R.id.requestedTime);
        userImg = (ImageView) itemView.findViewById(R.id.lockImg);
        boardR = (RelativeLayout) itemView.findViewById(R.id.boardR);
        accepted = (Button) itemView.findViewById(R.id.btn2);
        rejected = (Button) itemView.findViewById(R.id.btn1);
    }
}