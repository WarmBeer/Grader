package mickvd.grader;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class RateMeetingActivity extends AppCompatActivity {

    Toolbar toolbar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_meeting);
        String meetingId;
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





        Log.d("test", "YEET");

        final DocumentReference docRef = db.collection("meetings").document(meetingId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        Log.d("test", "DocumentSnapshot data: " + document.getData());
                        toolbar.setTitle(document.getString("title"));
                        teacherName.setText(document.getString("teacherName"));
                        Date startTime = document.getDate("startTime");
                        startDateRateMeeting.setText("start time:  " + startTime.toString());
                        Date endTime = document.getDate("endTime");
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
}
