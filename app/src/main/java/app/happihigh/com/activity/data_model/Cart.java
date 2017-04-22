package app.happihigh.com.activity.data_model;

import android.graphics.Bitmap;

/**
 * Created by 312817 on 4/20/2017.
 */

public class Cart {
    private String dish_name;
    private Bitmap dish_image;
    private String dish_qty;
    private String dish_price;


    public String getDish_name() {
        return dish_name;
    }

    public void setDish_name(String dish_name) {
        this.dish_name = dish_name;
    }

    public Bitmap getDish_image() {
        return dish_image;
    }

    public void setDish_image(Bitmap dish_image) {
        this.dish_image = dish_image;
    }

    public String getDish_qty() {
        return dish_qty;
    }

    public void setDish_qty(String dish_qty) {
        this.dish_qty = dish_qty;
    }

    public String getDish_price() {
        return dish_price;
    }

    public void setDish_price(String dish_price) {
        this.dish_price = dish_price;
    }
}
