package com.example;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import com.example.db.DataManager;
import com.example.model.Category;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;

public class CategoriesController implements Initializable {
    
    @FXML private AnchorPane mainAnchorPane;
    @FXML private JFXTextField newCategoryField;
    @FXML private Label new_category_field_msg;


    public void initialize(URL arg0, ResourceBundle arg1) {
		
    }
	// test if the category already exists
    public void newCategoryBtnOnAction(ActionEvent event){
        if(validTextField(newCategoryField)) {
			DataManager dataManager = new DataManager();
			
			String newCategory = newCategoryField.getText();
			

			Category category = new Category();
			
			category.setName(newCategory);
			
			if(dataManager.addCategory(category)) {
				displayMessage(AlertType.INFORMATION, "Succes", "Opération effectuée avec succès.");

				mainAnchorPane.getScene().getWindow().hide();	
				Stage stage = (Stage) mainAnchorPane.getScene().getWindow();
				stage.getOnCloseRequest().handle(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));

			}else {
				displayMessage(AlertType.ERROR, "Echec", "Opération echouée !\nVeuillez vérifier que les informations fournies sont correctes et non pas dupliquées.");
			}
		}else {
			addListeners();
			if(validTextField(newCategoryField) == false ) {
				new_category_field_msg.setText("Ce champs est obligatoire.");
			}
		}
    }
    
    public void displayMessage(AlertType alertType, String title, String msg) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.showAndWait();
	}
    
	public boolean validTextField(JFXTextField textField) {
		boolean result = false ;
		if(textField.getText() != null && textField.getText().isEmpty() == false) {
			result = true;
		}
		return result;
	}

    public void addListeners() {
		ChangeListener<Boolean> changeEvent = new ChangeListener<Boolean>() {
			@Override
		    public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
		    {
		        new_category_field_msg.setText("");
				
		    }
		};
		
		new_category_field_msg.focusedProperty().addListener(changeEvent);
		

	}
	
}
