package com.aseupc.flattitude.Activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.aseupc.flattitude.Models.BudgetOperation;
import com.aseupc.flattitude.Models.Flat;
import com.aseupc.flattitude.Models.IDs;
import com.aseupc.flattitude.Models.User;
import com.aseupc.flattitude.R;
import com.aseupc.flattitude.utility_REST.ArrayAdapterWithIcon;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BudgetActivity extends AppCompatActivity {
    private Context context;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        context = this;
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_logo_app);

        setTitle("Budget Management");

        double flatBalance = IDs.getInstance(context).getBalance();
        TextView flatBalanceView = (TextView) findViewById(R.id.commonBudgetBalance);
        flatBalanceView.setText(flatBalance + "€");

        double personalBalance = IDs.getInstance(context).getPersonalExpense();
        TextView personalBalanceView = (TextView) findViewById(R.id.personalBudgetBalance);

        GradientDrawable bgPersonalShape = (GradientDrawable)personalBalanceView.getBackground();
        bgPersonalShape.setColor(Color.parseColor("#d0e3e4"));
        personalBalanceView.setText(personalBalance + "€");

        Flat currentFlat = IDs.getInstance(context).getFlat(context);
        User currentUser = IDs.getInstance(context).getUser(context);

        // List of fictives operation in order to test
        ArrayList<BudgetOperation> operations = new ArrayList<BudgetOperation>();
        operations.add(new BudgetOperation(currentUser, currentFlat, new Date(), 10, "Putting 10 € on the common account"));
        operations.add(new BudgetOperation(null, currentFlat, new Date(), 10, "Shopping for common food"));
        operations.add(new BudgetOperation(currentUser, currentFlat, new Date(), 1, "Putting 1 € on the common account"));

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        String keys[] = {"date", "title", "description"};

        HashMap<String, String> element;
        DateFormat dateFormat = new SimpleDateFormat("MM/dd");
        for (BudgetOperation operation : operations) {
            element = new HashMap<String, String>();

            element.put(keys[0], dateFormat.format(operation.getDate())+": ");
            element.put(keys[1], operation.getUser() == null ?
                    "" + operation.getAmount() + "€ were used from the common budget" :
                    operation.getUser().getLastname() + " put " + operation.getAmount() + "€ on the common budget");
            element.put(keys[2], operation.getDescription());
            list.add(element);
        }

        ListAdapter adapter = new SimpleAdapter(this, list,
                R.layout.budget_list_element,
                keys,
                new int[] {R.id.date_budget_operation, R.id.title_budget_operation, R.id.description_budget_operation});

        ListView operationsListView = (ListView) findViewById(R.id.listView_budget);
        operationsListView.setAdapter(adapter);

//        //operationsListView.setVisibility(View.VISIBLE);
//        ArrayList<String> resultStr = new ArrayList<String>();
//        ArrayList<Integer> imageStr = new ArrayList<Integer>();
//        resultStr.add("Valentin");
//        resultStr.add("Jordi");
//        resultStr.add("Fara");
//        imageStr.add(R.drawable.vavou);
//        imageStr.add(R.drawable.jordi);
//        imageStr.add(R.drawable.fara);
//
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
//                this,
//                android.R.layout.simple_list_item_1,
//                resultStr );
//
//        ArrayAdapterWithIcon adapter=new ArrayAdapterWithIcon(this, resultStr, imageStr);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_budget, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
