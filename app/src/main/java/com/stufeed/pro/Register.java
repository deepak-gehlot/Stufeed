package com.stufeed.pro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stufeed.R;
import com.stufeed.pojo.CreateAccount;
import com.stufeed.pojo.User;

import java.util.ArrayList;

import static com.stufeed.utility.SowmitrasMethod.*;
import static com.stufeed.utility.SowmitrasStrings.*;
import static com.stufeed.utility.LocVari.*;

/**
 * Created by sowmitras on 24-10-2017.
 */

public class Register extends AppCompatActivity implements View.OnClickListener{

    private Spinner spinner;
    private Button submit;
    private EditText email,password, fullName, contact, userName;
    private String emailStr, passwordStr, fullNameStr, contactStr, roleStr, userNameStr;

    private ProgressDialog progressDialog;
    private ProgressBar progressBar;

    private ArrayAdapter<String> categoryAdapter;

    private ArrayList<String> userList;

    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mFirebaseAuth;
    private AdView mAdView;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private int checkPostion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableDisableAnalytics(this);
        setContentView(R.layout.register_activity);
        progressBar = setProgressBar(this, R.id.progressBarAds);
        mAdView = showAds(this, R.id.mainAd,progressBar);

        email = (EditText) findViewById(R.id.emailEditText);
        password = (EditText) findViewById(R.id.signUpPassword);
        fullName = (EditText) findViewById(R.id.fullName);
        contact = (EditText) findViewById(R.id.contactNoEt);
        userName = (EditText) findViewById(R.id.userNameEt);

        spinner = (Spinner) findViewById(R.id.profileSpinner);

        submit = (Button) findViewById(R.id.registerSubmitButton);

        progressDialog = loadProgressDialog(Register.this, LOADING, CENTER_DISPLAY);


        categoryAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, USER_ROLE);
        spinner.setAdapter(categoryAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                roleStr = USER_ROLE[position].toLowerCase();
                hideKeyBoard(spinner, Register.this);
                checkPostion = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        submit.setOnClickListener(this);
        sharedPref = getSharedPreferences(CST_SHARED_REFERENCE, Activity.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerSubmitButton:
                mFirebaseAuth = FirebaseAuth.getInstance();
                userNameStr = userName.getText().toString().trim();
                fullNameStr = fullName.getText().toString();
                emailStr = email.getText().toString().trim();
                passwordStr = password.getText().toString().trim();
                contactStr = contact.getText().toString().trim();

                if (TextUtils.isEmpty(emailStr)) {
                    showSnackBar(email, ENTER_EMAIL_ADDRESS, "");
                    return;
                }
                if (TextUtils.isEmpty(passwordStr)) {
                    showSnackBar(email, ENTER_PASSWORD, "");
                    return;
                }
                if (passwordStr.length() < 6) {
                    showSnackBar(email, PASSWORD_TOO_SHORT, "");
                    return;
                }
                if (TextUtils.isEmpty(fullNameStr)) {
                    showSnackBar(fullName,getResources().getString(R.string.enter_user_name),"");
                    return;
                }
                if (TextUtils.isEmpty(contactStr)) {
                    showSnackBar(contact,getResources().getString(R.string.contact),"");
                    return;
                }

                if (TextUtils.isEmpty(userNameStr)) {
                    showSnackBar(userName,getResources().getString(R.string.enter_user_name),"");
                    return;
                }

                if(roleStr.equals("--Who Are You--")){
                    showSnackBar(userName,getResources().getString(R.string.select_role),"");
                    return;
                }

                if(checkPostion!=0){
                    progressDialog.setMessage("Registering your profile");
                    progressDialog.show();
                    hideKeyBoard(submit, Register.this);
                    checkUserValidation();
                }else{
                    spinner.performClick();
                    showSnackBar(spinner, "Please Select Your Role","");
                }

                break;
        }
    }

    private void checkUserValidation() {
        progressDialog.setMessage("Authenticate Your Login");
        mFirebaseAuth.createUserWithEmailAndPassword(emailStr, passwordStr)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            showSnackBar(submit,"Email already in use...","");
                            progressDialog.dismiss();
                        } else {
                            progressDialog.setMessage("Checking username availability");
                            getUsers();
                        }
                    }
                });
    }

    private void getUsers() {
        userList = new ArrayList<String>();
        DATABASE_REFERENCE = setFireBaseKey(CST_ACCOUNTS);
        DATABASE_REFERENCE.orderByChild(CST_USER_NAME).equalTo(userNameStr).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count  = (int) dataSnapshot.getChildrenCount();
                if (count == 0) {
                    createUserProfile();
                }else{
                    userName.requestFocus();
                    userName.setError("User Name Already In user");
                    deleteEmail();
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void createUserProfile() {
        CreateAccount studentDetails = new CreateAccount(emailStr,userNameStr,contactStr,roleStr,"","");
        DATABASE_REFERENCE = setFireBaseKey(ACCOUNTS);
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        DATABASE_REFERENCE.child(mFirebaseUser.getUid()).setValue(studentDetails);
        User user = new User(mFirebaseUser.getUid(),IMAGE_URL_CONSTANT,fullNameStr,
                CST_PROGRAM,CST_BRANCH,
                CST_GRADUATION_YEAR,CST_COLLEGE,
                CST_ABOUT_STATUS, CST_GENDER,
                contactStr, emailStr,
                CST_BIRTHDAY, roleStr,
                CST_SEARCH_USER,CST_DEGREE,
                CST_DESIGNATION,CST_DEPARTMENT,CST_FACULTY,"0",0);

        saveUserDetails(user,userNameStr);
        boolean b = setToLocalDataBase(editor, sharedPref, COMMON_FLAG, SELECT_COLLEGE);
        boolean b2 = setToLocalDataBase(editor, sharedPref, CST_USER_NAME,userNameStr);
        boolean b3 = setToLocalDataBase(editor,sharedPref, CST_ROLE,roleStr);
        boolean b4 = setToLocalDataBase(editor,sharedPref, CST_EMAIL_ID,emailStr);
        boolean b5 = setToLocalDataBase(editor,sharedPref, CST_CONTACT_NUMBER,contactStr);
        boolean b6 = setToLocalDataBase(editor,sharedPref, PASSWORD,passwordStr);
        boolean b7 = setToLocalDataBase(editor,sharedPref, CST_USER_FULL_NAME,fullNameStr);
        boolean b8 = setToLocalDataBase(editor,sharedPref, CST_ACCOUNT_KEY,mFirebaseUser.getUid());
        boolean b9 = setToLocalDataBase(editor,sharedPref, CST_USER_IMAGE,IMAGE_URL_CONSTANT);

        if(b & b2 & b3 & b4 & b5 & b6 & b7 & b8 & b9){
            progressDialog.dismiss();

            goToNext(this, COLLEGE_APPROVAL_SCREEN,true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAffinity();
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToNext(Register.this, LOGIN_SCREEN,true);
    }

    public void alreadyRegistered(View view){
        onBackPressed();
    }
}