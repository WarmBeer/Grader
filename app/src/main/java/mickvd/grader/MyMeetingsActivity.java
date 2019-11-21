package mickvd.grader;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import mickvd.grader.adapters.MeetingsAdapter;
import mickvd.grader.models.Meeting;
import mickvd.grader.services.MeetingsDAO;

import static mickvd.grader.MainActivity.StudentID;
import static mickvd.grader.utils.DateString.getCurrentDateString;

public class MyMeetingsActivity extends AppCompatActivity implements Observer, Serializable {

    private MeetingsDAO meetingsDao;
    private ArrayList<Meeting> meetings = new ArrayList<>();
    private ListView mListView1;
    private TextView text1;
    private MeetingsAdapter day1;

    @Override
    public void update(Observable o, Object meetings) {
        this.meetings = (ArrayList<Meeting>) meetings;
        updateAdapters((ArrayList<Meeting>) meetings);
    }

    private void initListViews() {
        mListView1 = (ListView)findViewById(R.id.listView1);

        mListView1.setAdapter(day1);
        mListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyMeetingsActivity.this, RateMeetingActivity.class);
                String meetingId = ((Meeting) parent.getItemAtPosition(position)).getId();
                intent.putExtra("meetingId",meetingId);
                startActivity(intent);
            }
        });
    }

    private void updateAdapters(ArrayList<Meeting> meetings) {
        day1.setDataSet(meetings);
        MainActivity.ListUtils.setDynamicHeight(mListView1);
    }

    private void initAdapters() {
        day1 = new MeetingsAdapter(this, meetings, getCurrentDateString(0));
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_all:
                    Intent a = new Intent(MyMeetingsActivity.this, MainActivity.class);
                    startActivity(a);
                    break;
                case R.id.navigation_my:
                    break;
                case R.id.navigation_add:
                    Intent c = new Intent(MyMeetingsActivity.this, AddMeetingActivity.class);
                    startActivity(c);
                    break;
            }
            return false;
        }
    };

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void refresh() {
        meetingsDao.getMeetingsByStudentID(StudentID);
    }

    public void deleteMeeting(View v) {
        ImageButton b = (ImageButton) v;
        String meetingID = (String) b.getTag();
        meetingsDao.deleteById(meetingID);
        meetingsDao.getMeetingsByStudentID(StudentID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        meetingsDao = new MeetingsDAO();
        setContentView(R.layout.layout_my_meetings);
        initAdapters();
        initListViews();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                meetingsDao.getMeetingsByStudentID(StudentID);
                pullToRefresh.setRefreshing(false);
            }
        });

        meetingsDao.addObserver(this);
        meetingsDao.getMeetingsByStudentID(StudentID);
    }
}
