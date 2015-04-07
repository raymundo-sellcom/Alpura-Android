package com.sellcom.apps.tracker_material.Activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.sellcom.apps.tracker_material.Async_Request.RequestManager;
import com.sellcom.apps.tracker_material.Fragments.LoginFragment;
import com.sellcom.apps.tracker_material.R;


public class LoginActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        RequestManager.sharedInstance().setActivity(this);

        LoginFragment frag      = new LoginFragment();
        FragmentManager fm      = getFragmentManager();
        FragmentTransaction ft  = fm.beginTransaction();
        ft.add(R.id.layoutLogin,frag,"LogIn");
        ft.commit();
    }
}
