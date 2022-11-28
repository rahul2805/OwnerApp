package com.abc.templateownerapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.abc.templateownerapp.Model.Item;
import com.abc.templateownerapp.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class ItemsAdapter extends ArrayAdapter<Item> {
    public ItemsAdapter(@NonNull Context context, int resource, @NonNull List<Item> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_list_layout, parent, false);
        }

        Item item = getItem(position);

        ((TextView)view.findViewById(R.id.item_name)).setText(item.getItemName());
        ((TextView)view.findViewById(R.id.item_description)).setText(item.getItemDescription());
        ((TextView)view.findViewById(R.id.item_price)).setText("Price: " + item.getItemPrice());
        ((TextView)view.findViewById(R.id.item_stock)).setText("Stock: " + String.valueOf(item.getItemStock()));

        ImageView imageView = view.findViewById(R.id.item_image);
        Picasso.get().load(item.getImageURL()).into(imageView);
        return  view;
    }
}
