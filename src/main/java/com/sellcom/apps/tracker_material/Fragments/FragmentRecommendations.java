package com.sellcom.apps.tracker_material.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import com.sellcom.apps.tracker_material.Adapters.RecommendationsAdapter;
import com.sellcom.apps.tracker_material.Adapters.SimpleSpinnerAdapter;
import com.sellcom.apps.tracker_material.Async_Request.METHOD;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;
import com.sellcom.apps.tracker_material.Utils.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRecommendations extends TrackerFragment {

    private RecommendationsAdapter  adapter;
    private ExpandableListView      exp_lst_recommendations;

    private List<String>                                listDataHeader;
    private HashMap<String, List<Map<String,String>>>   listDataChild;

    public FragmentRecommendations() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        listDataHeader          = new ArrayList<>();
        listDataChild           = new HashMap<>();

        View view               = inflater.inflate(R.layout.fragment_recommendations,container,false);
        exp_lst_recommendations = (ExpandableListView)view.findViewById(R.id.exp_list_recommendations);

        adapter = new RecommendationsAdapter(getActivity().getApplicationContext(),listDataHeader,listDataChild);
        exp_lst_recommendations.setAdapter(adapter);

        prepareRequest(METHOD.GET_RECOMMENDATIONS,new HashMap<String, String>(),true);

        return view;
    }

    @Override
    public void decodeResponse(String stringResponse) {
        JSONObject resp;

        try {
            resp = new JSONObject(stringResponse);

            if (resp.getString("method").equalsIgnoreCase(METHOD.GET_RECOMMENDATIONS.toString())){
                JSONArray rec_json_array  = resp.getJSONArray("recomendaciones");
                for (int i=0; i<rec_json_array.length(); i++){
                    JSONObject  recom   = rec_json_array.getJSONObject(i);
                    listDataHeader.add(recom.getString("titulo"));
                    JSONArray det_json_array                = recom.getJSONArray("detalle");
                    List<Map<String,String>> map_det_arr    = new ArrayList<Map<String,String>>();
                    for (int j=0; j<det_json_array.length(); j++){
                        JSONObject          obj     = det_json_array.getJSONObject(j);
                        Map<String,String>  map     = new HashMap<String, String>();
                        Iterator<?> keys = obj.keys();
                        while( keys.hasNext() ){
                            String key = (String)keys.next();
                            map.put(key,obj.getString(key));
                        }
                        map_det_arr.add(map);
                    }
                    listDataChild.put(listDataHeader.get(i),map_det_arr);
                }
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }}
