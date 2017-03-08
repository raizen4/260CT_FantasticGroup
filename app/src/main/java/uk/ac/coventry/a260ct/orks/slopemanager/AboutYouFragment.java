package uk.ac.coventry.a260ct.orks.slopemanager;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
    EditText dobField;


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
            firstNameField= (EditText) getActivity().findViewById(R.id.firstName_edit_text);
            surnameField= (EditText) getActivity().findViewById(R.id.surname_edit_text);
            emailField= (EditText) getActivity().findViewById(R.id.email_edit_text);
            phoneField= (EditText) getActivity().findViewById(R.id.phone_edit_text);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        firstNameField= (EditText) getActivity().findViewById(R.id.firstName_edit_text);
        surnameField= (EditText) getActivity().findViewById(R.id.surname_edit_text);
        emailField= (EditText) getActivity().findViewById(R.id.email_edit_text);
        phoneField= (EditText) getActivity().findViewById(R.id.phone_edit_text);
        dobField=(EditText)getActivity().findViewById(R.id.dob_edit_text);
        dobField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b==true){
                    DatePickerFragment picker=new DatePickerFragment(view);
                    FragmentTransaction transaction=getFragmentManager().beginTransaction();
                    picker.show(transaction,"DATE_PICKER");
                }
            }
        });


    }



    @Override
    public void setInfo(String fragmentName, HashMap<String, String> infoToUpdate) {
        if(fragmentName.matches("AboutYouFragment")) {
            String firstName=null;
            String surname=null ;
            String phone=null ;
            String email=null ;
            String dob=null;
            try {
                 firstName = firstNameField.getText().toString();
                 surname = surnameField.getText().toString();
                 phone = phoneField.getText().toString();
                 email = emailField.getText().toString();
                 dob=dobField.getText().toString();

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                infoToUpdate.put("firstName", firstName);
                infoToUpdate.put("surname", surname);
                infoToUpdate.put("email", email);
                infoToUpdate.put("phone", phone);
                infoToUpdate.put("dob",dob);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }



    @Override
    public void setInfo(String fragmentName, ArrayList<String> infoToUpdate, ArrayList<String> infoToUpdate2) {

    }



}
