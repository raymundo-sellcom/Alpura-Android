package com.sellcom.apps.tracker_material.Fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sellcom.apps.tracker_material.Activities.MainActivity;
import com.sellcom.apps.tracker_material.Adapters.OnListClickListener;
import com.sellcom.apps.tracker_material.Adapters.WorkPlanListAdapter;
import com.sellcom.apps.tracker_material.Async_Request.METHOD;
import com.sellcom.apps.tracker_material.NavigationDrawer.NavigationDrawerFragment;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;
import com.sellcom.apps.tracker_material.Utils.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentWorkPlan extends TrackerFragment implements OnListClickListener {

    String                      TAG = "FRAG_WORK_PLAN";
    RecyclerView                lst_work_plan;
    List<Map<String,String>>    pdvs_array;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_plan,container,false);

        lst_work_plan    = (RecyclerView) view.findViewById(R.id.lst_work_plan);
        lst_work_plan.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        lst_work_plan.setLayoutManager(llm);

        pdvs_array                  = new ArrayList<Map<String, String>>();
        WorkPlanListAdapter adapter = new WorkPlanListAdapter(pdvs_array);
        adapter.listener            = this;
        lst_work_plan.setAdapter(adapter);

        prepareRequest(METHOD.GET_USER_PDVS,new HashMap<String, String>(),true);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void decodeResponse(String stringResponse) {
        JSONObject resp;

        try {
            resp = new JSONObject(stringResponse);

            if (resp.getString("method").equalsIgnoreCase(METHOD.GET_USER_PDVS.toString())){
                JSONArray   pdv_json_array  = resp.getJSONArray("ruta");
                pdvs_array.clear();
                for (int i=0; i<pdv_json_array.length(); i++ ){
                    pdvs_array.add(Utilities.jsonToMap(pdv_json_array.getJSONObject(i)));
                }
                lst_work_plan.getAdapter().notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clickFromList(int position) {
        Log.d(TAG,"Clicked in: "+position);

        FragmentManager fragmentManager         = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Map<String,String> pdv = pdvs_array.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("id_pdv", pdv.get("id_pdv"));
        bundle.putString("name", pdv.get("name"));
        bundle.putString("number", pdv.get("number"));
        FragmentVisit fragment  = new FragmentVisit();
        fragment.tag            =   TrackerFragment.FRAGMENT_TAG.FRAG_WORKPLAN.toString();
        fragment.section_index  =   NavigationDrawerFragment.WORK_PLAN;
        fragment.setArguments(bundle);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.container, fragment, TrackerFragment.FRAGMENT_TAG.FRAG_HOME.toString());
        fragmentTransaction.commit();
        ((MainActivity) getActivity()).depthCounter = 2;
    }
}