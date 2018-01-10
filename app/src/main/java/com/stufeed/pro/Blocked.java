package com.stufeed.pro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.stufeed.R;
import com.stufeed.holder.UserConnectHolder;
import com.stufeed.pojo.User;

import java.util.Map;

import static com.stufeed.utility.LocVari.*;
import static com.stufeed.utility.SowmitrasMethod.*;
import static com.stufeed.utility.SowmitrasStrings.*;

/**
 * Created by sowmitras on 29/12/17.
 */

public class Blocked extends AppCompatActivity {

    private AdView followfollowingAdView;
    private Toolbar toolbar;


    private TextView empty;
    private ProgressBar progressBar;
    private RecyclerView followingView;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private FirebaseRecyclerAdapter<User, UserConnectHolder> followingAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_recycler_layout);
        progressBar = setProgressBar(this, R.id.progressBarAds);
        followfollowingAdView = showAds(this, R.id.mainAd,progressBar);

        sharedPreferences = setSharedPreference(this,CST_SHARED_REFERENCE);
        editor = sharedPreferences.edit();
        empty = (TextView) findViewById(R.id.empty);

        followingView = setRecyclerViewActivity(this, 1,R.id.common_recycler_view);
        toolbar = (Toolbar) findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        setTitle(R.string.blocked);

        DATABASE_REFERENCE = setFireBaseKey(CST_USER_LISTS);
        followingAdapter = new FirebaseRecyclerAdapter<User, UserConnectHolder>(
                User.class,
                R.layout.list_view_item,
                UserConnectHolder.class,
                DATABASE_REFERENCE.child(getCurrentUser(this)).child(CST_BLOCKED))
        {
            @Override
            protected void populateViewHolder(final UserConnectHolder viewHolder, final User model, int position) {
                showUserDetails(Blocked.this,model.getUser_id(),viewHolder.accountkey,viewHolder.userImage,viewHolder.progressBar,viewHolder.userFullName);
                viewHolder.followButton.setText(R.string.un_block);
                viewHolder.followButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showToast(Blocked.this, model.getUser_id());
                        DatabaseReference ref = setFireBaseKey(CST_USER_LISTS);
                        ref.child(getCurrentUser(Blocked.this)).child(CST_BLOCKED).child(model.getUser_id()).removeValue();
                    }
                });
            }
        };

        followingAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                empty.setVisibility(View.GONE);
                super.onItemRangeInserted(positionStart, itemCount);
            }
            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                empty.setText("Student Not Found");
                empty.setVisibility(View.VISIBLE);
                super.onItemRangeRemoved(positionStart,itemCount);
            }

        });
        followingView.setAdapter(followingAdapter);
    }
}
