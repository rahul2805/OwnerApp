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
import com.abc.templateownerapp.Model.MainTask;
import com.abc.templateownerapp.R;
import com.abc.templateownerapp.utils.UsersAdapter;
import com.abc.templateownerapp.utils.callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UsersFragment extends Fragment {
    SwipeRefreshLayout swipeRefreshLayout;
    ListView usersListView;
    ArrayList<JSONObject> usersArrayList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_users, container, false);

        swipeRefreshLayout = root.findViewById(R.id.users_fragment_swipe_layout);
        usersListView = root.findViewById(R.id.users_list_view);

        getUsersData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUsersData();
            }
        });

        return root;
    }

    private void getUsersData() {
        usersArrayList.clear();
        swipeRefreshLayout.setRefreshing(true);

        MainTask.getData(getContext(), "app/getUsers", new callback<String>() {
            @Override
            public void onSucess(String s) {
                swipeRefreshLayout.setRefreshing(false);
                addUsersToList(s);
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

    private void addUsersToList(String data) {
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i ++) {
                usersArrayList.add(jsonArray.getJSONObject(i));
            }
            UsersAdapter usersAdapter = new UsersAdapter(getContext(), 0, usersArrayList);
            usersListView.setAdapter(usersAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
