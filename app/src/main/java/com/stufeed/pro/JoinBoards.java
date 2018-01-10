package com.stufeed.pro;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stufeed.R;
import com.stufeed.holder.BoardViewHolder;
import com.stufeed.pojo.Board;

import static com.stufeed.utility.SowmitrasMethod.*;
import static com.stufeed.utility.SowmitrasStrings.*;
import static com.stufeed.utility.LocVari.*;

/**
 * Created by sowmitras on 25/11/17.
 */

public class JoinBoards extends AppCompatActivity {
    private Toolbar toolbar;
    private FirebaseRecyclerAdapter<Board, BoardViewHolder> boardAdapter;

    private ProgressDialog progressDialog;

    private RecyclerView boardRecyclerView;
    private LinearLayoutManager boardrecyclerViewLayoutManager;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private Bundle bundle;
    private AppBarLayout appBarLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableDisableAnalytics(this);

        bundle = new Bundle();
        bundle = getIntent().getExtras();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        progressDialog = loadProgressDialog(this, "",CENTER_DISPLAY);
        setContentView(R.layout.common_recycler_layout);



        setTitle(getResources().getString(R.string.join_board));
        toolbar = (Toolbar) findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

       /* boardRecyclerView = (RecyclerView) findViewById(R.id.common_recycler_view);
        boardrecyclerViewLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        boardRecyclerView.setLayoutManager(boardrecyclerViewLayoutManager);
        boardrecyclerViewLayoutManager = new LinearLayoutManager(this);
        boardrecyclerViewLayoutManager.setReverseLayout(true);
        boardrecyclerViewLayoutManager.setStackFromEnd(true);*/


       if(bundle==null){
           showBoard(progressDialog,this,R.id.common_recycler_view,2,getCurrentUser(this),mFirebaseUser.getUid(),BOARD_PRIVACY_ON, BOARD_EDIT_OFF,true);
       }else{
           showBoard(progressDialog,this,R.id.common_recycler_view,2,bundle.get(USER_ID).toString(),mFirebaseUser.getUid(),BOARD_PRIVACY_ON, BOARD_EDIT_OFF,true);
       }


        /* DATABASE_REFERENCE = setFireBaseKey(CST_USER_LISTS);
        Query query = DATABASE_REFERENCE.child(getCurrentUser(JoinBoards.this)).child(CST_JOIN_BOARD).orderByChild(CST_JOINED);
        boardAdapter = new FirebaseRecyclerAdapter<Board, BoardViewHolder>(
                Board.class,
                R.layout.board_item_view,
                BoardViewHolder.class,
                query   ) {
            @Override
            protected void populateViewHolder(final BoardViewHolder viewHolder, final Board model, int position) {
                DATABASE_REFERENCE = setFireBaseKey(CST_BOARD);
                DATABASE_REFERENCE.child(model.getJoined()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount()!=0){
                            showToast(JoinBoards.this, dataSnapshot.toString());
//                            viewHolder.getReferenceKey.setText(getDataSnapShotValue(dataSnapshot, CST_POST_REFERENCE_KEY));
                            viewHolder.createdBy.setText(getDataSnapShotValue(dataSnapshot, CST_BOARD_CREATED_BY));
                            viewHolder.nameOfBoard.setText(getDataSnapShotValue(dataSnapshot, CST_BOARD_NAME));
                            viewHolder.getBoardKey.setText(getDataSnapShotValue(dataSnapshot, CST_BOARD_KEY));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };
        boardAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int historyCount = boardAdapter.getItemCount();
                int lastHistory = boardrecyclerViewLayoutManager.findLastCompletelyVisibleItemPosition();
                if ((lastHistory == -1 || (positionStart >= (historyCount - 1) && lastHistory == (positionStart - 1)))) {
                    boardRecyclerView.scrollToPosition(positionStart);
                }
            }
        });
        boardRecyclerView.setLayoutManager(boardrecyclerViewLayoutManager);
        boardRecyclerView.setAdapter(boardAdapter);
        boardRecyclerView.setNestedScrollingEnabled(false);*/
    }
}
