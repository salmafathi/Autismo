package com.salma.loginlayout.database;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable {
    private int category_id;
    private String category_name ;
    private int main_category_id ;

    public Category(int category_id, String category_name, int main_category_id) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.main_category_id = main_category_id;
    }

    protected Category(Parcel in) {
        category_id = in.readInt();
        category_name = in.readString();
        main_category_id = in.readInt();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getMain_category_id() {
        return main_category_id;
    }

    public void setMain_category_id(int main_category_id) {
        this.main_category_id = main_category_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(category_id);
        dest.writeString(category_name);
        dest.writeInt(main_category_id);
    }
}
