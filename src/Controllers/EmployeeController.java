package Controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.PriorityBlockingQueue;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;

class Employee {
    String name;
    Integer ID;

    Employee(String name, int ID) {
        this.name = name;
        this.ID = ID;
    }

    @Override
    public String toString() {
        return name;
    }

    public String info() {
        return "Name : " + name + "\n" + "ID : " + ID.toString();
    }

    public static List<Employee> EmployeeList = new ArrayList<>();

}

public class EmployeeController implements Initializable {

    @FXML
    private ListView<Employee> employeeListView;
    @FXML
    private Button addButton, modifyButton, removeButton, viewButton, backButton;

    public void goBack(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    // Refreshes the EmployeeListView
    private void refresh() {
        ObservableList<Employee> employeeObservableList = FXCollections.observableArrayList();

        for (Employee p : Employee.EmployeeList) {
            employeeObservableList.add(p);
        }

        // set list to ListView
        employeeListView.setItems(employeeObservableList);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        refresh();

        addButton.setOnAction(event -> {
            Dialog<Employee> dialog = new Dialog<>();
            dialog.setTitle("Add Employee");

            ButtonType addButtonType = new ButtonType("Add", ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(20, 10, 10, 10));

            TextField nameTextField = new TextField();
            nameTextField.setPromptText("Name");
            TextField idTextField = new TextField();
            idTextField.setPromptText("ID");

            gridPane.add(new Label("Name:"), 0, 0);
            gridPane.add(nameTextField, 1, 0);
            gridPane.add(new Label("ID:"), 0, 1);
            gridPane.add(idTextField, 1, 1);

            dialog.getDialogPane().setContent(gridPane);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == addButtonType) {
                    String name = nameTextField.getText().trim();
                    String idString = idTextField.getText().trim();
                    if (!name.isEmpty() && !idString.isEmpty()) {
                        try {
                            int id = Integer.parseInt(idString);
                            return new Employee(name, id);
                        } catch (NumberFormatException e) {
                            // Invalid ID, do nothing
                        }
                    }
                }
                return null;
            });

            Optional<Employee> result = dialog.showAndWait();
            result.ifPresent(employee -> {
                Employee.EmployeeList.add(employee);
            });

            refresh();
        });

        // Remove Object
        removeButton.setOnAction(event -> {
            Employee selectedEmployee = employeeListView.getSelectionModel().getSelectedItem();
            if (selectedEmployee != null) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Remove Employee");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to remove this employee?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    Employee.EmployeeList.remove(selectedEmployee);
                }
            }

            refresh();
        });

        // Modify Selected Object
        modifyButton.setOnAction(event -> {
            Employee selectedEmployee = employeeListView.getSelectionModel().getSelectedItem();
            if (selectedEmployee != null) {
                Dialog<Employee> dialog = new Dialog<>();
                dialog.setTitle("Modify Employee");

                // Set the button types
                ButtonType modifyButtonType = new ButtonType("Modify", ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(modifyButtonType, ButtonType.CANCEL);

                // Create the name and ID labels and text fields
                Label nameLabel = new Label("Name:");
                TextField nameField = new TextField(selectedEmployee.name);
                Label idLabel = new Label("ID:");
                TextField idField = new TextField(selectedEmployee.ID.toString());

                // Add the labels and text fields to the dialog pane
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.add(nameLabel, 1, 1);
                grid.add(nameField, 2, 1);
                grid.add(idLabel, 1, 2);
                grid.add(idField, 2, 2);
                dialog.getDialogPane().setContent(grid);

                // Request focus on the name field by default
                Platform.runLater(() -> nameField.requestFocus());

                // Convert the result to a Employee object when the Modify button is clicked
                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == modifyButtonType) {
                        String name = nameField.getText();
                        Integer id = Integer.parseInt(idField.getText());
                        selectedEmployee.name = name;
                        selectedEmployee.ID = id;
                        Employee.EmployeeList.set(Employee.EmployeeList.indexOf(selectedEmployee), selectedEmployee);
                        return selectedEmployee;
                    }
                    return null;
                });

                Optional<Employee> result = dialog.showAndWait();
            }
            refresh();
        });

        // View button action
        viewButton.setOnAction(event -> {
            Employee selectedEmployee = employeeListView.getSelectionModel().getSelectedItem();

            if (selectedEmployee != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Employee Information");
                alert.setHeaderText(null);
                alert.setContentText(selectedEmployee.info());
                alert.showAndWait();
            }
        });

    }
}