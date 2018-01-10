package com.stufeed.pro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.stufeed.R;
import com.stufeed.holder.FeedPostViewHolder;
import com.stufeed.pojo.FeedPost;

import static com.stufeed.utility.SowmitrasMethod.*;

/**
 * Created by sowmitras on 25/11/17.
 */

public class GetFeed extends AppCompatActivity {
    private Toolbar toolbar;
    private FirebaseRecyclerAdapter<FeedPost, FeedPostViewHolder> loadPost;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_recycler_layout);
        enableDisableAnalytics(this);
        setTitle(getResources().getString(R.string.posts));
        toolbar = (Toolbar) findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        loadPost = loadPost(this, getCurrentUser(this));
        final RecyclerView postRecyclerView = (RecyclerView) findViewById(R.id.common_recycler_view);
        LinearLayoutManager recyclerViewLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        postRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerViewLayoutManager.setReverseLayout(true);
        recyclerViewLayoutManager.setStackFromEnd(true);

        final FirebaseRecyclerAdapter<FeedPost, FeedPostViewHolder> finalFeedAdapter = loadPost;
        final LinearLayoutManager finalRecyclerViewLayoutManager = recyclerViewLayoutManager;
        loadPost.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int historyCount = finalFeedAdapter.getItemCount();
                int lastHistory = finalRecyclerViewLayoutManager.findLastCompletelyVisibleItemPosition();
                if ((lastHistory == -1 || (positionStart >= (historyCount - 1) && lastHistory == (positionStart - 1)))) {
                    postRecyclerView.scrollToPosition(positionStart);
                }
            }
        });
        postRecyclerView.setLayoutManager(finalRecyclerViewLayoutManager);
        postRecyclerView.setAdapter(loadPost);
        postRecyclerView.setNestedScrollingEnabled(false);

    }
}
