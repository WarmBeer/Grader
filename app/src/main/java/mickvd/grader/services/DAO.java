package mickvd.grader.services;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import mickvd.grader.MainActivity;
import mickvd.grader.R;
import mickvd.grader.adapters.MeetingsAdapter;
import mickvd.grader.models.Meeting;

import static android.content.ContentValues.TAG;

public class DAO extends Observable {

    private ArrayList<Meeting> meetings = new ArrayList<>();
    private List<Observer> observers = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    public void notifyObservers(ArrayList<Meeting> meetings) {
        this.meetings = meetings;
        for (Observer observer : this.observers) {
            observer.update(this, this.meetings);
        }
    }

    public void getMeetings() {
        db.collection("meetings").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<Meeting> meetings = new ArrayList<>();
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        Meeting meeting = document.toObject(Meeting.class);
                        meetings.add(meeting);
                    }
                    notifyObservers(meetings);
                } else {
                    Log.d("MissionActivity", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void sendMeeting(Meeting meeting) {
        db.collection("meetings").document("3")
                .set(meeting)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
}
