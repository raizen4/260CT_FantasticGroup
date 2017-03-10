package uk.ac.coventry.a260ct.orks.slopemanager;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class LoginDetailsFragment extends Fragment implements SendInfo
{
    EditText username;
    EditText password;
    Button submitForm;
    User newUser;
    HashMap<String,String> masterInfoCopy=new HashMap<>();


    public LoginDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_details, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        username= (EditText) getActivity().findViewById(R.id.username_edit_text_login_frag);
        password= (EditText) getActivity().findViewById(R.id.user_password_loggin_fragment);
        submitForm= (Button) getActivity().findViewById(R.id.buttonSubmitForm);
        submitForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!username.getText().toString().matches("") && !password.getText().toString().matches("")) {
                    SlopeDatabase database = new SlopeDatabase(getActivity().getApplicationContext());
                    Toast.makeText(getActivity().getApplicationContext(),"All details needed are here, wait for the function to be implemented",Toast.LENGTH_SHORT).show();
                    // database.addUser();
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(),"You must have both username and password setted",Toast.LENGTH_SHORT).show();

                }


            }
        });

    }


    @Override
    public void sendHashMap(HashMap<String, String> masterInfo) {
        masterInfoCopy=masterInfo;
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
