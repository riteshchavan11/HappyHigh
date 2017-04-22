package app.happihigh.com.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import app.happihigh.com.activity.data_model.Cart;
import app.happihigh.com.activity.other.Utility;
import app.happihigh.com.happihigh.R;

/**
 * Created by 312817 on 4/20/2017.
 */

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.DataObjectHolder>  {
    private static final String TAG = CartListAdapter.class.getCanonicalName();
    public static Utility utility = Utility.getInstance();
    private static String LOG_TAG = "CartListAdapter";
    private ArrayList<Cart> mDataset;
    private static CartListAdapter.MyClickListener myClickListener;
    CartListAdapter.DataObjectHolder cart_holder;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener  {

        TextView dish_name;
        TextView dish_price;
        TextView dish_qty;
        ImageView dish_img;
        ImageView add_item;
        ImageView remove_item;
        public DataObjectHolder(View itemView) {
            super(itemView);
            dish_name = (TextView) itemView.findViewById(R.id.crt_dish_name);
            dish_price = (TextView) itemView.findViewById(R.id.crt_dish_price);
            dish_qty = (TextView) itemView.findViewById(R.id.quantity_dish);
            dish_img = (ImageView) itemView.findViewById(R.id.crt_dish_img);
            add_item = (ImageView) itemView.findViewById(R.id.add_qty);
            remove_item = (ImageView) itemView.findViewById(R.id.remove_qty);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }


    }
    public CartListAdapter(ArrayList<Cart> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public CartListAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_list, parent, false);

        CartListAdapter.DataObjectHolder dataObjectHolder = new CartListAdapter.DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(CartListAdapter.DataObjectHolder holder, int position) {
        cart_holder = holder;
        holder.dish_name.setText(mDataset.get(position).getDish_name());
        holder.dish_price.setText(mDataset.get(position).getDish_price());
        holder.dish_qty.setText(mDataset.get(position).getDish_qty());
        holder.dish_img.setImageBitmap(mDataset.get(position).getDish_image());

    }


    public void addItem(Cart dataObj, int index) {
        mDataset.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setOnItemClickListener(CartListAdapter.MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }


}
