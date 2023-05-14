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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.PriorityBlockingQueue;

import javax.xml.transform.Source;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import java.io.*;

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

        String filename = "src/Controllers/employeeInfo.txt";

        // create a FileReader object to read from the file
        try {
            FileReader reader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line;
            Employee.EmployeeList.clear();
            // read each line of the file and extract the name and id
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split(" ");
                String name = tokens[0];
                int id = Integer.parseInt(tokens[1]);
                Employee x = new Employee(name, id);
                Employee.EmployeeList.add(x);
                // print the name and id to the console
                // System.out.println("Name: " + name + ", ID: " + id);
            }

            // close the BufferedReader
            bufferedReader.close();

        } catch (IOException e) {
            System.out.println("An error occurred while reading from the file.");
            e.printStackTrace();
        }

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
            // String employeeName = nameTextField.getText();
            // String employeeId = idTextField.getText();

            // System.out.println(employeeId + " " + employeeName);
            gridPane.add(new Label("Name:"), 0, 0);
            gridPane.add(nameTextField, 1, 0);
            gridPane.add(new Label("ID:"), 0, 1);
            gridPane.add(idTextField, 1, 1);

            dialog.getDialogPane().setContent(gridPane);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == addButtonType) {
                    String name = nameTextField.getText().trim();
                    String idString = idTextField.getText().trim();

                    for (Employee x : Employee.EmployeeList) {
                        if (Integer.parseInt(idString) == x.ID) {
                            // Show Error
                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Invalid ID");
                            alert.setHeaderText(null);
                            alert.setContentText("The ID you entered is taken. Please try again.");
                            alert.showAndWait();
                            return new Employee("john", 12);
                        }
                    }

                    if (!name.isEmpty() && !idString.isEmpty()) {
                        try {
                            int id = Integer.parseInt(idString);

                            try {
                                FileWriter writer = new FileWriter("src/Controllers/employeeInfo.txt", true);
                                BufferedWriter bufferedWriter = new BufferedWriter(writer);

                                // write the name and id to the file on a new line
                                bufferedWriter.write(name + " " + id);
                                bufferedWriter.newLine();
                                bufferedWriter.close();
                            } catch (IOException e) {
                                System.out.println("An error occurred while writing to the file.");
                                e.printStackTrace();
                            }
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

                    String filename = "src/Controllers/employeeInfo.txt";
                    String nameToRemove = selectedEmployee.name;

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

                    } catch (IOException e) {
                        System.out.println("An error occurred while reading/writing the file.");
                        e.printStackTrace();
                    }
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

                        try {
                            FileWriter writer = new FileWriter("src/Controllers/employeeInfo.txt", true);
                            BufferedWriter bufferedWriter = new BufferedWriter(writer);

                            // write the name and id to the file on a new line
                            bufferedWriter.write(name + " " + id);
                            bufferedWriter.newLine();
                            bufferedWriter.close();
                        } catch (IOException e) {
                            System.out.println("An error occurred while writing to the file.");
                            e.printStackTrace();
                        }

                        String filename = "src/Controllers/employeeInfo.txt";
                        String nameToRemove = selectedEmployee.name;
                        System.out.println(nameToRemove + " khaisi tore");
                        try {
                            // create a FileReader object to read from the file
                            FileReader reader = new FileReader(filename);
                            BufferedReader bufferedReader = new BufferedReader(reader);

                            // create a FileWriter object to write to a temporary file
                            FileWriter writer = new FileWriter("src/Controllers/temp.txt");
                            String line;

                            while ((line = bufferedReader.readLine()) != null) {
                                String[] parts = line.split(" ");

                                if (parts[0].equals(selectedEmployee.name)
                                        && parts[1].equals(selectedEmployee.ID.toString())) {
                                    System.out.println("paisi bedare");
                                } else {
                                    writer.write(line + "\n");

                                }
                            }
                            refresh();
                            // read each line of the file and copy lines that do not contain the name
                            // while ((line = bufferedReader.readLine()) != null) {
                            // String[] parts = line.split(" ");
                            // // System.out.println(parts + " eigula venge paisi");
                            // if (!(parts[0] == nameToRemove) && !(parts[1] ==
                            // selectedEmployee.ID.toString())) {
                            // System.out.println(line);
                            // // writer.write(line + "\n");
                            // }
                            // }

                            // close the BufferedReader and the FileWriter
                            bufferedReader.close();
                            writer.close();

                        } catch (IOException e) {
                            System.out.println("An error occurred while reading/writing the file.");
                            e.printStackTrace();
                        }

                        // String givenName = selectedEmployee.name; // replace with your given name
                        // String givenId = selectedEmployee.ID.toString();

                        // try (BufferedReader br = new BufferedReader(new
                        // FileReader("src/Controllers/temp.txt"));
                        // FileWriter fw = new FileWriter("src/Controllers/employeeInfo.txt")) {
                        // String line;
                        // while ((line = br.readLine()) != null) {
                        // String[] parts = line.split(" ");
                        // if (!parts[0].equals(givenName) && !parts[1].equals(givenId)) {
                        // fw.write(line + System.lineSeparator());
                        // }
                        // }

                        // } catch (Exception e) {
                        // System.err.println("Error: " + e.getMessage());
                        // }
                        refresh();

                        // System.out.println(name + " " + id);
                        return selectedEmployee;
                    }
                    return null;
                });
                String filename = "src/Controllers/employeeInfo.txt";
                String nameToRemove = selectedEmployee.name;

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

                } catch (IOException e) {
                    System.out.println("An error occurred while reading/writing the file.");
                    e.printStackTrace();
                }
                Employee.EmployeeList.remove(selectedEmployee);
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