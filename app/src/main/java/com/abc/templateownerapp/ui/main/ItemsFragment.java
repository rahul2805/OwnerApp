package com.abc.templateownerapp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.abc.templateownerapp.LoginActivity;
import com.abc.templateownerapp.Model.Item;
import com.abc.templateownerapp.Model.MainTask;
import com.abc.templateownerapp.R;
import com.abc.templateownerapp.utils.ItemsAdapter;
import com.abc.templateownerapp.utils.callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ItemsFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView itemsListView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_items, container, false);
        swipeRefreshLayout = root.findViewById(R.id.items_refresh_layout);
        itemsListView = root.findViewById(R.id.items_list_view);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getItemsData();
            }
        });
        getItemsData();
        return root;
    }

    private void getItemsData() {
        swipeRefreshLayout.setRefreshing(true);
        MainTask.getData(getContext(), "app/getItems", new callback<String>() {
            @Override
            public void onSucess(String s) {
                swipeRefreshLayout.setRefreshing(false);
                addItemsToList(s);
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

    private void addItemsToList(String data) {
        ArrayList<Item> itemArrayList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i ++) {
                JSONObject object = jsonArray.getJSONObject(i);
                itemArrayList.add(new Item(
                        object.getString("itemName"),
                        object.getString("description"),
                        object.getString("price"),
                        object.getString("imageURL"),
                        object.getInt("stock"),
                        object.getInt("id")));
            }
            ItemsAdapter itemsAdapter = new ItemsAdapter(getContext(), 0, itemArrayList);
            itemsListView.setAdapter(itemsAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
