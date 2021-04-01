package com.salma.loginlayout.database;

import com.salma.loginlayout.ui.fragments.Sub_Category_Fragment;

public class Tab {

    Category category;
    Sub_Category_Fragment fragment ;

    public Tab(Category category, Sub_Category_Fragment fragment) {
        this.category = category;
        this.fragment = fragment;
    }

    public Category get_category() {
        return category;
    }

    public void set_category(Category category) {
        this.category = category;
    }

    public Sub_Category_Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Sub_Category_Fragment fragment) {
        this.fragment = fragment;
    }
}
