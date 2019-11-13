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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import mickvd.grader.adapters.MeetingsAdapter;
import mickvd.grader.models.Meeting;
import mickvd.grader.services.DAO;
import mickvd.grader.utils.Time;

public class MainActivity extends AppCompatActivity implements Observer {

    private TextView mTextMessage;
    private DAO dao;
    private ArrayList<Meeting> meetings = new ArrayList<>();
    private ListView mListView1, mListView2;
    private List<MeetingsAdapter> adapters = new ArrayList<>();
    private MeetingsAdapter day1;
    private MeetingsAdapter day2;

    @Override
    public void update(Observable o, Object meetings) {
        this.meetings = (ArrayList<Meeting>) meetings;
        updateAdapters((ArrayList<Meeting>) meetings);
    }

    private void updateAdapters(ArrayList<Meeting> meetings) {
        for (MeetingsAdapter ma : adapters) {
            ma.setDataSet(meetings);
        }
        ListUtils.setDynamicHeight(mListView1);
        ListUtils.setDynamicHeight(mListView2);
    }

    private void initAdapters() {
        day1 = new MeetingsAdapter(this, meetings);
        day2 = new MeetingsAdapter(this, meetings);
        adapters.add(day1);
        adapters.add(day2);
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
        setContentView(R.layout.layout_main);
        initAdapters();

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mListView1 = (ListView)findViewById(R.id.listView1);
        mListView2 = (ListView)findViewById(R.id.listView2);

        mListView1.setAdapter(day1);
        mListView2.setAdapter(day2);

        /*
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
         */
        dao.addObserver(this);
        dao.getMeetings();
    }

    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }
}