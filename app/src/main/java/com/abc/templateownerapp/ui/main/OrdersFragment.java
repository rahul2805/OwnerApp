package com.abc.templateownerapp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.abc.templateownerapp.LoginActivity;
import com.abc.templateownerapp.Model.MainTask;
import com.abc.templateownerapp.R;
import com.abc.templateownerapp.utils.OrdersAdapter;
import com.abc.templateownerapp.utils.UsersAdapter;
import com.abc.templateownerapp.utils.callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class OrdersFragment extends Fragment {

    SwipeRefreshLayout swipeRefreshLayout;
    ExpandableListView ordersListView;
    ArrayList<JSONObject> pendingOrdersList = new ArrayList<>();
    ArrayList<JSONObject> completedOrdersList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_orders, container, false);

        swipeRefreshLayout = root.findViewById(R.id.orders_fragment_swipe_layout);
        ordersListView = root.findViewById(R.id.orders_list_view);

        getOrdersData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOrdersData();
            }
        });

        return root;
    }

    private void getOrdersData() {
        pendingOrdersList.clear();
        completedOrdersList.clear();
        swipeRefreshLayout.setRefreshing(true);

        MainTask.getData(getContext(), "app/getOrders", new callback<String>() {
            @Override
            public void onSucess(String s) {
                swipeRefreshLayout.setRefreshing(false);
                addOrdersToList(s);
            }

            @Override
            public void onError(Exception e) {
                if (e.getMessage().equals("Invalid Token")) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
                swipeRefreshLayout.setRefreshing(false);
                Log.d("Error", e.getMessage());
                Toast.makeText(getContext(), "Something went Wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addOrdersToList(String data) {
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i ++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getInt("status") == 0) {
                    pendingOrdersList.add(jsonObject);
                } else {
                    completedOrdersList.add(jsonObject);
                }
            }
            OrdersAdapter ordersAdapter = new OrdersAdapter(getContext(), pendingOrdersList, completedOrdersList);
            ordersListView.setAdapter(ordersAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
