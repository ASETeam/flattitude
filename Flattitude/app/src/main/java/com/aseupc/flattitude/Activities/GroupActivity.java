package com.aseupc.flattitude.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.aseupc.flattitude.InternalDatabase.DAO.UserDAO;
import com.aseupc.flattitude.Models.Flat;
import com.aseupc.flattitude.Models.User;
import com.aseupc.flattitude.R;
import com.aseupc.flattitude.databasefacade.UserFacade;
import com.aseupc.flattitude.utility_REST.ResultContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class GroupActivity extends AppCompatActivity {
    private ListView invitations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);


        final Button mCreateFlat = (Button) findViewById(R.id.create_flat_button);
        mCreateFlat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createIntent = new Intent(mCreateFlat.getContext(), CreateFlat.class);
                startActivity(createIntent);
            }

        } );
        invitations = (ListView) findViewById(R.id.invitations);
        List<String> population = new ArrayList<String>();
       // population.add("foo");
        UserDAO userDAO = new UserDAO(getApplicationContext());
        User user = userDAO.getUser();
        consultInfo callConsult = new consultInfo();
        ArrayList<Flat> result = new ArrayList<Flat>();
        try {
            result = callConsult.execute(user.getServerid()).get(50000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        ArrayList<String> resultStr = new ArrayList<String>();
        for (int i = 0; i < result.size(); i++)
        {
            resultStr.add(result.get(i).getName());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                resultStr );

        invitations.setAdapter(arrayAdapter);

        final ArrayList<Flat> finalFlats = result;
        final ArrayList<String> finalResult = resultStr;
        invitations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = finalResult.get(position);
                Intent detailsIntent = new Intent(getApplicationContext(), InvitationDetails.class);
                detailsIntent.putExtra("Flatname", selected);
                detailsIntent.putExtra("FlatId", finalFlats.get(position).getServerid());
                startActivity(detailsIntent);
            }
        });
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

    public class consultInfo extends AsyncTask<String, Void, ArrayList<Flat>>{

        @Override
        protected ArrayList<Flat> doInBackground(String... params) {
            ResultContainer<User> result = UserFacade.consultInvitations(params[0]);
            ArrayList<Flat> invitations = result.getTemplate().getInvitations();

            return invitations;
        }
    }
}
