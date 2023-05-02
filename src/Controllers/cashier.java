package Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.Node;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class cashier implements Initializable {

    @FXML
    private ListView<Product> myListView;

    @FXML
    private ListView<Product> carListView;

    @FXML
    private Button addButton, backButton, removeButton, ViewButton, checkoutButton;

    @FXML
    private TextField TotAmount;

    public void goBack(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    float total = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        List<Product> cart = new ArrayList<>();

        ObservableList<Product> productNames = FXCollections.observableArrayList();
        for (Product p : productClass.products) {
            productNames.add(p);
        }

        // set list to ListView
        myListView.setItems(productNames);

        removeButton.setOnAction(e -> {
            // Get the index of the selected item in the ListView
            int selectedIndex = carListView.getSelectionModel().getSelectedIndex();

            // If an item is selected, remove it from the ListView and the products list
            if (selectedIndex != -1) {
                Product selectedProduct = carListView.getSelectionModel().getSelectedItem();
                carListView.getItems().remove(selectedProduct);
                // productClass.products.remove(selectedProduct);
                total -= selectedProduct.unitCost * selectedProduct.quantity;
                TotAmount.setText(String.valueOf(total));
                System.out.println(selectedProduct.quantity + " " + selectedProduct.unitCost);

                for (Product x : productClass.products) {
                    if (selectedProduct.ID == x.ID) {
                        x.quantity += selectedProduct.quantity;
                    }
                }
            }
        });

        addButton.setOnAction(e -> {
            // Get the selected item in the ListView
            Product selectedProduct = myListView.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                // Show a dialog box to modify the selected product
                Dialog<Product> dialog = new Dialog<>();
                dialog.setTitle("Edit Product");
                dialog.setHeaderText(null);

                // Set up the dialog box content
                Label quantityLabel = new Label("Quantity:");
                TextField quantityField = new TextField(null);
                // Label quantityLabel = new Label("Quantity:");
                // TextField quantityField = new
                // TextField(String.valueOf(selectedProduct.quantity));
                // Label costLabel = new Label("Unit Cost:");
                // TextField costField = new
                // TextField(String.valueOf(selectedProduct.unitCost));
                // Label IDLabel = new Label("ID :");
                // TextField IDfield = new TextField(String.valueOf(selectedProduct.ID));

                GridPane content = new GridPane();
                content.setHgap(10);
                content.setVgap(10);
                content.add(quantityLabel, 0, 0);
                content.add(quantityField, 1, 0);
                // content.add(quantityLabel, 0, 1);
                // content.add(quantityField, 1, 1);
                // content.add(costLabel, 0, 2);
                // content.add(costField, 1, 2);
                // content.add(IDLabel, 0, 3);
                // content.add(IDfield, 1, 3);
                dialog.getDialogPane().setContent(content);

                // Set up the dialog box buttons
                ButtonType saveButtonType = new ButtonType("Ok", ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

                // Wait for the user to click a button
                Optional<Product> result = dialog.showAndWait();

                boolean ok = false;

                // If the user clicked the save button, update the selected product
                while (result.isPresent() && result.get() != null && ok == false) {
                    int quantity = Integer.parseInt(quantityField.getText());

                    if (quantity > selectedProduct.quantity) {
                        quantity = selectedProduct.quantity;
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Quantity Exceeded");
                        alert.setHeaderText(null);
                        alert.setContentText("Max quantity is added " + selectedProduct.quantity);
                        alert.showAndWait();
                    }

                    total += quantity * selectedProduct.unitCost;
                    TotAmount.setText(String.valueOf(total));

                    selectedProduct.quantity = selectedProduct.quantity - quantity;
                    // int unitCost = Integer.parseInt(costField.getText());
                    // int ID = Integer.parseInt(IDfield.getText());
                    String name = selectedProduct.name;
                    // int quantity = Integer.parseInt(quantityField.getText());
                    int unitCost = selectedProduct.unitCost;
                    int ID = selectedProduct.ID;
                    Product newProduct = new Product(name, quantity, ID, unitCost);
                    cart.add(newProduct);
                    carListView.getItems().add(newProduct);
                    ok = true;
                    // nameField.clear();
                    // costField.clear();
                    // quantityField.clear();
                    // IDfield.clear();
                    // Update the ListView to reflect the changes
                    carListView.refresh();

                }
            }
        });

        // Checkout of the sale
        checkoutButton.setOnAction(e -> {
            Alert alert = new Alert(AlertType.WARNING,
                    "Are you sure you want to checkout? This action cannot be undone.",
                    ButtonType.YES, ButtonType.NO);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                // Perform the checkout action
                // List<Product> selectedProducts = carListView.getItems();
                // salesClass.addSale(selectedProducts);

                // adjusting sales money
                salesClass.Total_sales = salesClass.Total_sales + 1;
                salesClass.Total_sales_money += total;

                carListView.getItems().clear();
                total = 0;
                TotAmount.setText(String.valueOf(total));

            } else {
                // Do nothing and close the alert dialog
            }
        });

    }

    // View the product
    public void viewproduct(ActionEvent event) throws IOException {
        Product selectedProduct = myListView.getSelectionModel().getSelectedItem();

        if (selectedProduct != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Product Information");
            alert.setHeaderText(null);
            alert.setContentText(selectedProduct.info());
            alert.showAndWait();
        }
    }

}
