package mickvd.grader.models;

public class Rating {

    private String meeting;
    private int rating;
    private String studentID;

    public Rating(String meeting, int rating, String studentID) {
        this.meeting = meeting;
        this.rating = rating;
        this.studentID = studentID;
    }

    public String getMeeting() {
        return meeting;
    }

    public void setMeeting(String meeting) {
        this.meeting = meeting;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }
}
