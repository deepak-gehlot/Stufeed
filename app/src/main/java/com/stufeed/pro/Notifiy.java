package com.stufeed.pro;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stufeed.R;
import com.stufeed.frag.ApproveReject;

import static com.stufeed.utility.LocVari.*;
import static com.stufeed.utility.SowmitrasMethod.*;
import static com.stufeed.utility.SowmitrasStrings.*;

/**
 * Created by sowmitras on 6/11/17.
 */

public class Notifiy extends AppCompatActivity{

    int count = 0;
    private Toolbar toolbar;
    private TextView requestCount;
    private RelativeLayout relativeLayout;
    private FrameLayout approveRejectLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableDisableAnalytics(this);
        setContentView(R.layout.notification);

        toolbar = (Toolbar) findViewById(R.id.notifiy_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getResources().getString(R.string.notification));

        approveRejectLayout = (FrameLayout) findViewById(R.id.approveReject);

        requestCount = (TextView) findViewById(R.id.requestCount);
        relativeLayout =(RelativeLayout) findViewById(R.id.rel);
        approveRejectLayout = (FrameLayout) findViewById(R.id.approveReject);
        DATABASE_REFERENCE = setFireBaseKey("approve_reject");
        DATABASE_REFERENCE.child(getCurrentUser(Notifiy.this)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0) {
                   /* if(!String.valueOf(dataSnapshot.child("request").getValue().toString()).equals("0")){*/
                        relativeLayout.setVisibility(View.VISIBLE);
                        Log.i(LOGCAT,dataSnapshot.getValue().toString());
                        requestCount.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                        return;
                    /*}else{
                        requestCount.setText("0");
                        relativeLayout.setVisibility(View.GONE);
                    }*/
                }else{
                    requestCount.setText("0");
                    relativeLayout.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = 1;
                approveRejectLayout.setVisibility(View.VISIBLE);
                Fragment fr = new ApproveReject();
                FragmentManager fm = getFragmentManager();
                final FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.approveReject, fr).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(count==1){
            approveRejectLayout.setVisibility(View.GONE);
            super.onBackPressed();
            return;
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if(count==1){
            approveRejectLayout.setVisibility(View.GONE);
            super.onBackPressed();
        }else{
            super.onBackPressed();
        }
        return super.onSupportNavigateUp();
    }
}
