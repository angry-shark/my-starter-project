package DataModel;

public class Prereq {
    private String  course_id;
    private String  prereq_id;

    public String getCourseid() {
        return course_id;
    }

    public String getPrereqId() {
        return prereq_id;
    }

    public void setCourseid(String courseid) {
        this.course_id = courseid;
    }

    public void setPrereqId(String prereqId) {
        this.prereq_id = prereqId;
    }
}
