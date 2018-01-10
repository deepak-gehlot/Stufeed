package com.stufeed.holder;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.stufeed.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.stufeed.utility.SowmitrasStrings.LOGCAT;

/**
 * Created by sowmitras on 30/12/17.
 */

public class UserSearchHolder extends RecyclerView.Adapter<UserSearchHolder.MyViewHolder> {

private List<String> usersList;
private List<String> userId;
private List<String> userImage;
private Activity activity;

public class MyViewHolder extends RecyclerView.ViewHolder {

    private TextView userFullName;
    private TextView userid;
    private TextView accountkey;
    private Button followButton;
    private CircleImageView userImage;
    private ProgressBar progressBar;
    private LinearLayout profileView;

    public MyViewHolder(View view) {
        super(view);
        userFullName = (TextView)view.findViewById(R.id.userFullName);
        userid = (TextView)view.findViewById(R.id.userId);
        accountkey = (TextView)view.findViewById(R.id.accountkey);
        followButton = (Button)view.findViewById(R.id.followButton);
        userImage = (CircleImageView)view.findViewById(R.id.userConnectImg);
        progressBar = (ProgressBar)view.findViewById(R.id.userProgressBar);
        profileView = (LinearLayout) view.findViewById(R.id.profileView);
    }
}


    public UserSearchHolder(List<String> usersList, List<String> userId, List<String> userImage, Activity activity) {
        this.usersList = usersList;
        this.userId = userId;
        this.userImage = userImage;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final String username = usersList.get(position);
        final String userid = userId.get(position);
        final String userimage = userImage.get(position);
        holder.userFullName.setText(username);
        holder.userid.setText(userid);
        Picasso.with(activity)
                .load(userimage)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.userImage, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(activity)
                                .load(userimage)
                                .error(R.drawable.ic_launcher_web)
                                .into(holder.userImage, new Callback() {
                                    @Override
                                    public void onSuccess(){
                                    }

                                    @Override
                                    public void onError() {
                                        Log.v("Picasso", "Could not fetch image");
                                    }
                                });
                    }
                });
        holder.profileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(LOGCAT,String.valueOf(username));

                /*Intent intent = new Intent(activity, UserView.class);
                intent.putExtra(CST_TYPE, BOARD_SCREEN);
                intent.putExtra(USER_ID, userid);
                activity.startActivity(intent);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public void setFilter(List<String> coun){
        usersList = new ArrayList<>();
        usersList.addAll(coun);
        notifyDataSetChanged();
    }
}