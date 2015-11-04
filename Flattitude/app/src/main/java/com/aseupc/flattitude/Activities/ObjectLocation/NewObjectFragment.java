package com.aseupc.flattitude.Activities.ObjectLocation;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.aseupc.flattitude.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewObjectFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewObjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewObjectFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private View view;

    public NewObjectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewObjectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewObjectFragment newInstance() {
        NewObjectFragment fragment = new NewObjectFragment();
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
        view = inflater.inflate(R.layout.fragment_new_object, container, false);
        final EditText objectNameTE = (EditText) view.findViewById(R.id.objectName);

        final Button addObject = (Button) view.findViewById(R.id.addButton);
        addObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String objectName = objectNameTE.getText().toString();
                mListener.onAddObjectConfirmed(objectName);
            }
        });

        final Button currentLocation = (Button) view.findViewById(R.id.currentLocationButton);
        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCurrentLocationPressed();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (OnFragmentInteractionListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void reset(){
        EditText objectNameTE = (EditText) view.findViewById(R.id.objectName);
        objectNameTE.setText("");
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
    public interface OnFragmentInteractionListener {
        public void onAddObjectConfirmed(String name);
        public void onCurrentLocationPressed();
    }

}
