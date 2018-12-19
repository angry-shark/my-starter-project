package DataModel;

public class Student {
    private String ID;
    private String name;
    private String dept_name;
    private int tot_cred;


    public String getDept_name() {
        return dept_name;
    }

    public String getName() {
        return name;
    }

    public int getTotalCred() {
        return tot_cred;
    }

    public String getID() {
        return ID;
    }
}
