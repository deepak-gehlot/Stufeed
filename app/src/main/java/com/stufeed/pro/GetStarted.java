package com.stufeed.pro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stufeed.R;

import static com.stufeed.utility.SowmitrasStrings.*;
import static com.stufeed.utility.SowmitrasMethod.*;
import static com.stufeed.utility.LocVari.*;

/**
 * Created by sowmitras on 25-10-2017.
 */

public class GetStarted extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;
    private ProgressBar progressBar;
    private AdView mAdView;
    private SharedPreferences sharedPref;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private Button retryBtn;
    private String emails, temp_pass, check;
    private boolean b;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableDisableAnalytics(this);
        setContentView(R.layout.get_started);
        progressBar = setProgressBar(this, R.id.progressBarAds);
        mAdView = showAds(this, R.id.mainAd,progressBar);
        progressDialog = loadProgressDialog(GetStarted.this, LOADING, CENTER_DISPLAY);
        progressDialog.show();
        check = "y";
        sharedPref = this.getSharedPreferences(CST_SHARED_REFERENCE, Activity.MODE_PRIVATE);
        emails = sharedPref.getString(CST_EMAIL_ID, "");
        temp_pass = sharedPref.getString(PASSWORD, "");
        FirebaseAuth mFireBase = FirebaseAuth.getInstance();
        mFireBase.signInWithEmailAndPassword(emails, temp_pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {

                        } else {
                            mFirebaseAuth = FirebaseAuth.getInstance();
                            mFirebaseUser = mFirebaseAuth.getCurrentUser();
                            sharedPref = getSharedPreferences(CST_SHARED_REFERENCE, Activity.MODE_PRIVATE);
                            editor = sharedPref.edit();
                            retryBtn = (Button) findViewById(R.id.retryBtn);
                            retryBtn.setOnClickListener(GetStarted.this);
                            b = checkIfEmailVerified(mFirebaseAuth, mFirebaseUser);
                            if(b){
                                retryBtn.setText("Get Started");
                                progressDialog.dismiss();
                            }else{
                                retryBtn.setText("Please Verify Account");
                                progressDialog.dismiss();
                            }
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.retryBtn:
                progressDialog.show();
                if (!checkIfEmailVerified(mFirebaseAuth,mFirebaseUser)) {
                    retryBtn.setText("Lets Start");
                    getUserDetails(mFirebaseAuth, mFirebaseUser, progressDialog, retryBtn, sharedPref, editor, getFromLocalDataBase(sharedPref, PASSWORD), GetStarted.this);
                    return;
                }
                progressDialog.dismiss();
                break;
        }
    }
}