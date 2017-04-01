package app.happihigh.com.activity.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import app.happihigh.com.activity.activity.MenuActivity;
import app.happihigh.com.activity.adapter.RestaurantAdapter;
import app.happihigh.com.activity.data_model.RestaurantObject;
import app.happihigh.com.activity.other.Utility;
import app.happihigh.com.activity.service.GPSTracker;
import app.happihigh.com.happihigh.R;


public class HomeFragment extends Fragment implements ConnectionCallbacks, OnConnectionFailedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "HomeFragment";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int no_of_restro_found;
    private OnFragmentInteractionListener mListener;

    String mprovider;
    Utility utility;
    // GPSTracker class
    GPSTracker gps;
    private Marker marker;

    MapView mMapView;
    private GoogleMap googleMap;
    double latitude;
    double longitude;
    TextView location_name;

    String current_address;
    public HomeFragment() {
        // Required empty public constructor
    }

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "RecyclerViewActivity";

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utility = Utility.getInstance();
        utility.setContext(getContext());
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        gps = new GPSTracker(utility.getContext());
        // check if GPS enabled
        if (gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            current_address = getCompleteAddressString(latitude,longitude);
            // \n is for new line
           /* Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                    + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();*/
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            Log.e(TAG,"can't get location GPS or Network is not enabled Ask user to enable GPS/network in settings");
            //gps.showSettingsAlert();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately


        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(utility.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(utility.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(latitude, longitude);
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        //set current address
        location_name = (TextView) v.findViewById(R.id.location);
        location_name.setText(current_address);



        mRecyclerView = (RecyclerView) v.findViewById(R.id.restaurant_list_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(utility.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RestaurantAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(utility.getContext(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        Toast.makeText(utility.getContext(),(no_of_restro_found+1)+"Restaurants Found ",Toast.LENGTH_LONG ).show();
        return v;
    }

    private ArrayList<RestaurantObject> getDataSet() {
        ArrayList results = new ArrayList<RestaurantObject>();
        for (int index = 0; index < 20; index++) {
            no_of_restro_found = index;
            Bitmap icon = BitmapFactory.decodeResource(utility.getContext().getResources(),R.drawable.restaurant6);
            RestaurantObject obj = new RestaurantObject();
            obj.setRes_name("Restaurant Name : " + index);
            obj.setAddress("Address : " + index);
            obj.setRes_Image(icon);
            results.add(index, obj);
        }
        return results;
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(utility.getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.e(TAG,"My Current loction address" + strReturnedAddress.toString());
            } else {
                Log.e(TAG,"No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Canont get Address!");
        }
        return strAdd;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        // Implementing ActionBar Search inside a fragment
        MenuItem item = menu.add("Search");
        item.setIcon(R.drawable.search); // sets icon
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        SearchView sv = new SearchView(getActivity());

        // modifying the text inside edittext component
        int id = sv.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) sv.findViewById(id);
        textView.setHint("Search location...");
        textView.setHintTextColor(getResources().getColor(android.R.color.darker_gray
        ));
        textView.setTextColor(getResources().getColor(android.R.color.holo_blue_bright));

        // implementing the listener
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.length() < 4) {
                    Toast.makeText(getActivity(),
                            "Your search query must not be less than pizza characters",
                            Toast.LENGTH_LONG).show();
                    return true;
                } else {
                    Log.e("----------- ","Do search : "+s);
                    //doSearch(s);
                    return false;
                }
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        item.setActionView(sv);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    public void searchRestaurants(){
        Toast.makeText(getContext(),"Clicked on search",Toast.LENGTH_LONG).show();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        ((RestaurantAdapter) mAdapter).setOnItemClickListener(new RestaurantAdapter.MyClickListener()
        {
            @Override
            public void onItemClick ( int position, View v){
                Intent menu_intent = new Intent(utility.getContext(), MenuActivity.class);
                String name = (String) ((TextView)v.findViewById(R.id.name)).getText();
                Log.e(TAG,"Restaurant Name : "+name);

                ImageView img = (ImageView)v.findViewById(R.id.restaurant_image);
                img.buildDrawingCache();
                Bitmap image= img.getDrawingCache();
                utility.setImg_bitmap(image);
                Bundle extras = new Bundle();
                extras.putString("restro_name",name);
                menu_intent.putExtras(extras);
                startActivity(menu_intent);

            Log.e(LOG_TAG, " Clicked on Item " + position);
        }
        });

    }


    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        //Disconnect from API onPause()

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();

    }
}
