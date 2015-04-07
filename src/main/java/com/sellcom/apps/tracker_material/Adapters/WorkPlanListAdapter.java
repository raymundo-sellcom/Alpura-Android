package com.sellcom.apps.tracker_material.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sellcom.apps.tracker_material.R;

import java.util.List;
import java.util.Map;

public class WorkPlanListAdapter extends RecyclerView.Adapter<WorkPlanListAdapter.PDVItemViewHolder> {

    private List<Map<String,String>>    pdvs_list;
    public  OnListClickListener         listener;

    public WorkPlanListAdapter(List<Map<String,String>> list) {
        this.pdvs_list = list;
    }

    @Override
    public int getItemCount() {
        return pdvs_list.size();
    }

    @Override
    public void onBindViewHolder(PDVItemViewHolder pdvViewHolder, int i) {
        Map<String,String> pdv = pdvs_list.get(i);

        pdvViewHolder.txt_pdv_name.setText(pdv.get("name"));
        pdvViewHolder.txt_pdv_address.setText(pdv.get("address"));
        pdvViewHolder.txt_pdv_number.setText(pdv.get("number"));

        pdvViewHolder.setClickListener(new OnCardClickListener() {
            @Override
            public void onClick(View v, int position) {
                listener.clickFromList(position);
            }
        });
    }

    @Override
    public PDVItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.pdv_card, viewGroup, false);

        return new PDVItemViewHolder(itemView);
    }

    public static class PDVItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView txt_pdv_name;
        protected TextView txt_pdv_number;
        protected TextView txt_pdv_address;
        protected TextView txt_pdv_init_date_time;
        protected TextView txt_pdv_status;

        protected View     lyt_pdv_real_init_date_time;
        protected TextView txt_pdv_real_init_date_time;

        protected View     lyt_pdv_real_end_date_time;
        protected TextView txt_pdv_real_end_date_time;

        private OnCardClickListener clickListener;

        public PDVItemViewHolder(View view) {
            super(view);
            txt_pdv_name    = (TextView) view.findViewById(R.id.txt_pdv_name);
            txt_pdv_address = (TextView) view.findViewById(R.id.txt_pdv_address);
            txt_pdv_number  = (TextView) view.findViewById(R.id.txt_pdv_number);

            txt_pdv_status = (TextView) view.findViewById(R.id.txt_pdv_status);

            lyt_pdv_real_init_date_time = view.findViewById(R.id.lyt_pdv_real_init_date_time);
            txt_pdv_real_init_date_time = (TextView) view.findViewById(R.id.txt_pdv_real_init_date_time);

            lyt_pdv_real_end_date_time = view.findViewById(R.id.lyt_pdv_real_end_date_time);
            txt_pdv_real_end_date_time = (TextView) view.findViewById(R.id.txt_pdv_real_end_date_time);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getPosition());
        }

        public void setClickListener(OnCardClickListener clickListener) {
            this.clickListener = clickListener;
        }
    }
}
