package com.stufeed.pro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.stufeed.R;
import com.stufeed.holder.BoardViewHolder;
import com.stufeed.pojo.Board;
import com.stufeed.pojo.User;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.stufeed.utility.LocVari.*;
import static com.stufeed.utility.SowmitrasMethod.*;
import static com.stufeed.utility.SowmitrasStrings.*;

/**
 * Created by sowmitras on 2/11/17.
 */

public class UserView extends AppCompatActivity implements View.OnClickListener{

    private AdView homeAdView;

    private DrawerLayout drawer;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private ActionBarDrawerToggle toggle;
    private NavigationView homeNavigation;
    private BottomNavigationView homeBtnNavigView;
    private RelativeLayout myProfile, board, connect, feed;

    private LinearLayout postLinearLayout,followerLinearLayout,join_board_layout;
    private TextView role, fullName, commonProfiler, collageName, followerCounts, postCount,joinBoardCount;

    private FirebaseRecyclerAdapter<Board, BoardViewHolder> mFirebaseAdapter;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mFirebaseStorageReference;

    private Toolbar toolbar;
    private ImageButton edit_profile_details;
    private Button followBTN;

    private CircleImageView profileImg;

    private Home home = new Home();
    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableDisableAnalytics(this);
        setContentView(R.layout.activity_home);
        setView();
        edit_profile_details = (ImageButton)findViewById(R.id.edit_profile_details);
        profileImg = (CircleImageView) findViewById(R.id.profileImg);
        followBTN = (Button)findViewById(R.id.followBTN);
        edit_profile_details.setVisibility(View.GONE);
        followBTN.setVisibility(View.VISIBLE);
        bundle = getIntent().getExtras();
        if(bundle!=null){
            followerCounts = getCount(CST_FOLLOWER,CST_FOLLOWER,String.valueOf(bundle.get(USER_ID).toString()),R.id.followerCounts,this);
            DATABASE_REFERENCE = setFireBaseKey(CST_USER_LISTS);
            DATABASE_REFERENCE.child(bundle.get(USER_ID).toString()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getChildren()!=null){
                        //Log.i(LOGCAT, String.valueOf(dataSnapshot.getValue()));

                        switch (getDataSnapShotValue(dataSnapshot,CST_ROLE)){
                            case CST_STUDENT:
                                break;
                            case CST_DEPARTMENT:
                                break;
                            case CST_FACULTY:
                                break;
                            case CST_ACADEMY:
                                break;
                            case CST_SCHOOL:
                                break;
                            case CST_COACHING:
                                break;
                            case CST_INSTITUTE:
                                break;
                        }

                        String Name = String.valueOf(dataSnapshot.child(CST_USER_FULL_NAME).getValue());
                        String college = String.valueOf(dataSnapshot.child(CST_COLLEGE).getValue());
                        String depart = String.valueOf(dataSnapshot.child(CST_DEPARTMENT).getValue().toString());
                        String design = String.valueOf(dataSnapshot.child(CST_DESIGNATION).getValue().toString());
                        String degree = String.valueOf(dataSnapshot.child(CST_DEGREE).getValue().toString());
                        String faculty = String.valueOf(dataSnapshot.child(CST_FACULTY).getValue().toString());
                        String branch = String.valueOf(dataSnapshot.child(CST_BRANCH).getValue().toString());
                        role.setText(getDataSnapShotValue(dataSnapshot,CST_ROLE));
                        fullName.setText(Name);
                        collageName.setText(college);
//                        showToast(UserView.this, getDataSnapShotValue(dataSnapshot,CST_ACCOUNT_KEY));
                        setPicasso(getDataSnapShotValue(dataSnapshot,CST_USER_IMAGE),profileImg,UserView.this,progressDialog);

                        DATABASE_REFERENCE = setFireBaseKey(CST_FOLLOWING);
                        followBTN.setEnabled(false);
                        DATABASE_REFERENCE.child(getCurrentUser(UserView.this)).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getChildrenCount()!=0){
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        if(bundle.get(USER_ID).toString().equals(String.valueOf(snapshot.child(USER_ID).getValue().toString()))){
                                            followBTN.setEnabled(true);
                                            followBTN.setText("Followed");

                                        }else{
                                            followBTN.setEnabled(true);
                                        }
                                    }
                                }else{
                                    followBTN.setEnabled(true);
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        followBTN.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(followBTN.getText().toString().equals("Followed")){
                                    followBTN.setText("Follow");
                                    DATABASE_REFERENCE = setFireBaseKey(CST_FOLLOWER);
                                    DATABASE_REFERENCE.child(bundle.get(USER_ID).toString()).child(getDataSnapShotValue(dataSnapshot,CST_ACCOUNT_KEY)+"_"+getCurrentUser(UserView.this)).removeValue();

                                    DATABASE_REFERENCE = setFireBaseKey(CST_FOLLOWING);
                                    DATABASE_REFERENCE.child(getCurrentUser(UserView.this)).child(getDataSnapShotValue(dataSnapshot,CST_ACCOUNT_KEY)).removeValue();
                                }else{
                                    followBTN.setText("Followed");
                                    DATABASE_REFERENCE = setFireBaseKey(CST_FOLLOWER);

                                    Map<String, Object> taskMap = setMapValue(USER_ID,getCurrentUser(UserView.this));

                                    DATABASE_REFERENCE.child(bundle.get(USER_ID).toString()).child(getDataSnapShotValue(dataSnapshot,CST_ACCOUNT_KEY)+"_"+getCurrentUser(UserView.this)).updateChildren(taskMap);
                                    DATABASE_REFERENCE = setFireBaseKey(CST_FOLLOWING);
                                    Map<String, Object> task = setMapValue(USER_ID,bundle.get(USER_ID).toString());

                                    DATABASE_REFERENCE.child(getCurrentUser(UserView.this)).child(getDataSnapShotValue(dataSnapshot,CST_ACCOUNT_KEY)).updateChildren(task);

                                }
                            }
                        });


                        //Log.i(LOGCAT, depart+" "+design/*+" "+degree+" "+faculty+" "+branch*/);
                        if(getDataSnapShotValue(dataSnapshot,CST_ROLE).equals("department")){
                            commonProfiler.setText(depart);
                        }else if(getDataSnapShotValue(dataSnapshot,CST_ROLE).equals("faculty")){
                            commonProfiler.setText(faculty +"("+design+")");
                        }else{
                            commonProfiler.setText(degree +"("+branch +")");
                        }
                    }
                    showBoard(progressDialog,UserView.this,
                            R.id.boardRcView,2,
                            bundle.get(USER_ID).toString(),
                            mFirebaseUser.getUid(),
                            BOARD_PRIVACY_ON, BOARD_EDIT_OFF, false);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }else{
            showSnackBar(joinBoardCount,"Something went wrong", null);
            onBackPressed();
        }
    }



    private void setView() {
          /*TODO FireBase Controller*/
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseStorageReference = mFirebaseStorage.getReferenceFromUrl("gs://stufeed-dev.appspot.com");

        progressDialog = loadProgressDialog(this,"Loading Data..",CENTER_DISPLAY);
        progressDialog.show();
          /*TODO: AdView Home*/
        progressBar = setProgressBar(this, R.id.progressBarAds);
        homeAdView = showAds(this, R.id.mainAd,progressBar);


         /*TODO: BottomNavigationView*/
        homeBtnNavigView = (BottomNavigationView)findViewById(R.id.homeBtnNavigView);


        /*TODO: Navigation Drawer*/
        homeNavigation = (NavigationView) findViewById(R.id.home_navigation);
        homeBtnNavigView.setVisibility(View.GONE);


        /*TODO: ToolBar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

          /*TODO: DrawerLayout*/
        drawer= (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        /*TODO All Relative Layout*/
        myProfile = (RelativeLayout)findViewById(R.id.myProfile);
        board = (RelativeLayout)findViewById(R.id.board);

        feed = (RelativeLayout)findViewById(R.id.feedButton);
        connect = (RelativeLayout)findViewById(R.id.connectivity);
        /*TODO TextView*/
        role = (TextView)findViewById(R.id.userRole);
        fullName = (TextView)findViewById(R.id.fullName);
        commonProfiler = (TextView)findViewById(R.id.commonProfiler);
        collageName = (TextView)findViewById(R.id.userCollegeName);

        postLinearLayout = (LinearLayout)findViewById(R.id.postLinearLayout);
        postLinearLayout.setOnClickListener(UserView.this);

        followerLinearLayout = (LinearLayout)findViewById(R.id.followerLinearLayout);
        followerLinearLayout.setOnClickListener(UserView.this);

        join_board_layout = (LinearLayout)findViewById(R.id.join_board_layout);
        join_board_layout.setOnClickListener(UserView.this);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.postLinearLayout:
                break;
            case R.id.followerLinearLayout:
                Intent intent = new Intent(this, FollowerFollowing.class);
                intent.putExtra(CST_USER_NAME, bundle.get(USER_ID).toString());
                intent.putExtra(CST_TYPE, CST_FOLLOWER);
                startActivity(intent);
                break;
            case R.id.join_board_layout:
                break;
                default:
                    break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.block, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.block) {
            DATABASE_REFERENCE = setFireBaseKey(CST_USER_LISTS);
            Map<String, Object> blocked = setMapValue(USER_ID,bundle.get(USER_ID).toString());
            DATABASE_REFERENCE.child(getCurrentUser(this)).child(CST_BLOCKED).child(bundle.get(USER_ID).toString()).updateChildren(blocked);
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
