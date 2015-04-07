package com.sellcom.apps.tracker_material.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.sellcom.apps.tracker_material.Adapters.SimpleSpinnerAdapter;
import com.sellcom.apps.tracker_material.Adapters.WorkPlanListAdapter;
import com.sellcom.apps.tracker_material.Async_Request.METHOD;
import com.sellcom.apps.tracker_material.Async_Request.RequestManager;
import com.sellcom.apps.tracker_material.Async_Request.UIResponseListenerInterface;
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

public class FragmentBudget extends TrackerFragment {

    List<Map<String,String>>    budget_cat_list;
    Spinner                     spn_categories;
    TextView                    txt_pdv_name,txt_pdv_number,
                                txt_cat_id,txt_cat_name,
                                txt_cat_day_sales,txt_tot_day_sales,
                                txt_cat_month_avg,txt_tot_month_avg,
                                txt_cat_month_perc,txt_tot_month_perc,
                                txt_cat_prev_month_avg,txt_tot_prev_month_avg,
                                txt_cat_ppto,txt_tot_ppto,
                                txt_cat_goals,txt_tot_goals,
                                txt_cat_prev_year_perc,txt_tot_prev_year_perc,
                                txt_cat_perc_a_prev_year,txt_tot_perc_a_prev_year,
                                txt_cat_dif_vs_ant,txt_tot_dif_vs_ant;
    String                      id_pdv,name_pdv,number_pdv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view               = inflater.inflate(R.layout.fragment_budget,container,false);

        id_pdv                  = getArguments().getString("id_pdv");
        name_pdv                = getArguments().getString("name_pdv");
        number_pdv              = getArguments().getString("number_pdv");

        spn_categories          = (Spinner)view.findViewById(R.id.spn_categories);

        txt_pdv_name            = (TextView)view.findViewById(R.id.txt_pdv_name);
        txt_pdv_number          = (TextView)view.findViewById(R.id.txt_pdv_number);
        txt_cat_id              = (TextView)view.findViewById(R.id.txt_cat_id);
        txt_cat_name            = (TextView)view.findViewById(R.id.txt_cat_name);
        txt_cat_day_sales       = (TextView)view.findViewById(R.id.txt_cat_day_sales);
        txt_tot_day_sales       = (TextView)view.findViewById(R.id.txt_tot_day_sales);
        txt_cat_month_avg       = (TextView)view.findViewById(R.id.txt_cat_month_avg);
        txt_tot_month_avg       = (TextView)view.findViewById(R.id.txt_tot_month_avg);
        txt_cat_month_perc      = (TextView)view.findViewById(R.id.txt_cat_month_perc);
        txt_tot_month_perc      = (TextView)view.findViewById(R.id.txt_tot_month_perc);
        txt_cat_prev_month_avg  = (TextView)view.findViewById(R.id.txt_cat_prev_month_avg);
        txt_tot_prev_month_avg  = (TextView)view.findViewById(R.id.txt_tot_prev_month_avg);
        txt_cat_ppto            = (TextView)view.findViewById(R.id.txt_cat_ppto);
        txt_tot_ppto            = (TextView)view.findViewById(R.id.txt_tot_ppto);
        txt_cat_goals           = (TextView)view.findViewById(R.id.txt_cat_goals);
        txt_tot_goals           = (TextView)view.findViewById(R.id.txt_tot_goals);
        txt_cat_prev_year_perc  = (TextView)view.findViewById(R.id.txt_cat_prev_year_perc);
        txt_tot_prev_year_perc  = (TextView)view.findViewById(R.id.txt_tot_prev_year_perc);

        txt_cat_perc_a_prev_year= (TextView)view.findViewById(R.id.txt_cat_perc_a_prev_year);
        txt_tot_perc_a_prev_year= (TextView)view.findViewById(R.id.txt_tot_perc_a_prev_year);

        txt_cat_dif_vs_ant      = (TextView)view.findViewById(R.id.txt_cat_dif_vs_ant);
        txt_tot_dif_vs_ant      = (TextView)view.findViewById(R.id.txt_tot_dif_vs_ant);

        txt_pdv_name.setText(name_pdv);
        txt_pdv_number.setText(number_pdv);

        budget_cat_list = new ArrayList<Map<String, String>>();

        prepareRequest(METHOD.GET_BUDGET_CATS,new HashMap<String, String>(),true);

        return view;
    }

    public void populateViewWithInfoFromCategoryAndTotal(Map<String,String> cat_map,Map<String,String> tot_map){
        // Category info
        txt_cat_id.setText(cat_map.get("id_category"));
        txt_cat_name.setText(cat_map.get("category"));

        txt_cat_day_sales.setText(cat_map.get("day_sales"));
        txt_cat_month_avg.setText(cat_map.get("month_avg"));
        txt_cat_month_perc.setText(cat_map.get("month_perc"));
        txt_cat_prev_month_avg.setText(cat_map.get("prev_month_avg"));
        txt_cat_ppto.setText(cat_map.get("ppto"));
        txt_cat_goals.setText(cat_map.get("goal"));
        txt_cat_prev_year_perc.setText(cat_map.get("prev_year_perc"));

        txt_cat_perc_a_prev_year.setText(cat_map.get("perc_a_prev_year"));
        txt_cat_dif_vs_ant.setText(cat_map.get("dif_vs_ant"));

        // Total info
        txt_tot_day_sales.setText(tot_map.get("day_sales"));
        txt_tot_month_avg.setText(tot_map.get("month_avg"));
        txt_tot_month_perc.setText(tot_map.get("month_perc"));
        txt_tot_prev_month_avg.setText(tot_map.get("prev_month_avg"));
        txt_tot_ppto.setText(tot_map.get("ppto"));
        txt_tot_goals.setText(tot_map.get("goal"));
        txt_tot_prev_year_perc.setText(tot_map.get("prev_year_perc"));

        txt_tot_perc_a_prev_year.setText(tot_map.get("perc_a_prev_year"));
        txt_tot_dif_vs_ant.setText(tot_map.get("dif_vs_ant"));
    }

    @Override
    public void decodeResponse(String stringResponse) {
        JSONObject resp;

        try {
            resp = new JSONObject(stringResponse);

            if (resp.getString("method").equalsIgnoreCase(METHOD.GET_BUDGET_CATS.toString())){
                JSONArray   cat_json_array  = resp.getJSONArray("categories");
                budget_cat_list.clear();
                for (int i=0; i<cat_json_array.length(); i++ ){
                    budget_cat_list.add(Utilities.jsonToMap(cat_json_array.getJSONObject(i)));
                }
                spn_categories.setAdapter(new SimpleSpinnerAdapter(getActivity(),budget_cat_list));
                spn_categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String cat_id = (budget_cat_list.get(position)).get("id_category");
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("id_pdv",id_pdv);
                        params.put("id_bud_cat",""+cat_id+",31");
                        prepareRequest(METHOD.GET_BUDGET,params,true);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Log.d("nothing", "selected");
                    }
                });
            }
            if (resp.getString("method").equalsIgnoreCase(METHOD.GET_BUDGET.toString())){
                JSONArray   bud_json_array  = resp.getJSONArray("budget");

                populateViewWithInfoFromCategoryAndTotal(Utilities.jsonToMap(bud_json_array.getJSONObject(0)),
                                                         Utilities.jsonToMap(bud_json_array.getJSONObject(1)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}