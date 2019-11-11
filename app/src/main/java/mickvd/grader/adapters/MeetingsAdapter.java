package mickvd.grader.adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import mickvd.grader.R;
import mickvd.grader.models.Meeting;
import mickvd.grader.utils.Time;

public class MeetingsAdapter extends ArrayAdapter<Meeting> implements View.OnClickListener{

    private ArrayList<Meeting> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView txtName;
        TextView txtType;
        TextView txtStartTime;
        TextView txtEndTime;
    }

    public MeetingsAdapter(ArrayList<Meeting> data, Context context) {
        super(context, R.layout.activity_listview, data);
        this.dataSet = data;
        this.mContext = context;

    }

    public void setDataSet(ArrayList<Meeting> dataSet) {
        this.dataSet.clear();
        this.dataSet.addAll(dataSet);
        this.notifyDataSetChanged();
        System.out.println("Data changed: " + this.dataSet.get(0).getTitle());
    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Meeting meeting=(Meeting) object;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Meeting meeting = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.activity_listview, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.txtType = (TextView) convertView.findViewById(R.id.type);
            viewHolder.txtStartTime = (TextView) convertView.findViewById(R.id.start_time);
            viewHolder.txtEndTime = (TextView) convertView.findViewById(R.id.end_time);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        viewHolder.txtName.setText(meeting.getTitle());
        viewHolder.txtType.setText(meeting.getTeacherName());
        viewHolder.txtStartTime.setText(Time.getTime(meeting.getStartDate()));
        viewHolder.txtEndTime.setText(Time.getTime(meeting.getEndDate()));

        return convertView;
    }
}
