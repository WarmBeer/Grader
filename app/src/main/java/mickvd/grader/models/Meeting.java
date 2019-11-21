package mickvd.grader.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Meeting implements Comparable {

    private String id;
    private String title;
    private String teacherName;
    private String teacherID;
    private Date startDate;
    private Date endDate;
    //private Date date;

    public Meeting(){}

    public Meeting(String id, String title, String teacherID, String teacherName, Date startDate, Date endDate) {
        this.id = id;
        this.title = title;
        this.teacherID = teacherID;
        this.teacherName = teacherName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public int compareTo(Object o) {
        Meeting m = (Meeting) o;
        return startDate.compareTo(m.getStartDate());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }

    public String getStartTime() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(startDate);
    }

    public Date getStartDate() {
        return startDate;
    }

    public String getEndTime() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(endDate);
    }

//    public Date getDate() {
//        return date;
//    }
//
//    public void setDate(Date date) {
//        this.date = date;
//    }


    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
