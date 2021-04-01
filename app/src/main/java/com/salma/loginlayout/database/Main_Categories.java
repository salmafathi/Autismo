package com.salma.loginlayout.database;

import android.os.Parcel;
import android.os.Parcelable;

public class Main_Categories implements Parcelable {
    private int id;
    private String main_category_name ;
    private String main_category_icon ;
    private String main_category_description ;



    public Main_Categories(int id, String category_name, String category_image, String categorydescription) {
        this.id = id;
        this.main_category_name = category_name;
        this.main_category_icon = category_image;
        this.main_category_description= categorydescription;
    }

    protected Main_Categories(Parcel in) {
        id = in.readInt();
        main_category_name = in.readString();
        main_category_icon = in.readString();
        main_category_description = in.readString();
    }

    public static final Creator<Main_Categories> CREATOR = new Creator<Main_Categories>() {
        @Override
        public Main_Categories createFromParcel(Parcel in) {
            return new Main_Categories(in);
        }

        @Override
        public Main_Categories[] newArray(int size) {
            return new Main_Categories[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMain_category_name() {
        return main_category_name;
    }

    public void setMain_category_name(String main_category_name) {
        this.main_category_name = main_category_name;
    }

    public String getMain_category_icon() {
        return main_category_icon;
    }

    public void setMain_category_icon(String main_category_icon) {
        this.main_category_icon = main_category_icon;
    }

    public String getMain_category_description() {
        return main_category_description;
    }

    public void setMain_category_description(String main_category_description) {
        this.main_category_description = main_category_description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(main_category_name);
        dest.writeString(main_category_icon);
        dest.writeString(main_category_description);
    }
}

