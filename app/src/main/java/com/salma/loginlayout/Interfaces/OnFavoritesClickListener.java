package com.salma.loginlayout.Interfaces;

public interface OnFavoritesClickListener {
    void onCheckStar(int id, String sub_category_name, String sub_category_image,int category_id, int star_state, int privacy);
    void onUncheckStar(int id, String sub_category_name, String sub_category_image,int category_id, int star_state, int privacy);

}
