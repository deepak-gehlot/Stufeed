package com.stufeed.frag;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.google.firebase.database.ValueEventListener;
import com.stufeed.R;
import com.stufeed.holder.FeedPostViewHolder;
import com.stufeed.pojo.FeedPost;
import com.stufeed.pro.ViewBoard;

import java.util.ArrayList;

import static com.stufeed.utility.LocVari.*;
import static com.stufeed.utility.SowmitrasMethod.*;
import static com.stufeed.utility.SowmitrasStrings.*;

public class ViewBoardPosts extends Fragment {

    private TextView empty;
    private ProgressBar progressBar;
    private RecyclerView postViewBoard;
    private AdView postViewBoardAdView;

    private RecyclerView.LayoutManager recyclerLayoutManager;
    private FirebaseRecyclerAdapter<FeedPost, FeedPostViewHolder> followingAdapter;

    private ProgressDialog progressDialog;

    private ArrayList<String> postKey;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        DATABASE_REFERENCE = setFireBaseKey(CST_BOARD);
        DATABASE_REFERENCE.child(((ViewBoard)getActivity()).boardKey).child("post_index").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()!=0){
                    postKey = new ArrayList<String>();
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        Log.i(LOGCAT, String.valueOf(snapshot.getKey()));
                        postKey.add(String.valueOf(snapshot.getKey()));
                    }
                    showPost(postKey,progressDialog,getActivity(),R.id.common_recycler_view,1,getCurrentUser(getActivity()),((ViewBoard)getActivity()).firebaseUser.getUid(),false, false);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        return view;
    }
}
