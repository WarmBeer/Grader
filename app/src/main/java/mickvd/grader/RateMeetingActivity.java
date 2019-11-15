package mickvd.grader;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import mickvd.grader.models.Meeting;
import mickvd.grader.models.Rating;

import static mickvd.grader.MainActivity.StudentID;

public class RateMeetingActivity extends AppCompatActivity {

    Toolbar toolbar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String meetingId;
    public static final int[] RGB_COLORS = {
            Color.parseColor("#D32F2F"), Color.parseColor("#FFEB3B"), Color.parseColor("#4CAF50")
    };

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
        Bundle extras = getIntent().getExtras();
        if(extras == null){
            meetingId = null;
        } else{
            meetingId = extras.getString("meetingId");
        }

        final TextView teacherName = findViewById(R.id.teacherName);
        final TextView startDateRateMeeting = findViewById(R.id.startDateRateMeeting);
        final TextView endDateRateMeeting = findViewById(R.id.endDateRateMeeting);
        final TextView title = findViewById(R.id.title);
        final Button redButton = findViewById(R.id.buttonRed);
        final Button yellowButton = findViewById(R.id.buttonYellow);
        final Button greenButton = findViewById(R.id.buttonGreen);

        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rateRed(view);
            }
        });

        yellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rateYellow(view);
            }
        });

        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rateGreen(view);
            }
        });

        barLayout = (LinearLayout)this.findViewById(R.id.barLayout);
        ratingLayout = (LinearLayout)this.findViewById(R.id.ratingLayout);
        barChart = (BarChart) findViewById(R.id.barchart);


        getRatings(meetingId);

        final DocumentReference docRef = db.collection("meetings").document(meetingId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                try {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("test", "DocumentSnapshot data: " + document.getData());


                            SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");

                            title.setText(document.getString("title"));

                            if (document.getString("teacherName") == null) {
                                teacherName.setText("");
                            } else {
                                teacherName.setText("with " + document.getString("teacherName"));
                            }

                            if (document.getDate("startDate") == null) {
                                startDateRateMeeting.setText("No start time");
                            } else {
                                String startDate = document.getDate("startDate").toString();
                                Date parsedStartDate = sdf.parse(startDate);
                                startDateRateMeeting.setText("From " + sdf2.format(parsedStartDate));

                            }


                            if (document.getDate("endDate") == null) {
                                endDateRateMeeting.setText("No end time");
                            } else {
                                String endDate = document.getDate("endDate").toString();
                                Date parsedEndDate = sdf.parse(endDate);
                                endDateRateMeeting.setText("Till " + sdf2.format(parsedEndDate));
                            }


                        } else {
                            Log.d("test", "No such document!");
                        }
                    } else {
                        Log.d("test", "get failed with ", task.getException());
                    }
                }catch(Exception e){
                    e.printStackTrace();
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

        ArrayList<String> labels = new ArrayList<>();
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

    public void rateMeeting(View view, int rating){
        Rating r = new Rating(meetingId,rating,StudentID);
        db.collection("ratings").document().set(r)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        added();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        error();
                    }
                });
    }

    private void added() {
        Toast.makeText(this, "Rating sent!", Toast.LENGTH_LONG).show();
        getRatings(meetingId);
    }

    private void error() {
        Toast.makeText(this, "Error sending rating, please try again.", Toast.LENGTH_LONG).show();
    }

    public void rateGreen(View view){
        rateMeeting(view,2);
    }

    public void rateYellow(View view){
        rateMeeting(view,1);

    }

    public void rateRed(View view){
        rateMeeting(view,0);

    }
}
