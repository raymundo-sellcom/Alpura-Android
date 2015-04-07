package com.sellcom.apps.tracker_material.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.sellcom.apps.tracker_material.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by raymundo.piedra on 23/02/15.
 */
public class RecommendationsAdapter extends BaseExpandableListAdapter {

    private Context                                     context;
    private List<String>                                listHeader;
    private HashMap<String,List<Map<String,String>>>    listHeaderDataChild;

    public RecommendationsAdapter (Context context, List<String> listHeader, HashMap<String,List<Map<String,String>>>listHeaderDataChild){
        this.context                = context;
        this.listHeader             = listHeader;
        this.listHeaderDataChild    = listHeaderDataChild;
    }

    @Override
    public int getGroupCount() {
        return this.listHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listHeaderDataChild.get(this.listHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listHeaderDataChild.get(this.listHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle  = (String)getGroup(groupPosition);

        if (convertView == null){
            convertView     = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.simple_spinner_item,null);
            convertView.setBackgroundColor(context.getResources().getColor(R.color.primaryColor));
        }

        TextView    aux_txt = (TextView)convertView.findViewById(R.id.txt_spn_item);
        aux_txt.setTextColor(context.getResources().getColor(R.color.white));
        aux_txt.setTypeface(null, Typeface.BOLD);
        aux_txt.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Map<String,String> childData  = (Map<String,String>) getChild(groupPosition,childPosition);

        if (convertView == null){
            convertView     = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.recommendation_item,null);
        }

        convertView.setTag(childPosition);

        TextView    txt_aux    = (TextView)convertView.findViewById(R.id.txt_recom_date);
        txt_aux.setTypeface(null, Typeface.BOLD);
        txt_aux.setText("Fecha: "+childData.get("fecha"));

        txt_aux    = (TextView)convertView.findViewById(R.id.txt_recom_text);
        txt_aux.setText(childData.get("texto"));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
