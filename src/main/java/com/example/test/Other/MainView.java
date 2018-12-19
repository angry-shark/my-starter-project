package com.example.test.Other;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

/**
 * The main view contains a button and a click listener.
 */
@Route("test")
public class MainView extends VerticalLayout {

    private CustomerService service = CustomerService.getInstance();
    private Grid<Customer> grid = new Grid<>();

    private TextField filterText = new TextField();

    private CustomerForm form = new CustomerForm(this);

    public MainView() {
//        Button button = new Button("Click me",
//                event -> Notification.show("Clicked!"));
//        add(button);
        filterText.setPlaceholder("filter name");
        filterText.setValueChangeMode(ValueChangeMode.EAGER);
        filterText.addValueChangeListener(e -> updateList());

        Button clearFilterTextBtn = new Button(new Icon(VaadinIcon.CLOSE_CIRCLE));
        clearFilterTextBtn.addClickListener(e -> filterText.clear());

        HorizontalLayout filtering = new HorizontalLayout(filterText,clearFilterTextBtn);

        Button addCustomerData = new Button("Add new Customer");
        addCustomerData.addClickListener(e -> {
            form.setVisible(!form.isVisible());
            grid.asSingleSelect().clear();
            form.setCustomer(new Customer());
        });

        HorizontalLayout toolbar = new HorizontalLayout(filtering,addCustomerData);

        grid.setSizeFull();

        grid.addColumn(Customer::getFirstName).setHeader("FirstName");
        grid.addColumn(Customer::getLastName).setHeader("LastName");
        grid.addColumn(Customer::getStatus).setHeader("Status");

        grid.asSingleSelect().addValueChangeListener(e -> {
            form.setCustomer(e.getValue());
        });

        HorizontalLayout main = new HorizontalLayout(grid,form);
        form.setVisible(false);
        main.setAlignItems(Alignment.START);
        main.setSizeFull();


        add(toolbar,main);


        setHeight("100vh");
        updateList();
    }

    public void updateList(){
        grid.setItems(service.findAll(filterText.getValue()));
    }
}
