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
import java.io.*;
import java.util.*;

public class cashier implements Initializable {

    @FXML
    public ListView<Product> myListView;

    @FXML
    public ListView<Product> carListView;

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
        ArrayList<Product> productList = new ArrayList<>();
        File inputFile = new File("src/Controllers/storeProducts.txt");
        try (Scanner scanner = new Scanner(inputFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");
                String name = parts[0].trim();
                int ID = Integer.parseInt(parts[1].trim());
                int quantity = Integer.parseInt(parts[2].trim());
                int cost = Integer.parseInt(parts[3].trim());
                Product product = new Product(name, ID, quantity, cost);
                productList.add(product);
                productNames.add(product);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        myListView.getItems().clear();
        myListView.getItems().addAll(productList);
        // for (Product ele : productList)
        // System.out.println(ele.name);
        // // myListView.setItems(productList);
        // // set list to ListView
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

            if (selectedProduct.quantity == 0) {
                return;
            }

            if (selectedProduct != null) {
                // Show a dialog box to modify the selected product
                Dialog<Product> dialog = new Dialog<>();
                dialog.setTitle("Edit Product");
                dialog.setHeaderText(null);

                // Set up the dialog box content
                Label quantityLabel = new Label("Quantity:");
                TextField quantityField = new TextField(null);

                GridPane content = new GridPane();
                content.setHgap(10);
                content.setVgap(10);
                content.add(quantityLabel, 0, 0);
                content.add(quantityField, 1, 0);

                dialog.getDialogPane().setContent(content);

                // Set up the dialog box buttons
                ButtonType saveButtonType = new ButtonType("Ok", ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

                // Wait for the user to click a button
                Optional<Product> result = dialog.showAndWait();

                boolean ok = false;
                // total = 0;
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
                    // TotAmount.setText(String.valueOf(total));

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

                    carListView.refresh();
                    TotAmount.setText(String.valueOf(total));

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

                try {
                    BufferedReader reader = new BufferedReader(new FileReader("src/Controllers/total_sales.txt"));

                    // Read the first integer
                    salesClass.Total_sales = Integer.parseInt(reader.readLine());
                    salesClass.Total_sales++;
                    reader.close();
                } catch (Exception eee) {

                }

                // adjusting sales money
                // salesClass.Total_sales = salesClass.Total_sales + 1;
                salesClass.Total_sales_money += total;

                String fileName = "src/Controllers/cash.txt";
                String lineToAppend = "1000";

                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
                    for (Product x : carListView.getItems()) {
                        System.out.println(x.name + " " + x.quantity + " " + x.ID + " eigula bechsi");
                        lineToAppend = salesClass.Total_sales + "." + x.name + " " + x.quantity + " " + x.ID + " "
                                + x.unitCost * x.quantity;
                        writer.append(lineToAppend);
                        writer.newLine();
                        // start
                        String fileNamed = "src/Controllers/storeProducts.txt";
                        String nameToUpdate = x.name;

                        try (BufferedReader reader = new BufferedReader(new FileReader(fileNamed))) {
                            String line;
                            StringBuilder content = new StringBuilder();

                            while ((line = reader.readLine()) != null) {
                                String[] parts = line.split(" ");
                                if (parts.length >= 4 && parts[0].equals(nameToUpdate)) {
                                    int quantity = Integer.parseInt(parts[1]);
                                    quantity -= x.quantity;
                                    System.out.println("vai, paisi to0" + quantity + "->" + x.quantity);
                                    parts[1] = String.valueOf(quantity);
                                    line = String.join(" ", parts);
                                }
                                content.append(line).append("\n");
                            }

                            try (BufferedWriter writerss = new BufferedWriter(new FileWriter(fileName))) {
                                writerss.write(content.toString());
                            }

                            System.out.println("Quantity updated successfully.");
                        } catch (IOException es) {
                            es.printStackTrace();
                        }
                    }
                    // end

                    writer.close();

                    System.out.println("Line added to file: " + lineToAppend);
                } catch (Exception ee) {
                    System.out.println("An error occurred: " + ee.getMessage());
                }

                carListView.getItems().clear();
                // total = 0;
                TotAmount.setText(String.valueOf(total));

                // Update total sales

                try {
                    int first, second;
                    // Open the file for reading
                    BufferedReader reader = new BufferedReader(new FileReader("src/Controllers/total_sales.txt"));

                    // Read the first integer
                    first = Integer.parseInt(reader.readLine());

                    // Read the second integer
                    second = Integer.parseInt(reader.readLine());
                    // Close the file
                    reader.close();

                    // Add 10 to each integer
                    first += 1;
                    second += total;
                    // Total_sales_money = first;
                    // Total_sales = second;
                    // System.out.println(Total_sales + " " + Total_sales_money);

                    // Open the file for writing
                    BufferedWriter writer = new BufferedWriter(new FileWriter("src/Controllers/total_sales.txt"));

                    // Write the updated values to the file
                    writer.write(Integer.toString(first));
                    writer.newLine();
                    writer.write(Integer.toString(second));

                    // Close the file
                    writer.close();
                } catch (IOException eee) {
                    System.out.println("An error occurred: " + eee.getMessage());
                }

                // resetting total to 0
                total = 0;
                TotAmount.setText("0");

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
