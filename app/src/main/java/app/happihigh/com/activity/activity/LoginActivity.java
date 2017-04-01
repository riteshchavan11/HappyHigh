package app.happihigh.com.activity.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import app.happihigh.com.activity.other.Utility;
import app.happihigh.com.happihigh.R;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 100;
    private final String TAG = "HappiHigh";
    ImageView fblogin, google_login;
    Utility utility;
    private CallbackManager callbackManager;
    Activity act;
    ProgressDialog progressDialog;
    SignInButton signInButton;
    GoogleApiClient mGoogleApiClient;
    GoogleSignInAccount acct;
    Context context;

    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        act = this;
        context = getApplicationContext();
        utility = Utility.getInstance();

        utility.setActivity(act);
        utility.setContext(context);
        final LocationManager manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE );
        //ask permission
        if (!checkPermission()) {

            requestPermission();

        } else {

            Log.e("Ask Permission", "Permission already granted.");

        }

        //check gps enable
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) )
            Toast.makeText(context, "GPS is disable!", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context, "GPS is Enable!", Toast.LENGTH_LONG).show();






        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        google_login = (ImageView) findViewById(R.id.google_login);
        findViewById(R.id.google_login).setOnClickListener(this);

        AppEventsLogger.activateApp(this);
        fblogin = (ImageView) findViewById(R.id.facebook_login);
        findViewById(R.id.facebook_login).setOnClickListener(this);


    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch(requestCode){
            case PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0){
                    boolean locationAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted){
                        Toast.makeText(getApplicationContext(),"Permission Granted, Now you can access location data", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(),"Permission Denied, You cannot access location data", Toast.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    private void loginfacebook() {
        LoginManager.getInstance().logInWithReadPermissions(act, Arrays.asList("user_friends", "email", "public_profile", "user_birthday"));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        utility.setLoggedIn(true);
                        utility.setLoggedOut(false);
                        fblogin.setEnabled(true);
                        setFacebookData(loginResult);
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.e("------------ ", "Exception : " + exception.getStackTrace());
                    }
                });
    }

    private void setFacebookData(final LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
                        try {
                            Log.i("Response", response.toString());

                            String email = response.getJSONObject().getString("email");
                            String firstName = response.getJSONObject().getString("first_name");
                            String lastName = response.getJSONObject().getString("last_name");
                            String gender = response.getJSONObject().getString("gender");
                            String bday = response.getJSONObject().getString("birthday");

                            utility.setName(firstName + " " + lastName);
                            utility.setEmail(email);

                            Profile profile = Profile.getCurrentProfile();
                            String id = profile.getId();
                            String link = profile.getLinkUri().toString();
                            Log.i("Link", link);
                            if (Profile.getCurrentProfile() != null) {
                                Log.i("Login", "ProfilePic" + Profile.getCurrentProfile().getProfilePictureUri(200, 200));
                            }
                            Log.i("Login" + "Email", email);
                            Log.i("Login" + "FirstName", firstName);
                            Log.i("Login" + "LastName", lastName);
                            Log.i("Login" + "Gender", gender);
                            Log.i("Login" + "Bday", bday);

                            new getProfilepic().execute(response.getJSONObject().getString("id"));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name,gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public static Bitmap getFacebookProfilePicture(String userID) {
        URL imageURL = null;
        Bitmap bitmap = null;
        try {
            imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
            bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return bitmap;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.facebook_login:
                loginfacebook();
                fblogin.setEnabled(false);
                break;
            case R.id.google_login:
                google_login.setEnabled(false);
                  signIn();
                break;
            // ...
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("----------- ", "Google Client Connection : " + connectionResult.getErrorMessage());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void LogoutfromGoogle() {
        Log.e(TAG, "in LogoutfromGoogle");
        Auth.GoogleSignInApi.signOut(utility.getGoogleApiClient()).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.e(TAG, "Status : " + status.getStatusCode() + "\n" + status.getStatusMessage() + "\n" + status.getStatus());
                        updateUI(false);
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            utility.setLoggedIn(true);
            utility.setLoggedOut(false);
            google_login.setEnabled(true);
            acct = result.getSignInAccount();
            Log.e(TAG, "Name : " + acct.getDisplayName());
            utility.setName(acct.getDisplayName());
            utility.setEmail(acct.getEmail());
            Log.e(TAG, "image url : " + acct.getPhotoUrl());
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private void updateUI(boolean b) {
        if (!b)
            Log.e(TAG, "Sign in failed ");
        else {
            new LoadGoogleProfileImage().execute(acct.getPhotoUrl().toString());

        }
    }


    private class getProfilepic extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(act);
            progressDialog.setMessage("Profile picture loading");
            progressDialog.show();
        }


        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap mBitmap = getFacebookProfilePicture(strings[0]);
            return mBitmap;
        }


        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            utility.setImg(bitmap);
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private class LoadGoogleProfileImage extends AsyncTask<String, Void, Bitmap> {

        //ImageView bmImage;

        /*public LoadGoogleProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }*/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(act);
            progressDialog.setMessage("Profile picture loading");
            progressDialog.show();
        }

        protected Bitmap doInBackground(String... uri) {
            String url = uri[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {

            if (result != null) {

                Bitmap resized = Bitmap.createScaledBitmap(result, 200, 200, true);
                utility.setImg(resized);
                //bmImage.setImageBitmap(ImageHelper.getRoundedCornerBitmap(getContext(),resized,250,200,200, false, false, false, false));
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "in onRestart : " + utility.isLoggedOut());

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "is connected : " + mGoogleApiClient.isConnected());
        if (!mGoogleApiClient.isConnected()) {
            Log.e(TAG, "----------- in if --------");
            mGoogleApiClient.connect();
            utility.setGoogleApiClient(mGoogleApiClient);
        } else {
            Log.e(TAG, "----------in else ---------");
        }
        Log.e(TAG, "in onResume : " + utility.isLoggedOut());
        if (utility.isLoggedOut()) {
            //LogoutfromGoogle();
        }
    }
}
