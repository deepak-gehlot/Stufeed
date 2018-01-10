package com.stufeed.pro;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.stufeed.R;
import com.stufeed.holder.BoardViewHolder;
import com.stufeed.holder.FeedPostViewHolder;
import com.stufeed.adapters.BoardPager;
import com.stufeed.adapters.ConnectPager;
import com.stufeed.pojo.Board;
import com.stufeed.pojo.FeedPost;
import com.stufeed.utility.BottomNavigationHelper;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.stufeed.utility.LocVari.*;
import static com.stufeed.utility.SowmitrasMethod.*;
import static com.stufeed.utility.SowmitrasStrings.*;


/**
 * Created by Sowmitras on 25-10-2017.
 */

public class Home extends AppCompatActivity implements TabLayout.OnTabSelectedListener,
        View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener{

    public DatabaseReference userDatabaseReference = setFireBaseKey(CST_USER_LISTS);
    private DatabaseReference mFirebaseDatabaseReference;
    private ImageButton edit_profile_details;
    private CircleImageView profileImg;
    private Button followBTN;
    private AdView homeAdView;
    private DrawerLayout drawer;
    private ProgressBar progressBar;
    private LinearLayout postLinearLayout,followerLinearLayout,join_board_layout;

    private TabLayout connectTabLayout, boardTabLayout;
    private ViewPager connectViewPager, boardViewPager;
    private ProgressDialog progressDialog;
    private ActionBarDrawerToggle toggle;
    private NavigationView homeNavigation;
    private BottomNavigationView homeBtnNavigView;
    private RelativeLayout myProfile, board, connect, feed;
    private TextView role, fullName, commonProfiler, collageName, followerCounts, postCount,joinBoardCount;

    private TextView unFollow, block, hide_post, delete, textView4;

    public FirebaseAuth mFirebaseAuth;
    public FirebaseUser mFirebaseUser;

    private FirebaseStorage mFirebaseStorage;
    private StorageReference mFirebaseStorageReference;
    private FirebaseRecyclerAdapter<Board, BoardViewHolder> boardAdapter;
    private FirebaseRecyclerAdapter<FeedPost, FeedPostViewHolder> feedAdapter;
    private ArrayList<String> userArray;

    private Toolbar toolbar;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    public ArrayList<String> get_Follower_Name_list ;

    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.feed:
                    enableFeed();
                    return true;
                case R.id.connect:
                    connectButton();
                    return true;
                case R.id.postButton:
                    postButton();
                    return true;
                case R.id.board:
                    boardButton();
                    return true;
                case R.id.you:
                    youButton();
                    return true;
            }return false;}};

    private void connectButton() {
        // showToast(Home.this, "Connect");
        connect.setVisibility(View.VISIBLE);

        feed.setVisibility(View.GONE);
        myProfile.setVisibility(View.GONE);
        board.setVisibility(View.GONE);
    }

    private void postButton() {
        // showToast(Home.this, "post");
        goToNext(this, POST, true);
    }

    private void boardButton() {
        // showToast(Home.this, "board");

        board.setVisibility(View.VISIBLE);

        feed.setVisibility(View.GONE);
        myProfile.setVisibility(View.GONE);
        connect.setVisibility(View.GONE);
    }

    private void youButton() {
        // showToast(Home.this, "you");

        myProfile.setVisibility(View.VISIBLE);

        feed.setVisibility(View.GONE);
        board.setVisibility(View.GONE);
        connect.setVisibility(View.GONE);
    }

    private void enableFeed() {
        feed.setVisibility(View.VISIBLE);
        myProfile.setVisibility(View.GONE);
        board.setVisibility(View.GONE);
        connect.setVisibility(View.GONE);

        get_Follower_Name_list = new ArrayList<String>();

        progressDialog.setMessage("Fetching Post Data");
        progressDialog.show();


        DATABASE_REFERENCE = setFireBaseKey(CST_FOLLOWING);
        DATABASE_REFERENCE.child(getCurrentUser(Home.this)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        snapshot.getChildren().iterator().hasNext();
                        get_Follower_Name_list.add(snapshot.child("user_id").getValue().toString());
                    }
                }
                progressDialog.dismiss();
//                feedAdapter = showPost(get_Follower_Name_list,progressDialog,Home.this,R.id.feedRecyclerView,1,getCurrentUser(Home.this),mFirebaseUser.getUid(),true, true);
                showPost(get_Follower_Name_list,progressDialog,Home.this,R.id.feedRecyclerView,1,getCurrentUser(Home.this),mFirebaseUser.getUid(),true, true);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        enableDisableAnalytics(this);
        setView();

        progressDialog = loadProgressDialog(this, "Checking server", BOTTOM_DISPLAY);
        progressDialog.show();

        //TODO FOR CONNECT (FACULTY, STUDENT, DEPARTMENT AND FACULTY)
        ConnectPager connectPager = new ConnectPager(getSupportFragmentManager(), connectTabLayout.getTabCount());
        connectViewPager.setAdapter(connectPager);
        connectTabLayout.setOnTabSelectedListener(this);
        connectViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(connectTabLayout));

        //TODO FOR BOARD (CREATE AND JOIN BOARD)
        BoardPager facultyBoard = new BoardPager(getSupportFragmentManager(), boardTabLayout.getTabCount());
        boardViewPager.setAdapter(facultyBoard);
        boardTabLayout.setOnTabSelectedListener(this);
        boardViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(boardTabLayout));
        showBoard(progressDialog,Home.this,R.id.boardRcView,2,getCurrentUser(this), mFirebaseUser.getUid(),BOARD_PRIVACY_ON,BOARD_EDIT_ON,false);

        FirebaseRecyclerAdapter<FeedPost, FeedPostViewHolder>loadPost = loadPost(this, getCurrentUser(this));
        final RecyclerView postRecyclerView = (RecyclerView) findViewById(R.id.postRcView);
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



       /* DATABASE_REFERENCE = setFireBaseKey(CST_POST);
        DATABASE_REFERENCE.orderByChild(CST_POST_BY).equalTo(getCurrentUser(this)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()!=0){
                    userArray= new ArrayList<String>();
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        Log.i(LOGCAT, String.valueOf(snapshot.getKey()));
                        userArray.add(getCurrentUser(Home.this));
                    }
                    showPost(userArray,progressDialog,Home.this,R.id.postRcView,1,getCurrentUser(Home.this),mFirebaseUser.getUid(),true, false);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.saved_menu){
            goToNext(Home.this, SAVEPOST_SCREEN, false);
        }else if (id == R.id.archive_menu) {
            goToNext(Home.this, ARCHIVE_SCREEN, false);
        } else if (id == R.id.block_menu) {
            //postButton();
             goToNext(this, BLOCKED_SCREEN, false);
        }
        else if(id ==R.id.logout_menu){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            builder.setTitle("Confirm action");
            builder.setMessage("Do you want to exit sign out");
            builder.setCancelable(false);
            View dialogView= inflater.inflate(R.layout.exit_layout, null);
            builder.setView(dialogView);
            final AdView adView1 = (AdView)dialogView.findViewById(R.id.exitAd);
            final ProgressBar progressBar = (ProgressBar)dialogView.findViewById(R.id.progressBarExit);
            AdRequest adRequest1= new AdRequest.Builder().build();
            adView1.loadAd(adRequest1);
            adView1.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    adView1.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    super.onAdFailedToLoad(errorCode);
                    adView1.setVisibility(View.GONE);
                }
            });

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    logOut(editor,"","", 1);
                    mFirebaseAuth.signOut();
                    goToNext(Home.this, SPLASH_SCREEN, true);

                }
            }).setNegativeButton("No",null);
            builder.create();
            builder.show();


        }else if(id == R.id.following){
            Intent intent = new Intent(this, FollowerFollowing.class);
            intent.putExtra(CST_USER_NAME, getCurrentUser(Home.this));
            intent.putExtra(CST_TYPE, CST_FOLLOWING);
            startActivity(intent);
            //finish();
            // showToast(Home.this,FOLLOWED_SCREEN);
        }else if(id == R.id.setting){
            goToNext(Home.this, SETTING_SCREEN,false);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        connectViewPager.setCurrentItem(tab.getPosition());
        boardViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edit_profile_details:
                goToNext(Home.this, EDIT_PROFILE, false);
                break;
            case R.id.postLinearLayout:
                goToNext(Home.this, GET_FEEDS_SCREEN,false);
                break;
            case R.id.followerLinearLayout:
                Intent intent = new Intent(this, FollowerFollowing.class);
                intent.putExtra(CST_USER_NAME, getCurrentUser(Home.this));
                intent.putExtra(CST_TYPE, CST_FOLLOWER);
                startActivity(intent);
                break;
            case R.id.join_board_layout:
                goToNext(Home.this, JOIN_BOARD_SCREEN,false);
                break;
            default:
                showToast(Home.this, "Empty");
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void setView() {

        /*TODO FireBase Controller*/
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseStorageReference = mFirebaseStorage.getReferenceFromUrl("gs://stufeed-dev.appspot.com");


          /*TODO: AdView Home*/
        progressBar = setProgressBar(this, R.id.progressBarAds);
        homeAdView = showAds(this, R.id.mainAd,progressBar);


        /*TODO: For Later Use*/
        if(mFirebaseUser==null){
            //// showToast(Home.this, "EMpty");
        }else{
            //// showToast(Home.this, ""+mFirebaseUser.getEmail());
        }

        /*TODO: Shared Preference*/
        sharedPreferences = setSharedPreference(this, CST_SHARED_REFERENCE);
        editor = sharedPreferences.edit();

        /*TODO: BottomNavigationView*/
        homeBtnNavigView = (BottomNavigationView)findViewById(R.id.homeBtnNavigView);
        homeBtnNavigView.setVisibility(View.VISIBLE);
        BottomNavigationHelper.disableShiftMode(homeBtnNavigView);
        homeBtnNavigView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        /*TODO: Navigation Drawer*/
        homeNavigation = (NavigationView) findViewById(R.id.home_navigation);
        homeNavigation.setNavigationItemSelectedListener(this);

        /*TODO: ToolBar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*TODO: DrawerLayout*/
        drawer= (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        /*TODO All Relative Layout*/
        myProfile = (RelativeLayout)findViewById(R.id.myProfile);
        board = (RelativeLayout)findViewById(R.id.board);

        feed = (RelativeLayout)findViewById(R.id.feedButton);
        connect = (RelativeLayout)findViewById(R.id.connectivity);

        /*TODO TextView*/
        role = (TextView)findViewById(R.id.userRole);
        fullName = (TextView)findViewById(R.id.fullName);
        commonProfiler = (TextView)findViewById(R.id.commonProfiler);
        textView4 = (TextView)findViewById(R.id.textView4);
        collageName = (TextView)findViewById(R.id.userCollegeName);

        /*TODO Set TextView*/
        role.setText((getFromLocalDataBase(sharedPreferences, CST_ROLE)).substring(0, 1).toUpperCase() + (getFromLocalDataBase(sharedPreferences, CST_ROLE)).substring(1));
        collageName.setText(getFromLocalDataBase(sharedPreferences, CST_COLLEGE));
        fullName.setText(getFromLocalDataBase(sharedPreferences,CST_USER_FULL_NAME));
        if(getFromLocalDataBase(sharedPreferences,CST_ROLE).equals("department")){
            commonProfiler.setText(getFromLocalDataBase(sharedPreferences,CST_DEPARTMENT));
            textView4.setVisibility(View.GONE);
        }else if(getFromLocalDataBase(sharedPreferences,CST_ROLE).equals("faculty")){
            commonProfiler.setText(getFromLocalDataBase(sharedPreferences,CST_DEPARTMENT));
            textView4.setText(getFromLocalDataBase(sharedPreferences,CST_DESIGNATION));
            textView4.setVisibility(View.VISIBLE);
        }else{
            textView4.setVisibility(View.GONE);
            commonProfiler.setText(getFromLocalDataBase(sharedPreferences,CST_PROGRAM) +"("+getFromLocalDataBase(sharedPreferences,CST_BRANCH) +")");
        }

        /*TODO ProgressDialog*/
        progressDialog = loadProgressDialog(this, "Loading Data", CENTER_DISPLAY);

        /*TODO ViewPager*/
        connectTabLayout = (TabLayout) findViewById(R.id.connectTL);
        connectViewPager = (ViewPager) findViewById(R.id.connectPager);

        connectTabLayout.addTab(connectTabLayout.newTab().setText("Academy"));
        connectTabLayout.addTab(connectTabLayout.newTab().setText("Faculty"));
        connectTabLayout.addTab(connectTabLayout.newTab().setText("Student"));
        connectTabLayout.addTab(connectTabLayout.newTab().setText("Department"));

        connectTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        boardTabLayout = (TabLayout) findViewById(R.id.commonTabLayout);
        boardViewPager = (ViewPager) findViewById(R.id.commonViewPager);

        boardTabLayout.addTab(boardTabLayout.newTab().setText("Create Board"));
        boardTabLayout.addTab(boardTabLayout.newTab().setText("Join Board"));


        boardTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        /*TODO Follower Count*/
        followerCounts = getCount(CST_FOLLOWER,CST_FOLLOWER,getCurrentUser(this),R.id.followerCounts,this);

        postCount = getCount(CST_POST,CST_POST,getCurrentUser(this),R.id.postCount,this);

        joinBoardCount = getCount(CST_JOINED,CST_USER_LISTS,getCurrentUser(this),R.id.joinBoardCount,this);



        edit_profile_details = (ImageButton)findViewById(R.id.edit_profile_details);
        followBTN = (Button)findViewById(R.id.followBTN);
        edit_profile_details.setVisibility(View.VISIBLE);
        followBTN.setVisibility(View.GONE);


        /**TODO Edit Profile Button*/
        edit_profile_details = (ImageButton) findViewById(R.id.edit_profile_details);
        edit_profile_details.setOnClickListener(this);

        profileImg = (CircleImageView) findViewById(R.id.profileImg);
        Picasso.with(this)
                .load(getFromLocalDataBase(sharedPreferences,CST_USER_IMAGE))
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(profileImg, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(Home.this)
                                .load(getFromLocalDataBase(sharedPreferences,CST_USER_IMAGE))
                                .error(R.drawable.ic_launcher_web)
                                .into(profileImg, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        progressDialog.dismiss();
                                    }

                                    @Override
                                    public void onError() {
                                        progressDialog.dismiss();
                                        Log.v("Picasso", "Could not fetch image");
                                    }
                                });
                    }
                });


         /*TODO: Check Server Connection*/

        DATABASE_REFERENCE = setFireBaseKey(ACCOUNTS);
        DATABASE_REFERENCE.child(getFromLocalDataBase(sharedPreferences,CST_ACCOUNT_KEY)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()==0){
                    logOut(editor,"","", 1);
                    mFirebaseAuth.signOut();
                    goToNext(Home.this, SPLASH_SCREEN, true);
                }else{
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        postLinearLayout = (LinearLayout)findViewById(R.id.postLinearLayout);
        postLinearLayout.setOnClickListener(this);

        followerLinearLayout = (LinearLayout)findViewById(R.id.followerLinearLayout);
        followerLinearLayout.setOnClickListener(this);

        join_board_layout = (LinearLayout)findViewById(R.id.join_board_layout);
        join_board_layout.setOnClickListener(this);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.notification:
                goToNext(Home.this, NOTIFICATION,false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}