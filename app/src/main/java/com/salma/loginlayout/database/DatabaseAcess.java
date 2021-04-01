package com.salma.loginlayout.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseAcess {

    private SQLiteDatabase db ;
    private SQLiteOpenHelper dbopenhelper;
    private static  DatabaseAcess instance;

    private DatabaseAcess(Context context) {
        this.dbopenhelper = new Database(context);
    }

    public static DatabaseAcess getInstance(Context context){
        if(instance==null){
            instance = new DatabaseAcess(context);
        }
        return instance;
    }


    public void open(){
        this.db = this.dbopenhelper.getWritableDatabase();
    }

    public void close(){
        if (this.db!=null){
            this.db.close();
        }

    }

    //-----------------------------------------------------------------------------------
    //category table operations .

    public boolean insert_category(Category cat){
        ContentValues values = new ContentValues();
        values.put(Database.category_cln_name,cat.getCategory_name());
        long result = db.insert(Database.category_table_name,null,values);
        // بيرجع برقم العنصر اللى تم اضافته ولو العمليه فشلت بيرجع ب -1
        return result != -1 ;
    }


    public boolean update_category(Category cat){
        ContentValues values = new ContentValues();
        values.put(Database.category_cln_name,cat.getCategory_name());
        String args [] = {cat.getCategory_id()+""};
        int result = db.update(Database.category_table_name,values,"id=?",args);
        //بيرجع بعدد الجداول اللى تم تعديل عليها ولو معدلش بيرجع بصفر
        return result > 0 ;
    }

    public long no_of_categories(){
        return DatabaseUtils.queryNumEntries(db, Database.category_table_name);
    }

    public boolean delete_category(Category cat){
        String args [] = {cat.getCategory_id()+""};
        int result = db.delete(Database.category_table_name,"id=?",args);
        //بيرجع عدد الصفوف اللى تمت حذفها
        return result > 0 ;
    }

    public ArrayList<Category> get_all_categories(){
        ArrayList<Category> cat = new ArrayList<>();
        Cursor cr = db.rawQuery("SELECT * FROM "+ Database.category_table_name,null);
        if(cr != null && cr.moveToFirst()){
            do {
                int id = cr.getInt(cr.getColumnIndex(Database.category_cln_id)) ;
                String cat_name =cr.getString(cr.getColumnIndex(Database.category_cln_name)) ;
                int main_cat_id = cr.getInt(cr.getColumnIndex(Database.mcategory_id)) ;

                Category category = new Category(id,cat_name,main_cat_id);
                cat.add(category);
            }
            while(cr.moveToNext());
            cr.close();
        }
        return  cat;
    }

    public ArrayList<Category> get_all_Nouns_categories(){
        ArrayList<Category> nouns = new ArrayList<>();
        Cursor cr = db.rawQuery("SELECT * FROM "+ Database.category_table_name+" WHERE "+Database.mcategory_id+" = 1"    ,null);
        if(cr != null && cr.moveToFirst()){
            do {
                int id = cr.getInt(cr.getColumnIndex(Database.sub_category_cln_id)) ;
                String cat_name =cr.getString(cr.getColumnIndex(Database.sub_category_cln_name)) ;
                int main_cat_id = cr.getInt(cr.getColumnIndex(Database.mcategory_id)) ;
                Category category = new Category(id,cat_name,main_cat_id);
                nouns.add(category);
            }
            while(cr.moveToNext());
            cr.close();
        }
        return  nouns;
    }

    public ArrayList<Category> get_all_Verbs_categories(){
        ArrayList<Category> nouns = new ArrayList<>();
        Cursor cr = db.rawQuery("SELECT * FROM "+ Database.category_table_name+" WHERE "+Database.mcategory_id+" = 2"    ,null);
        if(cr != null && cr.moveToFirst()){
            do {
                int id = cr.getInt(cr.getColumnIndex(Database.sub_category_cln_id)) ;
                String cat_name =cr.getString(cr.getColumnIndex(Database.sub_category_cln_name)) ;
                int main_cat_id = cr.getInt(cr.getColumnIndex(Database.mcategory_id)) ;
                Category category = new Category(id,cat_name,main_cat_id);
                nouns.add(category);
            }
            while(cr.moveToNext());
            cr.close();
        }
        return  nouns;
    }

    public ArrayList<Category> get_all_categories_by_maincatid( int mid){
        ArrayList<Category> nouns = new ArrayList<>();
        Log.d("pppppppppppda", "get_all_categories_by_maincatid: "+mid+" query = "+"SELECT * FROM "+ Database.category_table_name+" WHERE "+Database.mcategory_id+" = "+mid );
        String[] arguments = new String[]{String.valueOf(mid)};
        Cursor cr = db.rawQuery("SELECT * FROM "+ Database.category_table_name+" WHERE "+Database.mcategory_id+" = ?" ,arguments);
        if(cr != null && cr.moveToFirst()){
            do {
                int id = cr.getInt(cr.getColumnIndex(Database.category_cln_id)) ;
                String cat_name =cr.getString(cr.getColumnIndex(Database.category_cln_name)) ;
                int main_cat_id = cr.getInt(cr.getColumnIndex(Database.mcategory_id)) ;
                Category category = new Category(id,cat_name,main_cat_id);
                nouns.add(category);
            }
            while(cr.moveToNext());
            cr.close();
        }
        return  nouns;
    }

    public String get_categorynamebycatid( int catid){
        String catname = null;
        String[] arguments = new String[]{String.valueOf(catid)};
        Cursor cr = db.rawQuery("SELECT * FROM "+ Database.category_table_name+" WHERE "+Database.category_cln_id+" = ?" ,arguments);
        if(cr != null && cr.moveToFirst()){
            do {
                catname =cr.getString(cr.getColumnIndex(Database.category_cln_name)) ;
            }
            while(cr.moveToNext());
            cr.close();
        }
        return  catname;
    }
    public int get_MainCategoryIDbycatid( int catid){
        int mcatid = 1;
        String[] arguments = new String[]{String.valueOf(catid)};
        Cursor cr = db.rawQuery("SELECT * FROM "+ Database.category_table_name+" WHERE "+Database.category_cln_id+" = ?" ,arguments);
        if(cr != null && cr.moveToFirst()){
            do {
                mcatid =cr.getInt(cr.getColumnIndex(Database.mcategory_id)) ;
            }
            while(cr.moveToNext());
            cr.close();
        }
        return  mcatid;
    }


    //--------------------------------------------------------------------------------------------------------
    // Sub Cateories table operations .

    public ArrayList<Subcategory>get_all_custom_subcats (){
        ArrayList<Subcategory> customsubcat = new ArrayList<>();
        Cursor cr = db.rawQuery("SELECT * FROM "+ Database.sub_category_table_name+" WHERE "+Database.sub_category_cln_privacy+" = 1",null);
        if(cr != null && cr.moveToFirst()){
            do {
                int id = cr.getInt(cr.getColumnIndex(Database.sub_category_cln_id)) ;
                String cat_name =cr.getString(cr.getColumnIndex(Database.sub_category_cln_name)) ;
                String cat_image = cr.getString(cr.getColumnIndex(Database.sub_category_cln_image));
                int star_state = cr.getInt(cr.getColumnIndex(Database.star_state));
                int categoryid = cr.getInt(cr.getColumnIndex(Database.sub_category_cln_category_id));
                int privacy = cr.getInt(cr.getColumnIndex(Database.sub_category_cln_privacy));
                Subcategory subcategory = new Subcategory(id,cat_name,cat_image,categoryid,star_state,privacy);
                customsubcat.add(subcategory);
            }
            while(cr.moveToNext());
            cr.close();
        }

        return  customsubcat;
    }

    public ArrayList<Subcategory> get_custom_subcategories_by_catid(int catid){
        ArrayList<Subcategory> sub_categories = new ArrayList<>();
        String[] arguments = new String[]{String.valueOf(catid)};
        Cursor cr = db.rawQuery("SELECT * FROM "+ Database.sub_category_table_name+" WHERE "+Database.sub_category_cln_category_id+" = ? "+"AND "+Database.sub_category_cln_privacy+" = 1"  ,arguments);
        if(cr != null && cr.moveToFirst()){
            do {
                int id = cr.getInt(cr.getColumnIndex(Database.sub_category_cln_id)) ;
                String cat_name =cr.getString(cr.getColumnIndex(Database.sub_category_cln_name)) ;
                String cat_image = cr.getString(cr.getColumnIndex(Database.sub_category_cln_image));
                int star_state = cr.getInt(cr.getColumnIndex(Database.star_state));
                int categoryid = cr.getInt(cr.getColumnIndex(Database.sub_category_cln_category_id));
                int privacy = cr.getInt(cr.getColumnIndex(Database.sub_category_cln_privacy));

                Subcategory subcategory = new Subcategory(id,cat_name,cat_image,categoryid,star_state,privacy);
                sub_categories.add(subcategory);
            }
            while(cr.moveToNext());
            cr.close();
        }
        return  sub_categories;
    }


    public boolean insert_subcategory(Subcategory subcat){
        ContentValues values = new ContentValues();
        values.put(Database.sub_category_cln_name,subcat.getSub_category_name());
        values.put(Database.sub_category_cln_image,subcat.getSub_category_image());
        values.put(Database.sub_category_cln_category_id,subcat.getCategory_id());
        values.put(Database.star_state,subcat.getStar_state());

        long result = db.insert(Database.sub_category_table_name,null,values);
        // بيرجع برقم العنصر اللى تم اضافته ولو العمليه فشلت بيرجع ب -1
        return result != -1 ;
    }


    public ArrayList<Subcategory> get_all_Sub_categories(){
        ArrayList<Subcategory> subcat = new ArrayList<>();
        Cursor cr = db.rawQuery("SELECT * FROM "+ Database.sub_category_table_name+" WHERE "+Database.sub_category_cln_privacy+" = 0",null);
        if(cr != null && cr.moveToFirst()){
            do {
                int id = cr.getInt(cr.getColumnIndex(Database.sub_category_cln_id)) ;
                String cat_name =cr.getString(cr.getColumnIndex(Database.sub_category_cln_name)) ;
                String cat_image = cr.getString(cr.getColumnIndex(Database.sub_category_cln_image));
                int star_state = cr.getInt(cr.getColumnIndex(Database.star_state));
                int categoryid = cr.getInt(cr.getColumnIndex(Database.sub_category_cln_category_id));
                int privacy = cr.getInt(cr.getColumnIndex(Database.sub_category_cln_privacy));
                Subcategory subcategory = new Subcategory(id,cat_name,cat_image,categoryid,star_state,privacy);
                subcat.add(subcategory);
            }
            while(cr.moveToNext());
            cr.close();
        }
        return  subcat;
    }

    public ArrayList<Subcategory> get_subcategories_by_catid(int catid){
        ArrayList<Subcategory> sub_categories = new ArrayList<>();
        String[] arguments = new String[]{String.valueOf(catid)};
        Cursor cr = db.rawQuery("SELECT * FROM "+ Database.sub_category_table_name+" WHERE "+Database.sub_category_cln_category_id+" = ? "+"AND "+Database.sub_category_cln_privacy+" = 0"  ,arguments);
        if(cr != null && cr.moveToFirst()){
            do {
                int id = cr.getInt(cr.getColumnIndex(Database.sub_category_cln_id)) ;
                String cat_name =cr.getString(cr.getColumnIndex(Database.sub_category_cln_name)) ;
                String cat_image = cr.getString(cr.getColumnIndex(Database.sub_category_cln_image));
                int star_state = cr.getInt(cr.getColumnIndex(Database.star_state));
                int categoryid = cr.getInt(cr.getColumnIndex(Database.sub_category_cln_category_id));
                int privacy = cr.getInt(cr.getColumnIndex(Database.sub_category_cln_privacy));
                Subcategory subcategory = new Subcategory(id,cat_name,cat_image,categoryid,star_state,privacy);
                sub_categories.add(subcategory);
            }
            while(cr.moveToNext());
            cr.close();
        }
        return  sub_categories;
    }


    public ArrayList<Subcategory> get_all_Fruits(){
        ArrayList<Subcategory> fruits = new ArrayList<>();
        Cursor cr = db.rawQuery("SELECT * FROM "+ Database.sub_category_table_name+" WHERE "+Database.sub_category_cln_category_id+" = 4"    ,null);
        if(cr != null && cr.moveToFirst()){
            do {
                int id = cr.getInt(cr.getColumnIndex(Database.sub_category_cln_id)) ;
                String cat_name =cr.getString(cr.getColumnIndex(Database.sub_category_cln_name)) ;
                String cat_image = cr.getString(cr.getColumnIndex(Database.sub_category_cln_image));
                int star_state = cr.getInt(cr.getColumnIndex(Database.star_state));
                int categoryid = cr.getInt(cr.getColumnIndex(Database.sub_category_cln_category_id));
                int privacy = cr.getInt(cr.getColumnIndex(Database.sub_category_cln_privacy));
                Subcategory subcategory = new Subcategory(id,cat_name,cat_image,categoryid,star_state,privacy);
                fruits.add(subcategory);
            }
            while(cr.moveToNext());
            cr.close();
        }
        return  fruits;
    }


    public ArrayList<Subcategory> get_all_Body(){
        ArrayList<Subcategory> Body = new ArrayList<>();
        Cursor cr = db.rawQuery("SELECT * FROM "+ Database.sub_category_table_name+" WHERE "+Database.sub_category_cln_category_id+" = 2"    ,null);
        if(cr != null && cr.moveToFirst()){
            do {
                int id = cr.getInt(cr.getColumnIndex(Database.sub_category_cln_id)) ;
                String cat_name =cr.getString(cr.getColumnIndex(Database.sub_category_cln_name)) ;
                String cat_image = cr.getString(cr.getColumnIndex(Database.sub_category_cln_image));
                int star_state = cr.getInt(cr.getColumnIndex(Database.star_state));
                int categoryid = cr.getInt(cr.getColumnIndex(Database.sub_category_cln_category_id));
                int privacy = cr.getInt(cr.getColumnIndex(Database.sub_category_cln_privacy));
                Subcategory subcategory = new Subcategory(id,cat_name,cat_image,categoryid,star_state,privacy);
                Body.add(subcategory);
            }
            while(cr.moveToNext());
            cr.close();
        }

        return  Body;
    }


    //--------------------------------------------------------------------------------------------------------
    // Main Cateories table operations .
    public ArrayList<Main_Categories> get_all_Main_categories(){
        ArrayList<Main_Categories> main_category = new ArrayList<>();
        Cursor cr = db.rawQuery("SELECT * FROM "+ Database.main_category_table_name,null);
        if(cr != null && cr.moveToFirst()){
            do {
                int id = cr.getInt(cr.getColumnIndex(Database.main_category_cln_id)) ;
                String cat_name =cr.getString(cr.getColumnIndex(Database.main_category_cln_name)) ;
                String cat_image = cr.getString(cr.getColumnIndex(Database.main_category_cln_icon));
                String cat_description = cr.getString(cr.getColumnIndex(Database.main_category_cln_description));

                Main_Categories mcategory = new Main_Categories(id,cat_name,cat_image,cat_description);
                main_category.add(mcategory);
            }
            while(cr.moveToNext());
            cr.close();
        }
        return  main_category;
    }

    public String get_MainCategoryNamebymaincatid( int mcatid){
        String mcatname = null ;
        String[] arguments = new String[]{String.valueOf(mcatid)};
        Cursor cr = db.rawQuery("SELECT * FROM "+ Database.main_category_table_name+" WHERE "+Database.main_category_cln_id+" = ?" ,arguments);
        if(cr != null && cr.moveToFirst()){
            do {
                mcatname =cr.getString(cr.getColumnIndex(Database.main_category_cln_name)) ;
            }
            while(cr.moveToNext());
            cr.close();
        }
        return  mcatname;
    }
    
}
