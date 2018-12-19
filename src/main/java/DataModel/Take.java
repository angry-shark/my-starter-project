package DataModel;

public class Take {
    private String ID;
    private String course_id;
    private String sec_id;
    private String semester;
    private int year;
    private String grade;

    public String getID() {
        return ID;
    }

    public String getSemester() {
        return semester;
    }

    public String getSec_id() {
        return sec_id;
    }

    public int getYear() {
        return year;
    }

    public String getCourseId() {
        return course_id;
    }

    public String getGrade() {
        return grade;
    }
}
