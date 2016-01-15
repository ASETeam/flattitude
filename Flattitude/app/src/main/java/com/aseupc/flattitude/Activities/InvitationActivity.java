package com.aseupc.flattitude.Activities;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aseupc.flattitude.InternalDatabase.DAO.FlatDAO;
import com.aseupc.flattitude.InternalDatabase.DAO.UserDAO;
import com.aseupc.flattitude.Models.Flat;
import com.aseupc.flattitude.Models.User;
import com.aseupc.flattitude.databasefacade.FlatFacade;
import com.aseupc.flattitude.R;
import com.aseupc.flattitude.utility_REST.CallAPI;
import com.aseupc.flattitude.utility_REST.ResultContainer;
import com.etsy.android.grid.StaggeredGridView;

import java.util.ArrayList;

public class InvitationActivity extends AppCompatActivity {
    private EditText mInvitee;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);
        context = this;
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_logo_app);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        SpannableString s = new SpannableString("Flattitude");
        s.setSpan(new ForegroundColorSpan(Color.rgb(33, 33, 33)),0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new TypefaceSpan("Montserrat-Bold.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setTitle(s);

        mInvitee = (EditText) findViewById(R.id.invitee);

        ArrayList<String> resultStr = new ArrayList<String>();
        ArrayList<Integer> imageStr = new ArrayList<Integer>();

        resultStr.add("Jordi Flattitude");
        resultStr.add("Guille Flattitude");
        resultStr.add("Anisa Flattitude");
        resultStr.add("Valentin Flattitude");
        resultStr.add("Fara  Flattiude");
            imageStr.add(R.drawable.ic_user);
        imageStr.add(R.drawable.ic_user);
        imageStr.add(R.drawable.ic_userf);
        imageStr.add(R.drawable.ic_user);
        imageStr.add(R.drawable.ic_userf);

        ArrayAdapterWithIcon adapter = new ArrayAdapterWithIcon(this, resultStr, imageStr);

        StaggeredGridView gridView = (StaggeredGridView) findViewById(R.id.grid_view);

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CallAPI.makeToast(context, "Let's not go there!");
            }
        });



        mInvitee.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            mInvitee.setText("");
            }
        });
        Button mSendInvite = (Button) findViewById(R.id.send_invitation);
        mSendInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean processed = false;


                String toInvite = (String) mInvitee.getText().toString();
                String userID;
                String flatID;


                UserDAO userDAO = new UserDAO(context);
                User me = userDAO.getUser();
                if (me != null) {
                 userID = me.getServerid();}
                else{
                     userID = "2";
                }


                FlatDAO flatDAO = new FlatDAO(context);
                Flat flat = flatDAO.getFlat();
                if (flat != null)
                { flatID = flat.getServerid();
                }else {
                 flatID = "0";}


                ResultContainer<Flat> result = FlatFacade.inviteMember(userID, flatID, toInvite);
                processed = result.getSucces();


                if (processed)
                {
                    CharSequence text = "Invitation sent !";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                     toast.show();
                }
                else {
                    // CharSequence text = result.getReason.... ;
                    CharSequence text = "This user is not yet using Flattitude !";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_invitation, menu);
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
