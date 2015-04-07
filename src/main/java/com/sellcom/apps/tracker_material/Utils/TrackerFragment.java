package com.sellcom.apps.tracker_material.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.sellcom.apps.tracker_material.Activities.MainActivity;
import com.sellcom.apps.tracker_material.Async_Request.METHOD;
import com.sellcom.apps.tracker_material.Async_Request.RequestManager;
import com.sellcom.apps.tracker_material.Async_Request.UIResponseListenerInterface;

import java.util.Map;

public class TrackerFragment extends Fragment implements UIResponseListenerInterface {
    public boolean  isFromDrawer    = true;
    public int      section_index   = 0;
    public String   tag             = "";

    public enum FRAGMENT_TAG {
        FRAG_HOME ("home"),
        FRAG_WORKPLAN ("workplan"),
        FRAG_BUDGETS ("budgets"),
        FRAG_RECOMMENDATIONS ("recommendations"),
        FRAG_PARTIAL_SETTLEMENT ("partial_sett"),
        FRAG_ORDERS ("orders"),
        FRAG_ADD_PROSPECT_COSTUMER ("add_prospect"),
        FRAG_VISIT ("visit"),
        FRAG_SETTINGS ("settings"),
        ;

        private final String name;

        private FRAGMENT_TAG(String s) {
            name = s;
        }

        public boolean equalsName(String otherName){
            return (otherName == null)? false:name.equals(otherName);
        }
        public String toString(){
            return name;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(section_index);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).onSectionAttached(section_index);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void prepareRequest(METHOD method, Map<String, String> params, boolean includeCredentials) {
        RequestManager.sharedInstance().setListener(this);
        RequestManager.sharedInstance().makeRequestWithDataAndMethodIncludeCredentials(params, method,includeCredentials);
    }

    @Override
    public void decodeResponse(String stringResponse) {

    }
}