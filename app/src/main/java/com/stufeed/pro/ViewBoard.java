package com.stufeed.pro;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.stufeed.R;
import com.stufeed.adapters.ViewBoardPager;

import static com.stufeed.utility.SowmitrasMethod.*;
import static com.stufeed.utility.SowmitrasStrings.*;

/**
 * Created by sowmitras on 6/12/17.
 */

public class ViewBoard extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    public TabLayout viewBoardTabLayout;
    public ViewPager viewBoardViewPager;
    public SharedPreferences.Editor editor;
    public SharedPreferences sharedPreferences;
    Bundle bundle = new Bundle();

    public DatabaseReference post, member;
    public String boardKey;

    public FirebaseAuth firebaseAuth;
    public FirebaseUser firebaseUser;

    public Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableDisableAnalytics(this);
        setContentView(R.layout.common_tab_layout_with_view_pager);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        bundle = getIntent().getExtras();
        sharedPreferences = setSharedPreference(this,CST_SHARED_REFERENCE);
        editor = sharedPreferences.edit();

        toolbar = (Toolbar) findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if(bundle!=null&&!bundle.isEmpty()){
            boardKey = bundle.get("board_key").toString();
         //   Toast.makeText(this, String.valueOf(boardKey), Toast.LENGTH_SHORT).show();
        }else{
         //   Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
        }
        viewBoardTabLayout = (TabLayout) findViewById(R.id.commonTabLayout);
        viewBoardViewPager = (ViewPager) findViewById(R.id.commonViewPager);

        viewBoardTabLayout.addTab(viewBoardTabLayout.newTab().setText("Posts"));
        viewBoardTabLayout.addTab(viewBoardTabLayout.newTab().setText("Members"));
        viewBoardTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        ViewBoardPager facultyBoard = new ViewBoardPager(getSupportFragmentManager(), viewBoardTabLayout.getTabCount());
        viewBoardViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(viewBoardTabLayout));
        viewBoardViewPager.setAdapter(facultyBoard);
        viewBoardTabLayout.setOnTabSelectedListener(this);

    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewBoardViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
