package com.aseupc.flattitude.Activities.ObjectLocation;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.aseupc.flattitude.Models.MapObject;
import com.aseupc.flattitude.R;
import com.google.android.gms.maps.MapsInitializer;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditObjectFragment.OnEditFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditObjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditObjectFragment extends Fragment {

    private OnEditFragmentInteractionListener mListener;
    private View view;
    private MapObject object;

    public EditObjectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewObjectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditObjectFragment newInstance() {
        EditObjectFragment fragment = new EditObjectFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_object, container, false);
        final EditText objectNameTE = (EditText) view.findViewById(R.id.objectName);
        final EditText descriptionTE = (EditText) view.findViewById(R.id.objectDescription);
        objectNameTE.setText(object.getName());
        descriptionTE.setText(object.getDescription());
        final Button confirmEdit = (Button) view.findViewById(R.id.confirmButton);
        confirmEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String objectName = objectNameTE.getText().toString();
                String objectDescription = descriptionTE.getText().toString();
                mListener.onEditObjectConfirmed(objectName, objectDescription);
            }
        });

        final Button currentLocation = (Button) view.findViewById(R.id.currentLocationButton);
        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onEditCurrentLocationPressed();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (OnEditFragmentInteractionListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void reset(){
        EditText objectNameTE = (EditText) view.findViewById(R.id.objectName);
        objectNameTE.setText("");
        EditText descTE = (EditText) view.findViewById(R.id.objectDescription);
        descTE.setText("");
    }

    public void setEditedObject(MapObject object){
        this.object = object;
        if(view != null) {
            EditText objectNameTE = (EditText) view.findViewById(R.id.objectName);
            objectNameTE.setText(object.getName());
            EditText descTE = (EditText) view.findViewById(R.id.objectDescription);
            descTE.setText(object.getDescription());
        }
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnEditFragmentInteractionListener {
        public void onEditObjectConfirmed(String name, String description);
        public void onEditCurrentLocationPressed();
    }

}
