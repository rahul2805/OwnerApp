package com.abc.templateownerapp.Model;

public class Item {
    String itemName, itemDescription, itemPrice, imageURL;
    Integer itemStock, itemId;
    public Item(String name, String description, String price, String url, Integer stock, Integer id) {
        itemName = name;
        itemDescription = description;
        itemPrice = price;
        imageURL = url;
        itemStock = stock;
        itemId = id;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public Integer getItemId() {
        return itemId;
    }

    public Integer getItemStock() {
        return itemStock;
    }
}
