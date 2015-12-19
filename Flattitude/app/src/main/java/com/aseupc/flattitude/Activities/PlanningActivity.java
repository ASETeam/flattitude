package com.aseupc.flattitude.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;

import com.aseupc.flattitude.InternalDatabase.AsyncTasks.AsyncRemovePlanningTaskTask;
import com.aseupc.flattitude.InternalDatabase.DAO.PlanningDAO;
import com.aseupc.flattitude.Models.IDs;
import com.aseupc.flattitude.Models.PlanningTask;
import com.aseupc.flattitude.R;
import com.aseupc.flattitude.database.PlanningTask_Web_Services;
import com.aseupc.flattitude.utility_REST.ListAdapter;
import com.aseupc.flattitude.utility_REST.ResultContainer;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class PlanningActivity extends AppCompatActivity implements PlanningTask_Web_Services.DeletePlanningTaskWSListener, AsyncRemovePlanningTaskTask.OnTaskRemovedListener {
    private CalendarView mCalendar;
    private Map<Date,Integer> backgrounddates;
    private ListView plans;
    private View mProgressView;
    private Dialog mOverlayDialog; //display an invisible overlay dialog to prevent user interaction and pressing back
    private Calendar currentSelected;
    private Date previous;
    private CaldroidFragment caldroidFragment;

    private static int ADD_CODE = 1;
    private static int EDIT_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_logo_app);
        plans = (ListView) findViewById(R.id.plans_listview);

        Button addButton = (Button) findViewById(R.id.button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addClicked(v);
            }
        });
        backgrounddates = new HashMap<>();
        computeCalendar();

        plans.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListAdapter adapter = (ListAdapter) parent.getAdapter();
                editTaskPressed(adapter.getItem(position),view);
            }
        });

        mOverlayDialog = new Dialog(this, android.R.style.Theme_Panel);
        mOverlayDialog.setCancelable(false);
        mProgressView = findViewById(R.id.progressBar);
        currentSelected = null;
        showProgress(false);
    }

    private void addClicked(View v){
        Intent intent = new Intent(v.getContext(), NewTaskActivity.class);
        intent.putExtra("date",currentSelected);
        startActivityForResult(intent, ADD_CODE);
    }

    public void computeCalendar(){
        mCalendar = (CalendarView) findViewById(R.id.calendarView);
        caldroidFragment = new CaldroidFragment();
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
        for (PlanningTask task:tasks) {
            Date date = new Date(task.getPlannedTime().get(Calendar.YEAR)-1900,task.getPlannedTime().get(Calendar.MONTH), task.getPlannedTime().get(Calendar.DATE));
            addElementOnDate(date);
        }

        caldroidFragment.setCaldroidListener(new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                dateSelected(date);
            }
        });

    }

    private void dateSelected(Date date){
        currentSelected = Calendar.getInstance();
        currentSelected.setTime(date);
        caldroidFragment.clearSelectedDates();
        if ((previous != null) && (backgrounddates.containsKey(previous))) {
            Log.i("HAnas", "Entered background");
            caldroidFragment.setBackgroundResourceForDate(R.color.AnasBlue, previous);
        }
        PlanningDAO dao = new PlanningDAO(getApplicationContext());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        List<PlanningTask> tasks = dao.getFilteredPlanningTasks(cal);
        for (PlanningTask task : tasks) {
            Log.i("FAnas 7.5", task.getID() + " " + PlanningTask.getCleanDate(task.getPlannedTime()));
            //CallAPI.makeToast(getApplicationContext(),task.getID() + " " + task.getPlannedTime().getTime().toString() );
        }
        ArrayList<String> resultStr = new ArrayList<String>();
        List<PlanningTask> planninTasks = new ArrayList<PlanningTask>();
        for (int i = 0; i < tasks.size(); i++) {
            resultStr.add(tasks.get(i).getType() + " - " + tasks.get(i).getDescription());
            planninTasks.add(tasks.get(i));
        }

        ListAdapter customAdapter = new ListAdapter(getApplicationContext(), R.layout.itemlistrow, planninTasks,
                new ListAdapter.TaskDeletePressedListener() {
                    @Override
                    public void OnTaskDeletePressed(PlanningTask task) {
                        deleteTaskPressed(task);
                    }
                });
        plans.setAdapter(customAdapter);
        Log.i("FAnas 7.8", "AFTER");

        previous = date;

        caldroidFragment.clearBackgroundResourceForDate(date);

        Log.i("FAnas 4,", date.toString());
        caldroidFragment.setSelectedDate(date);
        caldroidFragment.setCalendarDate(date);
        caldroidFragment.refreshView();
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

    public void editTaskPressed(PlanningTask task, View v) {
        Intent intent = new Intent(v.getContext(), EditTaskActivity.class);
        intent.putExtra("task",task);
        startActivityForResult(intent, EDIT_CODE);
    }

    public void deleteTaskPressed(PlanningTask task) {
        final PlanningTask task2 = task;
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Remove object")
                .setMessage("Are you sure you want to remove " + task2.getType() + "?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTaskConfirmed(task2);
                    }

                })
                .setNegativeButton("No", null)
                .show();    }

    public void deleteTaskConfirmed(PlanningTask task){
        //showProgress(true);
        IDs ids = IDs.getInstance(this);
        PlanningTask_Web_Services ws = new PlanningTask_Web_Services();
        ws.ws_deleteTask(task, ids.getUserId(this), ids.getUserToken(this), ids.getFlatId(this), this);
    }

    public void showTaskNotRemoved()
    {
        new AlertDialog.Builder(this)
                .setTitle("Deletion failed")
                .setMessage("The task couldn't be deleted. Check your internet connection and try again")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onDeleteWSFinished(ResultContainer<PlanningTask> result) {
        if(result.getSucces()){
            //Internal database
            AsyncRemovePlanningTaskTask async = new AsyncRemovePlanningTaskTask(result.getTemplate(),this);
            async.execute();
        }
        else {
            showProgress(false);
            showTaskNotRemoved();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == ADD_CODE) {
                PlanningTask task = (PlanningTask) data.getSerializableExtra("task");
                Date date = task.getPlannedTime().getTime();
                Date date2 = new Date(date.getYear(), date.getMonth(), date.getDate());
                addElementOnDate(date2);
                if(currentSelected != null) {
                    previous = currentSelected.getTime();
                    dateSelected(currentSelected.getTime());
                }
            } else if (requestCode == EDIT_CODE) {
                PlanningTask task = (PlanningTask) data.getSerializableExtra("task");
                Date date = task.getPlannedTime().getTime();
                Date date2 = new Date(date.getYear(), date.getMonth(), date.getDate());
                addElementOnDate(date2);
                Calendar c = (Calendar) data.getSerializableExtra("originalDate");
                date = c.getTime();
                date2 = new Date(date.getYear(), date.getMonth(), date.getDate());
                removeElementFromDate(date2);
                if(currentSelected != null) {
                    previous = currentSelected.getTime();
                    dateSelected(currentSelected.getTime());
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.

        if(show) mOverlayDialog.show();
        else mOverlayDialog.hide();

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
    public void onRemoved(PlanningTask task) {
        Date date = task.getPlannedTime().getTime();
        Date date2 = new Date(date.getYear(),date.getMonth(),date.getDate());
        removeElementFromDate(date2);
        if(currentSelected != null)
            dateSelected(currentSelected.getTime());

        //showProgress(false);
    }

    private void addElementOnDate(Date date){
        if (backgrounddates.containsKey(date))
            backgrounddates.put(date, backgrounddates.get(date) + 1);
        else {
            backgrounddates.put(date, 1);
            caldroidFragment.setBackgroundResourceForDate(R.color.AnasBlue, date);
        }
    }

    private void removeElementFromDate(Date date){
        int n = backgrounddates.get(date)-1;
        if(n == 0) {
            backgrounddates.remove(date);
            caldroidFragment.clearBackgroundResourceForDate(date);
        }
        else
            backgrounddates.put(date,n);
    }
}
