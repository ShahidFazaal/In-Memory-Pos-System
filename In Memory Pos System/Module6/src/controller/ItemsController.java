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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Customer;
import model.Items;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class ItemsController {

    public TextField txtItemCode;
    public TextField txtDescription;
    public TextField txtUnitPrice;
    public TextField txtQuantity;
    public Button btnAddNewItem;
    public Button btnSave;
    public Button btnDelete;
    public TableView <Items>tblItem;
    public AnchorPane Item;
    static ArrayList<Items> itemsDB = new ArrayList<>();

    static {
        itemsDB.add(new Items("I001", "Egg", 50, 12.00));
        itemsDB.add(new Items("I002", "SunLight", 60, 50.00));
        itemsDB.add(new Items("I003", "Sugar", 40, 100.00));
        itemsDB.add(new Items("I004", "Anchor", 80, 400.00));
    }


    public void initialize(){

        ObservableList<Items> itemss = FXCollections.observableList(itemsDB);
        tblItem.setItems(itemss);
        txtQuantity.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtQuantity.setText(newValue.replaceAll("[^\\d]", ""));
                    txtQuantity.setStyle("-fx-border-color:#F4200A ");

                }else
                    txtQuantity.setStyle("-fx-border-color:#D0CCCB ");
            }
        });
        txtUnitPrice.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtUnitPrice.setText(newValue.replaceAll("[^\\d]", ""));
                    txtUnitPrice.setStyle("-fx-border-color:#F4200A ");
                }
                else{
                    txtUnitPrice.setStyle("-fx-border-color:#D0CCCB");
                }
            }
        });



        txtItemCode.setDisable(true);
        txtDescription.setDisable(true);
        txtQuantity.setDisable(true);
        txtUnitPrice.setDisable(true);
        btnDelete.setDisable(true);
        btnSave.setDisable(true);



        tblItem.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("code"));
        tblItem.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
        tblItem.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tblItem.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        tblItem.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Items>() {
            @Override
            public void changed(ObservableValue<? extends Items> observable, Items oldValue, Items newValue) {



                btnDelete.setDisable(false);
                int selectedIndex = tblItem.getSelectionModel().getSelectedIndex();
                if (selectedIndex == -1) {
                    txtUnitPrice.clear();
                    txtQuantity.clear();
                    txtItemCode.clear();
                    txtDescription.clear();
                    return;
                }
                Items selectedCustomer = tblItem.getSelectionModel().getSelectedItem();
                txtDescription.setText(selectedCustomer.getDescription());
                txtItemCode.setText(selectedCustomer.getCode());
                txtUnitPrice.setText(selectedCustomer.getUnitPrice()+"");
                txtQuantity.setText(selectedCustomer.getQuantity()+"");
                txtItemCode.setDisable(false);
                txtDescription.setDisable(false);
                txtQuantity.setDisable(false);
                txtUnitPrice.setDisable(false);
                btnSave.setText("Update");
                btnSave.setDisable(false);
                btnAddNewItem.setDisable(false);




            }
        });




    }

    public void btn_AddNewItem(ActionEvent event) {
        ObservableList<Items> items = tblItem.getItems();
        if (items.size() == 0){
            txtItemCode.setText("I001");
        }else{
            Items lastItem = items.get(items.size()-1);
            String lastItemsCode = lastItem.getCode();
            String number = lastItemsCode.substring(1,4);
            int newItemCode = Integer.parseInt(number)+1;
            if (newItemCode < 10) {

                txtItemCode.setText("I00" + newItemCode);
            } else if (newItemCode < 100) {

                txtItemCode.setText("I0" + newItemCode);
            } else {

                txtItemCode.setText("I" + newItemCode);
            }
        }
        btnSave.setDisable(false);
        txtItemCode.setDisable(false);
        txtDescription.setDisable(false);
        txtQuantity.setDisable(false);
        txtUnitPrice.setDisable(false);
        btnSave.setDisable(false);
        txtDescription.requestFocus();
        btnDelete.setDisable(true);
        btnSave.setText("Save");
        btnAddNewItem.setDisable(true);

        txtDescription.clear();
        txtQuantity.clear();
        txtUnitPrice.clear();


    }

    public void btn_Save(ActionEvent event) {
        String code =txtItemCode.getText();
        String description =txtDescription.getText();
        int quantity =Integer.parseInt(txtQuantity.getText());
        double unitPrice = Double.parseDouble(txtUnitPrice.getText()) ;
        if (description.trim().length() == 0 || quantity == 0){
            new Alert(Alert.AlertType.ERROR,"Fields cant't be empty",ButtonType.OK).show();
            return;
        }
        if (btnSave.getText().equals("Save")) {

            ObservableList<Items> items = tblItem.getItems();
            items.add(new Items(code, description, quantity, unitPrice));
            new Alert(Alert.AlertType.INFORMATION, "Item added successfully", ButtonType.OK).show();

        }else {
            ObservableList<Items> items = tblItem.getItems();
            int selectIndex = tblItem.getSelectionModel().getSelectedIndex();
            items.get(selectIndex).setDescription(txtDescription.getText());
            items.get(selectIndex).setQuantity(Integer.parseInt(txtQuantity.getText()));
            items.get(selectIndex).setUnitPrice(Double.parseDouble(txtUnitPrice.getText()));
            tblItem.getSelectionModel().clearSelection();
            tblItem.refresh();
            txtUnitPrice.setDisable(true);
            txtQuantity.setDisable(true);
            txtDescription.setDisable(true);
            btnSave.setDisable(true);
            btnAddNewItem.requestFocus();
        }
        txtItemCode.clear();
        txtDescription.clear();
        txtQuantity.clear();
        txtUnitPrice.clear();
        btnSave.setDisable(true);
        txtDescription.setDisable(true);
        txtQuantity.setDisable(true);
        txtUnitPrice.setDisable(true);
        btnDelete.setDisable(true);
        btnAddNewItem.setDisable(false);



    }

    public void btn_Delete(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Do you want to delete?",ButtonType.YES,ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES){
            ObservableList<Items> items = tblItem.getItems();
            int selectedItem = tblItem.getSelectionModel().getSelectedIndex();
            items.remove(selectedItem);
            if (items.size()==0){
                txtUnitPrice.setDisable(true);
                txtQuantity.setDisable(true);
                txtItemCode.setDisable(true);
                txtDescription.setDisable(true);
                btnSave.setDisable(true);
                btnAddNewItem.requestFocus();
                btnDelete.setDisable(true);
            }
        }
    }


    public void btnDelete_onMouseEntered(MouseEvent mouseEvent) {
        btnDelete.setStyle("-fx-border-color: #F4200A ");
    }

    public void btn_Delete_onMuseClicked(MouseEvent mouseEvent) {

    }

    public void btnDelete_onMouseExit(MouseEvent mouseEvent) {
        btnDelete.setStyle("-fx-border-color: #D0CCCB ");
    }

    public void home_OnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/MainWndw.fxml"));
        Scene itemForm = new Scene((root));
        Stage mainStage = (Stage) this.Item.getScene().getWindow();
        mainStage.setScene(itemForm);
        mainStage.setTitle("Main");
        mainStage.centerOnScreen();
    }
}
