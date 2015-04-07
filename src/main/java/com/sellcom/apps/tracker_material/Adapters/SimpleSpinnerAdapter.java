package com.sellcom.apps.tracker_material.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sellcom.apps.tracker_material.R;

import java.util.List;
import java.util.Map;

/**
 * Created by raymundo.piedra on 22/02/15.
 */
public class SimpleSpinnerAdapter extends BaseAdapter{

    String  TAG = "SIMPLE_SPINNER_TAG";

    private Context                     context;
    private List<Map<String,String>>    list;

    public SimpleSpinnerAdapter (Context context, List<Map<String,String>> items){
        this.context    = context;
        this.list       = items;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        Map<String,String> item    = list.get(i);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.simple_spinner_item, null);
        }

        TextView main_text = (TextView) view .findViewById(R.id.txt_spn_item);
        main_text.setText(item.get("category"));

        if (item.get("category") != null)
            main_text.setText(item.get("category"));
        else if (item.get("nombre") != null)
            main_text.setText(item.get("nombre"));

        return view;
    }
}
