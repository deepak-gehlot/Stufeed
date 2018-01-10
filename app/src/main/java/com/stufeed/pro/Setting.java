package com.stufeed.pro;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.stufeed.R;

import java.util.HashMap;
import java.util.Map;

import static com.stufeed.utility.SowmitrasMethod.*;
import static com.stufeed.utility.SowmitrasStrings.*;
import static com.stufeed.utility.LocVari.*;

/**
 * Created by sowmitras on 27/11/17.
 */

public class Setting extends AppCompatActivity implements View.OnClickListener {
    private Switch seo, sound, notification;
    private ImageButton delete;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableDisableAnalytics(this);
        setContentView(R.layout.activity_setting);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        sharedPref = setSharedPreference(this, CST_SHARED_REFERENCE);
        editor = sharedPref.edit();
        setView();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        toolbar = (Toolbar) findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        setTitle(getResources().getString(R.string.action_settings));
        seo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    updateSEO("search", "1", getCurrentUser(Setting.this));
                }else{
                    updateSEO("search", "0", getCurrentUser(Setting.this));
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.delete_btn:
                final Dialog board_dialog = new Dialog(this);
                board_dialog.setContentView(R.layout.delete_profile);
                board_dialog.show();
                Button cancelbtn = (Button) board_dialog.findViewById(R.id.cancel);
                Button deleteBtn = (Button) board_dialog.findViewById(R.id.delete);
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressDialog.setMessage("Account Deletion In Progress Please Wait....");
                        progressDialog.show();
                        DATABASE_REFERENCE = null;
                        DATABASE_REFERENCE = FirebaseDatabase.getInstance().getReference("userslist");
                        DATABASE_REFERENCE.child(getCurrentUser(Setting.this)).removeValue();

                        DATABASE_REFERENCE = null;
                        DATABASE_REFERENCE = FirebaseDatabase.getInstance().getReference(getFromLocalDataBase(sharedPref,CST_CG_ID));
                        DATABASE_REFERENCE.child(getFromLocalDataBase(sharedPref,CST_ROLE)).child(currentUser.getUid()).removeValue();


                        currentUser.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(LOGCAT, "User account deleted.");
                                        }
                                    }
                                });
                        mAuth.signOut();
                        editor = sharedPref.edit();
                        editor.clear();
                        editor.commit();

                        goToNext(Setting.this,SPLASH_SCREEN,true);
                    }
                });
                cancelbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        board_dialog.dismiss();
                        //delete.setChecked(false);
                    }
                });
                break;
        }
    }

    private void setView() {
        seo = (Switch) findViewById(R.id.switch1);
        sound = (Switch) findViewById(R.id.switch2);
        notification = (Switch) findViewById(R.id.switch3);
        delete = (ImageButton) findViewById(R.id.delete_btn);
        delete.setOnClickListener(this);
        //delete.setChecked(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save) {
            if(seo.isChecked()&& sound.isChecked() && notification.isChecked()){
                updateSEO("search", "1", getCurrentUser(Setting.this));

            }else{

            }
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateSEO(String search, String searchValue, String username){
        DATABASE_REFERENCE = null;
        DATABASE_REFERENCE = FirebaseDatabase.getInstance().getReference("userslist");
        Map<String, Object> kvMap = new HashMap<String, Object>();
        kvMap.put(search, searchValue);
        DATABASE_REFERENCE.child(username).updateChildren(kvMap);
    }
}
