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

    EditText firstNameField;
    EditText surnameField;
    EditText emailField;
    EditText phoneField;


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

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            firstNameField= (EditText) getActivity().findViewById(R.id.firstNameField);
            surnameField= (EditText) getActivity().findViewById(R.id.surnameField);
            emailField= (EditText) getActivity().findViewById(R.id.emailField);
            phoneField= (EditText) getActivity().findViewById(R.id.phoneField);
            firstNameField.setText(savedInstanceState.getString("firstName"));
            surnameField.setText(savedInstanceState.getString("surname"));
            emailField.setText(savedInstanceState.getString("email"));
            phoneField.setText(savedInstanceState.getString("phone"));

        }
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_you, container, false);
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
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setInfo(int fragmentNumber, ArrayList<String> infoToUpdate, String foodPref) {

    }


    @Override
    public void showInfo(HashMap<String, String> infoToUpdate) {


    }

    @Override
    public void showInfo(ArrayList<String> info) {

    }


}
