package Controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.print.DocFlavor.STRING;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

class Product {

    public int ID;
    public String name;
    public int quantity;

    public Product(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return name;
    }
};

public class StoreController implements Initializable {

    @FXML
    private ListView<Product> myListView;

    @FXML
    private TextField nameField;

    @FXML
    private TextField priceField;

    @FXML
    private Button addButton;

    @FXML
    private Button removeButton;

    @FXML
    private Button editButton;

    private List<Product> products = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up the add button
        addButton.setOnAction(e -> {
            String name = nameField.getText();
            int price = Integer.parseInt(priceField.getText());
            Product newProduct = new Product(name, price);
            products.add(newProduct);
            myListView.getItems().add(newProduct);
            nameField.clear();
            priceField.clear();
        });

        // Set up the remove button
        removeButton.setOnAction(e -> {
            // Get the index of the selected item in the ListView
            int selectedIndex = myListView.getSelectionModel().getSelectedIndex();

            // If an item is selected, remove it from the ListView and the products list
            if (selectedIndex != -1) {
                Product selectedProduct = myListView.getSelectionModel().getSelectedItem();
                myListView.getItems().remove(selectedProduct);
                products.remove(selectedProduct);
            }
        });

        editButton.setOnAction(e -> {
            // Get the selected item in the ListView
            Product selectedProduct = myListView.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                // Show a dialog box to modify the selected product
                Dialog<Product> dialog = new Dialog<>();
                dialog.setTitle("Edit Product");
                dialog.setHeaderText(null);

                // Set up the dialog box content
                Label nameLabel = new Label("Name:");
                TextField nameField = new TextField(selectedProduct.name);
                Label quantityLabel = new Label("Quantity:");
                TextField quantityField = new TextField(String.valueOf(selectedProduct.quantity));
                GridPane content = new GridPane();
                content.setHgap(10);
                content.setVgap(10);
                content.add(nameLabel, 0, 0);
                content.add(nameField, 1, 0);
                content.add(quantityLabel, 0, 1);
                content.add(quantityField, 1, 1);
                dialog.getDialogPane().setContent(content);

                // Set up the dialog box buttons
                ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

                // Wait for the user to click a button
                Optional<Product> result = dialog.showAndWait();

                // If the user clicked the save button, update the selected product
                if (result.isPresent() && result.get() != null) {
                    String name = nameField.getText();
                    int quantity = Integer.parseInt(quantityField.getText());
                    selectedProduct.name = name;
                    selectedProduct.quantity = quantity;
                    // Update the ListView to reflect the changes
                    myListView.refresh();
                }
            }
        });
    }
}
