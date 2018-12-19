package com.example.test.Other;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;


public class CustomerForm  extends FormLayout {
    private TextField firstName = new TextField("FirstName");
    private TextField lastName = new TextField("LastName");
    private ComboBox<CustomerStatus> status = new ComboBox<>("Status");

    private CustomerService service = CustomerService.getInstance();
    private MainView mainView;
    private Customer customer;

    private Button save = new Button("save");
    private Button delete = new Button("delete");

    private Binder<Customer> binder = new Binder<>(Customer.class);

    public CustomerForm(MainView _mainView){
        this.mainView = _mainView;
        customer=null;
        save.addClickListener(e -> this.save());
        delete.addClickListener(e -> this.delete());
        HorizontalLayout Buttons = new HorizontalLayout(save,delete);
        add(firstName,lastName,status,Buttons);
        save.getElement().setAttribute("theme","primary");
        status.setItems(CustomerStatus.values());
        binder.bindInstanceFields(this);
    }

    public void setCustomer(Customer _customer){
        this.customer = _customer;
        binder.setBean(customer);
        Boolean enabled = (customer != null);
        save.setEnabled(enabled);
        delete.setEnabled(enabled);
        if(enabled){
            firstName.focus();
        }
    }

    public void save(){
        service.save(customer);
        mainView.updateList();
        setCustomer(null);
    }

    public void delete(){
        service.delete(customer);
        mainView.updateList();
        setCustomer(null);
    }
}
