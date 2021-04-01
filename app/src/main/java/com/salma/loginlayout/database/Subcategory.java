package com.salma.loginlayout.database;

import android.os.Parcel;
import android.os.Parcelable;

public class Subcategory implements Parcelable {

    private int id;
    private String sub_category_name ;
    private String sub_category_image ;
    private String sub_category_3dimage ;
    private String sub_category_voice;
    private int category_id ;
    private int star_state ;
    private int privacy ;

    public Subcategory() {
    }
    public Subcategory(int id, String sub_category_name, String sub_category_image,int category_id,int star_state, int privacy) {
        this.id = id;
        this.sub_category_name = sub_category_name;
        this.sub_category_image = sub_category_image;
        this.star_state =  star_state ;
        this.privacy = privacy ;
        this.category_id = category_id ;
    }

    public Subcategory(String sub_category_name, String sub_category_image,int category_id, int star_state) {
        this.sub_category_name = sub_category_name;
        this.sub_category_image = sub_category_image;
        this.star_state = star_state;
        this.category_id = category_id;
    }

    protected Subcategory(Parcel in) {
        id = in.readInt();
        sub_category_name = in.readString();
        sub_category_image = in.readString();
        sub_category_3dimage = in.readString();
        sub_category_voice = in.readString();
        category_id = in.readInt();
        star_state = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(sub_category_name);
        dest.writeString(sub_category_image);
        dest.writeString(sub_category_3dimage);
        dest.writeString(sub_category_voice);
        dest.writeInt(category_id);
        dest.writeInt(star_state);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Subcategory> CREATOR = new Creator<Subcategory>() {
        @Override
        public Subcategory createFromParcel(Parcel in) {
            return new Subcategory(in);
        }

        @Override
        public Subcategory[] newArray(int size) {
            return new Subcategory[size];
        }
    };

    public Subcategory(int id, String sub_category_name, String sub_category_image, String sub_category_3dimage, String sub_category_voice, int category_id, int star_state) {
        this.id = id;
        this.sub_category_name = sub_category_name;
        this.sub_category_image = sub_category_image;
        this.sub_category_3dimage = sub_category_3dimage;
        this.sub_category_voice = sub_category_voice;
        this.category_id = category_id;
        this.star_state = star_state;
    }

    public int getId() {
        return id;
    }
    public int getPrivacy() {
        return privacy;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setPrivacy(int privacy) {
        this.privacy = privacy;
    }

    public String getSub_category_name() {
        return sub_category_name;
    }

    public void setSub_category_name(String sub_category_name) {
        this.sub_category_name = sub_category_name;
    }

    public String getSub_category_image() {
        return sub_category_image;
    }

    public void setSub_category_image(String sub_category_image) {
        this.sub_category_image = sub_category_image;
    }

    public String getSub_category_3dimage() {
        return sub_category_3dimage;
    }

    public void setSub_category_3dimage(String sub_category_3dimage) {
        this.sub_category_3dimage = sub_category_3dimage;
    }

    public String getSub_category_voice() {
        return sub_category_voice;
    }

    public void setSub_category_voice(String sub_category_voice) {
        this.sub_category_voice = sub_category_voice;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getStar_state() {
        return star_state;
    }

    public void setStar_state(int star_state) {
        this.star_state = star_state;
    }
}
