package com.aseupc.flattitude.Activities;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.aseupc.flattitude.Models.BudgetOperation;
import com.aseupc.flattitude.Models.Flat;
import com.aseupc.flattitude.Models.IDs;
import com.aseupc.flattitude.Models.User;
import com.aseupc.flattitude.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BudgetActivity extends AppCompatActivity {
    private Context context;

    /**
     * The pop up which display the information to create a new budget operation
     */
    private Dialog newOperationDialog;

    /**
     * The adapter of the last operations list
     */
    private BaseAdapter listAdapter;

    private List<Map<String, String>> list;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        context = this;
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_logo_app);

        setTitle("Budget Management");

        // Update the background of the personal budget balance
        TextView personalBalanceView = (TextView) findViewById(R.id.personalBudgetBalance);
        GradientDrawable bgPersonalShape = (GradientDrawable)personalBalanceView.getBackground();
        bgPersonalShape.setColor(Color.parseColor("#d0e3e4"));
        updateBalances();

        // Add a click listener to the button
        Button newOperationButton = (Button) findViewById(R.id.new_budget_operation_button);
        newOperationButton.setOnClickListener(newOperationClickListener);

        // In order to fulfill the list
        Flat currentFlat = IDs.getInstance(context).getFlat(context);
        User currentUser = IDs.getInstance(context).getUser(context);

        // List of fictives operation in order to test
        ArrayList<BudgetOperation> operations = new ArrayList<BudgetOperation>();
        operations.add(new BudgetOperation(currentUser, currentFlat, new Date(), 10, "Putting 10 € on the common account"));
        operations.add(new BudgetOperation(null, currentFlat, new Date(), 10, "Shopping for common food"));
        operations.add(new BudgetOperation(currentUser, currentFlat, new Date(), 1, "Putting 1 € on the common account"));

        list = new ArrayList<Map<String, String>>();

        for (BudgetOperation operation : operations) {
            list.add(putOperationInMap(operation));
        }

        listAdapter = new SimpleAdapter(this, list,
                R.layout.budget_list_element,
                keys,
                new int[] {R.id.date_budget_operation, R.id.title_budget_operation, R.id.description_budget_operation});

        ListView operationsListView = (ListView) findViewById(R.id.listView_budget);
        operationsListView.setAdapter(listAdapter);
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

    /**
     * The Id of the pop up which allows the user to create a new budget operation
     */
    private final static int ID_NEW_OPERATION_POPUP = 0;

    /**
     * The click listener of the new operation button
     */
    private View.OnClickListener newOperationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialog(ID_NEW_OPERATION_POPUP);
        }
    };

    /**
     * The click listener of the create operation button
     */
    private View.OnClickListener putMoneyOnCommonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                double amount = Double.parseDouble(((EditText) newOperationDialog.findViewById(R.id.new_budget_operation_amount))
                        .getText().toString());
                createOperation(amount,
                        ((EditText)newOperationDialog.findViewById(R.id.new_budget_operation_description)).getText().toString(), true);
            } catch (NumberFormatException | NullPointerException e) {
                e.printStackTrace();
            }
            dismissDialog(ID_NEW_OPERATION_POPUP);
        }
    };

    /**
     * The click listener of the create operation button
     */
    private View.OnClickListener pullMoneyOnCommonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            double amount;
            try {
                amount = Double.parseDouble(((EditText) newOperationDialog.findViewById(R.id.new_budget_operation_amount)).getText().toString());
                createOperation(amount,
                        ((EditText) newOperationDialog.findViewById(R.id.new_budget_operation_description)).getText().toString(), false);
            } catch (NumberFormatException | NullPointerException e) {
                e.printStackTrace();
            }
            dismissDialog(ID_NEW_OPERATION_POPUP);
        }
    };

    /**
     * Create the new operation pop up
     * Subscribe to events for the buttons of the dialog
     * @param id the id of the pop up (only the new operation pop up for this activity)
     * @return the created pop up
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        newOperationDialog = new Dialog(context);
        newOperationDialog.setContentView(R.layout.budget_operation_popup);
        newOperationDialog.setCancelable(true);
        newOperationDialog.setCanceledOnTouchOutside(true);
        newOperationDialog.setTitle("New budget operation");
        ((Button) newOperationDialog.findViewById(R.id.create_pull_budget_operation_button)).setOnClickListener(pullMoneyOnCommonClickListener);
        ((Button) newOperationDialog.findViewById(R.id.create_put_budget_operation_button)).setOnClickListener(putMoneyOnCommonClickListener);
        return newOperationDialog;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        ((EditText) dialog.findViewById(R.id.new_budget_operation_description)).setText("");
        ((EditText) dialog.findViewById(R.id.new_budget_operation_amount)).setText("");
    }

    /**
     * Create a new operation and add it to the list view
     * @param amount the amount of the operation
     * @param description the description of the operation
     */
    private void createOperation(double amount, String description, boolean toCommon) {
        IDs ids = IDs.getInstance(context);
        BudgetOperation newOperation = new BudgetOperation(toCommon ? ids.getUser(context) : null,
                ids.getFlat(context), new Date(), amount, description);
        if (toCommon) {
            ids.setBalance(ids.getBalance() + amount);
            ids.setPersonalExpense(ids.getPersonalExpense() + amount);
        }
        else {
            ids.setBalance(ids.getBalance() - amount);
            ids.setPersonalExpense(ids.getPersonalExpense() - ((int)(amount / 3 * 100))/100.0); //TODO To correct, here we suppose that there are only 3 roommates
        }
        updateBalances();
        list.add(0, putOperationInMap(newOperation));
        listAdapter.notifyDataSetChanged();
    }

    /**
     * Return a formated operation in order to appear correctly in the layout
     * @param budgetOperation
     * @return
     */
    private HashMap<String, String> putOperationInMap(BudgetOperation budgetOperation) {
        final DateFormat dateFormat = new SimpleDateFormat("MM/dd");
        HashMap<String, String> element = new HashMap<String, String>();

        element.put(keys[0], dateFormat.format(budgetOperation.getDate())+": ");
        element.put(keys[1], budgetOperation.getUser() == null ?
                "" + budgetOperation.getAmount() + "€ were used from the common budget" :
                budgetOperation.getUser().getLastname() + " put " + budgetOperation.getAmount() + "€ on the common budget");
        element.put(keys[2], budgetOperation.getDescription());

        return element;
    }

    /**
     * Update the display of the balance figures
     */
    private void updateBalances() {
        double personalBalance = IDs.getInstance(context).getPersonalExpense();
        TextView personalBalanceView = (TextView) findViewById(R.id.personalBudgetBalance);
        personalBalanceView.setText(personalBalance + "€");

        double flatBalance = IDs.getInstance(context).getBalance();
        TextView flatBalanceView = (TextView) findViewById(R.id.commonBudgetBalance);
        flatBalanceView.setText(flatBalance + "€");
    }

    /**
     * The keys for the attribute names in the map of operations
     */
    private static final String keys[] = {"date", "title", "description"};
}
