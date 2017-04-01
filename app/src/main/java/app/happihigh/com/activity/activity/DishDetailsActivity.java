package app.happihigh.com.activity.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import app.happihigh.com.happihigh.R;

public class DishDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView quantity;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_details);

        quantity = (TextView) findViewById(R.id.quantity);

        spinner = (Spinner) findViewById(R.id.quantity_spinner);
        spinner.setOnItemSelectedListener(this);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.quantity_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.e("--at Dish Act--","Quantity : "+i+"\n ---------------- : "+l);
        quantity.setText("Quantity : "+i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
