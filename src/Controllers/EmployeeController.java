package Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.Optional;

class Employee {

    public String name;
    public int id;

    public Employee(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSID() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}

class EmployeeList {
    private List<Employee> employeeList;

    public EmployeeList() {
        employeeList = new ArrayList<>();
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void addEmployee(Employee employee) {
        employeeList.add(employee);
    }

    public void removeEmployee(Employee employee) {
        employeeList.remove(employee);
    }

    public Employee getEmployeeById(int id) {
        for (Employee employee : employeeList) {
            if (employee.getSID() == id) {
                return employee;
            }
        }
        return null;
    }
}

class EmployeeForm extends GridPane {

    private final TextField nameField;
    private final TextField idField;

    public EmployeeForm() {
        // Set grid pane properties
        setPadding(new Insets(10));
        setHgap(10);
        setVgap(10);

        // Create name field and label
        Label nameLabel = new Label("Name:");
        nameField = new TextField();
        nameField.setPromptText("Enter employee name");

        // Add name label and field to grid pane
        add(nameLabel, 0, 0);
        add(nameField, 1, 0);

        // Create ID field and label
        Label idLabel = new Label("ID:");
        idField = new TextField();
        idField.setPromptText("Enter employee ID");

        // Add ID label and field to grid pane
        add(idLabel, 0, 1);
        add(idField, 1, 1);
    }

    public String getName() {
        return nameField.getText();
    }

    public int getSID() {
        try {
            return Integer.parseInt(idField.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void setName(String name) {
        nameField.setText(name);
    }

    public void setId(int id) {
        idField.setText(String.valueOf(id));
    }

    public void requestFocus() {
        nameField.requestFocus();
    }

    public Employee getEmployee() {
        return new Employee(getName(), getSID());
    }
}

public class EmployeeController {

    @FXML
    private ListView<Employee> employeeListView;
    @FXML
    private Button addButton, modifyButton, removeButton, viewButton, backButton;

    private EmployeeList employeeList;

    public void goBack(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    public void initialize() {
        employeeList = new EmployeeList(); // initialize the employee list
        // employeeListView.setItems(employeeList.getEmployeeList()); // set the
        // employee list
        // to the ListView
        ObservableList<Employee> observableEmployeeList = FXCollections
                .observableArrayList(employeeList.getEmployeeList());
        employeeListView.setItems(observableEmployeeList);

        employeeListView.setCellFactory(employeeListView -> new ListCell<>() {
            @Override
            protected void updateItem(Employee employee, boolean empty) {
                super.updateItem(employee, empty);

                if (empty || employee == null) {
                    setText(null);
                } else {
                    setText(employee.getName());
                }
            }
        });

        addButton.setOnAction(event -> addEmployee()); // set the Add button action
        modifyButton.setOnAction(event -> modifyEmployee()); // set the Modify button action
        removeButton.setOnAction(event -> removeEmployee()); // set the Remove button action
        viewButton.setOnAction(event -> viewEmployee()); // set the View button action
    }

    // Add button action
    private void addEmployee() {
        Dialog<Employee> dialog = new Dialog<>();
        dialog.setTitle("Add Employee");

        // Set the button types
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create the employee form
        EmployeeForm employeeForm = new EmployeeForm();

        // Set the content
        dialog.getDialogPane().setContent(employeeForm);

        // Request focus on the first field by default
        employeeForm.requestFocus();

        // Convert the result to an employee object when the add button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return employeeForm.getEmployee();
            }
            return null;
        });

        Optional<Employee> result = dialog.showAndWait();

        result.ifPresent(employee -> {
            employeeList.addEmployee(employee); // add the new employee to the list

            // add employee name to the ListView
            employeeListView.getItems().add(employee);

            // alternatively, you can use a forEach loop to add each employee name to the
            // ListView
            // tableView.getItems().forEach(emp -> listView.getItems().add(emp.getName()));
        });
    }

    // Modify button action
    // Modify button action
    private void modifyEmployee() {
        Employee selectedEmployee = employeeListView.getSelectionModel().getSelectedItem();

        if (selectedEmployee != null) {
            Dialog<Employee> dialog = new Dialog<>();
            dialog.setTitle("Modify Employee");

            // Set the button types
            ButtonType modifyButtonType = new ButtonType("Modify", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(modifyButtonType, ButtonType.CANCEL);

            // Create the employee form
            EmployeeForm employeeForm = new EmployeeForm();
            employeeForm.setName(selectedEmployee.getName());
            employeeForm.setId(selectedEmployee.getSID());

            // Set the content
            dialog.getDialogPane().setContent(employeeForm);

            // Request focus on the first field by default
            employeeForm.requestFocus();

            // Convert the result to an employee object when the modify button is clicked
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == modifyButtonType) {
                    selectedEmployee.setName(employeeForm.getName());
                    selectedEmployee.setId(employeeForm.getSID());
                    return selectedEmployee;
                }
                return null;
            });

            Optional<Employee> result = dialog.showAndWait();

            result.ifPresent(employee -> {
                employeeListView.refresh();
            });
        }
    }

    // Remove button action
    // Remove button action
    private void removeEmployee() {
        Employee selectedEmployee = employeeListView.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Are you sure you want to remove this employee?");
            alert.setContentText(selectedEmployee.getName() + " will be removed from the list.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                employeeList.removeEmployee(selectedEmployee);
                employeeListView.getItems().remove(selectedEmployee);
                ObservableList<Employee> updatedObservableList = FXCollections
                        .observableList(employeeList.getEmployeeList());
                employeeListView.setItems(updatedObservableList);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No employee selected");
            alert.setContentText("Please select an employee to remove.");

            alert.showAndWait();
        }
    }

    // View button action
    private void viewEmployee() {
        Employee selectedEmployee = employeeListView.getSelectionModel().getSelectedItem();

        if (selectedEmployee != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Employee Information");
            alert.setHeaderText(null);
            alert.setContentText(selectedEmployee.toString());
            alert.showAndWait();
        }
    }

}
