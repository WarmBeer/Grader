package mickvd.grader;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import mickvd.grader.adapters.MeetingsAdapter;
import mickvd.grader.models.Meeting;
import mickvd.grader.services.DAO;

import static mickvd.grader.utils.DateString.getCurrentDateString;

public class MainActivity extends AppCompatActivity implements Observer, Serializable {

    private DAO dao;
    private ArrayList<Meeting> meetings = new ArrayList<>();
    private List<MeetingsAdapter> adapters = new ArrayList<>();
    private ListView mListView1, mListView2, mListView3, mListView4, mListView5, mListView6, mListView7;
    private TextView text1, text2, text3, text4, text5, text6, text7;
    private MeetingsAdapter day1, day2, day3, day4, day5, day6, day7;

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
        ListUtils.setDynamicHeight(mListView3);
        ListUtils.setDynamicHeight(mListView4);
        ListUtils.setDynamicHeight(mListView5);
        ListUtils.setDynamicHeight(mListView6);
        ListUtils.setDynamicHeight(mListView7);

    }

    private void initAdapters() {
        day1 = new MeetingsAdapter(this, meetings, getCurrentDateString(0));
        day2 = new MeetingsAdapter(this, meetings, getCurrentDateString(1));
        day3 = new MeetingsAdapter(this, meetings, getCurrentDateString(2));
        day4 = new MeetingsAdapter(this, meetings, getCurrentDateString(3));
        day5 = new MeetingsAdapter(this, meetings, getCurrentDateString(4));
        day6 = new MeetingsAdapter(this, meetings, getCurrentDateString(5));
        day7 = new MeetingsAdapter(this, meetings, getCurrentDateString(6));
        adapters.add(day1);
        adapters.add(day2);
        adapters.add(day3);
        adapters.add(day4);
        adapters.add(day5);
        adapters.add(day6);
        adapters.add(day7);
    }

    private void initDays() {
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);
        text5 = findViewById(R.id.text5);
        text6 = findViewById(R.id.text6);
        text7 = findViewById(R.id.text7);
        text1.setText(getCurrentDateString(0));
        text2.setText(getCurrentDateString(1));
        text3.setText(getCurrentDateString(2));
        text4.setText(getCurrentDateString(3));
        text5.setText(getCurrentDateString(4));
        text6.setText(getCurrentDateString(5));
        text7.setText(getCurrentDateString(6));
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                case R.id.navigation_dashboard:

                case R.id.navigation_notifications:

            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dao = new DAO();
        setContentView(R.layout.layout_main);
        initDays();
        initAdapters();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mListView1 = (ListView)findViewById(R.id.listView1);
        mListView2 = (ListView)findViewById(R.id.listView2);
        mListView3 = (ListView)findViewById(R.id.listView3);
        mListView4 = (ListView)findViewById(R.id.listView4);
        mListView5 = (ListView)findViewById(R.id.listView5);
        mListView6 = (ListView)findViewById(R.id.listView6);
        mListView7 = (ListView)findViewById(R.id.listView7);

        mListView1.setAdapter(day1);
        mListView2.setAdapter(day2);
        mListView3.setAdapter(day3);
        mListView4.setAdapter(day4);
        mListView5.setAdapter(day5);
        mListView6.setAdapter(day6);
        mListView7.setAdapter(day7);

        mListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, RateMeetingActivity.class);
                //todo
                String meetingId = "a7673e73-0336-43e5-8ab2-ed481c00b6b4";
                intent.putExtra("meetingId",meetingId);
                startActivity(intent);
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