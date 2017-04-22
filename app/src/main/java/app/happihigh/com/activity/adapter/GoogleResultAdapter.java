package app.happihigh.com.activity.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.happihigh.com.activity.domain.Result;
import app.happihigh.com.happihigh.R;

/**
 * Created by 312817 on 4/15/2017.
 */

public class GoogleResultAdapter extends ArrayAdapter<Result> implements View.OnClickListener  {
    private ArrayList<Result> dataSet;
    Context mContext;

    public GoogleResultAdapter(ArrayList<Result> data, Context context) {
        super(context, R.layout.restaurant_list_view, data);
        this.dataSet = data;
        this.mContext=context;
    }

    private static class ViewHolder {
        TextView txtName;
        TextView vicinity;
        TextView rating;
        TextView openNow;

    }

    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Log.e("position ","position is "+position);


    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Result result = getItem(position);
        ViewHolder viewHolder;
        final View result_view;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.restaurant_list_view, parent, false);

           /* viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.vicinity = (TextView) convertView.findViewById(R.id.vicinity);
            viewHolder.rating= (TextView) convertView.findViewById(R.id.rating);
            viewHolder.openNow = (TextView) convertView.findViewById(R.id.opennow);*/

            result_view = convertView;
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
            result_view = convertView;
        }
        lastPosition = position;

        viewHolder.txtName.setText(result.getName());
        viewHolder.vicinity.setText(result.getVicinity());
        viewHolder.rating.setText(String.valueOf(result.getRating()));
        //viewHolder.openNow.setText(String.valueOf(result.getOpeningHours().getOpenNow()));

        return convertView;
    }


}
