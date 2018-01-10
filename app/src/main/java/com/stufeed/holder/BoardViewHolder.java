package com.stufeed.holder;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stufeed.R;

/**
 * Created by sowmitras on 5/11/17.
 */


public class BoardViewHolder extends RecyclerView.ViewHolder {
    public TextView nameOfBoard;
    public TextView postCount;
    public TextView followersText;
    public ImageView editTextView;
    public TextView getReferenceKey;
    public TextView createdBy;
    public TextView privacy;
    public TextView getBoardKey;
    public ImageView lockImg;
    public TextView commonBtn;
    public TextView memberCount;
    public RelativeLayout boardR;

    @SuppressLint("WrongViewCast")
    public BoardViewHolder(View v) {
        super(v);
        nameOfBoard = (TextView) itemView.findViewById(R.id.nameOfBoard);
        getBoardKey = (TextView) itemView.findViewById(R.id.getBoardKey);
        getReferenceKey = (TextView) itemView.findViewById(R.id.getReferenceKey);

        postCount = (TextView) itemView.findViewById(R.id.postCount);
        memberCount = (TextView) itemView.findViewById(R.id.memberCount);

        followersText = (TextView) itemView.findViewById(R.id.memberCount);
        editTextView = (ImageView) itemView.findViewById(R.id.edit_textview);
        createdBy = (TextView) itemView.findViewById(R.id.createdBy);
        privacy = (TextView) itemView.findViewById(R.id.privacy);
        commonBtn = (TextView) itemView.findViewById(R.id.commonBtn);
        lockImg = (ImageView) itemView.findViewById(R.id.lockImg);
        boardR = (RelativeLayout) itemView.findViewById(R.id.boardR);
    }
}
