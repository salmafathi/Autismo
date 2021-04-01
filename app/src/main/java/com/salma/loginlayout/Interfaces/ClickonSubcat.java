package com.salma.loginlayout.Interfaces;

import android.content.Context;
import android.content.Intent;

import com.salma.loginlayout.activites.ViewModel;
import com.salma.loginlayout.ui.fragments.Sub_Category_Fragment;

import java.util.ArrayList;

public class ClickonSubcat {

    public void GoToViewModelAfterClickOnSubcat(Context c ,String subitemname, String uriImage) {
        Intent intent = new Intent(c, ViewModel.class);
        ArrayList<String> data = new ArrayList<>() ;
        switch (subitemname.toLowerCase()){
            case "cat":
                data.add("model_Cat_20171119_172940665.sfb");
                data.add("cat");
                data.add(uriImage);
                break;
            case "elephant":
                data.add("Elephant.sfb");
                data.add("elephant");
                data.add(uriImage);
                break;
            case "bird":
                data.add("12213_Bird_v1_l3.sfb");
                data.add("bird");
                data.add(uriImage);
                break;
            case "dog":
                data.add("13041_Beagle_v1_L1.sfb");
                data.add("dog");
                data.add(uriImage);
                break;
            case "horse"  :
                data.add("10026_Horse_v01-it2.sfb");
                data.add("hourse");
                data.add(uriImage);
                break;
            case "chicken" :
                data.add("D0901A10.sfb");
                data.add("chicken");
                data.add(uriImage);
                break;
            case "cock" :
                data.add("rooster.sfb");
                data.add("rooster");
                data.add(uriImage);
                break;
            case "pigeon":
                data.add("D0901B73.sfb");
                data.add("dove");
                data.add(uriImage);
                break;

            case "hands":
                data.add("hand.sfb");
                data.add("hand");
                data.add(uriImage);
                break;
            case "lips":
                data.add("12191_lips_v1_L3.sfb");
                data.add("cat");
                data.add(uriImage);
                break;
            case "eyes":
                data.add("eyeball.sfb");
                data.add("eye");
                data.add(uriImage);
                break;

            case "orange":
                data.add("Orange.sfb");
                data.add("orange");
                data.add(uriImage);
                break;
            case "banana":
                data.add("banana.sfb");
                data.add("banana");
                data.add(uriImage);
                break;
            case "apple":
                data.add("uploads_files_2332293_OBJ.sfb");
                data.add("apple");
                data.add(uriImage);
                break;

            default:
                data.add("uploads_files_2332293_OBJ.sfb");
                data.add("apple");
                data.add(uriImage);
                break;


        }
        intent.putExtra("SubCategoryName",data);
        c.startActivity(intent);
    }
}
