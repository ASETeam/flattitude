package com.aseupc.flattitude.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.aseupc.flattitude.R;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity {
    private ListView invitations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        final Button mGoHome = (Button) findViewById(R.id.home_button);
        mGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(mGoHome.getContext(), MainActivity.class);
                startActivity(homeIntent);
            }

        });
        final Button mCreateFlat = (Button) findViewById(R.id.create_flat_button);
        mGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createIntent = new Intent(mCreateFlat.getContext(), CreateFlat.class);
                startActivity(createIntent);
            }

        } );
        invitations = (ListView) findViewById(R.id.invitations);
        List<String> population = new ArrayList<String>();
        population.add("foo");
        population.add("bar");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                population );

        invitations.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_group, menu);
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
