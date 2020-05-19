package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Customer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class CustomersController {
    public TextField txt_CustomerId;
    public TextField txt_CustomerName;
    public TextField txt_CustomerAddress;
    public TableView <Customer> tbl_Customers;
    public Button btnAddCustomers;
    public Button btnSave;
    public Button btnDelete;
    public AnchorPane Customers;
    static ArrayList<Customer> customersDB = new ArrayList<>();

    static {
        customersDB.add(new Customer("C001","Fazaal","Colombo"));
        customersDB.add(new Customer("C002","Sanoj","Matara"));
        customersDB.add(new Customer("C003","Gaka","Kurunegala"));
        customersDB.add(new Customer("C004","Lakshan","Galle"));
    }


    public void initialize(){

        ObservableList<Customer> customerss = FXCollections.observableList(customersDB);
        tbl_Customers.setItems(customerss);

        txt_CustomerId.setDisable(true);
        txt_CustomerName.setDisable(true);
        txt_CustomerAddress.setDisable(true);
        btnSave.setDisable(true);
        btnDelete.setDisable(true);

        tbl_Customers.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tbl_Customers.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tbl_Customers.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));



        tbl_Customers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Customer>() {
            @Override
            public void changed(ObservableValue<? extends Customer> observable, Customer oldValue, Customer newValue) {
                int selectedIndex = tbl_Customers.getSelectionModel().getSelectedIndex();
                if(selectedIndex ==-1){
                    txt_CustomerAddress.clear();
                    txt_CustomerName.clear();
                    txt_CustomerId.clear();
                    btnSave.setText("Save");
                    btnDelete.setDisable(true);
                    return;
                }
                Customer selectedCustomer = tbl_Customers.getSelectionModel().getSelectedItem();
                txt_CustomerId.setText(selectedCustomer.getId());
                txt_CustomerName.setText(selectedCustomer.getName());
                txt_CustomerAddress.setText(selectedCustomer.getAddress());
                btnDelete.setDisable(false);
                btnSave.setText("Update");
                txt_CustomerId.setDisable(false);
                txt_CustomerName.setDisable(false);
                txt_CustomerAddress.setDisable(false);
                btnSave.setDisable(false);
            }
        });



    }

    public void btn_AddNewCustomers(ActionEvent event) {
        tbl_Customers.getSelectionModel().clearSelection();
        btnAddCustomers.setDisable(true);
        ObservableList<Customer> customer = tbl_Customers.getItems();
        if (customer.size() == 0){
            txt_CustomerId.setText("C001");
        }else{
            Customer lastCustomer = customer.get(customer.size()-1);
            String lastCustomerId = lastCustomer.getId();
            String number = lastCustomerId.substring(1,4);
            int newId = Integer.parseInt(number)+1;

            if (newId < 10) {

                txt_CustomerId.setText("C00" + newId);
            } else if (newId < 100) {

                txt_CustomerId.setText("C0" + newId);
            } else {

                txt_CustomerId.setText("C" + newId);
            }

        }
        txt_CustomerId.setDisable(false);
        txt_CustomerName.setDisable(false);
        txt_CustomerAddress.setDisable(false);
        btnSave.setDisable(false);
        txt_CustomerName.requestFocus();
    }

    public void btn_SaveCustomers(ActionEvent event) {
        String id = txt_CustomerId.getText();
        String name = txt_CustomerName.getText();
        String address = txt_CustomerAddress.getText();

        if (name.trim().length() == 0 || address.trim().length() == 0){
            new Alert(Alert.AlertType.ERROR, "Customer name address cant be empty", ButtonType.OK).show();
            return;
        }
        if (btnSave.getText().equals("Save")) {
            ObservableList<Customer> customers = tbl_Customers.getItems();
            customers.add(new Customer(id, name, address));
            new Alert(Alert.AlertType.INFORMATION, "Customer added successfully", ButtonType.OK).show();
        }else{
            ObservableList <Customer> customers = tbl_Customers.getItems();
            int selectIndex = tbl_Customers.getSelectionModel().getSelectedIndex();
            customers.get(selectIndex).setName(txt_CustomerName.getText());
            customers.get(selectIndex).setAddress(txt_CustomerAddress.getText());
            tbl_Customers.getSelectionModel().clearSelection();
            tbl_Customers.refresh();
            txt_CustomerName.setDisable(true);
            txt_CustomerAddress.setDisable(true);
            btnSave.setDisable(true);
            btnAddCustomers.requestFocus();
        }
        txt_CustomerId.clear();
        txt_CustomerName.clear();
        txt_CustomerAddress.clear();
        btnSave.setDisable(true);
        btnAddCustomers.requestFocus();
        txt_CustomerId.setDisable(true);
        txt_CustomerName.setDisable(true);
        txt_CustomerAddress.setDisable(true);
        btnAddCustomers.setDisable(false);



    }

    public void btn_DeleteCustomers(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure whether you want to delete this customer?",ButtonType.YES,ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES){
            ObservableList<Customer> customers = tbl_Customers.getItems();
            int selectIndex = tbl_Customers.getSelectionModel().getSelectedIndex();
            customers.remove(selectIndex);
            if (customers.size()==0){
                txt_CustomerName.setDisable(true);
                txt_CustomerAddress.setDisable(true);
                btnSave.setDisable(true);
                btnAddCustomers.requestFocus();
            }
        }
    }

    public void homeOnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/MainWndw.fxml"));
        Scene itemForm = new Scene((root));
        Stage mainStage = (Stage) this.Customers.getScene().getWindow();
        mainStage.setScene(itemForm);
        mainStage.setTitle("Main");
        mainStage.centerOnScreen();
    }
}
