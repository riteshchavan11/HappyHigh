package app.happihigh.com.activity.data_model;

/**
 * Created by 312817 on 3/19/2017.
 */

public class RestaurantObject {
    private String res_name;
    private String res_Image_url;

    public RestaurantObject(String name, String image){
        res_name = name;
        res_Image_url = image;
    }

    public String getRes_name() {
        return res_name;
    }

    public void setRes_name(String res_name) {
        this.res_name = res_name;
    }

    public String getRes_Image_url() {
        return res_Image_url;
    }

    public void setRes_Image_url(String res_Image_url) {
        this.res_Image_url = res_Image_url;
    }
}
