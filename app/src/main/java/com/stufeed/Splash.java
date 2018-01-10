package com.stufeed;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;

import com.stufeed.utility.ScrollDetective;
import com.stufeed.utility.services.ExampleService;

import static com.stufeed.utility.SowmitrasMethod.*;
import static com.stufeed.utility.SowmitrasStrings.*;

/**
 * Created by sowmitras on 8/6/17.
 */

public class Splash extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String identifier;
    private Boolean alert;
    private Button accept, cancel;
    private CheckBox readed_agreement;
    private ScrollView scrollView;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        //startService(new Intent(getBaseContext(), ExampleService.class));

        sharedPreferences = getSharedPreferences(CST_POST_REFERENCE_KEY, Context.MODE_PRIVATE);
        alert = sharedPreferences.getBoolean("dialog", false);
        editor = sharedPreferences.edit();
        if(!alert){
            final Dialog board_dialog = new Dialog(this);
            board_dialog.setContentView(R.layout.eula_agreement);
            board_dialog.show();
            board_dialog.setCancelable(false);
            readed_agreement = (CheckBox)board_dialog.findViewById(R.id.readed_agreement);
            accept = (Button) board_dialog.findViewById(R.id.accept);
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(readed_agreement.isChecked()){
                        editor = editor.putBoolean("dialog",true);
                        editor.commit();
                        board_dialog.dismiss();
                        gotoNext();
                    }else{
                    }


                }
            });
            cancel = (Button) board_dialog.findViewById(R.id.cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert = sharedPreferences.getBoolean("dialog", false);
                    board_dialog.dismiss();
                    finish();
                }
            });

        }else{
            gotoNext();
        }
        //gotoNext();
    }
    private void gotoNext(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sharedPreferences = Splash.this.getSharedPreferences(CST_SHARED_REFERENCE, Activity.MODE_PRIVATE);
                //identifier = sharedPreferences.getString(COMMON_FLAG, "");
                identifier = getFromLocalDataBase(sharedPreferences, COMMON_FLAG);
                switch (identifier){
                    case LOGIN_FLAG:
                        break;
                    case SELECT_COLLEGE:
                        goToNext(Splash.this, COLLEGE_APPROVAL_SCREEN,true);
                        break;
                    case MY_PROFILE_FLAG:
                        goToNext(Splash.this, BOARD_SCREEN,true);
                        break;
                    case REGISTERED:
                        goToNext(Splash.this, GET_STARTED_SCREEN,true);
                        break;
                    case USER_PROFILE_DETAILS:
                        break;
                    case CST_INSTITUTES:
                        goToNext(Splash.this, EDIT_PROFILE, true);
                        break;
                    default:
                        goToNext(Splash.this, LOGIN_SCREEN,true);
                        break;
                }
            }
        },getResources().getInteger(R.integer.delay_timer));
    }
}