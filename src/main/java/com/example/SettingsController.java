package com.example;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.example.db.DataManager;
import com.example.model.Category;
import com.example.model.User;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class SettingsController implements Initializable{
 
    @FXML Circle circle;
    @FXML Label username;

	@FXML JFXTextField categoryTextField;
	@FXML Label error_msg_label;

	@FXML JFXTextField clientNameTextField;
	@FXML JFXTextField clientPhoneTextField;
	@FXML JFXTextField clientAddressTextField;

	@FXML JFXTextField nameTextField;
	@FXML JFXTextField usernameTextField;
	@FXML JFXPasswordField passwordField;

	private User user;

    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        initIcons();
    }

    public void initIcons() {

		File userIconFile =  new File("src/main/resources/com/example/img/user.png");
		Image userImage = new Image(userIconFile.toURI().toString());
		circle.setFill(new ImagePattern(userImage));
	
	}

    public void setupUserInfo(User user) {
		setUser(user);
		username.setText(user.getUsername());
		username.setAlignment(Pos.CENTER);
	}

	public void addCategoryBtnOnAction(){
		DataManager dataManager = new DataManager();
		if(dataManager.getCategoryByName(categoryTextField.getText()) != null){
			error_msg_label.setText("Cette catégorie existe déja");
		}else{
			Category category= new Category();
			category.setName(categoryTextField.getText());
			if(dataManager.addCategory(category)){
				displayMessage(AlertType.INFORMATION, "Succes", "Opération effectuée avec succes");
				error_msg_label.setText("");
				categoryTextField.clear();
			}else{
				displayMessage(AlertType.ERROR, "Echec", "Echec\n une erreur s'st produite lors de l'jout de cette catégorie");
				error_msg_label.setText("");
				categoryTextField.clear();
			}
		}
	}

	public void addClientBtnOnAction(){
		
	}

	public void addUserBtnOnAction(){
		
	}

	public void displayMessage(AlertType alertType, String title, String msg) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.showAndWait();
	}
	
	@FXML
	public void toDashboard(ActionEvent event) throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
		Parent root = (Parent) loader.load();
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		
		HomeController homeController = loader.getController();
		homeController.setupUserInfo(user);
		homeController.enableSearch();

		stage.show();

	}

	@FXML
	public void toFactures(ActionEvent event) throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("invoices.fxml"));
		Parent root = (Parent) loader.load();

		Stage stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);

		FacturesController facturesController = loader.getController();
		
		facturesController.setupUserInfo(user);
		facturesController.enableSearch();

		stage.show();

	}
   

}