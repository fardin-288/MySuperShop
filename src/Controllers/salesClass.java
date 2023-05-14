package Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.*;
import javafx.scene.Node;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class salesClass implements Initializable {

    public static int Total_sales;
    public static int Total_sales_money;

    @FXML
    public ListView<Product> salesListView;

    @FXML
    private TextField total_sales_money_textField, total_sales_TextField;

    @FXML
    private Button backButton;

    public void GoBack(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    public ObservableList<Product> salesData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String fileName = "src/Controllers/cash.txt";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                String[] parts = line.split(" ");
                salesData.add(new Product(parts[0] + "__" + parts[1] + "__" + parts[3], Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]), 100));
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        // Adding the products to the listview
        // ObservableList<Product> salesData = FXCollections.observableArrayList();
        // salesData.add(new Product("ahmed", 12, 21, 12));
        // salesData.add(new Product("conrad", 12, 21, 12));
        salesListView.setItems(salesData);
        salesListView.refresh();
        // Addition done

        // Open the file for reading
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/Controllers/total_sales.txt"));

            // Read the first integer
            Total_sales = Integer.parseInt(reader.readLine());

            // Read the second integer
            Total_sales_money = Integer.parseInt(reader.readLine());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

        total_sales_money_textField.setText(String.valueOf(Total_sales_money));
        System.out.println(Total_sales_money);
        total_sales_TextField.setText(String.valueOf(Total_sales));
        System.out.println(Total_sales);

    }

}
