package com.example.test;

import Dao.Dao;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;


@Route("")
public class testView extends VerticalLayout{

    private TextField username;
    private Button gotoMain;
    private Dao mydao = new Dao();
    private Div div;

    public testView() {
        gotoMain = new Button("Login");
        username = new TextField("用户名");
        gotoMain.addClickListener(e -> {
            gotoMain.getUI().ifPresent(ui -> {
                if (isVaild(username.getValue())){
                    gotoMain.getStyle().set("background","rgb(212, 169, 113)");
                    ui.navigate("System");
                }
                else{
                    username.clear();
                    Notification.show("username Error!");
                }
            });
        });

        VerticalLayout inputField = new VerticalLayout(username,gotoMain);
        gotoMain.getStyle().set("background","rgb(185, 238, 226)");
        inputField.setAlignItems(Alignment.CENTER);

        div = new Div(inputField);
        div.getStyle().set("border","solid 2px antiwhite").set("margin","auto").set("padding","25px");

        add(div);
        getStyle().set("margin","auto").set("background","rgb(255, 195, 205)");
        setAlignItems(Alignment.CENTER);
        setSizeFull();
        setHeight("100vh");
    }


    public boolean isVaild(String _username){
        return  mydao.checkCanLogin(_username);
        //return true;
    }
}
