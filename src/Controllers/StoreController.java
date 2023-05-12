package Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.print.DocFlavor.STRING;

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

public class StoreController implements Initializable {

    @FXML
    private ListView<Product> myListView;

    @FXML
    private TextField nameField;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField costField;

    @FXML
    private TextField IDfield;

    @FXML
    private Button addButton;

    @FXML
    private Button removeButton;

    @FXML
    private Button editButton;

    @FXML
    private Button backButton;

    @FXML
    private Stage stage;

    @FXML
    private TextField productNumberField;

    // public List<Product> products = new ArrayList<>();

    public void clickBackButton(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        productNumberField.setText(productClass.product_num().toString());

        List<Product> cart = new ArrayList<>();

        ObservableList<Product> productNames = FXCollections.observableArrayList();
        for (Product p : productClass.products) {
            productNames.add(p);
        }

        // set list to ListView
        myListView.setItems(productNames);

        // Set up the add button
        addButton.setOnAction(e -> {
            String name = nameField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            int unitCost = Integer.parseInt(costField.getText());
            int ID = Integer.parseInt(IDfield.getText());
            Product newProduct = new Product(name, quantity, ID, unitCost);
            productClass.products.add(newProduct);
            myListView.getItems().add(newProduct);
            nameField.clear();
            costField.clear();
            quantityField.clear();
            IDfield.clear();

            productNumberField.setText(productClass.product_num().toString());

        });

        // Set up the remove button
        removeButton.setOnAction(e -> {
            // Get the index of the selected item in the ListView
            int selectedIndex = myListView.getSelectionModel().getSelectedIndex();

            // If an item is selected, remove it from the ListView and the products list
            if (selectedIndex != -1) {
                Product selectedProduct = myListView.getSelectionModel().getSelectedItem();
                myListView.getItems().remove(selectedProduct);
                productClass.products.remove(selectedProduct);
            }

            productNumberField.setText(productClass.product_num().toString());

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
                Label costLabel = new Label("Unit Cost:");
                TextField costField = new TextField(String.valueOf(selectedProduct.unitCost));
                Label IDLabel = new Label("ID :");
                TextField IDfield = new TextField(String.valueOf(selectedProduct.ID));

                GridPane content = new GridPane();
                content.setHgap(10);
                content.setVgap(10);
                content.add(nameLabel, 0, 0);
                content.add(nameField, 1, 0);
                content.add(quantityLabel, 0, 1);
                content.add(quantityField, 1, 1);
                content.add(costLabel, 0, 2);
                content.add(costField, 1, 2);
                content.add(IDLabel, 0, 3);
                content.add(IDfield, 1, 3);
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
                    int unitCost = Integer.parseInt(costField.getText());
                    int ID = Integer.parseInt(IDfield.getText());
                    selectedProduct.name = name;
                    selectedProduct.quantity = quantity;
                    selectedProduct.unitCost = unitCost;
                    selectedProduct.ID = ID;
                    // Update the ListView to reflect the changes
                    myListView.refresh();
                }
            }
            productNumberField.setText(productClass.product_num().toString());

        });
    }
}
