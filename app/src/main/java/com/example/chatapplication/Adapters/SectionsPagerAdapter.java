package com.example.chatapplication.Adapters;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.chatapplication.Fragments.Chats;
import com.example.chatapplication.Fragments.ProfileFragment;
import com.example.chatapplication.Fragments.Users;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] childFragments;
    private String[] titles = {"Chats", "Users","Profile"};


    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
        childFragments = new Fragment[]{
                new Chats(), //0
                new Users(), //1
                new ProfileFragment()
        };

    }

    @Override
    public Fragment getItem(int position) {
        return childFragments[position];
    }

    @Override
    public int getCount() {
        return childFragments.length; //3 items
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
