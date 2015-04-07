package com.sellcom.apps.tracker_material.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sellcom.apps.tracker_material.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by raymundo.piedra on 23/02/15.
 */
public class FilterAdapter extends BaseAdapter {

    String TAG = "FILTER_ADAPTER_LOG";

    Context context;
    private List<Map<String,String>> products;
    private List<Map<String,String>> products_copy;

    public  boolean             isSearchByName;

    public  FilterAdapter (Context context, List<Map<String,String>> products){
        isSearchByName      = true;
        this.context        = context;
        this.products       = products;
        this.products_copy  = new ArrayList<>(products);
    }

    class ProductViewHolder{
        TextView    productName;
        TextView    productDescription;
        int         position;
        int         productId;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProductViewHolder   holder;
        if (convertView == null){
            holder                      = new ProductViewHolder();
            convertView                 = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_filter,parent,false);
            holder.productName          = (TextView)convertView.findViewById(R.id.filter_product_name);
            holder.productDescription   = (TextView)convertView.findViewById(R.id.filter_product_description);

            convertView.setTag(holder);
        }
        else{
            holder  = (ProductViewHolder)convertView.getTag();
        }

        Map<String,String> item = products.get(position);
        holder.position             = position;
        holder.productId            = Integer.parseInt(item.get("id_product"));

        if (isSearchByName) {
            holder.productName.setText(item.get("nombre"));
            holder.productDescription.setText(item.get("categoria"));
        }
        else{
            holder.productName.setText(item.get("codigo"));
            holder.productDescription.setText(item.get("nombre"));
        }

        return convertView;
    }

    public void filterProducts(String query){

        Log.d(TAG,"Filtering: "+query);

        query   = query.toLowerCase();
        products.clear();

        if (query.isEmpty())
            products.addAll(products_copy);
        else{
            for (Map<String,String> item :products_copy){
                String strToMatch;
                if (isSearchByName)
                    strToMatch  = (item.get("nombre")).toLowerCase();
                else
                    strToMatch  = (item.get("codigo")).toLowerCase();
                if(strToMatch.startsWith(query)){
                    if (!products.contains(item)){
                        products.add(item);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public void removeElement(Map<String,String> prod){
        products_copy.remove(prod);
        notifyDataSetChanged();
    }

    public void addElement(Map<String,String> prod){
        products_copy.add(prod);
        notifyDataSetChanged();
    }
}
