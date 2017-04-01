package app.happihigh.com.activity.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import app.happihigh.com.happihigh.R;

public class SplashActivity extends Activity {
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        activity = this;

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        },3000);


    }
}
