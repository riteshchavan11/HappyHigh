package app.happihigh.com.activity.other;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 312817 on pizza/15/2017.
 */

public class Utility {
    private static Utility instance;

    public static Utility getInstance() {
        if (instance== null) {
            instance = new Utility();
        }
        return instance;
    }

    Bitmap img;

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isLoggedOut;

    public boolean isLoggedOut() {
        return isLoggedOut;
    }

    public void setLoggedOut(boolean loggedOut) {
        isLoggedOut = loggedOut;
    }

    public boolean isLoggedIn;

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    private GoogleApiClient googleApiClient;

    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
    }

    private Activity activity;

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    private Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    private Bitmap img_bitmap;

    public Bitmap getImg_bitmap() {
        return img_bitmap;
    }

    public void setImg_bitmap(Bitmap img_bitmap) {
        this.img_bitmap = img_bitmap;
    }

    private ArrayList<HashMap<String,String>> cart_list;

    public ArrayList<HashMap<String, String>> getCart_list() {
        return cart_list;
    }

    public void setCart_list(ArrayList<HashMap<String, String>> cart_list) {
        this.cart_list = cart_list;
    }

    private ArrayList<HashMap<String, Bitmap>> img_list;

    public ArrayList<HashMap<String, Bitmap>> getImg_list() {
        return img_list;
    }

    public void setImg_list(ArrayList<HashMap<String, Bitmap>> img_list) {
        this.img_list = img_list;
    }
}
