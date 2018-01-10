package com.stufeed.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.stufeed.frag.CreateBoardFrag;
import com.stufeed.frag.JoinBoardFrag;

/**
 * Created by sowmitras on 3/11/17.
 */

public class BoardPager extends FragmentStatePagerAdapter {

    int tabCount;

    public BoardPager(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                CreateBoardFrag createBoardFragment = new CreateBoardFrag();
                return createBoardFragment;
            case 1:
                JoinBoardFrag joinBoardFragment = new JoinBoardFrag();
                return joinBoardFragment;
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
