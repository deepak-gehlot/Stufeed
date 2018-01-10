package com.stufeed.holder;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stufeed.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sowmitras on 30/10/17.
 */

public class UserConnectHolder extends RecyclerView.ViewHolder {

    public TextView userFullName;
    public TextView userid;
    public TextView accountkey;
    public Button followButton;
    public CircleImageView userImage;
    public ProgressBar progressBar;
    public LinearLayout profileView;

    public RelativeLayout user_main;

    public UserConnectHolder(View itemView) {
        super(itemView);
        userFullName = (TextView)itemView.findViewById(R.id.userFullName);
        userid = (TextView)itemView.findViewById(R.id.userId);
        accountkey = (TextView)itemView.findViewById(R.id.accountkey);
        followButton = (Button)itemView.findViewById(R.id.followButton);
        userImage = (CircleImageView)itemView.findViewById(R.id.userConnectImg);
        progressBar = (ProgressBar)itemView.findViewById(R.id.userProgressBar);
        profileView = (LinearLayout) itemView.findViewById(R.id.profileView);

        user_main = (RelativeLayout) itemView.findViewById(R.id.user_main);


    }

}
