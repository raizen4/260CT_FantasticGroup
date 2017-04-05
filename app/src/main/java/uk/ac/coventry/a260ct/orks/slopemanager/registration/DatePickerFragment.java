package uk.ac.coventry.a260ct.orks.slopemanager.registration;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

@SuppressLint("ValidFragment")
public class DatePickerFragment extends android.support.v4.app.DialogFragment implements DatePickerDialog.OnDateSetListener {
    EditText textToEdit;
    private boolean futureOnly;

    @SuppressLint("ValidFragment")
    public DatePickerFragment(View editText) {
       this.textToEdit=(EditText)editText;
    }

    public void setFutureOnly(boolean futureOnly) {
        this.futureOnly = futureOnly;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dialog = new DatePickerDialog(getActivity(),this, year, month, day);

        if (futureOnly) {
            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        }

        return dialog;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        textToEdit.setText(year+"-" + (+month + 1) +"-"+day, TextView.BufferType.EDITABLE);

    }


}