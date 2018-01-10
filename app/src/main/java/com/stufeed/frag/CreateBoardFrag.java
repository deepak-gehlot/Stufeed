package com.stufeed.frag;

import android.app.Dialog;
import android.content.Intent;
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
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.stufeed.R;
import com.stufeed.holder.BoardViewHolder;
import com.stufeed.pojo.Board;
import com.stufeed.pro.Home;
import com.stufeed.pro.Post;

import java.util.HashMap;
import java.util.Map;

import static com.stufeed.utility.SowmitrasStrings.*;
import static com.stufeed.utility.SowmitrasMethod.*;
import static com.stufeed.utility.LocVari.*;

/**
 * Created by sowmitras on 3/11/17.
 */

public class CreateBoardFrag extends Fragment {

    public Switch privacySwt;
    private FloatingActionButton addBoard;
    public EditText board_name, board_description;
    public Button cancel, done;
    private FirebaseRecyclerAdapter<Board, BoardViewHolder> mFirebaseAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_join_board, container, false);
        addBoard = (FloatingActionButton) view.findViewById(R.id.fab);
        addBoard.setVisibility(View.VISIBLE);
        addBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog board_dialog = new Dialog(getActivity());
                board_dialog.setContentView(R.layout.create_board_dialog);
                board_dialog.show();

                board_name = (EditText) board_dialog.findViewById(R.id.nameOfBoardEdit);
                board_description = (EditText) board_dialog.findViewById(R.id.boardDescription);
                cancel = (Button) board_dialog.findViewById(R.id.cancel_board);
                privacySwt = (Switch) board_dialog.findViewById(R.id.privacy);

                done = (Button) board_dialog.findViewById(R.id.done_board);
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (board_name.getText().toString().isEmpty()) {
                            showEmptyField(board_name, "Enter Board Name");
                        }else {
                            DATABASE_REFERENCE =setFireBaseKey(CST_BOARD);
                            String getKey = String.valueOf(DATABASE_REFERENCE.push().getKey());
                            if(board_description.getText().toString().isEmpty()){
                                saveBoard(getActivity(), privacySwt,board_dialog, getKey,board_name.getText().toString(), ((Home)getActivity()).mFirebaseUser.getUid()," ",2);
                            }else{
                                saveBoard(getActivity(), privacySwt,board_dialog, getKey,board_name.getText().toString(), ((Home)getActivity()).mFirebaseUser.getUid(),board_description.getText().toString(),2);
                            }
                        }
                    }

                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        board_dialog.dismiss();
                    }
                });
            }
        });

        RecyclerView boardRecyclerView = (RecyclerView) view.findViewById(R.id.boardRecyclerView);
        LinearLayoutManager recyclerViewLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        boardRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        DATABASE_REFERENCE = setFireBaseKey(CST_BOARD);
        Query query = DATABASE_REFERENCE.orderByChild(CST_BOARD_CREATED_BY).equalTo(getCurrentUser(getActivity()));
        mFirebaseAdapter = loadBoard(query,getActivity(),getCurrentUser(getActivity()),((Home)getActivity()).mFirebaseUser.getUid(),BOARD_EDIT_ON,BOARD_PRIVACY_ON,false, false);

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
            }
        });
        boardRecyclerView.setAdapter(mFirebaseAdapter);
        //TODO Floating Button With Animation Hide And Show
        boardRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0 ||dy<0 && addBoard.isShown())
                    addBoard.hide();
            }
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    addBoard.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        return view;
    }
/*    @Override
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
    }*/


}
