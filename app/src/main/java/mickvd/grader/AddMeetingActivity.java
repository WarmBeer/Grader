package mickvd.grader;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import mickvd.grader.models.Meeting;
import mickvd.grader.utils.Identifier;
import mickvd.grader.utils.TimePicker;

import static mickvd.grader.MainActivity.StudentID;

public class AddMeetingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Date startDate;
    private Date endDate;

    private Boolean setStartDate = true;
    private Boolean setStartTime = true;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_all:
                    Intent a = new Intent(AddMeetingActivity.this, MainActivity.class);
                    startActivity(a);
                    break;
                case R.id.navigation_my:
                    Intent b = new Intent(AddMeetingActivity.this, MyMeetingsActivity.class);
                    startActivity(b);
                    break;
                case R.id.navigation_add:
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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
        System.out.println("DateString" + currentDateString);

        if(setStartDate){
            TextView textView = (TextView) findViewById(R.id.pickStartDateTextView);
            textView.setText(currentDateString);
            try {
                startDate = new SimpleDateFormat("EEEE, MMMM dd, yyyy").parse(currentDateString);
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
            setStartDate = false;

        }else{
            TextView textView = (TextView) findViewById(R.id.pickEndDateTextView);
            textView.setText(currentDateString);
            try {
                endDate = new SimpleDateFormat("EEEE, MMMM dd, yyyy").parse(currentDateString);
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
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

        Meeting meeting = new Meeting(meetingId, title, StudentID, teacherName, startDate, endDate);
        db.collection("meetings").document(meetingId).set(meeting);


        startActivity(new Intent(AddMeetingActivity.this, MainActivity.class));

    }



}
