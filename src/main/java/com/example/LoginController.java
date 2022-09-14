package com.example;

import java.io.IOException;

import com.example.db.DataManager;
import com.example.model.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class LoginController {

    @FXML private AnchorPane mainAnchorPane;
	
	@FXML private Button login;
	@FXML private Label error_msg;
	//@FXML private ImageView LoginImageView;
	@FXML private TextField username;
	@FXML private PasswordField password;
	
	public void loginOnAction(ActionEvent event) throws IOException {
		if((username.getText().isBlank() == false ) &&  (password.getText().isBlank() == false ) ){
			DataManager dataManager = new DataManager();
			User user = dataManager.Login(username.getText(), password.getText());
			if (user != null) {
				toHome(user);
			}else {
				username.setStyle("-jfx-unfocus-color: #d70909; ");
				password.setStyle("-jfx-unfocus-color: #d70909; ");
				error_msg.setText("Nom d'utilisateur ou mot de passe incorrect !");
				error_msg.textFillProperty().setValue(Paint.valueOf("#d70909"));
				System.out.println(error_msg.getStyle());
			}
		}else {
			username.setStyle("-jfx-unfocus-color: #d70909; ");
			password.setStyle("-jfx-unfocus-color: #d70909; ");
			error_msg.setText("Veuillez remplir les champs");
			error_msg.textFillProperty().setValue(Paint.valueOf("#d70909"));
		}
	}
	
	
	public void toHome(User user) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
		Parent root = (Parent) loader.load();
		Stage stage = new Stage();

		stage.setScene(new Scene(root));
		HomeController homeController = loader.getController();
		
		homeController.setupUserInfo(user);
		homeController.enableSearch();
		stage.show();

		login.getScene().getWindow().hide();
	}
}
