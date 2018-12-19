package com.example.test;

import Dao.Dao;
import DataModel.Course;
import DataModel.Prereq;
import DataModel.Student;
import DataModel.Take;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.List;

/**
 * 1.学分同步到数据库(完成)
 * 2.先修课限制（完成)
 * 3.美化界面，添加已修课程的grid和先修课程的grid(完成)
 * 4.学分限制（完成）
 * TO do List:
 * 5.所需数据库（含新增表的打包）
 * 6.时间冲突限制
 * */



@Route("System")
public class SystemPanel extends VerticalLayout {

    private Dao mydao;

    private Label StudentInf = new Label();
    private Label StudentInf_a = new Label();
    private Button mySelect = new Button("mySelected");
    private Button canSelect = new Button("canSelected");
    private Button loginOut = new Button("logout");

    private List<Course> haveSelected;
    private List<Course> canSelected;
    private Grid<Course> grid = new Grid<>();
    private Grid<Course> grid02 = new Grid<>();

    private Grid<Take> gridTake = new Grid<>();
    private Grid<Prereq> gridPrereq = new Grid<>();

    private Course currentCourse = null;
    private Student currentUser = null;
    private int tempCredit = 0;


    public SystemPanel(){
        String InputID = "";
        if(mydao == null){
            mydao = new Dao();
            InputID = mydao.getCurrentStudent().get(0).getID();
            mydao.ClearCurrentStudent();
        }
        haveSelected = mydao.getSelectedCourese(InputID);
        canSelected = mydao.getCanSelectedCourse(InputID);

        for (int i = 0; i < mydao.getStudents().size() ; i++) {
            if(InputID.equals(mydao.getStudents().get(i).getID())){
               currentUser = mydao.getStudents().get(i);
               tempCredit = currentUser.getTotalCred();
               StudentInf.setText("ID is:" + currentUser.getID() +
                       "  Name is: "+ currentUser.getName());
               StudentInf_a.setText(" Current Credit is: "+ tempCredit );
            }
        }

        HorizontalLayout StudentInfPart = new HorizontalLayout(StudentInf,StudentInf_a);
        StudentInf.getStyle().set("float","left");
        StudentInf_a.getStyle().set("float","right");

        loginOut.getStyle().set("position","absolute").set("top","15px").set("right","15px");

        HorizontalLayout ButtonBar = new HorizontalLayout(mySelect,canSelect);

        grid.addColumn(Course::getCourseId).setHeader("CourseId");
        grid.addColumn(Course::getTitle).setHeader("Title");
        grid.addColumn(Course::getDept_name).setHeader("Dept_name");
        grid.addColumn(Course::getCredit).setHeader("Credit");

        grid02.addColumn(Course::getCourseId).setHeader("CourseId");
        grid02.addColumn(Course::getTitle).setHeader("Title");
        grid02.addColumn(Course::getDept_name).setHeader("Dept_name");
        grid02.addColumn(Course::getCredit).setHeader("Credit");

        gridPrereq.addColumn(Prereq::getPrereqId).setHeader("PrereqID");
        gridPrereq.addColumn(Prereq::getCourseid).setHeader("CourseID");

        VerticalLayout PrereqTable = new VerticalLayout(new Label("Prerequisites"),gridPrereq);
        gridPrereq.setItems(mydao.getPrereqs());
        PrereqTable.getStyle().set("float","left").set("width","20%");
        PrereqTable.setAlignItems(Alignment.CENTER);


        VerticalLayout HasTaken = new VerticalLayout(new Label("Have taken courses"),gridTake);
        gridTake.setItems(mydao.getSpecifyTake(currentUser.getID()));
        HasTaken.getStyle().set("float","right").set("width","20%");
        HasTaken.setAlignItems(Alignment.CENTER);

        gridTake.addColumn(Take::getCourseId).setHeader("CourseID");
        gridTake.addColumn(Take::getGrade).setHeader("Grade");

        grid.asSingleSelect().addValueChangeListener(e -> {
            currentCourse =  e.getValue();
        });

        grid02.asSingleSelect().addValueChangeListener(e -> {
            currentCourse = e.getValue();
        });


        updategrid();

        HorizontalLayout content = new HorizontalLayout(PrereqTable,grid,grid02,HasTaken);
        content.setSizeFull();
        content.setAlignItems(Alignment.CENTER);


        Button Selected = new Button("Ensure Selected");
        Button Cancel = new Button("Cancel Selected");

/*
* bind the listener for all button
* */
        Selected.addClickListener(e -> {
            if(currentCourse != null){
                if (mydao.checkPreReq(currentUser.getID(),currentCourse)
                        && mydao.checkCurrentCredEnough(currentUser.getID(),currentCourse)){
                    AtUIaddSelected(currentCourse);
                }else{
                    if (!mydao.checkCurrentCredEnough(currentUser.getID(),currentCourse)){//学分不够的分支
                        Notification.show("Your credit is not enough");
                    }else {//未先修的分支
                        Notification.show("You don't takes the preCourse of this course");
                    }
                }
            }else{//未选择课程的分支
                Notification.show("You need Select one Course");
            }
        });

        Cancel.addClickListener(e -> {
            if(currentCourse != null) {
                AtUiRemoveSelected(currentCourse);
            }else{
                Notification.show("You need Select one Course");
            }
        });

        mySelect.addClickListener(e -> {
            grid.setVisible(false);
            grid02.setVisible(true);
            Selected.setVisible(false);
            Cancel.setVisible(true);
            canSelect.getStyle().set("background","white");
            mySelect.getStyle().set("background","orange");
            currentCourse = null;
            updategrid();
        });

        canSelect.addClickListener(e -> {
            grid.setVisible(true);
            grid02.setVisible(false);
            Selected.setVisible(true);
            Cancel.setVisible(false);
            canSelect.getStyle().set("background","orange");
            mySelect.getStyle().set("background","white");
            currentCourse = null;
            updategrid();
        });

        loginOut.addClickListener(e -> {
            loginOut.getUI().ifPresent(ui ->{
                ui.navigate("");
            });
        });

        //init the UI component state
        grid.setVisible(true);
        grid02.setVisible(false);
        Selected.setVisible(true);
        Cancel.setVisible(false);
        canSelect.getStyle().set("background","orange");
        mySelect.getStyle().set("background","white");

        HorizontalLayout toolBar = new HorizontalLayout(Selected,Cancel);

        add(StudentInfPart,ButtonBar,content,toolBar,loginOut);
        getStyle().set("margin","auto").set("background","rgb(241, 204, 210)");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setHeight("100vh");
    }

    public void updategrid(){
        grid.setItems(canSelected);
        grid02.setItems(haveSelected);
    }

    public void AtUIaddSelected(Course _course){//绑定到Button Select
        if(_course != null){
            System.out.println("id is:" + _course.getCourseId());
            haveSelected.add(_course);
            canSelected.remove(_course);
            if(mydao.addSelectedCourse(currentUser.getID(),_course)) {
                ChangetheCredit(-1*_course.getCredit());
                updategrid();
            }else{
                Notification.show("Selected Fail!");
                System.out.println("Selected Fail");
                canSelected.add(_course);
                haveSelected.remove(_course);
            }
        }
    }

    public void AtUiRemoveSelected(Course _course){//绑定到Button Cancel
        if (_course != null){
            System.out.println("id is:" + _course.getCourseId());
            haveSelected.remove(_course);
            canSelected.add(_course);
            if(mydao.removeSelectedCourse(currentUser.getID(),_course)) {
                ChangetheCredit(_course.getCredit());
                updategrid();
            }else{
                Notification.show("remove Selected Fail!");
                System.out.println("remove Selected Fail");
                haveSelected.add(_course);
                canSelected.remove(_course);
            }

        }
    }

    //match the total credit
    public void ChangetheCredit(int _deltaCredit){
        tempCredit += _deltaCredit;
        //Modify in DB
        //To do

        mydao.SynTheCredot(currentUser.getID(),tempCredit);

        StudentInf.setText("ID is:" + currentUser.getID() +
                "  Name is: "+ currentUser.getName());
        StudentInf_a.setText(" Current Credit is: "+ tempCredit );
    }
}
