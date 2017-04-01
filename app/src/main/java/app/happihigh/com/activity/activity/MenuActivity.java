package app.happihigh.com.activity.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import app.happihigh.com.activity.other.Utility;
import app.happihigh.com.happihigh.R;
import app.happihigh.widgets.AnimatedExpandableListView;

public class MenuActivity extends AppCompatActivity{
    private AnimatedExpandableListView listView;
    private ExampleAdapter adapter;

    String[] menu = {"Main course","Appetizers","Pasta","Pizza","Drinks"};
    String[] drinks = {"Beer","Cider","Cocktails","Hard soda","Wine"};
    String[] main_course = {"Chicken Kadhai","Murg Khasta","Mutton Handi","Fish Curry","Murg Makhani"};
    String[] appetizers = {"Chocolate Chip Cheese Ball","Beer Dip","Cranberry Chili Meatballs","Deviled Eggs","Chicken Garlic Bites"};
    String[] pasta = {"Barbina","Bucatini","Fusilli","Matriciani","Maccheroni alla molinara"};
    String[] pizza = {"Double Cheese Pizza","Mexican Green Wave","Margherita","Quattro Formaggi","Mexicana"};
    public static final String TAG = "Menu Activity";
    Bitmap res_img_bmp;
    String res_name;

    ImageView restro_img;
    TextView restro_name;

    Utility utility;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        utility = Utility.getInstance();
        restro_img = (ImageView) findViewById(R.id.restro_img);
        restro_name = (TextView) findViewById(R.id.restro_name);

        Bundle extras = getIntent().getExtras();
        res_img_bmp = utility.getImg_bitmap();
        res_name = extras.getString("restro_name");

        restro_img.setImageBitmap(res_img_bmp);
        restro_name.setText(res_name);

        List<GroupItem> items = new ArrayList<GroupItem>();
        // Populate our list with groups and it's children
        Log.e(TAG,"Menu length : "+menu.length);
        for(int i = 0; i < menu.length; i++) {
            GroupItem item = new GroupItem();
            Log.e(TAG,"Title : "+menu[i]);
            item.title = menu[i];
            Bitmap icon = null;
            switch (i){
                case 0:
                    icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.maincourse);
                    item.img = icon;
                    for(int j = 0; j < main_course.length; j++) {
                        ChildItem child = new ChildItem();
                        child.title = main_course[j];
                        Random rand = new Random();
                        int price = rand.nextInt(100);
                        child.hint = "Rs. 10"+price;

                        item.items.add(child);
                    }

                    break;
                case 1:
                    icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.appetizer);
                    item.img = icon;
                    for(int j = 0; j < appetizers.length; j++) {
                        ChildItem child = new ChildItem();
                        child.title = appetizers[j];
                        Random rand = new Random();
                        int price = rand.nextInt(100);
                        child.hint = "Rs. "+price;

                        item.items.add(child);
                    }

                    break;
                case 2:
                    icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.pasta);
                    item.img = icon;
                    for(int j = 0; j < pasta.length; j++) {
                        ChildItem child = new ChildItem();
                        child.title = pasta[j];
                        Random rand = new Random();
                        int price = rand.nextInt(100);
                        child.hint = "Rs. "+price;

                        item.items.add(child);
                    }

                    break;
                case 3:
                    icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.pizza);
                    item.img = icon;
                    for(int j = 0; j < pizza.length; j++) {
                        ChildItem child = new ChildItem();
                        child.title = pizza[j];
                        Random rand = new Random();
                        int price = rand.nextInt(100);
                        child.hint = "Rs. "+price;

                        item.items.add(child);
                    }

                    break;
                case 4:
                    icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.drinks);
                    item.img = icon;
                    for(int j = 0; j < drinks.length; j++) {
                        ChildItem child = new ChildItem();
                        child.title = drinks[j];
                        Random rand = new Random();
                        int price = rand.nextInt(100);
                        child.hint = "Rs. "+price;

                        item.items.add(child);
                    }

                    break;

                default:
                    break;
            }

            items.add(item);
        }

        adapter = new ExampleAdapter(this);
        adapter.setData(items);

        listView = (AnimatedExpandableListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.
                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);
                } else {
                    listView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }

        });
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Log.e(TAG," i : "+i+"\n i1 : "+i1+"\n l : "+l);
                
                showdialog();
                return false;
            }
        });
    }

    private static class GroupItem {
        String title;
        Bitmap img;
        List<ChildItem> items = new ArrayList<ChildItem>();
    }

    private static class ChildItem {
        String title;
        String hint;
    }

    private static class ChildHolder {
        TextView title;
        TextView hint;
    }

    private static class GroupHolder {
        TextView title;
        ImageView logo_img;
    }

    /**
     * Adapter for our list of {@link GroupItem}s.
     */
    private class ExampleAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
        private LayoutInflater inflater;

        private List<GroupItem> items;

        public ExampleAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }
        public void setData(List<GroupItem> items) {
            this.items = items;
        }

        @Override
        public ChildItem getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).items.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder;
            ChildItem item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(R.layout.list_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.dish_name);
                holder.hint = (TextView) convertView.findViewById(R.id.dish_price);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }

            holder.title.setText(item.title);
            holder.hint.setText(item.hint);

            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return items.get(groupPosition).items.size();
        }

        @Override
        public GroupItem getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder holder;
            GroupItem item = getGroup(groupPosition);
            if (convertView == null) {
                holder = new GroupHolder();
                convertView = inflater.inflate(R.layout.group_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.tilte);
                holder.logo_img = (ImageView) convertView.findViewById(R.id.menu_img);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }

            holder.title.setText(item.title);
            holder.logo_img.setImageBitmap(item.img);
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }


        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }

    }

    public  void showdialog(){
        AlertDialog alertDialog = null;
        final AlertDialog.Builder d = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_dish_details, null);
        d.setView(dialogView);
        final TextView quantity = (TextView) dialogView.findViewById(R.id.quantity);
        final Spinner spinner =  (Spinner) dialogView.findViewById(R.id.quantity_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.quantity_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                quantity.setText("Quantity : "+i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        final ImageView cart = (ImageView) dialogView.findViewById(R.id.cart);
        alertDialog = d.create();


        final AlertDialog finalAlertDialog = alertDialog;
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Added to cart",Toast.LENGTH_LONG).show();
                if(finalAlertDialog.isShowing())
                    finalAlertDialog.dismiss();

            }
        });

        d.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(finalAlertDialog.isShowing())
                    finalAlertDialog.dismiss();

            }
        });

        alertDialog.show();
    }
}
