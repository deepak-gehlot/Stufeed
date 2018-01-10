package com.stufeed.pro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stufeed.R;
import com.stufeed.holder.UserSearchHolder;
import com.stufeed.pojo.User;

import java.util.ArrayList;
import java.util.List;

import static com.stufeed.utility.SowmitrasStrings.*;
import static com.stufeed.utility.LocVari.*;
import static com.stufeed.utility.SowmitrasMethod.*;

/**
 * Created by sowmitras on 30/12/17.
 */

public class UserSearch extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private Bundle bundle = new Bundle();
    private EditText search_list;
    private RecyclerView recyclerView;
    private UserSearchHolder searchAdapter;
    private List<User> userList ;
    private SharedPreferences sharedPreferences;
    private ArrayList<String> userIdList;
    private ArrayList<String> userName;
    private ArrayList<String> userImage;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_search);

        enableDisableAnalytics(this);
        sharedPreferences = getSharedPreferences(CST_SHARED_REFERENCE, Activity.MODE_PRIVATE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.common_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        search_list = (EditText) findViewById(R.id.search_list);

        bundle = getIntent().getExtras();
        if (bundle != null && !bundle.isEmpty()) {
            setTitle(getFirstLetterCaps(bundle.get("common").toString()));
            //Log.i(LOGCAT,String.valueOf(bundle.get("common")));
            userIdList = new ArrayList<String>();
            userName = new ArrayList<String>();
            userImage = new ArrayList<String>();
            DATABASE_REFERENCE = setFireBaseKey(getFromLocalDataBase(sharedPreferences,CST_CG_ID));
            DATABASE_REFERENCE.child(String.valueOf(bundle.get("common"))).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() != 0) {
                        //Log.i(LOGCAT,""+dataSnapshot.getValue().toString());
                        for (DataSnapshot snap : dataSnapshot.getChildren()) {
                            //Log.i(LOGCAT,""+snap.getValue().toString());
                            DATABASE_REFERENCE = setFireBaseKey(CST_USER_LISTS);
                            userIdList.add(String.valueOf(getDataSnapShotValue(snap,USER_ID)));
                            DATABASE_REFERENCE.child(getDataSnapShotValue(snap,USER_ID)).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getChildrenCount() != 0) {
                                        userName.add(String.valueOf(dataSnapshot.child(CST_USER_FULL_NAME).getValue())/*""+getDataSnapShotValue(snap, CST_USER_FULL_NAME)*/);
                                        userImage.add(String.valueOf(dataSnapshot.child(CST_USER_IMAGE).getValue())/*""+getDataSnapShotValue(snap, CST_USER_IMAGE)*/);
                                    }
                                    searchAdapter = new UserSearchHolder(userName,userIdList,userImage, UserSearch.this);
                                    recyclerView.setAdapter(searchAdapter);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_serarch, menu);
        final MenuItem item = menu.findItem(R.id.user_search_id);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        if(bundle!=null) {
            searchView.setOnQueryTextListener(UserSearch.this);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                showToast(UserSearch.this, "action");
                //goToNext(Home.this, NOTIFICATION,false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<String> filteredModelList = filter(userList,newText);
        searchAdapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<String> filter(List<User> models, String query) {
        query = query.toLowerCase();

        final List<String> filteredModelList = new ArrayList<>();
        for (String model : userName) {
            final String text = model.toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
