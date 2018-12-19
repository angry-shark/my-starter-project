package DataModel;

public class Section {
    private String course_id;
    private String sec_id;
    private String semester;
    private int year;
    private String building;
    private String room_number;
    private String time_slot_id;

    public String getTime_slot_id() {
        return time_slot_id;
    }

    public int getYear() {
        return year;
    }

    public String getBuilding() {
        return building;
    }

    public String getCourse_id() {
        return course_id;
    }

    public String getRoom_number() {
        return room_number;
    }

    public String getSec_id() {
        return sec_id;
    }

    public String getSemester() {
        return semester;
    }
}
