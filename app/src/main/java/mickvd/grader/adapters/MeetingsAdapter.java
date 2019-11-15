package mickvd.grader.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import mickvd.grader.R;
import mickvd.grader.models.Meeting;

import static mickvd.grader.MainActivity.StudentID;
import static mickvd.grader.utils.DateString.getCurrentDateString;
import static mickvd.grader.utils.DateString.getDateString;

public class MeetingsAdapter extends BaseAdapter {

    private ArrayList<Meeting> data;
    private static LayoutInflater inflater = null;
    private String date;
    Context context;

    public MeetingsAdapter(Context context, ArrayList<Meeting> dataSet, String date) {
        this.context = context;
        this.data = clean(dataSet);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.date = date;
    }

    public void setDataSet(ArrayList<Meeting> dataSet) {
        this.data.clear();
        this.data.addAll(clean(dataSet));
        this.notifyDataSetChanged();
    }

    private ArrayList<Meeting> clean(ArrayList<Meeting> dataSet){
        ArrayList<Meeting> filteredList = new ArrayList<>();

        for (Meeting m : dataSet) {
            try {
                System.out.println(getDateString(m.getStartDate()) + " " + date);
                if (getDateString(m.getStartDate()).equals(date)) {
                    filteredList.add(m);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Collections.sort(dataSet, new Comparator<Meeting>(){

            @Override
            public int compare(Meeting m1, Meeting m2)
            {
                return m1.compareTo(m2);
            }
        });

        return filteredList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.row, null);

        TextView Title = vi.findViewById(R.id.title);
        TextView Start_Time = vi.findViewById(R.id.start_time);
        ImageButton delete = vi.findViewById(R.id.delete);

        try {
            Title.setText(data.get(position).getTitle());
            Start_Time.setText(data.get(position).getStartTime());
            if (data.get(position).getTeacherID().equals(StudentID)) {
                System.out.println("matching id: " + data.get(position).getTeacherID() + " " + StudentID);
                delete.setVisibility(ImageButton.VISIBLE);
                delete.setTag(data.get(position).getID());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vi;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
