package com.gosemathraj.proscanner.utility;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.gosemathraj.proscanner.activities.MainActivity;

/**
 * Created by iamsparsh on 14/12/17.
 */

public class Util {

    public static Util util;

    public static Util getInstance(){
        if(util == null){
            util = new Util();
        }
        return util;
    }

    public void setBoolPref(Context context, String key, boolean val){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(key,val).commit();
    }

    public boolean getBoolPref(Context context,String key){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key,true);
    }

    public void showToast(Activity activity, String string) {
        Toast.makeText(activity,string,Toast.LENGTH_LONG).show();
    }
}
