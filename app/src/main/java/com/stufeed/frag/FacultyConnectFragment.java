package com.stufeed.frag;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.stufeed.R;
import com.stufeed.holder.UserConnectHolder;
import com.stufeed.pojo.User;
import com.stufeed.pro.Home;
import com.stufeed.pro.UserSearch;
import com.stufeed.pro.UserView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.stufeed.utility.SowmitrasMethod.*;
import static com.stufeed.utility.SowmitrasStrings.*;
import static com.stufeed.utility.LocVari.*;

/**
 * Created by sowmitras on 27/7/17.
 */

public class FacultyConnectFragment extends Fragment {

    private TextView empty;
    private FloatingActionButton fab;
    private RecyclerView facultyRecyclerView;
    private FirebaseRecyclerAdapter<User, UserConnectHolder> recyclerAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.connect_user, container, false);
        empty = (TextView) view.findViewById(R.id.empty);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserSearch.class);
                intent.putExtra("common", CST_FACULTY);
                startActivity(intent);
            }
        });
        facultyRecyclerView = setRecyclerViewFragment(getActivity(), view, 3, R.id.userConnectPager);
        DATABASE_REFERENCE = setFireBaseKey(getFromLocalDataBase(((Home) getActivity()).sharedPreferences, CST_CG_ID));
        recyclerAdapter = new FirebaseRecyclerAdapter<User, UserConnectHolder>(
                User.class,
                R.layout.user_connect_item,
                UserConnectHolder.class,
                DATABASE_REFERENCE.child(CST_FACULTY)
        ) {
            @Override
            protected void populateViewHolder(final UserConnectHolder viewHolder, final User model, int position) {
                DatabaseReference ce = setFireBaseKey(CST_USER_LISTS);
                ce.child(getCurrentUser(getActivity())).child(CST_BLOCKED).child(model.getUser_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() != 0) {
                            viewHolder.user_main.setVisibility(View.GONE);
                        } else {
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
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };
        recyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                empty.setVisibility(View.GONE);
                super.onItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                empty.setText("Student Not Found");
                empty.setVisibility(View.VISIBLE);
                super.onItemRangeRemoved(positionStart, itemCount);
            }

        });
        facultyRecyclerView.setAdapter(recyclerAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_search, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setVisibility(View.GONE);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
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

    public void setFilter(List<User> user) {
//        mCountryModel = new ArrayList<>();
//        mCountryModel.addAll(user);
//        notifyDataSetChanged();
    }
}




/*Social Login
E-mail Login
Dashboard
Camera / Photos
User Profiles
GeoLocation
Shopping Cart
Accept Payments
Search
3rd Party API Integration
Push Notifications
SMS Integration
Dynamic Content
Messaging
Approval / Moderation*/

/* showUserDetails(getActivity(),model.getUser_id(),viewHolder.accountkey,viewHolder.userImage,viewHolder.progressBar,viewHolder.userFullName);
                viewHolder.userid.setText(model.getUser_id());
                viewHolder.followButton.setEnabled(false);
                DATABASE_REFERENCE = setFireBaseKey(CST_FOLLOWING);
                DATABASE_REFERENCE.child(getCurrentUser(getActivity())).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount()!=0){
                            for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                if(model.getUser_id().equals(String.valueOf(snapshot.child(USER_ID).getValue().toString()))){
                                    viewHolder.followButton.setEnabled(true);
                                    viewHolder.followButton.setText("Followed");
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

                //getFromLocalDataBase(((Home)getActivity()).sharedPreferences,CST_USER_NAME)
                if(model.getUser_id().equals(getFromLocalDataBase(((Home)getActivity()).sharedPreferences,CST_USER_NAME))){
                    viewHolder.followButton.setVisibility(View.INVISIBLE);
                    viewHolder.profileView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                }else{
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
                        if(viewHolder.followButton.getText().toString().equals("Followed")){
                            viewHolder.followButton.setText("Follow");
                            DATABASE_REFERENCE = setFireBaseKey(CST_FOLLOWER);
                            DATABASE_REFERENCE.child(model.getUser_id()).child(viewHolder.accountkey.getText().toString()+"_"+getCurrentUser(getActivity())).removeValue();
                            DATABASE_REFERENCE = setFireBaseKey(CST_FOLLOWING);
                            DATABASE_REFERENCE.child(getCurrentUser(getActivity())).child(viewHolder.accountkey.getText().toString()).removeValue();
                        }else{
                            viewHolder.followButton.setText("Followed");
                            DATABASE_REFERENCE = setFireBaseKey(CST_FOLLOWER);

                            Map<String, Object> taskMap = setMapValue(USER_ID,getFromLocalDataBase(((Home)getActivity()).sharedPreferences,CST_USER_NAME));

                            DATABASE_REFERENCE.child(model.getUser_id()).child(viewHolder.accountkey.getText().toString()+"_"+getCurrentUser(getActivity())).updateChildren(taskMap);
                            DATABASE_REFERENCE = setFireBaseKey(CST_FOLLOWING);
                            Map<String, Object> task = setMapValue(USER_ID,model.getUser_id());

                            DATABASE_REFERENCE.child(getFromLocalDataBase(((Home)getActivity()).sharedPreferences,CST_USER_NAME)).child(viewHolder.accountkey.getText().toString()).updateChildren(task);

                        }
                    }
                });*/