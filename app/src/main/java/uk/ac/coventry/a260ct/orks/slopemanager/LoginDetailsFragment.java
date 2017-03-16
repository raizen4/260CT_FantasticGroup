package uk.ac.coventry.a260ct.orks.slopemanager;

import android.content.Context;
import android.content.Intent;
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

import java.util.HashMap;
import java.util.Random;


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
    int membershipPaid=-1;
    String userType;
    HashMap<User.ATTRIBUTES,String> masterInfoCopy=new HashMap<>();
    static HashMap<String,Integer> mapTypes;
    static{
    mapTypes=new HashMap<>();
        mapTypes.put("Customer",0);
        mapTypes.put("Instructor",1);
        mapTypes.put("Operator",2);
        mapTypes.put("Manager",3);
}


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
        Toast.makeText(getActivity().getApplicationContext(),userType,Toast.LENGTH_SHORT).show();
        username= (EditText) getActivity().findViewById(R.id.username_edit_text_login_frag);
        password= (EditText) getActivity().findViewById(R.id.user_password_loggin_fragment);
        submitForm= (Button) getActivity().findViewById(R.id.buttonSubmitForm);
        submitForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!username.getText().toString().matches("") && !password.getText().toString().matches("")) {
                    if(membershipPaid!=-1)
                    {
                        masterInfoCopy.put(User.ATTRIBUTES.MEMBERSHIP,"0");//basic user
                    }
                    else{
                        masterInfoCopy.put(User.ATTRIBUTES.MEMBERSHIP,"1");//member
                    }
                    Random randomGenerator=new Random();
                    int userId=randomGenerator.nextInt(10043)+1;
                    masterInfoCopy.put(User.ATTRIBUTES.ID, String.valueOf(userId));
                    SlopeDatabase database = new SlopeDatabase(getActivity().getApplicationContext());
                     User user=UserFactory.getUser(mapTypes.get(userType),masterInfoCopy);
                     database.addUser(user);
                     database.registerCredentials(userId,username.getText().toString(),password.getText().toString());


                    Toast.makeText(getActivity().getApplicationContext(),"I have created the user"+user.getFirstName()+"which is a "+user.getMembership(),Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(),"You must have both username and password setted",Toast.LENGTH_SHORT).show();

                }


            }
        });

    }


    @Override
    public void sendHashMap(HashMap<User.ATTRIBUTES, String> masterInfo) {
        masterInfoCopy=masterInfo;
    }

    @Override
    public void sendConfPayment(int successful) {
        membershipPaid=successful;
    }

    @Override
    public void sendTypeOfUser(String type) {
        userType=type;
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
