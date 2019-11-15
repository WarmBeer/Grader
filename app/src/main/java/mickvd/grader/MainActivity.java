package mickvd.grader;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import mickvd.grader.adapters.MeetingsAdapter;
import mickvd.grader.models.Meeting;
import mickvd.grader.services.MeetingsDAO;

import static mickvd.grader.utils.DateString.getCurrentDateString;

public class MainActivity extends AppCompatActivity implements Observer, Serializable {

    private MeetingsDAO meetingsDao;
    private ArrayList<Meeting> meetings = new ArrayList<>();
    private ArrayList<MeetingsAdapter> adapters = new ArrayList<>();
    private ArrayList<ListView> listviews = new ArrayList<>();
    private ListView mListView1, mListView2, mListView3, mListView4, mListView5, mListView6, mListView7;
    private TextView text1, text2, text3, text4, text5, text6, text7, title;
    private MeetingsAdapter day1, day2, day3, day4, day5, day6, day7;
    private String currentWeek = "This Week";
    private ImageButton previous, next;

    public static String StudentID;

    @Override
    public void update(Observable o, Object meetings) {
        this.meetings = (ArrayList<Meeting>) meetings;
        updateAdapters((ArrayList<Meeting>) meetings);
    }

    private void initListViews() {
        listviews.clear();
        mListView1 = (ListView)findViewById(R.id.listView1);
        mListView2 = (ListView)findViewById(R.id.listView2);
        mListView3 = (ListView)findViewById(R.id.listView3);
        mListView4 = (ListView)findViewById(R.id.listView4);
        mListView5 = (ListView)findViewById(R.id.listView5);
        mListView6 = (ListView)findViewById(R.id.listView6);
        mListView7 = (ListView)findViewById(R.id.listView7);
        listviews.add(mListView1);
        listviews.add(mListView2);
        listviews.add(mListView3);
        listviews.add(mListView4);
        listviews.add(mListView5);
        listviews.add(mListView6);
        listviews.add(mListView7);

        for (ListView lv : listviews) {
            lv.setAdapter(adapters.get(listviews.indexOf(lv)));
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, RateMeetingActivity.class);
                String meetingId = ((Meeting) parent.getItemAtPosition(position)).getID();
                intent.putExtra("meetingId",meetingId);
                startActivity(intent);
            }
            });
        }
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
        adapters.clear();
        int weekOffset = getWeekOffset();
        day1 = new MeetingsAdapter(this, meetings, getCurrentDateString(0 + weekOffset));
        day2 = new MeetingsAdapter(this, meetings, getCurrentDateString(1 + weekOffset));
        day3 = new MeetingsAdapter(this, meetings, getCurrentDateString(2 + weekOffset));
        day4 = new MeetingsAdapter(this, meetings, getCurrentDateString(3 + weekOffset));
        day5 = new MeetingsAdapter(this, meetings, getCurrentDateString(4 + weekOffset));
        day6 = new MeetingsAdapter(this, meetings, getCurrentDateString(5 + weekOffset));
        day7 = new MeetingsAdapter(this, meetings, getCurrentDateString(6 + weekOffset));
        adapters.add(day1);
        adapters.add(day2);
        adapters.add(day3);
        adapters.add(day4);
        adapters.add(day5);
        adapters.add(day6);
        adapters.add(day7);
    }

    private int getWeekOffset() {
        int i = 0;
        switch (currentWeek) {
            case "This Week":
                i = 0;
                break;
            case "Last Week":
                i = -7;
                break;
            case "Next Week":
                i = 7;
                break;
        }
        return i;
    }

    private void initDays() {
        int weekOffset = getWeekOffset();

        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);
        text5 = findViewById(R.id.text5);
        text6 = findViewById(R.id.text6);
        text7 = findViewById(R.id.text7);
        text1.setText(getCurrentDateString(0 + weekOffset));
        text2.setText(getCurrentDateString(1 + weekOffset));
        text3.setText(getCurrentDateString(2 + weekOffset));
        text4.setText(getCurrentDateString(3 + weekOffset));
        text5.setText(getCurrentDateString(4 + weekOffset));
        text6.setText(getCurrentDateString(5 + weekOffset));
        text7.setText(getCurrentDateString(6 + weekOffset));
    }

    private void updateWeek(String week) {
        currentWeek = week;
        try {
            title.setText(week);
        } catch (Exception e) {
            e.printStackTrace();
        }
        reloadView();
    }

    public void previous(View v) {
        switch (currentWeek) {
            case "This Week":
                updateWeek("Last Week");
                break;
            case "Last Week":
                break;
            case "Next Week":
                updateWeek("This Week");
                break;
        }
    }

    public void next(View v) {
        switch (currentWeek) {
            case "This Week":
                updateWeek("Next Week");
                break;
            case "Last Week":
                updateWeek("This Week");
                break;
            case "Next Week":
                break;
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_all:
                    break;
                case R.id.navigation_my:
                    Intent b = new Intent(MainActivity.this, MyMeetingsActivity.class);
                    startActivity(b);
                    break;
                case R.id.navigation_add:
                    Intent c = new Intent(MainActivity.this, AddMeetingActivity.class);
                    startActivity(c);
                    break;
            }
            return false;
        }
    };

    private void reloadView() {
        initDays();
        initAdapters();
        initListViews();
        meetingsDao.addObserver(this);
        meetingsDao.getMeetings();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void refresh() {
        meetingsDao.getMeetings();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if(extras == null){
            StudentID = "";
        } else{
            StudentID = extras.getString("studentID");
        }

        System.out.println("studentID set: " + StudentID);

        meetingsDao = new MeetingsDAO();
        setContentView(R.layout.layout_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                meetingsDao.getMeetings(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });

        title = findViewById(R.id.toolbar_title);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);



        reloadView();
    }

    public void deleteMeeting(View v) {
        ImageButton b = (ImageButton) v;
        String meetingID = (String) b.getTag();
        meetingsDao.deleteById(meetingID);
        meetingsDao.getMeetings();
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