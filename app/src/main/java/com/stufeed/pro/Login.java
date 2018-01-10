package com.stufeed.pro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stufeed.R;
import com.stufeed.utility.boardcast.SowmitrasReceiver;

import static com.stufeed.utility.LocVari.*;
import static com.stufeed.utility.SowmitrasMethod.*;
import static com.stufeed.utility.SowmitrasStrings.*;

/**
 * Created by sowmitras on 24-10-2017.
 */


public class Login extends AppCompatActivity implements View.OnClickListener{

    private Button loginBtn, registerBtn, instituteRegisterBtn,btn_reset_password;
    private EditText emailEditText, passwordEditText,emailText;
    private TextView forget_password;
    private ProgressDialog progressDialog;
    private ProgressBar progressBar;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private LinearLayout  login_screen;
    private RelativeLayout  forget_screen;

    private SowmitrasReceiver sowmitrasReceiver;
    private IntentFilter intentFilter;

    private FirebaseAuth mFireBaseAuth;
    private FirebaseUser mFireBaseUser;

    private AdView mAdView;
    private boolean backPress = false;
    private Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableDisableAnalytics(this);
        setContentView(R.layout.login_activity);
        progressBar = setProgressBar(this, R.id.progressBarAds);
        mAdView = showAds(this, R.id.mainAd, progressBar);
        mFireBaseAuth= FirebaseAuth.getInstance();
        sharedPref = getSharedPreferences(CST_SHARED_REFERENCE, Activity.MODE_PRIVATE);
        editor = sharedPref.edit();
        loginBtn = (Button) findViewById(R.id.loginButton);
        btn_reset_password = (Button) findViewById(R.id.btn_reset_password);
        btn_reset_password.setOnClickListener(this);
        registerBtn = (Button) findViewById(R.id.loginRegisterButton);

        instituteRegisterBtn = (Button) findViewById(R.id.register_institute_btn);
        instituteRegisterBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.loginPassword);

        emailText = (EditText) findViewById(R.id.email);

        forget_password = (TextView) findViewById(R.id.forget_password);
        forget_password.setOnClickListener(this);
        progressDialog = loadProgressDialog(this, LOADING, CENTER_DISPLAY);

        login_screen = (LinearLayout) findViewById(R.id.login_screen);
        forget_screen = (RelativeLayout) findViewById(R.id.forget_screen);


        toolbar = (Toolbar) findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.GONE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if(checkLoginStatus(mFireBaseAuth, mFireBaseUser)){
        }else{
            mFireBaseUser = mFireBaseAuth.getCurrentUser();
            if(!mFireBaseUser.getEmail().equals(REGISTER_EMAIL)) {
                if (checkIfEmailVerified(mFireBaseAuth, mFireBaseUser)) {
                    getUserDetails(mFireBaseAuth, mFireBaseUser, progressDialog, loginBtn, sharedPref, editor, getFromLocalDataBase(sharedPref, PASSWORD), Login.this);
                } else {
                    goToNext(Login.this, GET_STARTED_SCREEN, true);
                }
            }else{
                mFireBaseAuth.signOut();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.loginButton:
                final String email = emailEditText.getText().toString();
                final String password = passwordEditText.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    showSnackBar(emailEditText, ENTER_EMAIL_ADDRESS,"");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    showSnackBar(emailEditText, ENTER_PASSWORD,"");
                    return;
                }
                progressDialog.show();
                hideKeyBoard(loginBtn, Login.this);
                mFireBaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    if (password.length() < 6) {
                                        passwordEditText.setError(getString(R.string.minimum_password));
                                    } else {
                                        showSnackBar(loginBtn, getResources().getString(R.string.auth_failed),"");
                                    }
                                } else {
                                    sowmitrasReceiver = new SowmitrasReceiver();
                                    intentFilter = new IntentFilter("USER_UID");
                                    boolean b1 = setToLocalDataBase(editor,sharedPref, PASSWORD,password);
                                    boolean b2 = setToLocalDataBase(editor,sharedPref, CST_EMAIL_ID,email);
                                    if(checkIfEmailVerified(mFireBaseAuth, mFireBaseUser)){
                                        mFireBaseUser = mFireBaseAuth.getCurrentUser();
                                        getUserDetails(mFireBaseAuth,mFireBaseUser,progressDialog,loginBtn,sharedPref,editor, password,Login.this);
                                    }else{
                                        goToNext(Login.this, GET_STARTED_SCREEN,true);
                                    }
                                }
                            }
                        });
                break;
            case R.id.loginRegisterButton:
                goToNext(Login.this, REGISTER_SCREEN, true);
                break;
            case R.id.forget_password:
                toolbar.setVisibility(View.VISIBLE);
                backPress = true;
                login_screen.setVisibility(View.GONE);
                forget_screen.setVisibility(View.VISIBLE);
                break;
            case R.id.register_institute_btn:
                goToNext(this, INSTITUTE_REGISTRATION,true);
                break;
            case R.id.btn_reset_password:
                if(!TextUtils.isEmpty(emailText.getText())){
                    mFireBaseAuth.sendPasswordResetEmail(String.valueOf(emailText.getText()))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        showSnackBar(btn_reset_password,"Email Successfully Sent", "");
                                        login_screen.setVisibility(View.VISIBLE);
                                        forget_screen.setVisibility(View.GONE);
                                        toolbar.setVisibility(View.GONE);
                                    }else{
                                        showSnackBar(btn_reset_password,"Invalid Email Not Successfully Sent", "");
                                    }
                                }
                            });
                }else{
                    emailText.setError("Please Enter Email Address");
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(backPress){
            toolbar.setVisibility(View.GONE);
            login_screen.setVisibility(View.VISIBLE);
            forget_screen.setVisibility(View.GONE);
        }else{
            super.onBackPressed();
        }

    }
}