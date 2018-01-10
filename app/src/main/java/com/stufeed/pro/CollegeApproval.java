package com.stufeed.pro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stufeed.R;
import com.stufeed.pojo.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.stufeed.utility.SowmitrasStrings.*;
import static com.stufeed.utility.SowmitrasMethod.*;
import static com.stufeed.utility.LocVari.*;

/**
 * Created by sowmitras on 24-10-2017.
 */


public class CollegeApproval extends AppCompatActivity {

    private AdView mAdView;
    private Button proceed;
    private ArrayList<String> instituteList = new ArrayList<String>();
    private ArrayAdapter<String> arrayAdapter;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;
    private ProgressBar progressBar;

    private ListView filterList;
    private LinearLayout main;

    private String firebaseId;
    private EditText enterPin, upperEdit;
    private TextView collegeName, search_college;
    private int pos;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private boolean doubleBackToExitPressedOnce = false;

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableDisableAnalytics(this);
        setContentView(R.layout.college_approval);
        progressDialog = loadProgressDialog(this, LOADING_INSTITUTE_LIST,CENTER_DISPLAY);
        progressDialog.show();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.collegeApproval);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        sharedPref = CollegeApproval.this.getSharedPreferences(CST_SHARED_REFERENCE, Activity.MODE_PRIVATE);
        editor = sharedPref.edit();

        progressBar = setProgressBar(this, R.id.progressBarAds);
        mAdView = showAds(this, R.id.mainAd,progressBar);


        enterPin = (EditText) findViewById(R.id.enterPin);
        upperEdit = (EditText) findViewById(R.id.upperEdit);



        main = (LinearLayout) findViewById(R.id.main);
        filterList = (ListView) findViewById(R.id.list_item);
        collegeName = (TextView) findViewById(R.id.collegeName);

        DATABASE_REFERENCE = setFireBaseKey(ACCOUNTS);
        DATABASE_REFERENCE.orderByChild(CST_USER_NAME).equalTo(getFromLocalDataBase(sharedPref,CST_USER_NAME)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    firebaseId = snapshot.getKey();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        search_college = (TextView) findViewById(R.id.searchCollege);
        proceed = (Button) findViewById(R.id.proceedButton);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                // gotoBoard();
                final String tempAffliation = enterPin.getText().toString();
                DATABASE_REFERENCE = setFireBaseKey("institutes");
                DATABASE_REFERENCE.child(String.valueOf(pos)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.getChildrenCount()!=0){
                            if(dataSnapshot.child("affiliation_no").getValue().toString().equals(tempAffliation)){
                                progressDialog.dismiss();
                                showSnackBar(proceed,"Your Account is Verified","");
                                hideKeyBoard(proceed,CollegeApproval.this);
                                userCollegeVerification();
                                proceed.setText("Success");
                                proceed.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                            }else{
                                hideKeyBoard(proceed,CollegeApproval.this);
                                progressDialog.dismiss();
                                showSnackBar(proceed,"Invalid Code Try Again","");
                            }
                        }else
                        {
                           // Log.i("data not found" ,":'(");
                        }

                        progressDialog.dismiss();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        getCollegeList();


        search_college.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upperEdit.setVisibility(View.VISIBLE);
                upperEdit.performClick();
                search_college.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            }
        });

        upperEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                arrayAdapter = new ArrayAdapter<String>(CollegeApproval.this, R.layout.college_item, instituteList);
                main.setVisibility(View.GONE);
                filterList.setVisibility(View.VISIBLE);
                CollegeApproval.this.arrayAdapter.getFilter().filter(charSequence);
                filterList.setAdapter(arrayAdapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        filterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                collegeName.setText(arrayAdapter.getItem(i));
                search_college.setText(arrayAdapter.getItem(i));
                //showToast(CollegeApproval.this, String.valueOf(arrayAdapter.getPosition(collegeName.getText().toString())));
                filterList.setVisibility(View.GONE);
                main.setVisibility(View.VISIBLE);
                search_college.setVisibility(View.VISIBLE);
                upperEdit.setVisibility(View.GONE);
                search_college.clearFocus();
                hideKeyBoard(upperEdit,CollegeApproval.this);
            }
        });
    }



    private void userCollegeVerification() {
        DATABASE_REFERENCE = setFireBaseKey("verifiedusers");
        Map<String, Object> taskMap = setMapValue(firebaseId, getFromLocalDataBase(sharedPref,CST_USER_NAME));
        DATABASE_REFERENCE.child(getFromLocalDataBase(sharedPref,CST_USER_NAME)).setValue(taskMap);
    }

    private void gotoBoard() {

        String selected_college = collegeName.getText().toString();

        progressDialog.setMessage("Creating you profile");
        progressDialog.show();
        DATABASE_REFERENCE = setFireBaseKey(CST_ACCOUNTS);
        final Map<String, Object> taskMap = setMapValue(CST_COLLEGE,selected_college);
        final Map<String, Object> taskMap2 = setMapValue(CST_CG_ID,String.valueOf(pos));

        DATABASE_REFERENCE.child(firebaseId).updateChildren(taskMap);
        DATABASE_REFERENCE.child(firebaseId).updateChildren(taskMap2);


        final Map<String, Object> taskMap3 = setMapValue(USER_ID,getFromLocalDataBase(sharedPref, CST_USER_NAME));

        DATABASE_REFERENCE = setFireBaseKey(String.valueOf(pos));
        DATABASE_REFERENCE.child(getFromLocalDataBase(sharedPref,CST_ROLE)).child(firebaseUser.getUid()).setValue(taskMap3);

        User user = new User(firebaseUser.getUid(),IMAGE_URL_CONSTANT,getFromLocalDataBase(sharedPref,CST_USER_FULL_NAME),
                CST_PROGRAM,CST_BRANCH,
                CST_GRADUATION_YEAR,selected_college,
                CST_ABOUT_STATUS, CST_GENDER,
                getFromLocalDataBase(sharedPref,CST_CONTACT_NUMBER), getFromLocalDataBase(sharedPref,CST_EMAIL_ID),
                CST_BIRTHDAY, getFromLocalDataBase(sharedPref,CST_ROLE),
                CST_SEARCH_USER,CST_DEGREE,
                CST_DESIGNATION,CST_DEPARTMENT, CST_FACULTY,"0",0);
        saveUserDetails(user,getFromLocalDataBase(sharedPref, CST_USER_NAME));

        setToLocalDataBase(editor,sharedPref,CST_COLLEGE, collegeName.getText().toString());
        setToLocalDataBase(editor,sharedPref, CST_CG_ID, String.valueOf(pos));

        firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    firebaseAuth.signOut();
                    logOut(editor,COMMON_FLAG, REGISTERED, 0);
                    progressDialog.dismiss();
                    goToNext(CollegeApproval.this, GET_STARTED_SCREEN,true);
                }else{

                }
            }
        });
    }

    private void getCollegeList() {
        DATABASE_REFERENCE = setFireBaseKey("institutes");
        instituteList.clear();
        DATABASE_REFERENCE.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()!=0){
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        instituteList.add(""+snapshot.child("college_name").getValue().toString());
                    }
                    progressDialog.dismiss();
                }else{
                    //Log.i("adata not found", "ssssss");
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.skip, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.skip) {
            hideKeyBoard(proceed, this);
            if(collegeName.getText().toString().equals("Select College")){
                showSnackBar(collegeName, "Please select Your College","");
                hideKeyBoard(collegeName,CollegeApproval.this);
                progressDialog.dismiss();
            }else{
                //gotoBoard();
                int co=0;
                for(String s:instituteList){
                    if(collegeName.getText().toString().equals(s)){
                       pos = co;
                       gotoBoard();
                    }else{

                    }
                    co++;
                }

            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        main.setVisibility(View.VISIBLE);
        filterList.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 500);
    }
}
/*
public class CollegeApproval extends AppCompatActivity {

    private AdView mAdView;
    private Button proceed;
    private ArrayList<String> instituteList = new ArrayList<String>();
    private ArrayAdapter<String> arrayAdapter;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;
    private ProgressBar progressBar;

    private ListView filterList;
    private LinearLayout main;

    private String firebaseId;
    private EditText enterPin, search_college;
    private TextView collegeName;
    private int pos;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableDisableAnalytics(this);
        setContentView(R.layout.college_approval);
        progressDialog = loadProgressDialog(this, LOADING_INSTITUTE_LIST,CENTER_DISPLAY);
        progressDialog.show();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        setTitle(getResources().getString(R.string.collegeApproval));
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        sharedPref = CollegeApproval.this.getSharedPreferences(CST_SHARED_REFERENCE, Activity.MODE_PRIVATE);
        editor = sharedPref.edit();

        progressBar = setProgressBar(this, R.id.progressBarAds);
        mAdView = showAds(this, R.id.mainAd,progressBar);


        enterPin = (EditText) findViewById(R.id.enterPin);
        main = (LinearLayout) findViewById(R.id.main);
        filterList = (ListView) findViewById(R.id.list_item);
        collegeName = (TextView) findViewById(R.id.collegeName);

        DATABASE_REFERENCE = setFireBaseKey(ACCOUNTS);
        DATABASE_REFERENCE.orderByChild(CST_USER_NAME).equalTo(getFromLocalDataBase(sharedPref,CST_USER_NAME)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    firebaseId = snapshot.getKey();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        search_college = (EditText) findViewById(R.id.searchCollege);
        proceed = (Button) findViewById(R.id.proceedButton);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                // gotoBoard();
                final String tempAffliation = enterPin.getText().toString();

                DATABASE_REFERENCE = setFireBaseKey("institutes");
                DATABASE_REFERENCE.child(String.valueOf(pos)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.getChildrenCount()!=0){
                            if(dataSnapshot.child("affiliation_no").getValue().toString().equals(tempAffliation)){
                                progressDialog.dismiss();
                                showSnackBar(proceed,"Your Account is Verified","");
                                hideKeyBoard(proceed,CollegeApproval.this);
                                userCollegeVerification();
                                proceed.setText("Success");
                                proceed.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                            }else{
                                hideKeyBoard(proceed,CollegeApproval.this);
                                progressDialog.dismiss();
                                showSnackBar(proceed,"Invalid Code Try Again","");
                            }
                        }else
                        {
                            Log.i("data not found" ,":'(");
                        }

                        progressDialog.dismiss();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        getCollegeList();
        search_college.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                DATABASE_REFERENCE =setFireBaseKey("institutes");
                instituteList.clear();
                DATABASE_REFERENCE.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            instituteList.add(""+snapshot.child("college_name").getValue().toString());
                        }

                        arrayAdapter = new ArrayAdapter<String>(CollegeApproval.this, R.layout.college_item, instituteList);
                        main.setVisibility(View.GONE);
                        filterList.setVisibility(View.VISIBLE);
                        CollegeApproval.this.arrayAdapter.getFilter().filter(s);
                        filterList.setAdapter(arrayAdapter);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        filterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                collegeName.setText(arrayAdapter.getItem(i));
                search_college.setText(arrayAdapter.getItem(i));
                main.setVisibility(View.VISIBLE);
                filterList.setVisibility(View.GONE);
            }
        });
    }

    private void userCollegeVerification() {
        DATABASE_REFERENCE = setFireBaseKey("verifiedusers");
        Map<String, Object> taskMap = setMapValue(firebaseId, getFromLocalDataBase(sharedPref,CST_USER_NAME));
        DATABASE_REFERENCE.child(getFromLocalDataBase(sharedPref,CST_USER_NAME)).setValue(taskMap);
    }

    private void gotoBoard() {

        String selected_college = collegeName.getText().toString();

        progressDialog.setMessage("Creating you profile");
        progressDialog.show();
        DATABASE_REFERENCE = setFireBaseKey(CST_ACCOUNTS);
        final Map<String, Object> taskMap = setMapValue(CST_COLLEGE,selected_college);
        final Map<String, Object> taskMap2 = setMapValue(CST_CG_ID,String.valueOf(pos));

        DATABASE_REFERENCE.child(firebaseId).updateChildren(taskMap);
        DATABASE_REFERENCE.child(firebaseId).updateChildren(taskMap2);


        final Map<String, Object> taskMap3 = setMapValue(USER_ID,getFromLocalDataBase(sharedPref, CST_USER_NAME));

        DATABASE_REFERENCE = setFireBaseKey(String.valueOf(pos));
        DATABASE_REFERENCE.child(getFromLocalDataBase(sharedPref,CST_ROLE)).child(firebaseUser.getUid()).setValue(taskMap3);

        User user = new User(firebaseUser.getUid(),IMAGE_URL_CONSTANT,getFromLocalDataBase(sharedPref,CST_USER_FULL_NAME),
                CST_PROGRAM,CST_BRANCH,
                CST_GRADUATION_YEAR,selected_college,
                CST_ABOUT_STATUS, CST_GENDER,
                getFromLocalDataBase(sharedPref,CST_CONTACT_NUMBER), getFromLocalDataBase(sharedPref,CST_EMAIL_ID),
                CST_BIRTHDAY, getFromLocalDataBase(sharedPref,CST_ROLE),
                CST_SEARCH_USER,CST_DEGREE,
                CST_DESIGNATION,CST_DEPARTMENT, CST_FACULTY,"0",0);
        saveUserDetails(user,getFromLocalDataBase(sharedPref, CST_USER_NAME));

        setToLocalDataBase(editor,sharedPref,CST_COLLEGE, collegeName.getText().toString());
        setToLocalDataBase(editor,sharedPref, CST_CG_ID, String.valueOf(pos));

        firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    firebaseAuth.signOut();
                    logOut(editor,COMMON_FLAG, REGISTERED, 0);
                    progressDialog.dismiss();
                    goToNext(CollegeApproval.this, GET_STARTED_SCREEN,true);
                }else{

                }
            }
        });
    }

    private void getCollegeList() {
        DATABASE_REFERENCE = setFireBaseKey("institutes");
        instituteList.clear();
        DATABASE_REFERENCE.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()!=0){
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        instituteList.add(""+snapshot.child("college_name").getValue().toString());
                    }
                    progressDialog.dismiss();
                }else{
                    Log.i("adata not found", "ssssss");
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.skip, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.skip) {
            hideKeyBoard(proceed, this);
            if(collegeName.getText().toString().equals("Select College")){
                showSnackBar(collegeName, "Please select Your College","");
                hideKeyBoard(collegeName,CollegeApproval.this);
                progressDialog.dismiss();
            }else{
                gotoBoard();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        main.setVisibility(View.VISIBLE);
    }
}*/
