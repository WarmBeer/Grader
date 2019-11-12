package mickvd.grader.models;

import java.sql.Timestamp;
import java.util.Date;

public class Meeting {

    private String title;
    private String teacherName;
    private String teacherID;
    private Date startTime;
    private Date endTime;
    //private Date date;

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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startDate) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endDate) {
        this.endTime = endTime;
    }

//    public Date getDate() {
//        return date;
//    }
//
//    public void setDate(Date date) {
//        this.date = date;
//    }
}
