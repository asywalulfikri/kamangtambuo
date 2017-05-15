package asywalul.minang.wisatasumbar.adapter;

import asywalul.minang.wisatasumbar.ui.fragment.KulinerFragment;
import asywalul.minang.wisatasumbar.ui.fragment.QuestionFragment;
import asywalul.minang.wisatasumbar.ui.fragment.StoreFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by rio on 14/09/15.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int num) {

        if (num == 0) {
            QuestionFragment qustionFragment = new QuestionFragment();
            return qustionFragment;
        } else if (num == 1) {
            StoreFragment storeFragment = new StoreFragment();
            return storeFragment;
        } else {
            KulinerFragment kulinerFragment = new KulinerFragment();
            return kulinerFragment;
        }
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
