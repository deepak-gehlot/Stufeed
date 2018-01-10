package com.stufeed.frag;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.stufeed.R;
import com.stufeed.holder.UserConnectHolder;
import com.stufeed.pojo.User;
import com.stufeed.pro.UserView;
import com.stufeed.pro.ViewBoard;

import java.util.Map;

import static com.stufeed.utility.LocVari.*;
import static com.stufeed.utility.SowmitrasMethod.*;
import static com.stufeed.utility.SowmitrasStrings.*;


public class ViewBoardMembers extends Fragment {

    private TextView empty;

    private ProgressBar progressBar;
    private RecyclerView followingView;
    private AdView postViewBoardAdView;

    private RecyclerView.LayoutManager recyclerLayoutManager;
    private FirebaseRecyclerAdapter<User, UserConnectHolder> followingAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_recycler_layout, container, false);
        progressBar = view.findViewById(R.id.progressBarAds);
        postViewBoardAdView = (AdView) view.findViewById(R.id.mainAd);
        AdRequest adRequest = new AdRequest.Builder().build();
        postViewBoardAdView.loadAd(adRequest);
        postViewBoardAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                progressBar.setVisibility(View.GONE);
                postViewBoardAdView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                progressBar.setVisibility(View.GONE);
                postViewBoardAdView.setVisibility(View.GONE);
            }

        });

        followingView= (RecyclerView) view.findViewById(R.id.common_recycler_view);
        recyclerLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 1);
        followingView.setLayoutManager(recyclerLayoutManager);

        //showToast(getActivity(), ((ViewBoard)getActivity()).boardKey);

       /* DATABASE_REFERENCE = setFireBaseKey(CST_JOIN_BOARD);
        DATABASE_REFERENCE.child(((ViewBoard)getActivity()).boardKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()!=0);
                {
                    Log.i(LOGCAT, String.valueOf(dataSnapshot.getValue()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        DatabaseReference databaseReference = setFireBaseKey(CST_JOIN_BOARD);
        Query query = databaseReference.child(((ViewBoard)getActivity()).boardKey);
        followingAdapter = new FirebaseRecyclerAdapter<User, UserConnectHolder>(
                User.class,
                R.layout.list_view_item,
                UserConnectHolder.class,
                query) {
            @Override
            protected void populateViewHolder(final UserConnectHolder viewHolder, final User model, int position) {


                Log.i(LOGCAT,"user"+ model.getUser_id() );


                 showUserDetails(getActivity(),model.getUserUI(),viewHolder.accountkey,viewHolder.userImage,viewHolder.progressBar,viewHolder.userFullName);

                DATABASE_REFERENCE = setFireBaseKey(CST_FOLLOWING);
                viewHolder.followButton.setEnabled(false);
                DATABASE_REFERENCE.child(getCurrentUser(getActivity())).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount()!=0){
                            for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                if(model.getUserUI().equals(String.valueOf(snapshot.child(USER_ID).getValue().toString()))){
                                    viewHolder.followButton.setEnabled(true);
                                    viewHolder.followButton.setText("Followed");

                                    showToast(getActivity(),"");

                                }else{
                                    viewHolder.followButton.setEnabled(true);
                                }
                            }
                        }else{
                            viewHolder.followButton.setEnabled(true);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                if(model.getUserUI().equals(getCurrentUser(getActivity()))){
                    viewHolder.followButton.setVisibility(View.INVISIBLE);
                    viewHolder.profileView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });
                }else {
                    viewHolder.profileView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), UserView.class);
                            intent.putExtra(CST_TYPE, BOARD_SCREEN);
                            intent.putExtra(USER_ID, model.getUserUI());
                            startActivity(intent);

                        }
                    });
                }
                viewHolder.followButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(viewHolder.followButton.getText().toString().equals("Followed")){
                            viewHolder.followButton.setText("Follow");
                            DATABASE_REFERENCE = setFireBaseKey(CST_FOLLOWER);
                            DATABASE_REFERENCE.child(model.getUserUI()).child(viewHolder.accountkey.getText().toString()+"_"+getCurrentUser(getActivity())).removeValue();

                            DATABASE_REFERENCE = setFireBaseKey(CST_FOLLOWING);
                            DATABASE_REFERENCE.child(getCurrentUser(getActivity())).child(viewHolder.accountkey.getText().toString()).removeValue();
                        }else{
                            viewHolder.followButton.setText("Followed");
                            DATABASE_REFERENCE = setFireBaseKey(CST_FOLLOWER);

                            Map<String, Object> taskMap = setMapValue(USER_ID,getCurrentUser(getActivity()));
                            DATABASE_REFERENCE.child(model.getUserUI()).child(viewHolder.accountkey.getText().toString()+"_"+getCurrentUser(getActivity())).updateChildren(taskMap);

                            DATABASE_REFERENCE = setFireBaseKey(CST_FOLLOWING);
                            Map<String, Object> task = setMapValue(USER_ID,model.getUserUI());
                            DATABASE_REFERENCE.child(getCurrentUser(getActivity())).child(viewHolder.accountkey.getText().toString()).updateChildren(task);

                        }
                    }
                });


            }
        };

        followingAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                // empty.setVisibility(View.GONE);
                super.onItemRangeInserted(positionStart, itemCount);
            }
            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                // empty.setText("Student Not Found");
                // empty.setVisibility(View.VISIBLE);
                super.onItemRangeRemoved(positionStart,itemCount);
            }

        });
        followingView.setAdapter(followingAdapter);
        return view;
    }

}
