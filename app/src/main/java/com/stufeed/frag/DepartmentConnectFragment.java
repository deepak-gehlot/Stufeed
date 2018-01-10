package com.stufeed.frag;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stufeed.R;
import com.stufeed.holder.UserConnectHolder;
import com.stufeed.pojo.User;
import com.stufeed.pro.Home;
import com.stufeed.pro.UserSearch;
import com.stufeed.pro.UserView;

import java.util.Map;

import static com.stufeed.utility.SowmitrasMethod.*;
import static com.stufeed.utility.LocVari.*;
import static com.stufeed.utility.SowmitrasStrings.*;


/**
 * Created by sowmitras on 27/7/17.
 */

public class DepartmentConnectFragment extends Fragment {

    private FloatingActionButton fab;
    private RecyclerView departmentRecyclerView;
    private FirebaseRecyclerAdapter<User, UserConnectHolder> departmentRecyclerAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.connect_user, container, false);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserSearch.class);
                intent.putExtra("common", CST_DEPARTMENT);
                startActivity(intent);
            }
        });
        departmentRecyclerView = setRecyclerViewFragment(getActivity(), view, 3, R.id.userConnectPager);
        DATABASE_REFERENCE = setFireBaseKey(getFromLocalDataBase(((Home) getActivity()).sharedPreferences, CST_CG_ID));
        departmentRecyclerAdapter = new FirebaseRecyclerAdapter<User, UserConnectHolder>(
                User.class,
                R.layout.user_connect_item,
                UserConnectHolder.class,
                DATABASE_REFERENCE.child(CST_DEPARTMENT)
        ) {
            @Override
            protected void populateViewHolder(final UserConnectHolder viewHolder, final User model, int position) {
                showUserDetails(getActivity(), model.getUser_id(), viewHolder.accountkey, viewHolder.userImage, viewHolder.progressBar, viewHolder.userFullName);
                viewHolder.userid.setText(model.getUser_id());
                viewHolder.followButton.setEnabled(false);
                DATABASE_REFERENCE = setFireBaseKey(CST_FOLLOWING);
                DATABASE_REFERENCE.child(getCurrentUser(getActivity())).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() != 0) {
                            int tag = 0;
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if (model.getUser_id().equals(String.valueOf(snapshot.child(USER_ID).getValue().toString()))) {
                                    viewHolder.followButton.setEnabled(true);
                                    viewHolder.followButton.setText("Followed");
                                    tag = 1;
                                    break;
                                } else {
                                    viewHolder.followButton.setEnabled(true);
                                    viewHolder.followButton.setText("Follow");
                                }
                            }
                            if (tag == 1) {
                                viewHolder.followButton.setEnabled(true);
                                viewHolder.followButton.setText("Followed");
                            }
                        } else {
                            viewHolder.followButton.setEnabled(true);
                            viewHolder.followButton.setText("Follow");
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                //getFromLocalDataBase(((Home)getActivity()).sharedPreferences,CST_USER_NAME)
                if (model.getUser_id().equals(getFromLocalDataBase(((Home) getActivity()).sharedPreferences, CST_USER_NAME))) {
                    viewHolder.followButton.setVisibility(View.INVISIBLE);
                    viewHolder.profileView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                } else {
                    viewHolder.profileView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), UserView.class);
                            intent.putExtra(CST_TYPE, BOARD_SCREEN);
                            intent.putExtra(USER_ID, model.getUser_id());
                            startActivity(intent);

                        }
                    });
                }
                viewHolder.followButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (viewHolder.followButton.getText().toString().equals("Followed")) {
                            viewHolder.followButton.setText("Follow");
                            DATABASE_REFERENCE = setFireBaseKey(CST_FOLLOWER);
                            DATABASE_REFERENCE.child(model.getUser_id()).child(viewHolder.accountkey.getText().toString() + "_" + getCurrentUser(getActivity())).removeValue();
                            DATABASE_REFERENCE = setFireBaseKey(CST_FOLLOWING);
                            DATABASE_REFERENCE.child(getCurrentUser(getActivity())).child(viewHolder.accountkey.getText().toString()).removeValue();
                        } else {
                            viewHolder.followButton.setText("Followed");
                            DATABASE_REFERENCE = setFireBaseKey(CST_FOLLOWER);

                            Map<String, Object> taskMap = setMapValue(USER_ID, getFromLocalDataBase(((Home) getActivity()).sharedPreferences, CST_USER_NAME));

                            DATABASE_REFERENCE.child(model.getUser_id()).child(viewHolder.accountkey.getText().toString() + "_" + getCurrentUser(getActivity())).updateChildren(taskMap);
                            DATABASE_REFERENCE = setFireBaseKey(CST_FOLLOWING);
                            Map<String, Object> task = setMapValue(USER_ID, model.getUser_id());

                            DATABASE_REFERENCE.child(getFromLocalDataBase(((Home) getActivity()).sharedPreferences, CST_USER_NAME)).child(viewHolder.accountkey.getText().toString()).updateChildren(task);

                        }
                    }
                });

            }
        };
        departmentRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
            }
        });
        departmentRecyclerView.setAdapter(departmentRecyclerAdapter);

        return view;
    }


}