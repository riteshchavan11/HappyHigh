package app.happihigh.com.activity.other;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by 312817 on 3/15/2017.
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


}
