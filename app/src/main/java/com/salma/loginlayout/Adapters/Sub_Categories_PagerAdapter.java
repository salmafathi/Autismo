package com.salma.loginlayout.Adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.salma.loginlayout.database.Tab;

import java.util.ArrayList;

public class Sub_Categories_PagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<Tab> tabs = new ArrayList<>();

    public Sub_Categories_PagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public Sub_Categories_PagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public void addTab (Tab mytab){
        Log.d("salmaaaaaaaaaaaaTabs", "addTab() returned: " +mytab);
        tabs.add(mytab);
    }




    @NonNull
    @Override
    public Fragment getItem(int position) {
        Log.d("salmaaaaaaaaaaaaa", "getItem() returned: " +position);
         return tabs.get(position).getFragment();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).get_category().getCategory_name();
    }

    @Override
    public int getCount() {
        return tabs.size();
    }


}
