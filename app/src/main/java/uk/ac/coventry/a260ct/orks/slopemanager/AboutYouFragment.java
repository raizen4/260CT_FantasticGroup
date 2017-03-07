package uk.ac.coventry.a260ct.orks.slopemanager;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AboutYouFragment extends Fragment implements UpdateInfo {
    private HashMap<String,String> infoEntered;
    private OnFragmentInteractionListener mListener;

    EditText firstNameField;
    EditText surnameField;
    EditText emailField;
    EditText phoneField;
    Spinner spinnerAge;
    Spinner spinnerWeight;
    public AboutYouFragment() {
        // Required empty public constructor
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String firstName = firstNameField.getText().toString();
        String  surname = surnameField.getText().toString();
        String phone = phoneField.getText().toString();
        String email = emailField.getText().toString();
        outState.putString("firstName", firstName);
        outState.putString("surname", surname);
        outState.putString("email", email);
        outState.putString("phone", phone);
        outState.putString("age",spinnerAge.getSelectedItem().toString());
        outState.putString("weight",spinnerWeight.getSelectedItem().toString());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            firstNameField= (EditText) getActivity().findViewById(R.id.firstNameField);
            surnameField= (EditText) getActivity().findViewById(R.id.surnameField);
            emailField= (EditText) getActivity().findViewById(R.id.emailField);
            phoneField= (EditText) getActivity().findViewById(R.id.phoneField);
            spinnerAge= (Spinner) getActivity().findViewById(R.id.ageSpinner);
            spinnerWeight= (Spinner) getActivity().findViewById(R.id.weightSpinner);
            firstNameField.setText(savedInstanceState.getString("firstName"));
            surnameField.setText(savedInstanceState.getString("surname"));
            emailField.setText(savedInstanceState.getString("email"));
            phoneField.setText(savedInstanceState.getString("phone"));
            spinnerAge.setSelection(Integer.parseInt(savedInstanceState.getString("age")));
            spinnerWeight.setSelection(Integer.parseInt(savedInstanceState.getString("weight")));
        }
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_you, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        firstNameField= (EditText) getActivity().findViewById(R.id.firstNameField);
        surnameField= (EditText) getActivity().findViewById(R.id.surnameField);
        emailField= (EditText) getActivity().findViewById(R.id.emailField);
        phoneField= (EditText) getActivity().findViewById(R.id.phoneField);
        spinnerAge= (Spinner) getActivity().findViewById(R.id.ageSpinner);
        spinnerWeight= (Spinner) getActivity().findViewById(R.id.weightSpinner);
        ArrayList<Integer>ageArray=new ArrayList<>();
        ArrayList<Integer>weightArray=new ArrayList<>();
        HashMap<String,String> infoToPass;
        for(int age=18;age<=70;age++){
            ageArray.add(age);
        }
        for(int weight=30;weight<200;weight++){
            weightArray.add(weight);
        }
        ArrayAdapter<Integer> adapterAge=new ArrayAdapter<Integer>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,ageArray);
        ArrayAdapter<Integer> adapterWeight=new ArrayAdapter<Integer>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,weightArray);
        spinnerAge.setAdapter(adapterAge);
        spinnerWeight.setAdapter(adapterWeight);
        spinnerAge.setDropDownVerticalOffset(1000);
        spinnerWeight.setDropDownVerticalOffset(900);
        //make the drop down menu not to be SO BIG, resize it.

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindowAge = (android.widget.ListPopupWindow) popup.get(spinnerAge);
            android.widget.ListPopupWindow popupWindowWeight = (android.widget.ListPopupWindow) popup.get(spinnerWeight);

            // Set popupWindow height to 500px
            popupWindowAge.setHeight(1000);
            popupWindowWeight.setHeight(900);
        }
        catch (Exception e) {
            // silently fail...
        }

    }


    @Override
    public void setInfo(int fragmentNumber, HashMap<String, String> infoToUpdate) {
        if(fragmentNumber==0) {
            String firstName=null;
            String surname=null ;
            String phone=null ;
            String email=null ;
            try {
                 firstName = firstNameField.getText().toString();
                 surname = surnameField.getText().toString();
                 phone = phoneField.getText().toString();
                 email = emailField.getText().toString();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                infoToUpdate.put("firstName", firstName);
                infoToUpdate.put("surname", surname);
                infoToUpdate.put("email", email);
                infoToUpdate.put("phone", phone);
                infoToUpdate.put("age", spinnerAge.getSelectedItem().toString());
                infoToUpdate.put("weight", spinnerWeight.getSelectedItem().toString());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setInfo(int fragmentNumber, ArrayList<String> infoToUpdate,ArrayList<String> infoToUpdate2) {

    }

    @Override
    public void showInfo(HashMap<String, String> infoToUpdate) {
        List<String> keys = new ArrayList<String>(infoToUpdate.keySet());
        for(String key:keys)
        {
            try
            {
                Log.i("Info" + key, infoToUpdate.get(key));
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    @Override
    public void showInfo(ArrayList<String> info) {

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
