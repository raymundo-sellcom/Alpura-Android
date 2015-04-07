package com.sellcom.apps.tracker_material.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;
import com.sellcom.apps.tracker_material.Activities.MainActivity;
import com.sellcom.apps.tracker_material.Async_Request.METHOD;
import com.sellcom.apps.tracker_material.Async_Request.RequestManager;
import com.sellcom.apps.tracker_material.Async_Request.UIResponseListenerInterface;
import com.sellcom.apps.tracker_material.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.Utilities;

public class LoginFragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener, UIResponseListenerInterface {

    public  final   String LOG_LOGIN_FRAGMENT    = "login_fragment";

    EditText        txt_login_user,txt_login_pass;
    ButtonFlat      bnt_login_enter;
    TextView        versionText;

    String          txtUsername,txtPassword;

    Context         context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login,container,false);

        if (view != null) {

            context                 = getActivity();
            txt_login_user          = (EditText) view.findViewById(R.id.txt_login_user);
            txt_login_pass          = (EditText) view.findViewById(R.id.txt_login_pass);
            bnt_login_enter         = (ButtonFlat) view.findViewById(R.id.bnt_login_enter);
            versionText             = (TextView) view.findViewById(R.id.version_text);

            bnt_login_enter.setOnClickListener(this);

            Button print    = (Button) view.findViewById(R.id.print_button);
            print.setOnClickListener(this);

            print.setVisibility(View.GONE);

            txt_login_pass.setOnEditorActionListener(this);

            try {
                PackageManager manager  = getActivity().getPackageManager();
                PackageInfo info        = manager.getPackageInfo(getActivity().getPackageName(), 0);
                versionText.setText(getString(R.string.login_version_label) +" "+ info.versionName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            bnt_login_enter.performClick();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bnt_login_enter:
                txtUsername    = txt_login_user.getText().toString();
                txtPassword    = txt_login_pass.getText().toString();

                if (txtUsername.isEmpty()) {
                    txt_login_user.setError(getString(R.string.error_empty_field));
                    txt_login_user.requestFocus();
                    return;
                }
                else if (txtPassword.isEmpty()){
                    txt_login_pass.setError(getString(R.string.error_empty_field));
                    txt_login_pass.requestFocus();
                    return;
                }

                Utilities.hideKeyboard(context, txt_login_pass);

                final Map<String, String> params = new HashMap<String, String>();
                params.put("user", txtUsername);
                params.put("password", txtPassword);

                prepareRequest(METHOD.LOGIN,params,false);

                break;
        }
    }

    public void startMainActivity(){
        Intent i = new Intent(context, MainActivity.class);
        startActivity(i);
        getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public void decodeResponse(String stringResponse) {

        Log.d(LOG_LOGIN_FRAGMENT,stringResponse);

        try {
            JSONObject  resp        = new JSONObject(stringResponse);

            if (resp.getString("method").equalsIgnoreCase(METHOD.LOGIN.toString())){
                txt_login_user.setText("");
                txt_login_pass.setText("");

                String textToken   = resp.getString("token");

                SessionManager.saveSession(getActivity(),txtUsername,textToken);
                startMainActivity();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void prepareRequest(METHOD method, Map<String, String> params, boolean includeCredentials) {
        RequestManager.sharedInstance().setListener(this);
        RequestManager.sharedInstance().makeRequestWithDataAndMethodIncludeCredentials(params, method,includeCredentials);
    }
}