package app.happihigh.application;

import android.support.multidex.MultiDexApplication;

import app.happihigh.com.activity.other.Utility;

/**
 * Created by 312817 on pizza/16/2017.
 */

public class HappiHighApplication extends MultiDexApplication{

    @Override
    public void onCreate() {
        super.onCreate();
        Utility utility = Utility.getInstance();
        utility.setContext(getApplicationContext());
    }
}
