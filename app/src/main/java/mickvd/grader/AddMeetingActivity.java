package mickvd.grader;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import mickvd.grader.models.Meeting;
import mickvd.grader.utils.TimePicker;

public class AddMeetingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Date startDate;
    private Date endDate;

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
                addMeeting();
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

        TextView cancelTextView = findViewById(R.id.cancelMeeting);
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddMeetingActivity.this, MainActivity.class));
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
            startDate = new Date(year,month,dayOfMonth,0,0);
            setStartDate = false;

        }else{
            TextView textView = (TextView) findViewById(R.id.pickEndDateTextView);
            textView.setText(currentDateString);
            endDate = new Date(year,month,dayOfMonth,0,0);

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
        calendar.set(Calendar.SECOND, 0);
        String currentTimeString = DateFormat.getTimeInstance().format(calendar.getTime());


        if(setStartTime){
            TextView textView = (TextView) findViewById(R.id.pickStartTimeTextView);
            textView.setText(currentTimeString);
            Button button = (Button) findViewById(R.id.pickDateButton);
            startDate.setHours(hour);
            startDate.setMinutes(minutes);
            button.setText("Set End Date");


            setStartTime = false;
        }else{
            TextView textView = (TextView) findViewById(R.id.pickEndTimeTextView);
            textView.setText(currentTimeString);
            Button button = (Button) findViewById(R.id.pickDateButton);
            endDate.setHours(hour);
            endDate.setMinutes(minutes);
            button.setText("Set Start Date");

            setStartTime = true;
        }

    }

    public void addMeeting(){
        String meetingId = UUID.randomUUID().toString();
        EditText editTitle = findViewById(R.id.input_title);
        String title = editTitle.getText().toString();
        EditText editTeacherName = findViewById(R.id.input_teachername);
        String teacherName = editTeacherName.getText().toString();
        Date startDate = this.startDate;
        Date endDate = this.endDate;

        Log.d("test", meetingId);
        Log.d("test", title);
        Log.d("test", teacherName);
        Log.d("test", startDate.toString());
        Log.d("test", endDate.toString());

        Meeting meeting = new Meeting(title,teacherName,startDate,endDate);
        db.collection("meetings").document(meetingId).set(meeting);


        startActivity(new Intent(AddMeetingActivity.this, MainActivity.class));

    }



}
