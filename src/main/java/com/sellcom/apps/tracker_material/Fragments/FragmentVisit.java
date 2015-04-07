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
import android.widget.TextView;
import android.widget.Toast;

import com.sellcom.apps.tracker_material.Activities.MainActivity;
import com.sellcom.apps.tracker_material.Adapters.OnListClickListener;
import com.sellcom.apps.tracker_material.Adapters.StepVisitsAdapter;
import com.sellcom.apps.tracker_material.Async_Request.METHOD;
import com.sellcom.apps.tracker_material.NavigationDrawer.NavigationDrawerFragment;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.DatesHelper;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;
import com.sellcom.apps.tracker_material.Utils.TrackerManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import location.GPSTracker;

public class FragmentVisit extends TrackerFragment implements OnListClickListener {

    String              LOG_VISIT_FRAGMENT             = "LOG_VISIT_FRAGMENT";
    RecyclerView        lst_step_visit;
    StepVisitsAdapter   adapter;
    String              id_pdv,name_pdv, number_pdv;

    public FragmentVisit() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view           = inflater.inflate(R.layout.fragment_visit, container, false);
        name_pdv            = getArguments().getString("name");
        id_pdv              = getArguments().getString("id_pdv");
        number_pdv          = getArguments().getString("number");

        TrackerManager.sharedInstance().addInfoToCurrentUtils("id_pdv",id_pdv);

        TextView txt_header = (TextView)view.findViewById(R.id.txt_header);
        txt_header.setText(name_pdv);

        lst_step_visit    = (RecyclerView) view.findViewById(R.id.lst_step_visit);
        lst_step_visit.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        lst_step_visit.setLayoutManager(llm);

        adapter             = new StepVisitsAdapter(createListData(),getActivity());
        adapter.listener    = this;

        lst_step_visit.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public List<Map<String,String>> createListData(){
        int     images[]    = {R.drawable.btn_check_in,R.drawable.btn_order,R.drawable.btn_budget,R.drawable.btn_check_out};
        String  names []    = {"Check In", "Pedidos","Presupuesto","Check Out"};

        List<Map<String,String>> info_array                  = new ArrayList<Map<String, String>>();
        for (int i=0; i<images.length; i++){
            Map<String,String> info = new HashMap<String, String>();
            info.put("image",""+images[i]);
            info.put("name",names[i]);
            info_array.add(info);
        }
        return info_array;
    }

    @Override
    public void clickFromList(int position) {
        Log.d(LOG_VISIT_FRAGMENT,"Position: "+position);
        Map<String,String> params = new HashMap<String,String>();
        if (position == 0) {
            params.put("id_pdv", TrackerManager.sharedInstance().getInfoFromCurrentUtilsFromKey("id_pdv"));
            params.put("date", "" + DatesHelper.getStringDate(new Date()));
            prepareRequest(METHOD.USER_CHECK_IN, params, true);
        }
        else if (position == 3) {
            params.put("id_pdv", TrackerManager.sharedInstance().getInfoFromCurrentUtilsFromKey("id_pdv"));
            params.put("date", "" + DatesHelper.getStringDate(new Date()));
            params.put("id_visit",TrackerManager.sharedInstance().getInfoFromCurrentUtilsFromKey("id_visit"));
            prepareRequest(METHOD.USER_CHECK_OUT, params, true);
        }
        else if (position == 1){
            prepareRequest(METHOD.GET_PRODUCTS,params,true);
        }
        else if (position == 2){
            FragmentManager fragmentManager         = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Bundle bundle = new Bundle();
            bundle.putString("id_pdv", id_pdv);
            bundle.putString("name_pdv", name_pdv);
            bundle.putString("number_pdv", number_pdv);

            Log.d("FRAGMENT_VISIT",number_pdv);

            FragmentBudget fragment     = new FragmentBudget();
            fragment.tag                =   TrackerFragment.FRAGMENT_TAG.FRAG_WORKPLAN.toString();
            fragment.section_index      =   NavigationDrawerFragment.WORK_PLAN;
            fragment.setArguments(bundle);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.container, fragment, TrackerFragment.FRAGMENT_TAG.FRAG_HOME.toString());
            fragmentTransaction.commit();
            ((MainActivity) getActivity()).depthCounter = 3;
        }
    }

    @Override
    public void decodeResponse(String stringResponse) {
        Log.d(LOG_VISIT_FRAGMENT,stringResponse);

        try {
            JSONObject resp        = new JSONObject(stringResponse);

            if (resp.getString("method").equalsIgnoreCase(METHOD.USER_CHECK_IN.toString())){
                String id_visit = resp.getString("id_visit");
                Toast.makeText(getActivity(),"Visita "+id_visit+" iniciada satisfactoriamente",Toast.LENGTH_LONG).show();
                TrackerManager.sharedInstance().addInfoToCurrentUtils("id_visit",id_visit);
            }

            else if (resp.getString("method").equalsIgnoreCase(METHOD.USER_CHECK_OUT.toString())){
                Toast.makeText(getActivity(),"Visita finalizada",Toast.LENGTH_LONG).show();
                ((MainActivity)getActivity()).returnToHome();
            }
            else if (resp.getString("method").equalsIgnoreCase(METHOD.GET_PRODUCTS.toString())) {
                JSONArray   prod_json_array = resp.getJSONArray("productos");

                FragmentManager fragmentManager         = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putString("prods_array", prod_json_array.toString());
                bundle.putString("id_pdv", id_pdv);
                bundle.putString("name_pdv", name_pdv);
                bundle.putString("number_pdv", number_pdv);

                FragmentOrder fragment  = new FragmentOrder();
                fragment.tag            =   TrackerFragment.FRAGMENT_TAG.FRAG_WORKPLAN.toString();
                fragment.section_index  =   NavigationDrawerFragment.WORK_PLAN;
                fragment.setArguments(bundle);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container, fragment, TrackerFragment.FRAGMENT_TAG.FRAG_HOME.toString());
                fragmentTransaction.commit();
                ((MainActivity) getActivity()).depthCounter = 3;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.decodeResponse(stringResponse);
    }
}
