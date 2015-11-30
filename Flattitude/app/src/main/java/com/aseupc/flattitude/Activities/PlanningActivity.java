package com.aseupc.flattitude.Activities;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;

import com.aseupc.flattitude.InternalDatabase.DAO.PlanningDAO;
import com.aseupc.flattitude.Models.PlanningTask;
import com.aseupc.flattitude.R;
import com.aseupc.flattitude.utility_REST.CallAPI;
import com.aseupc.flattitude.utility_REST.ListAdapter;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import hirondelle.date4j.DateTime;

public class PlanningActivity extends AppCompatActivity {
    private CalendarView mCalendar;
    private TreeSet<Date> backgrounddates;
    private ListView plans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning);
        plans = (ListView) findViewById(R.id.plans_listview);
        backgrounddates = new TreeSet<Date>();
        computeCalendar();

    }

    public void computeCalendar(){
        mCalendar = (CalendarView) findViewById(R.id.calendarView);
        final CaldroidFragment caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendarView, caldroidFragment);
        t.commit();

        PlanningDAO planDAO = new PlanningDAO(getApplicationContext());
        List<PlanningTask> tasks = planDAO.getPlanningTasks();
        for (PlanningTask task:tasks
                ) {
            Date date = new Date(task.getPlannedTime().get(Calendar.YEAR)-1900,task.getPlannedTime().get(Calendar.MONTH), task.getPlannedTime().get(Calendar.DATE));
            Date date2 = new Date();
            date2.setMonth(task.getPlannedTime().get(Calendar.MONTH));
            date2.setYear(task.getPlannedTime().get(Calendar.YEAR) - 1900);
            date2.setDate(task.getPlannedTime().get(Calendar.DATE));
            caldroidFragment.setBackgroundResourceForDate(R.color.AnasBlue, date);
            backgrounddates.add(date);
            Log.i("FAnas 1", date.toString());
            Log.i("FAnas 2,", date2.toString());
            Log.i("FAnas 3,", task.getPlannedTime().toString());

        }


        caldroidFragment.setCaldroidListener(new CaldroidListener() {
            private Date previous = null;
            @Override
            public void onSelectDate(Date date, View view) {

                caldroidFragment.clearSelectedDates();
                if ((previous!= null) && (backgrounddates.contains(previous))) {
                    Log.i("HAnas", "Entered background");
                    caldroidFragment.setBackgroundResourceForDate(R.color.AnasBlue, previous);
                }
                    PlanningDAO dao = new PlanningDAO(getApplicationContext());
                    GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
                    cal.setTime(date);
                    int month = cal.get(Calendar.MONTH);
                    month++;
                    cal.set(Calendar.MONTH, month);
                   // cal.set(Calendar.MONTH, date.getMonth());
                    //cal.set(Calendar.YEAR, date.getYear());
                    //cal.set(Calendar.DATE, date.getDate());
                    Log.i("FAnas 7", "BEFORE");
                    Log.i("FAnas 7.2", date.toString());
                    List<PlanningTask> tasks = dao.getFilteredPlanningTasks(cal);
                    Log.i("FAnas 7.3", PlanningTask.getOnlyDate(cal));
                    for (PlanningTask task:tasks
                         ) {
                        Log.i("FAnas 7.5", task.getID() + " " + PlanningTask.getCleanDate(task.getPlannedTime()));
                     //   CallAPI.makeToast(getApplicationContext(),task.getID() + " " + task.getPlannedTime().getTime().toString() );
                    }
                ArrayList<String> resultStr =  new ArrayList<String>();
                List<PlanningTask> planninTasks =  new ArrayList<PlanningTask>();
                for (int i = 0; i < tasks.size(); i++)
                {
                    resultStr.add(tasks.get(i).getType() + " - " + tasks.get(i).getDescription() );
                    planninTasks.add(tasks.get(i));
                }


                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_list_item_1,
                        resultStr);
                ListAdapter customAdapter = new ListAdapter(getApplicationContext(), R.layout.itemlistrow, planninTasks);

                plans.setAdapter(customAdapter);
                    Log.i("FAnas 7.8", "AFTER");

                previous = date;

                caldroidFragment.clearBackgroundResourceForDate(date);

                Log.i("FAnas 4,", date.toString());
                caldroidFragment.setSelectedDate(date);
                caldroidFragment.setCalendarDate(date);
                caldroidFragment.refreshView();



            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_planning, menu);
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