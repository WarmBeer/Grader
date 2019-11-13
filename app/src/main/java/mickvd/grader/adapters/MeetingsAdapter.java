package mickvd.grader.adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import mickvd.grader.R;
import mickvd.grader.models.Meeting;
import mickvd.grader.utils.Time;

public class MeetingsAdapter extends BaseAdapter {

    private ArrayList<Meeting> data;
    private static LayoutInflater inflater = null;
    Context context;

    public MeetingsAdapter(Context context, ArrayList<Meeting> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setDataSet(ArrayList<Meeting> dataSet) {
        this.data.clear();
        this.data.addAll(dataSet);
        this.notifyDataSetChanged();
        System.out.println("Data changed: " + this.data.get(0).getTitle());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.row, null);

        TextView Title = vi.findViewById(R.id.title);
        TextView Start_Time = vi.findViewById(R.id.start_time);

        try {
            Title.setText(data.get(position).getTitle());
            Start_Time.setText(data.get(position).getStartTime());
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
