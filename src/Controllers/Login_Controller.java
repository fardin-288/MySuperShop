package Controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.*;

public class Login_Controller {
    @FXML
    private Stage stage;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    public void clickSignIn(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Check if the username and password are valid
        if (isValidLogin(username, password))
        // if (username == "name" && password == "passsword")
        {
            BorderPane root = FXMLLoader.load(getClass().getResource("menu.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } else {
            // Show an error message if the username and password are invalid
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Login");
            alert.setHeaderText(null);
            alert.setContentText("The username or password you entered is invalid. Please try again.");
            alert.showAndWait();
        }
    }

    private boolean isValidLogin(String username, String password) {

        if ((username.equals("fardin") || username.equals("rafin") || username.equals("riya"))
                && password.equals("password")) {
            return true;
        } else {
            return false;
            // return true;
        }

    }
}
