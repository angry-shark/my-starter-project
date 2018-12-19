package Dao;

import DataModel.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

public class Dao {
    private Sql2o sql2o;

    private List<Course> courses;
    private List<Prereq> prereqs;
    private List<Section> sections;
    private List<Student> students;
    private List<Take> takes;
    private List<Time_slot> time_slots;
    private List<Student> currentStudent;

    public Dao(){
//        sql2o = new Sql2o("jdbc:mysql://localhost:3306/smallrelations?characterEncoding=utf8&useSSL=false&serverTimezone=GMT","root","root");
        sql2o = new Sql2o("jdbc:mysql://localhost:3306/test?characterEncoding=utf8&useSSL=false&serverTimezone=GMT","root","root");

        courses = (List<Course>) getData(Course.class,"course");
        prereqs = (List<Prereq>) getData(Prereq.class,"prereq");
        sections = (List<Section>) getData(Section.class,"section");
        students = (List<Student>) getData(Student.class,"student");
        takes = (List<Take>) getData(Take.class,"takes");
        time_slots = (List<Time_slot>) getData(Time_slot.class,"time_slot");
        currentStudent = (List<Student>)getData(Student.class,"studenttemp");
    }

    public void readData(){
        //       sql2o = new Sql2o("jdbc:mysql://localhost:3306/smallrelations?characterEncoding=utf8&useSSL=false&serverTimezone=GMT","root","root");
        sql2o = new Sql2o("jdbc:mysql://localhost:3306/test?characterEncoding=utf8&useSSL=false&serverTimezone=GMT","root","root");

        courses = (List<Course>) getData(Course.class,"course");
        prereqs = (List<Prereq>) getData(Prereq.class,"prereq");
        sections = (List<Section>) getData(Section.class,"section");
        students = (List<Student>) getData(Student.class,"student");
        takes = (List<Take>) getData(Take.class,"takes");
        time_slots = (List<Time_slot>) getData(Time_slot.class,"time_slot");
    }

    public List<Course> getCourses() {
        return courses;
    }

    public List<Prereq> getPrereqs() {
        return prereqs;
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Student> getStudents() {
        return students;
    }

    public List<Take> getTakes() {
        return takes;
    }

    public List<Time_slot> getTime_slots() {
        return time_slots;
    }

//    public boolean delTakes(String _userid,String _course_id){
//        try {
//            Connection conn = sql2o.open();
//
//            conn.createQuery("delete from takes where ID=:uid and course_id=:cid").addParameter("uid",_userid).
//                    addParameter("cid",_course_id).executeUpdate();
//
//            return true;
//        }catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }
//    }

    public List<?> getData(Class T,String DBname){
        String sql = "Select * from " + DBname;

        try(Connection conn = sql2o.open()){
            return conn.createQuery(sql).executeAndFetch(T);
        }

    }


    //new method
    public List<Take> getSpecifyTake(String _uid){
        List<Take> result = new ArrayList<>();
        for (int i = 0; i < takes.size() ; i++) {
            if (_uid.equals(takes.get(i).getID())){
                result.add(takes.get(i));
            }
        }
        return result;
    }


    public List<Course> getSelectedCourese(String _uid){
        String sql = "Select * from SelectedCourse where ID=:uid";
        List<Take> templist;
        ArrayList<String> SelectedCourese = new ArrayList<String>();
        List<Course> result = new ArrayList<>();

        try (Connection conn = sql2o.open()){
            templist = conn.createQuery(sql).addParameter("uid",_uid).executeAndFetch(Take.class);
        }
        for (int i = 0; i < templist.size(); i++) {
            SelectedCourese.add(templist.get(i).getCourseId());
        }
        for (int i = 0; i < SelectedCourese.size(); i++) {
            try(Connection conn = sql2o.open()){
                result.addAll(conn.createQuery("Select * from course where course_id=:cid").
                        addParameter("cid",SelectedCourese.get(i)).executeAndFetch(Course.class));
            }
        }
        return result;
    }

    public List<Course> getCanSelectedCourse(String _uid){
        List<Course> courseTemp = new ArrayList<>();
        List<Course> hasSelected = getSelectedCourese(_uid);
        for (int i = 0; i < courses.size() ; i++) {
            courseTemp.add(courses.get(i));
            System.out.println("id "+ i + "is: " + courseTemp.get(i).getCourseId());
        }
        for (int i = 0; i < hasSelected.size() ; i++) {
            for (int j = 0; j < courseTemp.size(); j++) {
                if(hasSelected.get(i).getCourseId().equals(courseTemp.get(j).getCourseId())){
                    courseTemp.remove(j);
                    System.out.println("s" + i);
                }
            }
        }
        return courseTemp;
    }


    public List<Student> getCurrentStudent() {
        return  currentStudent;
    }

    public void ClearCurrentStudent(){
        currentStudent.remove(0);
        currentStudent = null;
        try {
            Connection conn = sql2o.open();
            conn.createQuery("delete from studenttemp").executeUpdate();
            System.out.println("clear currentStu Successful");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Boolean checkCanLogin(String _str){
        for (int i = 0; i < getStudents().size() ; i++) {
            if (_str.equals(getStudents().get(i).getID())){
                addCurrentStudent(_str);
                return true;
            }
        }
        return false;
    }


    //add one Student to CurrentStudent Table
    public Boolean addCurrentStudent(String _userid){
        Connection conn = null;
        Student currentStudent = null;

        //TO do
        try{
            conn = sql2o.open();
            for (int i = 0; i < students.size() ; i++) {
                if(_userid.equals(students.get(i).getID())){
                    currentStudent = students.get(i);
                }
            }
            if(currentStudent != null){
                conn.createQuery("INSERT INTO studenttemp  (ID, name, dept_name,tot_cred) VALUE(:userid,:name,:deptName,:totCred)")
                        .addParameter("userid",currentStudent.getID()).addParameter("name",currentStudent.getName())
                        .addParameter("deptName",currentStudent.getDept_name()).addParameter("totCred",currentStudent.getTotalCred())
                        .executeUpdate();
                System.out.println("login Successfully");
                return true;
            }else {
                System.out.println("login fail");
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(conn != null){
                conn.close();
            }
        }
        return false;
    }

//    public void addSelectedCourse(String _userid,Course _course){
//            final String insertQuery =
//                    "INSERT INTO takes (id, course_id, sec_id,semester,year,grade) " +
//                            "VALUES (:id, :course_id, :sec_id,:semester,:year,NULL)";
//
//            String secid = "";
//            String semester = "";
//            int year = 0;
//            String time_slot_id = "";
//
//            for (int i = 0; i < sections.size(); i++) {
//                if (_course.getCourseId().equals(sections.get(i).getCourse_id())){
//                    Section temp = sections.get(i);
//                    secid = temp.getSec_id();
//                    semester = temp.getSemester();
//                    year = temp.getYear();
//                    System.out.println("Successful");
//                }
//            }
//
//
//            try (Connection con = sql2o.beginTransaction()) {
//                con.createQuery(insertQuery)
//                        .addParameter("id", _userid)
//                        .addParameter("course_id", _course.getCourseId())
//                        .addParameter("sec_id",secid)
//                        .addParameter("semester",semester)
//                        .addParameter("year",year)
//                        .executeUpdate();
//                // Remember to call commit() when a transaction is opened,
//                // default is to roll back.
//                con.commit();
//            }
//
//
////        Connection conn = null;
////
////        //TO do
////        try{
////            conn = sql2o.open();
////
////            String secid = "";
////            String semester = "";
////            int year = 0;
////            String time_slot_id = "";
////
////            for (int i = 0; i < sections.size(); i++) {
////                if (_course.getCourseId().equals(sections.get(i).getCourse_id())){
////                    Section temp = sections.get(i);
////                    secid = temp.getSec_id();
////                    semester = temp.getSemester();
////                    year = temp.getYear();
////                    System.out.println("Successful");
////                }
////            }
////
//////            conn.createQuery("INSERT INTO takes VALUE(:useerid,:courseid,:secid,:semester,:year,NULL)")
//////                    .addParameter("userid",_userid).addParameter("courseid",_course.getCourseId())
//////                    .addParameter("secid",secid).addParameter("semester",semester)
//////                    .addParameter("year",year).executeUpdate();
////
////            ;
////
////            System.out.println("Insert Successfully");
////
////            return true;
////        }catch (Exception e){
////            e.printStackTrace();
////        }finally {
////            if(conn != null){
////                conn.close();
////            }
////        }
////        return false;
//    }

    //同步数据到数据库
        //对于表SelectedCourse
    public Boolean addSelectedCourse(String _userid ,Course _course){
        Connection conn = null;

        //TO do
        try{
            conn = sql2o.open();

            String secid = "";
            String semester = "";
            int year = 0;


            for (int i = 0; i < sections.size(); i++) {
                if (_course.getCourseId().equals(sections.get(i).getCourse_id())){
                    Section temp = sections.get(i);
                    secid = temp.getSec_id();
                    semester = temp.getSemester();
                    year = temp.getYear();
                    System.out.println("Successful");
                }
            }

            for (int i = 0; i < time_slots.size(); i++) {

            }


            conn.createQuery("INSERT INTO SelectedCourse  (id, course_id, sec_id,semester,year) VALUE(:userid,:courseid,:secid,:semester,:year)")
                    .addParameter("userid",_userid).addParameter("courseid",_course.getCourseId())
                    .addParameter("secid",secid).addParameter("semester",semester)
                    .addParameter("year",year).executeUpdate();


            System.out.println("Insert Successfully");

            return true;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(conn != null){
                conn.close();
            }
        }
        return false;
    }

    public Boolean removeSelectedCourse(String _userid ,Course _course){
        //To do
        Connection conn = null;

        //TO do
        try{
            conn = sql2o.open();

            String secid = "";
            String semester = "";
            int year = 0;

            for (int i = 0; i < sections.size(); i++) {
                if (_course.getCourseId().equals(sections.get(i).getCourse_id())){
                    Section temp = sections.get(i);
                    secid = temp.getSec_id();
                    semester = temp.getSemester();
                    year = temp.getYear();
                }
            }

            conn.createQuery("delete from SelectedCourse where ID=:uid and course_id=:cid").addParameter("uid",_userid).
                    addParameter("cid",_course.getCourseId()).executeUpdate();

            System.out.println("Delete Successfully");

            return true;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(conn != null){
                conn.close();
            }
        }
        return false;
    }

        //对于表Student同步学分
    public Boolean SynTheCredot(String _userid,int _resultCred){
        Connection conn = null;

        try{
            conn = sql2o.open();
            conn.createQuery("UPDATE student Set tot_cred = :resultCred where ID = :uid")
                    .addParameter("resultCred",_resultCred)
                    .addParameter("uid",_userid).executeUpdate();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(conn != null){
                conn.close();
            }
            return false;
        }
    }



    //The limit filter method
    public Boolean checkPreReq(String _userid,Course _course){
        Prereq tempItem = null;
        List<Take> takeCourse = new ArrayList<>();//已修完的课程

        for (int i = 0; i < takes.size(); i++) {
            if (_userid.equals(takes.get(i).getID())){
                takeCourse.add(takes.get(i));
            }
        }

        for (int i = 0; i < prereqs.size(); i++) {
            if (_course.getCourseId().equals(prereqs.get(i).getCourseid())){
                tempItem = prereqs.get(i);
            }
        }

        if (tempItem != null){
            for (int i = 0; i < takeCourse.size(); i++) {
                if (tempItem.getPrereqId().equals(takeCourse.get(i).getCourseId())){
                    System.out.println("has prereq");
                    return true; //即已修课程中已有要求先修课程
                }
            }
            System.out.println("NO prereq");
            return false; //即尚未修完改课程的先修课程
        }else {
            System.out.println("no need prereq");
            return true;//即无先修课程
        }
    }

    public Boolean checkTimeSlotCollsion(String _userid,Course _course){

        return true;
    }

    public Boolean checkCurrentCredEnough(String _userid,Course _course){
        Student student = null;

        for (int i = 0; i < students.size() ; i++) {
            if (_userid.equals(students.get(i).getID())){
                student = students.get(i);
            }
        }

        if (student != null){
            if (student.getTotalCred() >= _course.getCredit()){
                return true;//学分足够的分支
            }else{
                System.out.println("Credit not enough");//学分不足够的分支
                return false;
            }
        }else {
            System.out.println("UserID ERROR not Exist");//学号错误的分支
            return false;
        }
    }
}
