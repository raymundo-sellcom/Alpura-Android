package com.sellcom.apps.tracker_material.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sellcom.apps.tracker_material.R;

import java.util.List;
import java.util.Map;

/**
 * Created by raymundo.piedra on 21/02/15.
 */
public class StepVisitsAdapter extends RecyclerView.Adapter<StepVisitsAdapter.StepsItemViewHolder> {

    private List<Map<String,String>>    steps_list;
    public  OnListClickListener         listener;
    private Context                     context;

    public StepVisitsAdapter(List<Map<String,String>> list, Context ctx) {
        this.steps_list = list;
        this.context    = ctx;
    }

    @Override
    public int getItemCount() {
        return steps_list.size();
    }

    @Override
    public void onBindViewHolder(StepsItemViewHolder pdvViewHolder, int i) {
        Map<String,String> pdv = steps_list.get(i);

        pdvViewHolder.img_step_visit.setImageResource(Integer.parseInt(pdv.get("image")));
        pdvViewHolder.txt_step_visit.setText(pdv.get("name"));

        pdvViewHolder.setClickListener(new OnCardClickListener() {
            @Override
            public void onClick(View v, int position) {
                listener.clickFromList(position);
            }
        });
    }

    @Override
    public StepsItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.stepvisit_card, viewGroup, false);

        return new StepsItemViewHolder(itemView);
    }

    public static class StepsItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected ImageView img_step_visit;
        protected TextView txt_step_visit;

        private OnCardClickListener clickListener;

        public StepsItemViewHolder(View view) {
            super(view);
            img_step_visit = (ImageView) view.findViewById(R.id.img_step_visit);
            txt_step_visit = (TextView) view.findViewById(R.id.txt_step_visit);

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
