package com.salma.loginlayout.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.salma.loginlayout.database.User;

public class SharedPreferenceManager {

    protected Context mContext;
    protected SharedPreferences mSettings;
    protected SharedPreferences.Editor mEditor;

    public SharedPreferenceManager(Context ctx, String prefFileName) {
        mContext = ctx;

        mSettings = mContext.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
        mEditor = mSettings.edit();
    }

    /***
     * Set a value for the key
     ****/
    public void setValue(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }


    /***
     * Set a value for the key
     ****/
    public void setValue(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    /***
     * Set a value for the key
     ****/
    public void setValue(String key, double value) {
        setValue(key, Double.toString(value));
    }

    /***
     * Set a value for the key
     ****/
    public void setValue(String key, long value) {
        mEditor.putLong(key, value);
        mEditor.commit();
    }
    /***
     * Set a object for the key
     ****/

    public  void setValue(String key , User user){
        Gson gson = new Gson();
        String json = gson.toJson(user);
        mEditor.putString(key, json);
        mEditor.commit();
    }
    public  void updateValue(String key , User user){
        Gson gson = new Gson();
        String json = gson.toJson(user);
        mEditor.putString(key, json);
        mEditor.apply();
    }

    /****
     * Gets the value from the settings stored natively on the device.
     */

    public User getValue(String key){
        Gson gson = new Gson();
        String json = mSettings.getString(key,"");
        User obj = gson.fromJson(json, User.class);
        return obj;
    }

    public String getValue(String key, String defaultValue) {
        return mSettings.getString(key, defaultValue);
    }

    public int getIntValue(String key, int defaultValue) {
        return mSettings.getInt(key, defaultValue);
    }

    public long getLongValue(String key, long defaultValue) {
        return mSettings.getLong(key, defaultValue);
    }

    /****
     * Gets the value from the preferences stored natively on the device.
     *
     * @param defValue Default value for the key, if one is not found.
     **/
    public boolean getValue(String key, boolean defValue) {
        return mSettings.getBoolean(key, defValue);
    }

    public void setValue(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }



    /**
     * Clear all the preferences store in this {@link android.content.SharedPreferences.Editor}
     */
    public boolean clear() {
        try {
            mEditor.clear().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Removes preference entry for the given key.
     *
     * @param key
     */
    public void removeValue(String key) {
        if (mEditor != null) {
            mEditor.remove(key).commit();
        }
    }
}
