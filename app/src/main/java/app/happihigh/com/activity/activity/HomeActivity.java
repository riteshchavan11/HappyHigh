package app.happihigh.com.activity.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import app.happihigh.com.activity.other.Utility;
import app.happihigh.com.happihigh.R;

public class HomeActivity extends AppCompatActivity {
    Utility utility;
    private CallbackManager callbackManager;
    private TextView info;
    private ImageView profileimg;
    Activity act;
    Button login_button;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = this;
        utility = Utility.getInstance();
        setContentView(R.layout.activity_home);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        AppEventsLogger.activateApp(this);
        login_button = (Button) findViewById(R.id.login_button);
        info = (TextView)findViewById(R.id.fb_details);
        profileimg = (ImageView) findViewById(R.id.fb_profile_pic);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(act, Arrays.asList("user_friends", "email", "public_profile","user_birthday"));

                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                setFacebookData(loginResult);
                            }

                            @Override
                            public void onCancel() {
                            }

                            @Override
                            public void onError(FacebookException exception) {
                                Log.e("------------ ","Exception : "+exception.getStackTrace());
                            }
                        });

            }
        });




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void setFacebookData(final LoginResult loginResult)
    {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
                        try {
                            Log.i("Response",response.toString());

                            String email = response.getJSONObject().getString("email");
                            String firstName = response.getJSONObject().getString("first_name");
                            String lastName = response.getJSONObject().getString("last_name");
                            String gender = response.getJSONObject().getString("gender");
                            String bday= response.getJSONObject().getString("birthday");

                            utility.setName(firstName+" "+lastName);
                            utility.setEmail(email);

                            Profile profile = Profile.getCurrentProfile();
                            String id = profile.getId();
                            String link = profile.getLinkUri().toString();
                            Log.i("Link",link);
                            if (Profile.getCurrentProfile()!=null)
                            {
                                Log.i("Login", "ProfilePic" + Profile.getCurrentProfile().getProfilePictureUri(200, 200));
                            }
                            info.setText("Email : "+ email+"\n Name : "+firstName+" "+lastName+"\n Gender : "+gender+"\n Birth Date : "+bday);
                            Log.i("Login" + "Email", email);
                            Log.i("Login"+ "FirstName", firstName);
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

    private class getProfilepic extends AsyncTask<String,Void,Bitmap> {

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
            profileimg.setImageBitmap(bitmap);
            if(progressDialog.isShowing())
                progressDialog.dismiss();
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

}
