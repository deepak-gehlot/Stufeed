package com.stufeed.frag;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.stufeed.R;


/**
 * Created by sowmitras on 27/7/17.
 */

public class AcademyConnectFragment extends Fragment {

    private Button followBTN;
    private ImageButton edit_profile_details;
    private TextView userRole,fullName,commonProfiler,textView4,userCollegeName;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_profile, container, false);
        edit_profile_details = (ImageButton)view.findViewById(R.id.edit_profile_details);
        followBTN = (Button)view.findViewById(R.id.followBTN);

        userRole = (TextView) view.findViewById(R.id.userRole);
        fullName = (TextView) view.findViewById(R.id.fullName);
        commonProfiler = (TextView) view.findViewById(R.id.commonProfiler);
        textView4 = (TextView) view.findViewById(R.id.textView4);
        userCollegeName = (TextView) view.findViewById(R.id.userCollegeName);

        followBTN.setVisibility(View.VISIBLE);
        userCollegeName.setVisibility(View.GONE);
        textView4.setVisibility(View.GONE);
        commonProfiler.setVisibility(View.GONE);
        userRole.setVisibility(View.GONE);
        fullName.setVisibility(View.GONE);
        edit_profile_details.setVisibility(View.GONE);
        return view;
    }
}