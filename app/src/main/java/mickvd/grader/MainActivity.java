package mickvd.grader;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import mickvd.grader.adapters.MeetingsAdapter;
import mickvd.grader.models.Meeting;
import mickvd.grader.services.DAO;
import mickvd.grader.utils.Time;

public class MainActivity extends AppCompatActivity implements Observer {

    private TextView mTextMessage;
    ListView listView;
    private DAO dao;
    private ArrayList<Meeting> meetings = new ArrayList<>();
    MeetingsAdapter meetingsAdapter;

    @Override
    public void update(Observable o, Object meetings) {
        this.meetings = (ArrayList<Meeting>) meetings;
        meetingsAdapter.setDataSet(this.meetings);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dao = new DAO();
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        meetingsAdapter = new MeetingsAdapter(this.meetings, getApplicationContext());
        listView = (ListView) findViewById(R.id.simpleListView);

        listView.setAdapter(meetingsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Meeting Meeting = meetings.get(position);

                Snackbar.make(view, Meeting.getTitle() + "\n" + Meeting.getTeacherName() + " API: " + Time.getTime(Meeting.getStartTime()), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dao.getMeetings(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });

        FloatingActionButton fab = findViewById(R.id.addMeetingButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddMeetingActivity.class));
            }
        });

        dao.addObserver(this);
        dao.getMeetings();
    }
}