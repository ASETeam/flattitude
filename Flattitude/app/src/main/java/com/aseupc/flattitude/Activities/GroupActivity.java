package com.aseupc.flattitude.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.aseupc.flattitude.InternalDatabase.DAO.FlatDAO;
import com.aseupc.flattitude.InternalDatabase.DAO.NotificationsDAO;
import com.aseupc.flattitude.InternalDatabase.DAO.UserDAO;
import com.aseupc.flattitude.Models.Flat;
import com.aseupc.flattitude.Models.IDs;
import com.aseupc.flattitude.Models.Notification;
import com.aseupc.flattitude.Models.User;
import com.aseupc.flattitude.R;
import com.aseupc.flattitude.databasefacade.UserFacade;
import com.aseupc.flattitude.utility_REST.CallAPI;
import com.aseupc.flattitude.utility_REST.ResultContainer;
import com.asha.ChromeLikeSwipeLayout;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity {
    private ListView invitations;
    private View mProgressView;
    public static Menu menu;
    public static Context currentContext;
    public static MenuItem mItem;

    public Menu getMenu() {
        return menu;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        UserDAO userDAO = new UserDAO(getApplicationContext());
        User user = userDAO.getUser();
        final User userF = user;
        ChromeLikeSwipeLayout chromeLikeSwipeLayout = (ChromeLikeSwipeLayout) findViewById(R.id.chrome_like_swipe_layout);
        ChromeLikeSwipeLayout.makeConfig()
                .addIcon(R.drawable.ic_refresh)

                .radius(20)
                .gap(10)
                .circleColor(0xFF11CCFF)
                .listenItemSelected(new ChromeLikeSwipeLayout.IOnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int index) {
                        showProgress(true);
                        Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_SHORT).show();
                        consultInfo callConsult = new consultInfo();
                        callConsult.execute(userF.getServerid());
                    }
                })
                .setTo(chromeLikeSwipeLayout);
        mProgressView = findViewById(R.id.group_progress);
        showProgress(true);


        final Button mCreateFlat = (Button) findViewById(R.id.budget_button);
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

        consultInfo callConsult = new consultInfo();
        ArrayList<Flat> result = new ArrayList<Flat>();

        callConsult.execute(user.getServerid());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);


            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_create_group, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.global, menu);
        this.menu = menu;
        this.mItem = menu.getItem(1);
        this.currentContext =  getApplicationContext();
        menu.getItem(1).setVisible(false);
        menu.getItem(0).setVisible(false);
        return super.onCreateOptionsMenu(menu);
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
        } else if (id == R.id.logout)
        {
            UserDAO userDAO = new UserDAO(getApplicationContext());
            User thisUser = userDAO.getUser();

            if (thisUser != null)
            {
                // UserFacade.logoutUser(thisUser.getServerid(), thisUser.getToken());

                userDAO.deleteDept(thisUser);
                userDAO.deleteAll();
                //   Log.i("AfterDeletion", userDAO.getUser().getEmail());
                FlatDAO flatDAO = new FlatDAO(getApplicationContext());
                Flat thisFlat = flatDAO.getFlat();

                flatDAO.deleteAll();
                if (thisFlat != null)
                    flatDAO.deleteDept(thisFlat);
            }
            IDs.resetIDs();

            userDAO.deleteAll();
            FlatDAO flatDAO = new FlatDAO(getApplicationContext());
            flatDAO.deleteAll();
            NotificationsDAO notDAO = new NotificationsDAO(getApplicationContext());
            //  notDAO.deleteAll();
            Intent ReturnHome = new Intent(getApplicationContext(), LandingActivity.class);
            startActivity(ReturnHome);

        }

        return super.onOptionsItemSelected(item);
    }

    private void populateInvitations(ArrayList<Flat> flats)
    {
        showProgress(false);
        ArrayList<String> resultStr = new ArrayList<String>();
        for (int i = 0; i < flats.size(); i++)
        {
            resultStr.add(flats.get(i).getName());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                resultStr );


        invitations.setAdapter(arrayAdapter);

        final ArrayList<Flat> finalFlats = flats;
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

    public class consultInfo extends AsyncTask<String, Void, ArrayList<Flat>>{

        @Override
        protected ArrayList<Flat> doInBackground(String... params) {
            ResultContainer<User> result = UserFacade.consultInvitations(params[0]);
            ArrayList<Flat> invitations = result.getTemplate().getInvitations();

            return invitations;
        }

        @Override
        protected void onPostExecute(ArrayList<Flat> flats) {
            super.onPostExecute(flats);
            populateInvitations(flats);
        }
    }
}
