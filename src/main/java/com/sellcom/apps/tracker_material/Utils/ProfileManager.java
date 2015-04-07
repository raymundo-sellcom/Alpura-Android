package com.sellcom.apps.tracker_material.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;

import com.sellcom.apps.tracker_material.NavigationDrawer.NavigationItem;
import com.sellcom.apps.tracker_material.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raymundo.piedra on 07/02/15.
 */
public class ProfileManager {

    public enum PROFILE {
        SALESMAN_RAGASA ("salesman_ragasa");

        private final String name;

        private PROFILE(String s) {
            name = s;
        }

        public boolean equalsName(String otherName){
            return (otherName == null)? false:name.equals(otherName);
        }

        public String toString(){
            return name;
        }

    }

    public static List<NavigationItem> getDrawerModulesFromProfile(Activity context){

        String profileName         = SessionManager.getSessionProfileName(context);

        List<NavigationItem> items = new ArrayList<NavigationItem>();

        if (profileName.equalsIgnoreCase(PROFILE.SALESMAN_RAGASA.toString())){
            TypedArray iconsArray   = context.getResources().obtainTypedArray(R.array.drawer_items_icons_salesman_ragasa);
            String[] titlesArray    = context.getResources().getStringArray(R.array.drawer_items_names_salesman_ragasa);

            for (int i=0; i<titlesArray.length; i++)
                items.add(new NavigationItem(titlesArray[i], context.getResources().getDrawable(iconsArray.getResourceId(i,0))));
        }
        return items;
    }
}
