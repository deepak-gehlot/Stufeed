package com.stufeed.holder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stufeed.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sowmitras on 2/12/17.
 */

public class CommentViewHolder extends RecyclerView.ViewHolder {


    public CardView comment_cardView;
    public RelativeLayout comment_RL;
    public LinearLayout comment_select;
    public CircleImageView comment_user_image;
    public TextView comment_by;
    public TextView comment;
    public TextView comment_time;
    public TextView comment_reply;

    public RelativeLayout comment_item;
    public RelativeLayout.LayoutParams params;


    public CommentViewHolder(View itemView) {
        super(itemView);
        comment_item = (RelativeLayout) itemView.findViewById(R.id.comment_RL);
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        comment_user_image = (CircleImageView)itemView.findViewById(R.id.comment_user_image);
        comment = (TextView)itemView.findViewById(R.id.comment);
        comment_by = (TextView)itemView.findViewById(R.id.comment_by);
        comment_time = (TextView)itemView.findViewById(R.id.comment_time);
        comment_reply = (TextView)itemView.findViewById(R.id.comment_reply);
        comment_RL = (RelativeLayout)itemView.findViewById(R.id.comment_RL);
        comment_select = (LinearLayout)itemView.findViewById(R.id.comment_select);
        comment_cardView = (CardView)itemView.findViewById(R.id.comment_cardView);
    }

    public void Layout_hide() {
        params.height = 0;
        //itemView.setLayoutParams(params); //This One.
        comment_item.setLayoutParams(params);   //Or This one.
    }

    public void Layout_show() {
        params.height = RecyclerView.LayoutParams.WRAP_CONTENT;
        params.width = RecyclerView.LayoutParams.MATCH_PARENT;
        //itemView.setLayoutParams(params); //This One.
        params.setMargins(10,5,10,5);//Todo (Left,Top, Right,Bottom)
        comment_item.setLayoutParams(params);   //Or This one.
    }

}
