package DataModel;

public class Course {
    private String course_id;
    private String title;
    private String dept_name;
    private int credits;



    public String getTitle() {
        return title;
    }

    public int getCredit() {
        return credits;
    }

    public String getCourseId() {
        return course_id;
    }

    public String getDept_name() {
        return dept_name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setDept_name(String dept_name) {
        this.dept_name = dept_name;
    }
}
