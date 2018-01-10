package com.stufeed.utility;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.stufeed.R;

import static com.stufeed.utility.SowmitrasMethod.setProgressBar;
import static com.stufeed.utility.SowmitrasMethod.showAds;

/**
 * Created by sowmitras on 3/11/17.
 */

public class SowmitrasDialog { /*extends Dialog implements View.OnClickListener  {

    public Dialog dialog;
    public Activity activity;
    public ImageButton positive,neutral,negative;

    public SowmitrasDialog(@NonNull Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.attach_dialog_layout);
        positive = (ImageButton) findViewById(R.id.positive);
        neutral = (ImageButton) findViewById(R.id.neutral);
        negative = (ImageButton) findViewById(R.id.negative);
        positive.setOnClickListener(this);
        neutral.setOnClickListener(this);
        negative.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.positive:
                activity.finish();
                break;
            case R.id.neutral:
                dismiss();
                break;
            case R.id.negative:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }*/
}
