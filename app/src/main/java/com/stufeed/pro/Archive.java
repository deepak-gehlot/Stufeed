package com.stufeed.pro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Query;
import com.stufeed.R;
import com.stufeed.holder.BoardViewHolder;
import com.stufeed.pojo.Board;

import static com.stufeed.utility.LocVari.*;
import static com.stufeed.utility.SowmitrasStrings.*;
import static com.stufeed.utility.SowmitrasMethod.*;

/**
 * Created by sowmitras on 27/10/17.
 */

public class Archive extends AppCompatActivity {

    private AdView followfollowingAdView;
    private ProgressBar progressBar;
    private Switch privacySwt;
    private FloatingActionButton addBoard;
    private EditText board_name, board_description;
    private Button cancel, done;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private Toolbar toolbar;
    private FirebaseRecyclerAdapter<Board, BoardViewHolder> mFirebaseAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableDisableAnalytics(this);
        setContentView(R.layout.common_recycler_layout);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        progressBar = setProgressBar(this, R.id.progressBarAds);
        followfollowingAdView = showAds(this, R.id.mainAd,progressBar);
        toolbar = (Toolbar) findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);
        setTitle(getResources().getString(R.string.archive_board));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        RecyclerView boardRecyclerView = (RecyclerView) findViewById(R.id.common_recycler_view);
        LinearLayoutManager recyclerViewLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        boardRecyclerView.setLayoutManager(recyclerViewLayoutManager);

        DATABASE_REFERENCE = setFireBaseKey(CST_BOARD);
        Query query = DATABASE_REFERENCE.orderByChild(CST_BOARD_CREATED_BY).equalTo(ARCHIVE_SCREEN+"_"+getCurrentUser(this));
        mFirebaseAdapter = loadBoard(query,this,getCurrentUser(this),mFirebaseUser.getUid(),BOARD_EDIT_ON,BOARD_PRIVACY_ON, true, false);
        boardRecyclerView.setAdapter(mFirebaseAdapter);

    }
}
