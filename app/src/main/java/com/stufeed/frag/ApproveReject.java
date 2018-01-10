package com.stufeed.frag;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stufeed.R;
import com.stufeed.holder.JoinBoardViewHolder;
import com.stufeed.pojo.JoinBoard;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.stufeed.utility.SowmitrasMethod.*;
import static com.stufeed.utility.LocVari.*;
import static com.stufeed.utility.SowmitrasStrings.*;

/**
 * Created by sowmitras on 6/11/17.
 */

public class ApproveReject extends Fragment {

    private RecyclerView boardRecyclerView;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;

    private FirebaseRecyclerAdapter<JoinBoard, JoinBoardViewHolder> mFirebaseAdapter;
    private DatabaseReference mFirebaseDatabaseReference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.approve_reject, null, false);
        boardRecyclerView = (RecyclerView) view.findViewById(R.id.approveRejectRecyclerView);
        recyclerViewLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 1);
        boardRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference("approve_reject");
        mFirebaseAdapter = new FirebaseRecyclerAdapter<JoinBoard, JoinBoardViewHolder>
                (JoinBoard.class,
                        R.layout.approve_reject_item,
                        JoinBoardViewHolder.class,
                        mFirebaseDatabaseReference.child(getCurrentUser(getActivity()))) {
            @Override
            protected void populateViewHolder(final JoinBoardViewHolder viewHolder, final JoinBoard model, final int position) {
                DATABASE_REFERENCE = setFireBaseKey(CST_BOARD);
                DATABASE_REFERENCE.child(model.getBoard_key()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount()!=0){
                            viewHolder.boardName.setText(String.valueOf(dataSnapshot.child(CST_BOARD_NAME).getValue().toString()));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                viewHolder.username.setText(String.valueOf(model.getRequested_by()));

                viewHolder.requestedTime.setText(getRelativeTimeDisplay(model.getTime_requested()));
                viewHolder.accepted.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String board_Key = String.valueOf(model.getBoard_key());
                        String create_by = String.valueOf(model.getCreated_by());
                        final String requested_key = String.valueOf(model.getRequested_key());
                        String requested_by = String.valueOf(model.getRequested_by());
                        String time_requested = String.valueOf(model.getTime_requested());
                        String user_id = String.valueOf(model.getUser_id());

                        Log.i(LOGCAT, "Board Key "+board_Key+"\n"+"" +
                                "Created _by "+ create_by+"\n"+"" +
                                "Requested_by "+requested_by+"\n"+"" +
                                "Requested_time "+time_requested+"\n"+"" +
                                "Requested_key "+ requested_key+"\n"+"" +
                                "User_Id "+ user_id);


                        DATABASE_REFERENCE = setFireBaseKey(CST_USER_LISTS);
                        DATABASE_REFERENCE.child(requested_by).child("joined_board").child(board_Key).setValue(setMapValue("joined", board_Key));

                        DATABASE_REFERENCE = FirebaseDatabase.getInstance().getReference("boards");
                        DATABASE_REFERENCE.child(model.getBoard_key()).child("members").child(user_id).setValue(model.getRequested_by());

                        DATABASE_REFERENCE = setFireBaseKey(CST_JOIN_BOARD);
                        Map<String, Object> join_board = setMapValue("userUI", model.getRequested_by());
                        DATABASE_REFERENCE.child(model.getBoard_key()).child(user_id).setValue(join_board);


                        rejectRequest(model.getCreated_by(),model.getBoard_key(),model.getUser_id(),model.getRequested_key());

                    }
                });

                viewHolder.rejected.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rejectRequest(model.getCreated_by(),model.getBoard_key(),model.getUser_id(),model.getRequested_key());
                    }
                });
            }
        };
       /* mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
            }
        });*/
        boardRecyclerView.setAdapter(mFirebaseAdapter);
        return view;
    }

    private void rejectRequest(String created_by, String board_key, String user_id, final String requested_key) {
        DATABASE_REFERENCE = setFireBaseKey("board_requested");
        DATABASE_REFERENCE.child(created_by).child(board_key).child(user_id).removeValue();

        DATABASE_REFERENCE = setFireBaseKey("approve_reject");
        DATABASE_REFERENCE.child(getCurrentUser(getActivity())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()!=0){
                    dataSnapshot.getChildren().iterator().hasNext();
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        if(snapshot.child("requested_key").getValue().toString().equals(requested_key)){
                            DATABASE_REFERENCE = setFireBaseKey("approve_reject");
                            DATABASE_REFERENCE.child(getCurrentUser(getActivity())).child(requested_key).removeValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DATABASE_REFERENCE = FirebaseDatabase.getInstance().getReference(CST_BOARD);
        DATABASE_REFERENCE.child(board_key).child("requested").child(user_id).removeValue();
    }
}
