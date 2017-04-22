package app.happihigh.com.activity.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import app.happihigh.com.activity.adapter.CartListAdapter;
import app.happihigh.com.activity.data_model.Cart;

import app.happihigh.com.activity.other.Utility;
import app.happihigh.com.happihigh.R;

public class CartActivity extends AppCompatActivity {
    public static final String TAG = CartActivity.class.getCanonicalName();
    private static CartListAdapter.MyClickListener myClickListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    int dish_original_price=0;
    Utility utility;
    String res_name;
    TextView restro_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        utility = Utility.getInstance();

        mRecyclerView = (RecyclerView) findViewById(R.id.cart_list_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(utility.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CartListAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(utility.getContext(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        Bundle extras = getIntent().getExtras();
        res_name = extras.getString("restro_name");
        restro_name = (TextView) findViewById(R.id.restaurant_name);
        restro_name.setText(res_name);


        Toast.makeText(utility.getContext(), "Cart Loaded", Toast.LENGTH_LONG).show();
    }


    private ArrayList<Cart> getDataSet() {
        ArrayList results = new ArrayList<Cart>();
        ArrayList<HashMap<String,String>> cart_data = utility.getCart_list();
        ArrayList<HashMap<String,Bitmap>> img_lilst = utility.getImg_list();
        for (int index = 0; index < cart_data.size(); index++) {
            //Bitmap icon = BitmapFactory.decodeResource(utility.getContext().getResources(), R.drawable.restaurant6);

            Cart obj = new Cart();

            obj.setDish_name(cart_data.get(index).get("dish_name"));

            obj.setDish_price(cart_data.get(index).get("dish_price"));
            if(img_lilst.size()>0) {
                Bitmap icon = img_lilst.get(index).get("dish_img");
                obj.setDish_image(icon);
            }

            obj.setDish_qty(cart_data.get(index).get("dish_qty"));
            results.add(index, obj);
        }
        return results;
    }

    @Override
    public void onResume() {
        super.onResume();

        ((CartListAdapter) mAdapter).setOnItemClickListener(new CartListAdapter.MyClickListener()
        {
            @Override
            public void onItemClick ( int position, View v){
                final View cart_view = v;
                Log.e(TAG, " Clicked on Item " + position);

                ImageView add_qty = (ImageView) v.findViewById(R.id.add_qty);
                ImageView remove_qty = (ImageView) v.findViewById(R.id.remove_qty);
                add_qty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updatedUI(cart_view,true);
                    }
                });
                remove_qty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updatedUI(cart_view,false);
                    }
                });

            }
        });

    }

    public void updatedUI(View v , boolean add){
        final TextView cart_dish_name = (TextView) v.findViewById(R.id.crt_dish_name);
        final TextView cart_dish_price = (TextView) v.findViewById(R.id.crt_dish_price);
        final TextView cart_dish_qty = (TextView) v.findViewById(R.id.quantity_dish);

        int current_qty = Integer.parseInt(cart_dish_qty.getText().toString());
        String dish_price_string = cart_dish_price.getText().toString();
        String res_price = dish_price_string.replaceAll("[^0-9?!\\.]","");
        if(dish_original_price == 0) {
            dish_original_price = (Integer.parseInt(res_price) / current_qty);
            Log.e(TAG, "Dish Original price : " + dish_original_price);
        }
        if(add) {
            Log.e(TAG,"Clicked on add qty of : "+cart_dish_name.getText().toString());
            current_qty = current_qty + 1;
            int calculated_price = dish_original_price*current_qty;
            cart_dish_qty.setText(Integer.toString(current_qty));

            cart_dish_price.setText("Rs "+calculated_price);

        }else{
            Log.e(TAG,"Clicked on remove qty of : "+cart_dish_name.getText().toString());
            current_qty = current_qty - 1;
            int calculated_price = dish_original_price*current_qty;
            if(current_qty >= 0) {
                cart_dish_qty.setText(Integer.toString(current_qty));
                cart_dish_price.setText("Rs " + calculated_price);
            }
        }
    }


}
