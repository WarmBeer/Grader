package mickvd.grader.models;

import com.google.gson.Gson;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Meeting implements Comparable {

    private String ID;
    private String title;
    private String teacherName;
    private String teacherID;
    private Date startDate;
    private Date endDate;
    private String rating;
    //private Date date;

    public Meeting(){}

    public Meeting(String id, String title, String teacherID, String teacherName, Date startDate, Date endDate) {
        this.ID = id;
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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
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

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
