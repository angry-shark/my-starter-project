package DataModel;

public class Time_slot {
    private String time_slot_id;
    private String day;
    private int start_hr;
    private int start_min;
    private int end_hr;
    private int end_min;

    public int getEnd_hr() {
        return end_hr;
    }

    public int getEnd_min() {
        return end_min;
    }

    public int getStart_hr() {
        return start_hr;
    }

    public int getStart_min() {
        return start_min;
    }

    public String getDay() {
        return day;
    }

    public String getTime_slot_id() {
        return time_slot_id;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setEnd_hr(int end_hr) {
        this.end_hr = end_hr;
    }

    public void setEnd_min(int end_min) {
        this.end_min = end_min;
    }

    public void setStart_hr(int start_hr) {
        this.start_hr = start_hr;
    }

    public void setStart_min(int start_min) {
        this.start_min = start_min;
    }

    public void setTime_slot_id(String time_slot_id) {
        this.time_slot_id = time_slot_id;
    }
}
