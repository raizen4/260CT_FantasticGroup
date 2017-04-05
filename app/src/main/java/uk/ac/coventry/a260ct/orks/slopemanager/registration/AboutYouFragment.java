package uk.ac.coventry.a260ct.orks.slopemanager.registration;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.ac.coventry.a260ct.orks.slopemanager.R;
import uk.ac.coventry.a260ct.orks.slopemanager.database.User;

public class AboutYouFragment extends Fragment implements UpdateInfo {

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
    public void onAttach(Context context) {
        super.onAttach(context);
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
                    picker.show(transaction,"Date Piker");
                }
            }
        });
        phoneField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phoneNumber=s.toString();

                try {
                    if (phoneNumber.length() > 15) {
                        phoneField.setError("The number is too long");
                    } else if (phoneNumber.charAt(0) != '+') {
                        phoneField.setError("Put the prefix of your country first");
                    } else {
                        phoneField.setError(null);
                    }
                }
                catch(Exception e){}
            }


        });
        firstNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String firstname=s.toString();
                Pattern p = Pattern.compile("[^a-z]", Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(firstname);
                boolean special=m.find();
                if(special)
                {firstNameField.setError("That is not a valid first name, it should not have special characters or digits");}
                else{
                    firstNameField.setError(null);
                }
            }
        });
        emailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!isValidEmail(emailField.getText().toString()))
                    emailField.setError("Enter a valid address");
                else{
                    emailField.setError(null);
                }
            }


        });
        surnameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String surname=s.toString();
                Pattern p = Pattern.compile("[^a-z]", Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(surname);
                boolean special=m.find();
                if(special)
                {surnameField.setError("That is not a valid surname, it should not have special characters or digits");}
                else{
                    surnameField.setError(null);
                }

            }
        });

    }



    @Override
    public void setInfo(String fragmentName, HashMap<User.ATTRIBUTES, String> infoToUpdate) {
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
                infoToUpdate.put(User.ATTRIBUTES.FIRST_NAME, firstName);
                infoToUpdate.put(User.ATTRIBUTES.SURNAME, surname);
                infoToUpdate.put(User.ATTRIBUTES.EMAIL, email);
                infoToUpdate.put(User.ATTRIBUTES.PHONE, phone);
                infoToUpdate.put(User.ATTRIBUTES.DOB,dob);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }



    @Override
    public void setInfo(String fragmentName, ArrayList<String> infoToUpdate, ArrayList<String> infoToUpdate2) {

    }

    public final static boolean isValidEmail(String target) {
        if (target == null) {
            return false;
        } else {
            //android Regex to check the email address Validation
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    public final static boolean isValidPhone(String target) {
        if (target == null) {
            return false;
        } else {
            //android Regex to check the phone Validation
            return Patterns.PHONE.matcher(target).matches();
        }
    }

}
