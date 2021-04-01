package com.salma.loginlayout.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.sql.Blob;
import java.util.ArrayList;

public class Database extends SQLiteAssetHelper {

    public static final String DB_name = "allcategories.db";
    public static final int DB_version = 80;

    public static final String main_category_table_name = "main_categories";
    public static final String main_category_cln_id = "mcategory_id";
    public static final String main_category_cln_name ="mcategory_name";
    public static final String main_category_cln_icon ="mcategory_icon";
    public static final String main_category_cln_description ="mcategory_description";


    public static final String sub_category_table_name = "sub_categories";
    public static final String sub_category_cln_id = "scategory_id";
    public static final String sub_category_cln_name ="scategory_name";
    public static final String sub_category_cln_image ="scategory_image";
    public static final String sub_category_cln_image_3D ="scategory_3dimage";
    public static final String sub_category_cln_voice ="scategory_voice";
    public static final String sub_category_cln_category_id ="category_id";
    public static final String star_state ="star_state";
    public static final String sub_category_cln_privacy ="privacy";



    public static final String category_table_name = "categories";
    public static final String category_cln_id = "category_id";
    public static final String category_cln_name ="category_name";
    public static final String mcategory_id ="mcategory_id";


    public static final String user_table_name = "users";
    public static final String user_cln_id = "id";
    public static final String user_cln_email = "user_email";
    public static final String user_cln_password = "user_password";



    public Database(Context context){
        super(context,DB_name,null,DB_version);
    }

  /*  @Override
    public void onCreate(SQLiteDatabase db) {
        //يتم استدعائها مرة واحده فقط عند انشاء الداتابيز
        db.execSQL("CREATE TABLE "+user_table_name+" ( "+user_cln_id+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                " "+user_cln_email+" TEXT UNIQUE," +
                " "+user_cln_password+" TEXT)");


        db.execSQL("CREATE TABLE "+category_table_name+" ( "+category_cln_id+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                " "+category_cln_name+" TEXT UNIQUE," +
                " "+category_cln_image+" TEXT)");

        db.execSQL("CREATE TABLE "+sub_category_table_name+" ( "+sub_category_cln_id+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                " "+sub_category_cln_name+" TEXT UNIQUE," +
                " "+sub_category_cln_image_3D+" TEXT," +
                " "+category_cln_name+" TEXT REFERENCES "+category_table_name+"("+category_cln_id+")," +
                " "+sub_category_cln_image+" TEXT)");
    } */

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // يتم استدعائها عند التعديل على الداتا بيز او تغيير رقم الفيرجن لرقم اعلى
        db.execSQL("DROP TABLE  IF EXISTS "+main_category_table_name+"");
        db.execSQL("DROP TABLE  IF EXISTS "+sub_category_table_name+"");
        db.execSQL("DROP TABLE  IF EXISTS "+user_table_name+"");
        db.execSQL("DROP TABLE  IF EXISTS "+category_table_name+"");
        onCreate(db);
    }



}
