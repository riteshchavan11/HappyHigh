package app.happihigh.com.activity.data_model;

import android.graphics.Bitmap;

/**
 * Created by 312817 on pizza/19/2017.
 */

public class RestaurantObject {
    private String res_name;
    private String address;
    private Bitmap res_Image;

    /*public RestaurantObject(String name, String add,Bitmap img){
        this.res_name = name;
        this.address = add;
        this.res_Image = img;

    }*/

    public String getRes_name() {
        return res_name;
    }

    public void setRes_name(String res_name) {
        this.res_name = res_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String add) {
        this.address = add;
    }

    public Bitmap getRes_Image() {
        return res_Image;
    }

    public void setRes_Image(Bitmap res_Image) {
        this.res_Image = res_Image;
    }
}
