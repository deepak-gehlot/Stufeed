package com.stufeed.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.stufeed.frag.AcademyConnectFragment;
import com.stufeed.frag.DepartmentConnectFragment;
import com.stufeed.frag.FacultyConnectFragment;
import com.stufeed.frag.StudentConnectFragment;

/**
 * Created by sowmitras on 27/7/17.
 */

public class ConnectPager extends FragmentStatePagerAdapter {

    int tabCount;

    //Constructor to the class
    public ConnectPager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 1:
                FacultyConnectFragment facultyConnect = new FacultyConnectFragment();
                return facultyConnect;
            case 2:
                StudentConnectFragment studentConnect = new StudentConnectFragment();
                return studentConnect;
            case 3:
                DepartmentConnectFragment departmentConnect = new DepartmentConnectFragment();
                return departmentConnect;
            case 0:
                AcademyConnectFragment academyConnectFragment = new AcademyConnectFragment();
                return academyConnectFragment;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}
