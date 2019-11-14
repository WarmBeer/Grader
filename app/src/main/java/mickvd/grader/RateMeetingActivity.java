package mickvd.grader;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import static mickvd.grader.MainActivity.StudentID;

public class RateMeetingActivity extends AppCompatActivity {

    public static final int[] RGB_COLORS = {
            Color.parseColor("#D32F2F"), Color.parseColor("#FFEB3B"), Color.parseColor("#4CAF50")
    };

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean rated = false;
    private int red = 0;
    private int yellow = 0;
    private int green = 0;
    LinearLayout barLayout, ratingLayout;
    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_meeting);
        String meetingId;
        Bundle extras = getIntent().getExtras();
        if(extras == null){
            meetingId = null;
        } else{
            meetingId = extras.getString("meetingId");
        }

        final TextView teacherName = findViewById(R.id.teacherName);
        final TextView startDateRateMeeting = findViewById(R.id.startDateRateMeeting);
        final TextView endDateRateMeeting = findViewById(R.id.endDateRateMeeting);

        barLayout = (LinearLayout)this.findViewById(R.id.barLayout);
        ratingLayout = (LinearLayout)this.findViewById(R.id.ratingLayout);

        barChart = (BarChart) findViewById(R.id.barchart);

        getRatings(meetingId);

        final DocumentReference docRef = db.collection("meetings").document(meetingId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        Log.d("test", "DocumentSnapshot data: " + document.getData());
                        teacherName.setText(document.getString("teacherName"));
                        Date startTime = document.getDate("startDate");
                        startDateRateMeeting.setText("start time:  " + startTime.toString());
                        Date endTime = document.getDate("endDate");
                        endDateRateMeeting.setText("end time:  " + endTime.toString());
                    }else{
                        Log.d("test", "No such document!");
                    }
                }else{
                    Log.d("test","get failed with ", task.getException());
                }
            }
        });
    }

    private void setLayouts() {
        if (rated) {
            barLayout.setVisibility(LinearLayout.VISIBLE);
            ratingLayout.setVisibility(LinearLayout.GONE);
        } else {
            ratingLayout.setVisibility(LinearLayout.VISIBLE);
            barLayout.setVisibility(LinearLayout.GONE);
        }
    }

    private void loadChart() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(red, 0));
        entries.add(new BarEntry(yellow, 1));
        entries.add(new BarEntry(green, 2));

        BarDataSet bardataset = new BarDataSet(entries, "Cells");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Bad");
        labels.add("Neutral");
        labels.add("Good");

        BarData data = new BarData(labels, bardataset);
        barChart.setData(data); // set the data and list of labels into chart
        barChart.setDescription("Course Ratings");  // set the description
        bardataset.setColors(RGB_COLORS);
        barChart.animateY(2000);
    }

    private void getRatings(String meetingID) {
        db.collection("ratings")
                .whereEqualTo("meeting", meetingID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("rating", "found rating");
                                Log.d("StudentID", StudentID);
                                if (document.getString("studentID").equals(StudentID)) {
                                    rated = true;
                                    System.out.println("You have already rated this meeting!");
                                }
                                switch(document.getLong("rating").intValue()) {
                                    case 0:
                                        red++;
                                        break;
                                    case 1:
                                        yellow++;
                                        break;
                                    case 2:
                                        green++;
                                        break;
                                }
                            }
                            setLayouts();
                            loadChart();
                        } else {
                            Log.d("test", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
