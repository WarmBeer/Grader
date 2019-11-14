package mickvd.grader;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class RateMeetingActivity extends AppCompatActivity {

    Toolbar toolbar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String meetingId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_meeting);
        Bundle extras = getIntent().getExtras();
        if(extras == null){
            meetingId = null;
        }else{

            meetingId = extras.getString("meetingId");
        }

        toolbar = findViewById(R.id.toolbarRateMeeting);
        final TextView teacherName = findViewById(R.id.teacherName);
        final TextView startDateRateMeeting = findViewById(R.id.startDateRateMeeting);
        final TextView endDateRateMeeting = findViewById(R.id.endDateRateMeeting);
        final TextView title = findViewById(R.id.title);
        final Button redButton = findViewById(R.id.buttonRed);
        final Button yellowButton = findViewById(R.id.buttonYellow);
        final Button greenButton = findViewById(R.id.buttonGreen);
        View view = this.getWindow().getDecorView();


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





        Log.d("test", "YEET");

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

    public void rateMeeting(View view,String rating){
        DocumentReference docRef = db.collection("meetings").document(meetingId);
        docRef
                .update("rating",rating)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("test", "Rating toegevoegd aan meeting!");
                        Snackbar snackbar = Snackbar
                                .make((getWindow().getDecorView().getRootView()), "Rated!", Snackbar.LENGTH_SHORT);
                        snackbar.show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("test", "Rating NIET toegevoegd");
                        Snackbar snackbar = Snackbar
                                .make((getWindow().getDecorView().getRootView()), "Oops something went wrong!", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                });


    }

    public void rateGreen(View view){
        rateMeeting(view,"Green");
    }

    public void rateYellow(View view){
        rateMeeting(view,"Yellow");

    }

    public void rateRed(View view){
        rateMeeting(view,"Red");

    }
}
