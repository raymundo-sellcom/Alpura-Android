package com.sellcom.apps.tracker_material.Fragments;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import de.timroes.android.listview.EnhancedListView;

import com.sellcom.apps.tracker_material.Activities.MainActivity;
import com.sellcom.apps.tracker_material.Adapters.FilterAdapter;
import com.sellcom.apps.tracker_material.Adapters.OrdersAdapter;
import com.sellcom.apps.tracker_material.Async_Request.METHOD;
import com.sellcom.apps.tracker_material.Async_Request.RequestManager;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.DatesHelper;
import com.sellcom.apps.tracker_material.Utils.SessionManager;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;
import com.sellcom.apps.tracker_material.Utils.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class FragmentOrder extends TrackerFragment implements   View.OnFocusChangeListener, TextWatcher, EnhancedListView.OnDismissCallback,
                                                                OrdersAdapter.OnProductDeletedListener, OrdersAdapter.OnTotalChangedListener,
                                                                AdapterView.OnItemClickListener,RequestManager.ConfirmationDialogListener {

    String  TAG = "FRAG_ORD_TAG";

    List<Map<String,String>>    prod_array;
    List<Map<String,String>>    order_prod_list;

    EditText                    edt_search_product;
    EditText                    edt_dummy_search_product;
    EnhancedListView            enh_lst_prods;
    ListView                    lst_fil_prods;
    FilterAdapter               filterAdapter;
    OrdersAdapter               orderAdapter;

    String                      id_pdv,name_pdv;

    public static boolean       hideButtons;

    public FragmentOrder() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view       = inflater.inflate(R.layout.fragment_order,container,false);

        String prods_array_str  = getArguments().getString("prods_array");
        id_pdv                  = getArguments().getString("id_pdv");
        name_pdv                = getArguments().getString("name_pdv");

        TextView    txt_pdv_id  = (TextView)view.findViewById(R.id.txt_pdv_id);
        txt_pdv_id.setText(name_pdv);

        edt_search_product      = (EditText)view.findViewById(R.id.edt_search_product);
        edt_dummy_search_product= (EditText)view.findViewById(R.id.edt_dummy_search_product);
        enh_lst_prods       = (EnhancedListView)view.findViewById(R.id.enh_lst_prods);
        lst_fil_prods       = (ListView)view.findViewById(R.id.lst_fil_prods);
        edt_search_product.setOnFocusChangeListener(this);
        edt_search_product.addTextChangedListener(this);
        enh_lst_prods.setDismissCallback(this);
        enh_lst_prods.enableSwipeToDismiss();
        enh_lst_prods.setRequireTouchBeforeDismiss(false);

        Spinner spn_search_type = (Spinner)view.findViewById(R.id.spn_search_type);
        spn_search_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterAdapter.isSearchByName = (position == 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Log.d(TAG,"Products array: "+prods_array_str);

        prod_array              = new ArrayList<Map<String,String>>();
        order_prod_list         = new ArrayList<Map<String,String>>();

        try {
            JSONArray   prod_json_arr       = new JSONArray(prods_array_str);
            for (int i=0; i<prod_json_arr.length(); i++){
                JSONObject  object  = prod_json_arr.getJSONObject(i);
                prod_array.add(Utilities.jsonToMap(object));
            }

            Log.d(TAG,"Products array size: "+prod_array.size());

            filterAdapter   = new FilterAdapter(getActivity(),prod_array);
            lst_fil_prods.setAdapter(filterAdapter);

            orderAdapter    = new OrdersAdapter(getActivity(), order_prod_list);
            orderAdapter.setOnProductDeletedListener(this);
            orderAdapter.setOnTotalChangedListener(this);
            enh_lst_prods.setAdapter(orderAdapter);
            lst_fil_prods.setOnItemClickListener(this);

            toggleFilterList(View.GONE);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hideButtons = true;
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (!((MainActivity) getActivity()).isDrawerOpen) {
            menu.clear();
            inflater.inflate(R.menu.orders, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d(TAG,"Selected:  "+item.getItemId());

        switch (item.getItemId()) {
            case R.id.print_ticket:
                Log.d(TAG,"Printing receipt");
                return true;

            case R.id.send_order:
                //orderId = saveOrder();
                Log.d(TAG,"Enviando orden");
                sendOrder();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    public void sendOrder(){
        Log.d(TAG,"Send order: "+order_prod_list.size());

        Map<String,String> sessionInfo  = SessionManager.getSessionInfo(getActivity());
        String date_time    = DatesHelper.getStringDate(new Date());

        JSONArray  products = new JSONArray();

        for(Map<String,String> itemProduct : order_prod_list) {
            JSONObject  jsonObj = new JSONObject();
            try {
                jsonObj.put("id_product",itemProduct.get("id_product"));
                jsonObj.put("price",itemProduct.get("price"));
                jsonObj.put("quantity",itemProduct.get("units"));
                Log.d(TAG,"Units: "+itemProduct.get("units"));
                jsonObj.put("id_product_container",itemProduct.get("id_product_container"));

                products.put(jsonObj);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.d(TAG,"JSON ARRAY: "+products.toString());

        JSONObject requestData = new JSONObject();
        try {
            requestData.put("user",sessionInfo.get("user"));
            requestData.put("request", METHOD.SET_ORDER);
            requestData.put("token",sessionInfo.get("token"));
            requestData.put("id_pdv",id_pdv);
            requestData.put("date",date_time);
            requestData.put("products", products.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestManager.sharedInstance().setListener(this);
        RequestManager.sharedInstance().makeRequestWithJSONDataAndMethod(requestData,METHOD.SET_ORDER);
    }

    public void toggleFilterList(int visibility) {
        lst_fil_prods.setVisibility(visibility);
        if (visibility == View.GONE)
            Utilities.hideKeyboard(getActivity(), edt_search_product);
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId()   == R.id.edt_search_product){
            if (hasFocus){
                toggleFilterList(View.VISIBLE);
                filterAdapter.filterProducts("");
                Log.d(TAG,"Filtering products");
            }
            else{
                toggleFilterList(View.GONE);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        filterAdapter.filterProducts(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public EnhancedListView.Undoable onDismiss(EnhancedListView enhancedListView, int i) {
        final int position = i;
        final Map<String,String> product = order_prod_list.get(position);

        orderAdapter.deleteProduct(product, position);

        /*return new EnhancedListView.Undoable() {
            @Override
            public void undo() {
                order_prod_list.add(position, product);
                orderAdapter.notifyDataSetChanged();
                orderAdapter.updateSubtotal();

                filterAdapter.removeElement(product);

                getActivity().invalidateOptionsMenu();
            }

            @Override
            public String getTitle() {
                return "Producto Eliminado";
            }

            @Override
            public void discard() { }
        };
        */
        return null;
    }

    @Override
    public void onProductDeleted(Map<String, String> product) {

    }

    @Override
    public void OnTotalChanged(Double orderSubtotals, int cantidad) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String,String> product  = (Map<String,String>)filterAdapter.getItem(position);
        product.put("id_product_container",""+2);
        product.put("units",""+0);
        order_prod_list.add(product);
        orderAdapter.notifyDataSetChanged();

        filterAdapter.removeElement((Map<String,String>) filterAdapter.getItem(position));
        filterAdapter.notifyDataSetChanged();

        toggleFilterList(View.GONE);

        edt_search_product.setText("");
        edt_search_product.clearFocus();

        edt_dummy_search_product.requestFocus();

        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void decodeResponse(String stringResponse) {
        try {
            JSONObject resp          = new JSONObject(stringResponse);

            if (resp.getString("method").equalsIgnoreCase(METHOD.SET_ORDER.toString())) {
                RequestManager.sharedInstance().showConfirmationDialogWithListener("Pedido enviado exitosamente",getActivity(),this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void okFromConfirmationDialog(String message) {
        getActivity().onBackPressed();
    }
}
