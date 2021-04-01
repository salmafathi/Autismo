package com.salma.loginlayout.Interfaces;

public interface OnFragmentFavoritesClickListener {
    void onitemFragmentCheckStar(int id, String sub_category_name, String sub_category_image,int category_id, int star_state, int privacy);
    void onitemFragmentUncheckStar(int id, String sub_category_name, String sub_category_image,int category_id, int star_state, int privacy);
}
