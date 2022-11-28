package com.abc.templateownerapp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.abc.templateownerapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UsersAdapter extends ArrayAdapter<JSONObject> {
    public UsersAdapter(@NonNull Context context, int resource, ArrayList<JSONObject> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.user_list_layout, parent, false);
        }
        JSONObject object = getItem(position);

        try {
            ((TextView)view.findViewById(R.id.user_name_tv)).setText(object.getString("name"));
            ((TextView)view.findViewById(R.id.user_username_tv)).setText(object.getString("username"));
            ((TextView)view.findViewById(R.id.user_email_tv)).setText(object.getString("email"));
            ((TextView)view.findViewById(R.id.user_address_tv)).setText(object.getString("address"));
            ((TextView)view.findViewById(R.id.user_contact_tv)).setText(object.getString("contact"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }
}
