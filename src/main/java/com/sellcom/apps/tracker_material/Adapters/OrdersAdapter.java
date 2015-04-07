package com.sellcom.apps.tracker_material.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.sellcom.apps.tracker_material.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by juanc.jimenez on 15/07/14.
 */
public class OrdersAdapter extends BaseAdapter implements View.OnClickListener{

    Context                     context;
    List<Map<String,String>>    products;
    public List<Double>         subtotals;
    OnTotalChangedListener      totalChangedListener;
    OnProductDeletedListener    productDeletedListener;
    int                         cantidad = 0;

    boolean                     hideButtons;

    public OrdersAdapter(Context context, List<Map<String,String>> products) {

        hideButtons     = true;
        this.context    = context;
        this.products   = products;
        subtotals       = new ArrayList<Double>();
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        final ViewHolder viewHolder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_orders, viewGroup, false);

            viewHolder = new ViewHolder();

            viewHolder.deleteButton             = (ImageButton) view.findViewById(R.id.delete_button);

            viewHolder.txt_prod_code            = (TextView) view.findViewById(R.id.txt_prod_code);
            viewHolder.txt_prod_name            = (TextView) view.findViewById(R.id.txt_prod_name);
            viewHolder.txt_prod_pres            = (TextView) view.findViewById(R.id.txt_prod_pres);

            viewHolder.edt_product_units        = (EditText) view.findViewById(R.id.edt_product_units);

            viewHolder.spn_product_container    = (Spinner) view.findViewById(R.id.spn_product_container);

            viewHolder.deleteButton.setOnClickListener(this);

            view.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) view.getTag();

        Log.d("ORDER_ADAPTER","Products start: "+products.size());

        final Map<String,String> item = products.get(position);

        viewHolder.txt_prod_code.setText(item.get("codigo"));
        viewHolder.txt_prod_name.setText(item.get("nombre"));
        viewHolder.txt_prod_pres.setText(item.get("presentacion"));

        if (hideButtons)
            viewHolder.deleteButton.setVisibility(View.GONE);
        else
            viewHolder.deleteButton.setVisibility(View.VISIBLE);

        subtotals.add(0.0);
        viewHolder.deleteButton.setTag(viewHolder);
        viewHolder.position = position;

        viewHolder.edt_product_units.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String str = editable.toString();

                if (str.isEmpty())
                    str = "0";
                item.put("units",str);
            }
        });

        viewHolder.spn_product_container.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    item.put("id_product_container",""+2);
                else
                    item.put("id_product_container",""+1);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("nothing" , "selected");
            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {

        ViewHolder holder = (ViewHolder) view.getTag();
        int id = view.getId();
        if (id == R.id.delete_button) {
            Map<String,String> toDelete = products.get(holder.position);
            deleteProduct(toDelete, holder.position);
            holder  = null;
        } else {

        }
    }

    public void updateSubtotal(Map<String,String> product, String quantity, int position, TextView label) {

    }

    public void updateSubtotal() {

        double total = 0;
        for (Double item : subtotals) {
            total += item;
        }
        totalChangedListener.OnTotalChanged(total,cantidad);
    }

    public void deleteProduct(Map<String,String> product, int position) {

        subtotals.remove(position);
        products.remove(position);
        notifyDataSetChanged();

        Log.d("Order Adapter","Product lenght: "+products.size());

        productDeletedListener.onProductDeleted(product);
        updateSubtotal();
    }

    public void deleteAllProducts(){
        for (int i=0; i<products.size(); i++){
            Map<String,String> toDelete = products.get(i);
            deleteProduct(toDelete, i);
            cantidad = 0;
        }
    }

    public interface OnTotalChangedListener{

        public void OnTotalChanged(Double orderSubtotals,int cantidad);
    }

    public void setOnTotalChangedListener(OnTotalChangedListener listener) {

        totalChangedListener = listener;
    }

    public interface OnProductDeletedListener {
        public void onProductDeleted(Map<String,String> product);
    }

    public void setOnProductDeletedListener(OnProductDeletedListener listener) {

        productDeletedListener = listener;
    }

    class ViewHolder{

        ImageButton deleteButton;

        EditText    edt_product_units;
        TextView    txt_prod_code;
        TextView    txt_prod_name;
        TextView    txt_prod_pres;
        Spinner     spn_product_container;

        int         position  = 0;

        int         id_container    = 0;
        int         units           = 0;
        float       price           = 0;
    }
}
