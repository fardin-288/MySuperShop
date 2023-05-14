package Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.print.DocFlavor.STRING;
import javax.swing.plaf.TreeUI;

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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.*;
import java.util.*;

public class StoreController implements Initializable {

    @FXML
    public ListView<Product> myListView;

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
        ObservableList<Product> productNames = FXCollections.observableArrayList();

        List<Product> cart = new ArrayList<>();
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
                // productList.add(product);
                productNames.add(product);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (Product p : productClass.products) {
            productNames.add(p);
            // System.out.println(p.name + " " + p.quantity);

        }

        // set list to ListView
        myListView.setItems(productNames);

        // // Set up the add button
        addButton.setOnAction(e -> {
            String name = nameField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            int unitCost = Integer.parseInt(costField.getText());
            int ID = Integer.parseInt(IDfield.getText());

            // System.out.println(name + " " + quantity);
            String fileName = "src/Controllers/storeProducts.txt";
            try {
                FileWriter fw = new FileWriter(fileName, true);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write((name + " " + quantity + " " + ID + " " + unitCost));
                bw.newLine();
                bw.close();
                fw.close();
            } catch (IOException ex) {
                ex.printStackTrace();

            }
            Product newProduct = new Product(name, quantity, ID, unitCost);
            // productClass.products.add(newProduct);
            myListView.getItems().add(newProduct);
            nameField.clear();
            costField.clear();
            quantityField.clear();
            IDfield.clear();
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

                String filename = "src/Controllers/storeProducts.txt";
                String nameToRemove = selectedProduct.name;

                try {
                    // create a FileReader object to read from the file
                    FileReader reader = new FileReader(filename);
                    BufferedReader bufferedReader = new BufferedReader(reader);

                    // create a FileWriter object to write to a temporary file
                    FileWriter writer = new FileWriter("temp.txt", true);
                    String line;

                    // read each line of the file and copy lines that do not contain the name
                    while ((line = bufferedReader.readLine()) != null) {
                        if (!line.startsWith(nameToRemove)) {
                            writer.write(line + "\n");
                        }
                    }

                    // close the BufferedReader and the FileWriter
                    bufferedReader.close();
                    writer.close();

                    // delete the original file
                    if (!new File(filename).delete()) {
                        System.out.println("Failed to delete the original file.");
                    }

                    // rename the temporary file to the original file
                    if (!new File("temp.txt").renameTo(new File(filename))) {
                        System.out.println("Failed to rename the temporary file.");
                    }
                    System.out.println("Removed all lines with the name: " + nameToRemove);

                } catch (IOException ee) {
                    System.out.println("An error occurred while reading/writing the file.");
                    ee.printStackTrace();
                }

                // end
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
                String names = nameField.getText();
                int x = Integer.parseInt(IDfield.getText());
                // System.out.println(names + " " + x);
                // If the user clicked the save button, update the selected product
                if (result.isPresent() && result.get() != null) {
                    String name = nameField.getText();
                    int quantity = Integer.parseInt(quantityField.getText());
                    int unitCost = Integer.parseInt(costField.getText());
                    int ID = Integer.parseInt(IDfield.getText());

                    String filename = "src/Controllers/storeProducts.txt";
                    String nameToRemove = selectedProduct.name;

                    try {
                        // create a FileReader object to read from the file
                        FileReader reader = new FileReader(filename);
                        BufferedReader bufferedReader = new BufferedReader(reader);

                        // create a FileWriter object to write to a temporary file
                        FileWriter writer = new FileWriter("temp.txt");
                        String line;

                        // read each line of the file and copy lines that do not contain the name
                        while ((line = bufferedReader.readLine()) != null) {
                            if (!line.startsWith(nameToRemove)) {
                                writer.write(line + "\n");
                            }
                        }

                        // close the BufferedReader and the FileWriter
                        bufferedReader.close();
                        writer.close();

                        // delete the original file
                        if (!new File(filename).delete()) {
                            System.out.println("Failed to delete the original file.");
                        }

                        // rename the temporary file to the original file
                        if (!new File("temp.txt").renameTo(new File(filename))) {
                            System.out.println("Failed to rename the temporary file.");
                        }
                        System.out.println("Removed all lines with the name: " + nameToRemove);

                    } catch (IOException ee) {
                        System.out.println("An error occurred while reading/writing the file.");
                        ee.printStackTrace();
                    }

                    selectedProduct.name = name;
                    selectedProduct.quantity = quantity;
                    selectedProduct.unitCost = unitCost;
                    selectedProduct.ID = ID;
                    String fileName = "src/Controllers/storeProducts.txt";
                    try {
                        FileWriter fw = new FileWriter(fileName, true);
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write((name + " " + quantity + " " + ID + " " + unitCost));
                        bw.newLine();

                        bw.close();
                        fw.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();

                    }

                    for (Product ele : myListView.getItems())
                        System.out.println(ele.name + " " + ele.quantity);
                    // Update the ListView to reflect the changes
                    myListView.refresh();
                }
            }
        });
    }

}
