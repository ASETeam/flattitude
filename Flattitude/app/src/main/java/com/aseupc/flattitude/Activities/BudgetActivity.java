package com.aseupc.flattitude.Activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.aseupc.flattitude.Models.IDs;
import com.aseupc.flattitude.R;
import com.aseupc.flattitude.utility_REST.ArrayAdapterWithIcon;

import java.util.ArrayList;

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

        ListView listView = (ListView) findViewById(R.id.listView_budget);
        listView.setVisibility(View.VISIBLE);
        ArrayList<String> resultStr = new ArrayList<String>();
        ArrayList<Integer> imageStr = new ArrayList<Integer>();
         resultStr.add("Valentin");
        resultStr.add("Jordi");
        resultStr.add("Fara");
        imageStr.add(R.drawable.vavou);
        imageStr.add(R.drawable.jordi);
        imageStr.add(R.drawable.fara);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                resultStr );

        ArrayAdapterWithIcon adapter=new ArrayAdapterWithIcon(this, resultStr, imageStr);

        listView.setAdapter(adapter);
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
