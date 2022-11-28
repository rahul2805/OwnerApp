package com.abc.templateownerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.abc.templateownerapp.Model.MainTask;
import com.abc.templateownerapp.utils.callback;

import org.json.JSONException;
import org.json.JSONObject;

public class AddNewItemActivity extends AppCompatActivity {

    String itemName, itemDescription, itemURL, itemPrice, itemStock;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);

        Button addItemButton = findViewById(R.id.add_item_button);
        progressBar = findViewById(R.id.add_item_progressbar);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemName = ((EditText)findViewById(R.id.item_name_et)).getText().toString();
                itemDescription = ((EditText)findViewById(R.id.item_desc_et)).getText().toString();
                itemURL = ((EditText)findViewById(R.id.item_url_et)).getText().toString();
                itemPrice = ((EditText)findViewById(R.id.item_price_et)).getText().toString();
                itemStock = ((EditText)findViewById(R.id.item_stock_et)).getText().toString();

                if (itemName.length() == 0 || itemDescription.length() == 0 || itemURL.length() == 0 || itemPrice.length() == 0 || itemStock.length() == 0) {
                    Toast.makeText(AddNewItemActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("itemName", itemName);
                        jsonObject.put("description", itemDescription);
                        jsonObject.put("imageURL", itemURL);
                        jsonObject.put("price", itemPrice);
                        jsonObject.put("stock", itemStock);

                        MainTask.addItem(AddNewItemActivity.this, jsonObject, new callback<String>() {
                            @Override
                            public void onSucess(String s) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(AddNewItemActivity.this, "Item Added Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddNewItemActivity.this, MainActivity.class));
                            }

                            @Override
                            public void onError(Exception e) {
                                progressBar.setVisibility(View.GONE);
                                if (e.getMessage().equals("Invalid Token")) {
                                    startActivity(new Intent(AddNewItemActivity.this, LoginActivity.class));
                                }
                                Log.d("Error", e.getMessage());
                                Toast.makeText(AddNewItemActivity.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
