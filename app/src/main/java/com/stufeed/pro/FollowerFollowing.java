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
import com.google.firebase.database.ValueEventListener;
import com.stufeed.R;
import com.stufeed.holder.UserConnectHolder;
import com.stufeed.pojo.User;

import java.util.Map;

import static com.stufeed.utility.LocVari.*;
import static com.stufeed.utility.SowmitrasMethod.*;
import static com.stufeed.utility.SowmitrasStrings.*;


/**
 * Created by sowmitras on 7/9/17.
 */

public class FollowerFollowing extends AppCompatActivity {

    private AdView followfollowingAdView;
    private TextView empty;
    private ProgressBar progressBar;
    private RecyclerView followingView;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private FirebaseRecyclerAdapter<User, UserConnectHolder> followingAdapter;
    private Toolbar toolbar;
    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableDisableAnalytics(this);
        setContentView(R.layout.common_recycler_layout);
        sharedPreferences = setSharedPreference(this,CST_SHARED_REFERENCE);
        editor = sharedPreferences.edit();
        empty = (TextView) findViewById(R.id.empty);

          /*TODO: AdView Home*/
        progressBar = setProgressBar(this, R.id.progressBarAds);
        followfollowingAdView = showAds(this, R.id.mainAd,progressBar);

        followingView = setRecyclerViewActivity(this, 1,R.id.common_recycler_view);
        bundle = getIntent().getExtras();
        toolbar = (Toolbar) findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (bundle == null) {
        } else {
            setTitle(getFirstLetterCaps(bundle.get(CST_TYPE).toString()));
            DATABASE_REFERENCE = setFireBaseKey(bundle.get(CST_TYPE).toString());
            followingAdapter = new FirebaseRecyclerAdapter<User, UserConnectHolder>(
                    User.class,
                    R.layout.list_view_item,
                    UserConnectHolder.class,
                    DATABASE_REFERENCE.child(bundle.get(CST_USER_NAME).toString())
            ) {
                @Override
                protected void populateViewHolder(final UserConnectHolder viewHolder, final User model, int position) {
                    showUserDetails(FollowerFollowing.this,model.getUser_id(),viewHolder.accountkey,viewHolder.userImage,viewHolder.progressBar,viewHolder.userFullName);
                    viewHolder.userid.setText(model.getUser_id());
                    DATABASE_REFERENCE = setFireBaseKey(CST_FOLLOWING);
                    viewHolder.followButton.setEnabled(false);
                    DATABASE_REFERENCE.child(getCurrentUser(FollowerFollowing.this)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getChildrenCount()!=0){
                                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                    if(model.getUser_id().equals(String.valueOf(snapshot.child(USER_ID).getValue().toString()))){
                                        viewHolder.followButton.setEnabled(true);
                                        viewHolder.followButton.setText("Followed");

                                    }else{
                                        viewHolder.followButton.setEnabled(true);
                                        viewHolder.followButton.setText("Follow");
                                    }
                                }
                            }else{
                                viewHolder.followButton.setEnabled(true);
                                viewHolder.followButton.setText("Follow");
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    if(model.getUser_id().equals(getFromLocalDataBase(sharedPreferences,CST_USER_NAME))){
                        viewHolder.followButton.setVisibility(View.INVISIBLE);
                    }

                    viewHolder.profileView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(FollowerFollowing.this, UserView.class);
                            intent.putExtra(CST_TYPE, BOARD_SCREEN);
                            intent.putExtra(USER_ID, model.getUser_id());
                            startActivity(intent);
                        }
                    });
                    viewHolder.followButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(viewHolder.followButton.getText().toString().equals("Followed")){
                                viewHolder.followButton.setText("Follow");
                                DATABASE_REFERENCE = setFireBaseKey(CST_FOLLOWER);
                                DATABASE_REFERENCE.child(model.getUser_id()).child(viewHolder.accountkey.getText().toString()+"_"+getCurrentUser(FollowerFollowing.this)).removeValue();

                                DATABASE_REFERENCE = setFireBaseKey(CST_FOLLOWING);
                                DATABASE_REFERENCE.child(getCurrentUser(FollowerFollowing.this)).child(viewHolder.accountkey.getText().toString()).removeValue();
                            }else{
                                viewHolder.followButton.setText("Followed");
                                DATABASE_REFERENCE = setFireBaseKey(CST_FOLLOWER);

                                Map<String, Object> taskMap = setMapValue(USER_ID,getFromLocalDataBase(sharedPreferences,CST_USER_NAME));

                                DATABASE_REFERENCE.child(model.getUser_id()).child(viewHolder.accountkey.getText().toString()+"_"+getCurrentUser(FollowerFollowing.this)).updateChildren(taskMap);
                                DATABASE_REFERENCE = setFireBaseKey(CST_FOLLOWING);
                                Map<String, Object> task = setMapValue(USER_ID,model.getUser_id());

                                DATABASE_REFERENCE.child(getFromLocalDataBase(sharedPreferences,CST_USER_NAME)).child(viewHolder.accountkey.getText().toString()).updateChildren(task);

                            }
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
}