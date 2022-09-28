package com.fourrooms.inshortsapp;


import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    SharedPreferences mySharedPref ;
    public SharedPref(Context context) {
        mySharedPref = context.getSharedPreferences("sharedPref",Context.MODE_PRIVATE);
    }
    // this method will save the nightMode State : True or False
    public void setNightModeState(Integer state) {
        SharedPreferences.Editor editor = mySharedPref.edit();
        editor.putInt("NightMode",state);
        editor.commit();
    }
    // this method will load the Night Mode State
    public Integer loadNightModeState (){
        Integer state = mySharedPref.getInt("NightMode",0);
        return  state;
    }
}