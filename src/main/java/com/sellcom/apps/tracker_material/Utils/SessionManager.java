package com.sellcom.apps.tracker_material.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {

    public static Map<String,String> getSessionInfo (Activity activity){
        SharedPreferences sharedPref        = activity.getSharedPreferences("Tracker_Preferences",Context.MODE_PRIVATE);
        Map<String, String> session = new HashMap<>();
        if (!sharedPref.getString("user","CLEAR").equalsIgnoreCase("CLEAR")){
            session.put("user",sharedPref.getString("user", "CLEAR"));
            session.put("token",sharedPref.getString("token","CLEAR"));
        }
        return session;
    }

    public static void saveSession (Activity activity, String user, String token){
        SharedPreferences.Editor editor = (activity.getSharedPreferences("Tracker_Preferences",Context.MODE_PRIVATE)).edit();
        editor.putString("user", user);
        editor.putString("token", token);
        editor.commit();
    }

    public static void clearSession(Activity activity){
        saveSession(activity,"CLEAR","CLEAR");
    }

    public static void createProfile (Activity activity, String profileName){
        SharedPreferences.Editor editor     = createPreferencesEditor(activity);
        editor.putString("profile",profileName);
        editor.commit();
    }

    public static String getSessionProfileName(Activity activity){
        SharedPreferences sharedPref        = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString("profile_name", ProfileManager.PROFILE.SALESMAN_RAGASA.toString());
    }

    public static SharedPreferences.Editor createPreferencesEditor(Activity activity){
        SharedPreferences sharedPref        = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.edit();
    }
}
