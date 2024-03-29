package mickvd.grader.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;

import java.util.Calendar;

import mickvd.grader.AddMeetingActivity;
import mickvd.grader.MainActivity;

public class TimePicker extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

         TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                (TimePickerDialog.OnTimeSetListener)getActivity(),
                hour,minute, DateFormat.is24HourFormat(getActivity()));

        return timePickerDialog;
    }
}
