package com.example.project.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class NavigationFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mList;
    public NavigationFragmentPagerAdapter(@NonNull FragmentManager fm, int behavior, List<Fragment> list) {
        super(fm, behavior);
        this.mList = list;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return this.mList == null ? null : this.mList.get(position);
    }

    @Override
    public int getCount() {
        return this.mList == null ? 0 : this.mList.size();
    }
}
