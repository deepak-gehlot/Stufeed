package com.stufeed.frag;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.stufeed.R;
import com.stufeed.holder.BoardViewHolder;
import com.stufeed.pojo.Board;
import com.stufeed.pojo.JoinBoard;
import com.stufeed.pro.Home;

import static com.stufeed.utility.LocVari.*;
import static com.stufeed.utility.SowmitrasMethod.*;
import static com.stufeed.utility.SowmitrasStrings.*;

/**
 * Created by sowmitras on 3/11/17.
 */

public class JoinBoardFrag extends Fragment {

    private ProgressDialog progressDialog;
    public Switch privacySwt;
    private FloatingActionButton addBoard;
    public EditText board_name, board_description;
    public Button cancel, done;
    private FirebaseRecyclerAdapter<Board, BoardViewHolder> mFirebaseAdapter;

    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_join_board, container, false);
        progressDialog = loadProgressDialog(getActivity(), "",CENTER_DISPLAY);
        progressDialog.setMessage("Loading Board Details");
        RecyclerView boardRecyclerView = (RecyclerView) view.findViewById(R.id.boardRecyclerView);
        LinearLayoutManager recyclerViewLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        boardRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        //recyclerViewLayoutManager.setReverseLayout(true);
        recyclerViewLayoutManager.setStackFromEnd(true);

        DATABASE_REFERENCE = setFireBaseKey(CST_USER_LISTS);
        Query query = DATABASE_REFERENCE.child(getCurrentUser(getActivity())).child(CST_JOIN_BOARD).orderByChild(CST_JOINED);

        FirebaseRecyclerAdapter<Board, BoardViewHolder> mFireBaseAdapter = loadBoard(query,getActivity(), getCurrentUser(getActivity()), ((Home)getActivity()).mFirebaseUser.getUid(),BOARD_PRIVACY_ON, BOARD_EDIT_OFF,false,true);
        mFireBaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
            }
        });
        boardRecyclerView.setAdapter(mFireBaseAdapter);
        boardRecyclerView.setNestedScrollingEnabled(false);
        progressDialog.dismiss();
        return view;
    }

   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_search, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setVisibility(View.GONE);

        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        //recyclerAdapter.
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
    }
*/
    /*
        RecyclerView boardRecyclerView = (RecyclerView) view.findViewById(R.id.boardRecyclerView);
        LinearLayoutManager recyclerViewLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        boardRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        DATABASE_REFERENCE = setFireBaseKey(CST_USER_LISTS);
        DATABASE_REFERENCE.child(getCurrentUser(getActivity())).child("joined_board");
        DATABASE_REFERENCE = setFireBaseKey(CST_USER_LISTS);
        Query query = DATABASE_REFERENCE.child(getCurrentUser(getActivity())).child("joined_board");
        mFirebaseAdapter = loadBoard(query,getActivity(),getCurrentUser(getActivity()),((Home)getActivity()).mFirebaseUser.getUid(),BOARD_EDIT_OFF,BOARD_PRIVACY_OFF, false, true);


        final Activity activity, final DatabaseReference reference,

         final String userName, final String UID, final boolean privacy, final boolean editMode

        DATABASE_REFERENCE = setFireBaseKey(CST_USER_LISTS).child(getCurrentUser(getActivity()));
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Board, BoardViewHolder>
                (Board.class,
                        R.layout.board_item_view,
                        BoardViewHolder.class,
                        DATABASE_REFERENCE.child("joined_board")) {
            @Override
            protected void populateViewHolder(final BoardViewHolder viewHolder, final Board model, final int position) {

               if(model.getJoined()!=null&&!model.getJoined().equals("")){
                   DATABASE_REFERENCE = setFireBaseKey(CST_BOARD);
                   DATABASE_REFERENCE.child(model.getJoined()).addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           if(dataSnapshot.getChildrenCount()!=0){
                               viewHolder.getReferenceKey.setText(getDataSnapShotValue(dataSnapshot,CST_BOARD_KEY));
                               viewHolder.createdBy.setText(getDataSnapShotValue(dataSnapshot, CST_BOARD_CREATED_BY));
                               viewHolder.nameOfBoard.setText(getDataSnapShotValue(dataSnapshot,CST_BOARD_NAME));
                               viewHolder.getBoardKey.setText(getDataSnapShotValue(dataSnapshot,CST_BOARD_KEY));
                               viewHolder.postCount.setText(String.valueOf(dataSnapshot.child("post_index").getChildrenCount()));
                               viewHolder.memberCount.setText(String.valueOf(dataSnapshot.child("members").getChildrenCount()));

                             }
                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });
               }


                mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onItemRangeInserted(int positionStart, int itemCount) {
                        super.onItemRangeInserted(positionStart, itemCount);
                    }
                });
            }
        };

        boardRecyclerView.setAdapter(mFirebaseAdapter);
        //TODO Floating Button With Animation Hide And Show
        boardRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0 ||dy<0 && addBoard.isShown()){
     //               addBoard.hide();
                }
            }
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
//                    addBoard.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });*/
}
