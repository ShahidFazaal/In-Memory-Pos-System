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
import model.*;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class PlaceOrderController {
    public static ArrayList<String> orderIds = new ArrayList<>();
    public static ArrayList<Order> ordersDB = new ArrayList<>();
    public TextField txtOrderid;
    public TextField txtOrderDate;
    public TextField txtCName;
    public TextField txtDescription;
    public TextField txtQuantity;
    public TextField txtUnitPrice;
    public TextField txtQuantityOnhand;
    public ComboBox<Customer> cmbbxCustomerId;
    public ComboBox<Items> cmbbxItemId;
    public TextField txtTotal;
    public TableView<OrderDetailTable> tblOrderDetails;
    public AnchorPane OrderDetails;
    public Button btnAddQuantity;
    public Button btnPlaceOrder;
    public Button btnHome;
    ObservableList<String> orderId = FXCollections.observableList(orderIds);

    public void initialize() {


        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(date);
        txtOrderDate.setText(formattedDate);

        if (orderId.size() == 0) {
            txtOrderid.setText("O001");
        } else {
            String lastOrderId = orderId.get(orderId.size() - 1);

            String number = lastOrderId.substring(1, 4);
            int newId = Integer.parseInt(number) + 1;

            if (newId < 10) {

                txtOrderid.setText("O00" + newId);
            } else if (newId < 100) {

                txtOrderid.setText("O0" + newId);
            } else {

                txtOrderid.setText("O" + newId);
            }

        }

        //Customer Id load
        ObservableList<Customer> customers = FXCollections.observableList(CustomersController.customersDB);
        cmbbxCustomerId.setItems(customers);

        cmbbxCustomerId.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Customer>() {
            @Override
            public void changed(ObservableValue<? extends Customer> observable, Customer oldValue, Customer newValue) {
                txtCName.setText(newValue.getName());
            }
        });

        ObservableList<Items> alItems = FXCollections.observableList(ItemsController.itemsDB);
        cmbbxItemId.setItems(alItems);

        cmbbxItemId.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Items>() {
            @Override
            public void changed(ObservableValue<? extends Items> observable, Items oldValue, Items newValue) {
                if ((newValue == null)) {
                    txtUnitPrice.clear();
                    txtDescription.clear();
                    txtQuantityOnhand.clear();
                    return;
                }
                txtDescription.setText(newValue.getDescription());
                calculateQtyOnHand(newValue);
                txtUnitPrice.setText(newValue.getUnitPrice() + "");
                txtQuantity.requestFocus();

            }
        });
        // Let's map columns
        tblOrderDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("code"));
        tblOrderDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
        tblOrderDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("qty"));
        tblOrderDetails.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tblOrderDetails.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("total"));


    }


    public void btnAddQuantity(ActionEvent event) {

        // Let's do some validation

        if (txtQuantity.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Qty can't be empty", ButtonType.OK).show();
            return;
        }
        int qty = Integer.parseInt(txtQuantity.getText());
        if (qty < 1 || qty > Integer.parseInt(txtQuantityOnhand.getText())) {
            new Alert(Alert.AlertType.ERROR, "Invalid Qty.", ButtonType.OK).show();
            return;
        }

        Items selectedItem = cmbbxItemId.getSelectionModel().getSelectedItem();
        ObservableList<OrderDetailTable> orderDetails = tblOrderDetails.getItems();

        boolean exist = false;
        for (OrderDetailTable orderDetail : orderDetails) {
            if (orderDetail.getCode().equals(selectedItem.getCode())) {
                exist = true;
                orderDetail.setQty(orderDetail.getQty() + qty);
                orderDetail.setTotal(orderDetail.getQty() * orderDetail.getUnitPrice());
                tblOrderDetails.refresh();
                break;
            }
        }

        if (!exist) {

            orderDetails.add(new OrderDetailTable(selectedItem.getCode(), selectedItem.getDescription(), qty, selectedItem.getUnitPrice(), qty * selectedItem.getUnitPrice()));
        }

        calculateTotal();
        cmbbxItemId.getSelectionModel().clearSelection();
        txtQuantityOnhand.clear();
        txtUnitPrice.clear();
        txtQuantity.clear();
        txtDescription.clear();
        cmbbxItemId.requestFocus();

//        cmbbxItemId.getSelectionModel().clearSelection();
//        txtQuantity.clear();
//        cmbbxItemId.requestFocus();


    }

    public void btnPlaceOrder(ActionEvent event) throws IOException {
        if (cmbbxCustomerId.getSelectionModel().getSelectedIndex() == -1) {
            new Alert(Alert.AlertType.ERROR, "You need to select a customer", ButtonType.OK).show();
            cmbbxCustomerId.requestFocus();
            return;
        }

        if (tblOrderDetails.getItems().size() == 0) {
            new Alert(Alert.AlertType.ERROR, "Ubata pissuda yako, nikan order dannea", ButtonType.OK).show();
            cmbbxItemId.requestFocus();
            return;
        }


        // Let's save the order
        ArrayList<OrderDetail> alOrderDetails = new ArrayList<>();
        ObservableList<OrderDetailTable> allTheOrderDetailsInTheTable = tblOrderDetails.getItems();

        for (OrderDetailTable orderDetail : allTheOrderDetailsInTheTable) {
            // Let's update the stock
            updateStockQty(orderDetail.getCode(), orderDetail.getQty());
            alOrderDetails.add(new OrderDetail(orderDetail.getCode(), orderDetail.getQty(), orderDetail.getUnitPrice()));
        }
        Order newOrder = new Order(txtOrderid.getText(), LocalDate.now(),cmbbxCustomerId.getSelectionModel().getSelectedItem().getId(), alOrderDetails);
        //System.out.println( cmbbxCustomerId.getId());
        //System.out.println( cmbbxCustomerId.getSelectionModel().getSelectedItem().getId());
        ordersDB.add(newOrder);
        new Alert(Alert.AlertType.INFORMATION, "Mudalali wade goda", ButtonType.OK).showAndWait();
        tblOrderDetails.getItems().clear();
        txtQuantity.clear();
        // cmbbxCustomerId.getSelectionModel().clearSelection();
        cmbbxItemId.getSelectionModel().clearSelection();
        calculateTotal();
        orderId.add(txtOrderid.getText());

    }

    private void calculateQtyOnHand(Items item) {
        txtQuantityOnhand.setText(item.getQuantity() + "");
        ObservableList<OrderDetailTable> tableDetail = tblOrderDetails.getItems();
        System.out.println(tableDetail.size());

        for (int i = 0; i < tableDetail.size(); i++) {
            OrderDetailTable orderDetail = tableDetail.get(i);
            if (orderDetail.getCode().equals(item.getCode())) {
                int displayQty = item.getQuantity() - orderDetail.getQty();
                txtQuantityOnhand.setText(displayQty + "");
                break;

            }
        }
    }

    public void calculateTotal() {
        ObservableList<OrderDetailTable> orderDetails = tblOrderDetails.getItems();
        double netTotal = 0;
        for (OrderDetailTable orderDetail : orderDetails) {
            netTotal = netTotal + orderDetail.getTotal();
        }
        NumberFormat numberInstance = NumberFormat.getNumberInstance();
        numberInstance.setMaximumFractionDigits(2);
        numberInstance.setMinimumFractionDigits(2);
        numberInstance.setGroupingUsed(true);
        String formattedText = numberInstance.format(netTotal);
        txtTotal.setText(formattedText);
    }


    private void updateStockQty(String itemCode, int qty) {

        for (Items item : ItemsController.itemsDB) {
            if (item.getCode().equals(itemCode)) {
                item.setQuantity(item.getQuantity() - qty);
                return;
            }
        }
    }

    public void btnHome_OnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/MainWndw.fxml"));
        Scene placeOrder = new Scene((root));
        Stage mainStage = (Stage) this.OrderDetails.getScene().getWindow();
        mainStage.setScene(placeOrder);
        mainStage.setTitle("Search Order");
        mainStage.centerOnScreen();
    }
}
