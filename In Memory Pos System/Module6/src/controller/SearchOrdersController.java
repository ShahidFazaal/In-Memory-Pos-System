package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Customer;
import model.Order;
import model.OrderDetail;
import model.OrderTM;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class SearchOrdersController {
    public TextField txtSearch;
    public TableView <OrderTM> tblOrders;

    private ArrayList<OrderTM> orders = new ArrayList<>();

    public void initialize() {

        tblOrders.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("orderId"));
        tblOrders.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        tblOrders.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("customerId"));
        tblOrders.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("customerName"));
        tblOrders.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("orderTotal"));

        for (Order order : PlaceOrderController.ordersDB) {
            OrderTM o = new OrderTM(order.getId(), order.getDate(),
                    order.getCustomerId(), getCustomerName(order.getCustomerId()),
                    getOrderTotal(order.getOrderDetails()));
            orders.add(o);
            tblOrders.getItems().add(o);
        }

        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ObservableList<OrderTM> searchOrders = tblOrders.getItems();
                searchOrders.clear();
                for (OrderTM order : orders) {
                    if ((order.getOrderId().contains(newValue)||
                            order.getCustomerId().contains(newValue) ||
                            order.getCustomerName().contains(newValue) ||
                            order.getOrderDate().toString().contains(newValue))){
                        searchOrders.add(order);
                    }
                }
            }
        });

    }

    public void tblOrders_OnMouseClicked(MouseEvent mouseEvent) {
    }

    public void navigateToHome(MouseEvent mouseEvent) throws IOException {
        URL resource = this.getClass().getResource("/view/MainWndw.fxml");
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) (this.txtSearch.getScene().getWindow());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();

    }



    private String getCustomerName(String customerId){
        for (Customer customer : CustomersController.customersDB) {
            if (customer.getId().equals(customerId)){
                return customer.getName();
            }
        }
        return null;
    }

    private double getOrderTotal(ArrayList<OrderDetail> orderDetails){
        double total = 0;
        for (OrderDetail orderDetail : orderDetails) {
            total += orderDetail.getQty() * orderDetail.getUnitPrice();
        }
        return total;
    }
}
