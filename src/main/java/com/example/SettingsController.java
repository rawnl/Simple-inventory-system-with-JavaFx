package com.example;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.example.model.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class SettingsController implements Initializable{
 
    @FXML Circle circle;
    @FXML Label username;
    
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