package uk.ac.coventry.a260ct.orks.slopemanager;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Member;
import java.math.BigDecimal;


/**
 * A simple {@link Fragment} subclass.
 */
public class MembershipPayment extends Fragment {
    private static int PAYPAL_CODE=1234;
    private SendInfo confPaymentToActivity;
    private int paid=-1;
    //set the environment for production/sandbox/no netowrkOX
    //Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PaypalConfig.PAYPAL_ENVIRONMENT_SANDBOX)
            .clientId(PaypalConfig.CLIENT_ID);
    CheckBox paypalBox;
    Button payFee;
    public MembershipPayment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        confPaymentToActivity= (SendInfo) getActivity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        payFee= (Button) getActivity().findViewById(R.id.button_paypal);
        payFee.setVisibility(View.INVISIBLE);
        payFee.setOnClickListener(buttonListener);
        paypalBox= (CheckBox) getActivity().findViewById(R.id.checkBox_paypal);
        paypalBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    payFee.setVisibility(View.VISIBLE);
                else
                    payFee.setVisibility(View.INVISIBLE);
            }
        });
        Intent paypalService = new Intent(getActivity().getApplicationContext(), PayPalService.class);

        paypalService.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        getActivity().startService(paypalService);
    }



    @Override
    public void onDestroy() {
        getActivity().stopService(new Intent(getActivity().getApplicationContext(),PayPalService.class));
        super.onDestroy();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_membership_payment, container, false);
    }

    View.OnClickListener buttonListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(10),"GBP", "Member Fee",
                    PayPalPayment.PAYMENT_INTENT_SALE);

            Intent goToPaymentScreen = new Intent(getActivity().getApplicationContext(), PaymentActivity.class);

            goToPaymentScreen.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);


            startActivityForResult(goToPaymentScreen,PAYPAL_CODE );


        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PAYPAL_CODE && resultCode== Activity.RESULT_OK){
            PaymentConfirmation paymentConfirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if(paymentConfirmation!=null){
                Log.i("details",paymentConfirmation.toString());
                try {
                    JSONObject jsonObject=new JSONObject(paymentConfirmation.toJSONObject().toString());
                    AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                    builder.setTitle("Payment Details").setMessage("Please save the following details about your payment")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog dialog=builder.create();
                    View dialogView=getActivity().getLayoutInflater().inflate(R.layout.dialog_confirmation_paypal,null);
                    TextView date= ((TextView) dialogView.findViewById(R.id.editText_date));
                    date.setText(jsonObject.getJSONObject("response").getString("create_time"), TextView.BufferType.EDITABLE);
                    TextView paypalId=((TextView) dialogView.findViewById(R.id.editText_paypal_id));
                    paypalId.setText(jsonObject.getJSONObject("response").getString("id"), TextView.BufferType.EDITABLE);
                    TextView status= ((TextView) dialogView.findViewById(R.id.editText_state_transaction));
                    status.setText(jsonObject.getJSONObject("response").getString("state"));
                    dialog.setView(dialogView);
                    dialog.show();
                    paid=1;
                    confPaymentToActivity.sendConfPayment(paid);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
