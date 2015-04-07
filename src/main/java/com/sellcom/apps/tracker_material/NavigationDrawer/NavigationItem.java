package com.sellcom.apps.tracker_material.NavigationDrawer;

import android.graphics.drawable.Drawable;

/**
 * Created by raymundo.piedra on 07/02/15.
 */
public class NavigationItem {
    private String      nav_item_text;
    private Drawable    nav_item_img;

    public NavigationItem(String text, Drawable drawable) {
        nav_item_text   = text;
        nav_item_img    = drawable;
    }

    public String getText() {
        return nav_item_text;
    }

    public void setText(String text) {
        nav_item_text = text;
    }

    public Drawable getDrawable() {
        return nav_item_img;
    }

    public void setDrawable(Drawable drawable) {
        nav_item_img = drawable;
    }
}
