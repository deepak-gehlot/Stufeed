package com.stufeed.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.stufeed.frag.JoinBoardFrag;
import com.stufeed.frag.ViewBoardMembers;
import com.stufeed.frag.ViewBoardPosts;
import com.stufeed.pojo.JoinBoard;


/**
 * Created by sowmitras on 6/12/17.
 */

public class ViewBoardPager extends FragmentStatePagerAdapter {

    int tabCount;

    //Constructor to the class
    public ViewBoardPager(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 1:
                ViewBoardMembers viewBoardMembers = new ViewBoardMembers();
                return viewBoardMembers;
            case 0:
                ViewBoardPosts viewBoardPosts = new ViewBoardPosts();
                return viewBoardPosts;
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
