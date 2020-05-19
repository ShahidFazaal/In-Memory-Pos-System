package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWndwController {
    public Button btnManageCustomers;
    public Button btnManageItems;
    public Button btnPlaceOrders;
    public Button btnSearchOrders;
    public AnchorPane MainWindow;

    public void mangeCustomers_OnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/Customers.fxml"));
        Scene customerForm = new Scene((root));
        Stage mainStage = (Stage) this.MainWindow.getScene().getWindow();
        mainStage.setScene(customerForm);
        mainStage.setTitle("Customer");
        mainStage.centerOnScreen();
    }

    public void mangeItems_OnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/Items.fxml"));
        Scene itemForm = new Scene((root));
        Stage mainStage = (Stage) this.MainWindow.getScene().getWindow();
        mainStage.setScene(itemForm);
        mainStage.setTitle("Items");
        mainStage.centerOnScreen();
    }

    public void placeOrders_OnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/PlaceOrder.fxml"));
        Scene placeOrder = new Scene((root));
        Stage mainStage = (Stage) this.MainWindow.getScene().getWindow();
        mainStage.setScene(placeOrder);
        mainStage.setTitle("Place Order");
        mainStage.centerOnScreen();
    }

    public void Searchorders_OnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/SearchOrdersForm.fxml"));
        Scene placeOrder = new Scene((root));
        Stage mainStage = (Stage) this.MainWindow.getScene().getWindow();
        mainStage.setScene(placeOrder);
        mainStage.setTitle("Search Order");
        mainStage.centerOnScreen();
    }
}
