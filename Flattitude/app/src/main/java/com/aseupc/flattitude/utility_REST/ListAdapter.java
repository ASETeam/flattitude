package com.aseupc.flattitude.utility_REST;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.aseupc.flattitude.Models.PlanningTask;
import com.aseupc.flattitude.R;

import java.util.List;

/**
 * Created by MetzoDell on 30-11-15.
 */
public class ListAdapter extends ArrayAdapter<PlanningTask> {

    private TaskButtonsListener listener;

    public ListAdapter(Context context, int textViewResourceId, TaskButtonsListener listener) {
        super(context, textViewResourceId);
        this.listener = listener;
    }

    public ListAdapter(Context context, int resource, List<PlanningTask> items, TaskButtonsListener listener) {
        super(context, resource, items);
        this.listener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.itemlistrow, null);
        }

        PlanningTask p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.type);
            TextView tt2 = (TextView) v.findViewById(R.id.time);
            TextView tt3 = (TextView) v.findViewById(R.id.description);
          //  TextView tt4 = (TextView) v.findViewById(R.id.author);

            if (tt1 != null) {
                tt1.setText(p.getType());
            }

            if (tt2 != null) {
                tt2.setText(PlanningTask.getCleanDate(p.getPlannedTime()));
            }

            if (tt3 != null) {
                tt3.setText(p.getDescription());
            }
        /*    if (tt4 != null) {
                tt3.setText(p.getAuthor());
            }*/
            Button deleteButton = (Button) v.findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnTaskDeletePressed(getItem(position));
                }
            });

            Button setAlarmButton = (Button) v.findViewById(R.id.alarmButton);
            setAlarmButton.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,
                    p.getAlarmTime() == null ? R.drawable.ic_inactive : R.drawable.ic_active,0);
            setAlarmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnTaskSetAlarmPressed(getItem(position));
                }
            });
        }

        return v;
    }

    public interface TaskButtonsListener{
        public void OnTaskDeletePressed(PlanningTask task);
        public void OnTaskSetAlarmPressed(PlanningTask task);
    }

}
