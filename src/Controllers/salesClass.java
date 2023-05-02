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

    public static int Total_sales = 0;
    public static int Total_sales_money = 0;

    @FXML
    public static List<List<Product>> sales;

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

    // // public static List<List<Product>> sales;
    // public ListView<Product> listView;

    // public void Sales(List<List<Product>> sales, ListView<String> listView) {
    // this.sales = sales;
    // this.listView = listView;
    // displaySales();
    // }

    // public static void addSale(List<Product> listView) {
    // // Retrieve the items from the ListView and add them to a new List<Product>
    // List<Product> productList = new ArrayList<>();
    // productList.addAll(listView.getItems());
    // for (Product x : listView) {
    // sales.add((List<Product>) x);
    // }

    // // Add the new List<Product> to the sales list
    // sales.add(productList);
    // }

    // private void displaySales() {
    // ObservableList<String> saleStrings = FXCollections.observableArrayList();
    // int saleNumber = 1;
    // for (List<Product> sale : sales) {
    // LocalDateTime saleDateTime = LocalDateTime.now(); // Replace with actual sale
    // date/time
    // String saleString = "Sale " + saleNumber + " - "
    // + saleDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    // saleStrings.add(saleString);
    // for (Product product : sale) {
    // saleStrings.add(" " + product.getName() + " - $" + product.getPrice());
    // }
    // saleNumber++;
    // }

    // listView.setItems(saleStrings.sorted(Comparator.reverseOrder()));
    // }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // ListView<String> salesListView = new ListView<>();
        // ObservableList<String> salesData = FXCollections.observableArrayList();

        // for (int i = 0; i < sales.size(); i++) {
        // List<Product> sale = sales.get(i);
        // String saleInfo = "Sale " + (i + 1) + ":\n";

        // for (Product product : sale) {
        // saleInfo += product.getName() + " - $" + product.getPrice() + "\n";
        // }

        // salesData.add(saleInfo);
        // }

        // ((ListView<String>) saleslistview).setItems(salesData);

        total_sales_money_textField.setText(String.valueOf(Total_sales_money));
        total_sales_TextField.setText(String.valueOf(Total_sales));

    }

}
