package mickvd.grader;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;

import mickvd.grader.utils.TimePicker;

public class AddMeetingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private Boolean setStartDate = true;
    private Boolean setStartTime = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);

        Button confirm = findViewById(R.id.sendMeeting);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editTextTitle = findViewById(R.id.input_title);
                String title = editTextTitle.getText().toString();
            }
        });


        Button pickEndDateButton = (Button) findViewById(R.id.pickDateButton);
        pickEndDateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DialogFragment datePicker = new mickvd.grader.utils.DatePicker();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
    }

    @Override
    public void onDateSet (DatePicker view,int year, int month, int dayOfMonth){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        if(setStartDate){
            TextView textView = (TextView) findViewById(R.id.pickStartDateTextView);
            textView.setText(currentDateString);
            setStartDate = false;

        }else{
            TextView textView = (TextView) findViewById(R.id.pickEndDateTextView);
            textView.setText(currentDateString);
            setStartDate = true;
        }

        DialogFragment timePicker = new TimePicker();
        timePicker.show(getSupportFragmentManager(), "time picker");
    }


    @Override
    public void onTimeSet(android.widget.TimePicker timePicker, int hour, int minutes){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minutes);
        String currentTimeString = DateFormat.getTimeInstance().format(calendar.getTime());


        if(setStartTime){
            TextView textView = (TextView) findViewById(R.id.pickStartTimeTextView);
            textView.setText(currentTimeString);
            Button button = (Button) findViewById(R.id.pickDateButton);
            button.setText("Set End Date");


            setStartTime = false;
        }else{
            TextView textView = (TextView) findViewById(R.id.pickEndTimeTextView);
            textView.setText(currentTimeString);
            Button button = (Button) findViewById(R.id.pickDateButton);
            button.setText("Set Start Date");

            setStartTime = true;
        }

    }

}
