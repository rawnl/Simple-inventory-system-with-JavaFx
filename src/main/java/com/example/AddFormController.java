package com.example;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import com.example.db.DataManager;
import com.example.model.Article;
import com.example.model.Category;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;

public class AddFormController implements Initializable{
    
    //@FXML private Label error_msg;

	@FXML private StackPane stackPane;
	@FXML private AnchorPane mainAnchorPane;
    @FXML private JFXTextField barCodeField;
    @FXML private JFXTextField nameField;
    @FXML private JFXComboBox<Category> categoryField;
    @FXML private JFXTextField brandField;
    @FXML private JFXTextField modelField;
    @FXML private JFXTextField priceField;
    @FXML private JFXTextField quantityField;

    @FXML private JFXButton addCategoryBtn;
    @FXML private JFXButton addBtn;
    @FXML private JFXButton cancelBtn;

	@FXML private Label barcode_field_msg;
	@FXML private Label name_field_msg;
	@FXML private Label price_field_msg;
	@FXML private Label quantity_field_msg;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initCategories();

		Pattern validEditingState = Pattern.compile("-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?");

		UnaryOperator<TextFormatter.Change> filter = c -> {
			String text = c.getControlNewText();
			if (validEditingState.matcher(text).matches()) {
				return c ;
			} else {
				return null ;
			}
		};

		StringConverter<Double> converter = new StringConverter<Double>() {
			@Override
			public Double fromString(String s) {
				if (s.isEmpty() || "-".equals(s) || ".".equals(s) || "-.".equals(s)) {
					return 0.0 ;
				} else {
					return Double.valueOf(s);
				}
			}

			@Override
			public String toString(Double d) {
				return d.toString();
			}
		};

		TextFormatter<Double> textFormatter = new TextFormatter<>(converter, 0.0, filter);
		priceField.setTextFormatter(textFormatter);

		quantityField.setTextFormatter(new TextFormatter<>(change ->
        (change.getControlNewText().matches("([1-9][0-9]*)?")) ? change : null));
	
    }
	
	private void initCategories() {
		DataManager dataManager = new DataManager();
	
		ObservableList<Category> categoriesObsList = FXCollections.observableList(dataManager.getCategories());
		categoryField.setItems(categoriesObsList);
		
		categoryField.setConverter(new StringConverter<Category>() {

			@Override
			public String toString(Category category) {	
				if (category == null) {
                    return null;
                }
				return category.getName().toString();
			}
		
			@Override
			public Category fromString(String string) {
				return categoryField.getItems().stream().filter(ap -> 
					ap.getName().equals(string)).findFirst().orElse(null);
			}
		});
	}
	
	// add options : quit or add another 
	@FXML 
	public void addOnAction(ActionEvent event) {
		if(validateForm()) {
			DataManager dataManager = new DataManager();
			
			String barcode = barCodeField.getText();
			String name = nameField.getText();
						
			int category = categoryField.valueProperty().get().getId();
			String brand = brandField.getText();
			String model = modelField.getText();
			double price = Double.parseDouble(priceField.getText());
			int quantity = Integer.parseInt(quantityField.getText());

			Article article = new Article();
			
			article.setBarcode(barcode);
			article.setArticleName(name);
			article.setCategory(dataManager.getCategory(category));
			article.setBrand(brand);
			article.setModel(model);
			article.setPrice(price);
			article.setQuantity(quantity);
			
			if(dataManager.addArticle(article)) {
				displayMessage(AlertType.INFORMATION, "Succes", "Opération effectuée avec succès.");
				//displayMessage("success","Opération effectuée avec succès.");
				mainAnchorPane.getScene().getWindow().hide();	
				Stage stage = (Stage) mainAnchorPane.getScene().getWindow();
				stage.getOnCloseRequest().handle(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
			}else {
				displayMessage(AlertType.ERROR, "Echec", "Opération echouée !\nVeuillez vérifier que les informations fournies sont correctes et non pas dupliquées.");
				//displayMessage("echec","Echec d'ajout !\nVeuillez vérifier que les informations fournies sont \ncorrectes et non pas dupliquées.");
			}
		}else {
			addListeners();
			if(validTextField(barCodeField) == false ) {
				barcode_field_msg.setText("Ce champs est obligatoire.");
			}
			if(validTextField(nameField) == false) {
				name_field_msg.setText("Ce champs est obligatoire.");
			}
			if(validTextField(priceField) == false ) {
				price_field_msg.setText("Ce champs est obligatoire.");
			}
			if(validTextField(quantityField) == false ) {
				quantity_field_msg.setText("Ce champs est obligatoire.");
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
	
	//to get rid of
	public void displayMessage(String msgType, String msg) {
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		
		JFXDialog dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		dialog.setOverlayClose(false);
		
		JFXButton okBtn = new JFXButton("OK");
		okBtn.setStyle("-fx-background-color:  #708090; -fx-border-radius: 1em; -fx-text-fill: white ;");
		okBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent)->{
			dialog.close();
			/* 
			if(msgType == "success") {
				//closeBtn.fire();
				cancelBtn.getScene().getWindow().hide();
			}
			*/
		});
		
		dialogLayout.setBody(new Text(msg));
		dialogLayout.setActions(okBtn);
		dialog.show();

	}

	public boolean validateForm() {
		boolean result = false; 
		if(validTextField(barCodeField) && validTextField(nameField) && validTextField(priceField) && validTextField(quantityField)) {
			result = true ;
		}else {
			result = false ;
		}
		return result;
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
		        barcode_field_msg.setText("");
				name_field_msg.setText("");
				price_field_msg.setText("");
				quantity_field_msg.setText("");      
		    }
		};
		
		barcode_field_msg.focusedProperty().addListener(changeEvent);
		name_field_msg.focusedProperty().addListener(changeEvent);
		price_field_msg.focusedProperty().addListener(changeEvent);
		quantity_field_msg.focusedProperty().addListener(changeEvent);

	}

/*
    @FXML
	public void closeBtnOnAction(ActionEvent event) {
		closeBtn.getScene().getWindow().hide();	
		Stage stage = (Stage)closeBtn.getScene().getWindow();
		stage.getOnCloseRequest().handle(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
		//closeBtn.getScene().getWindow().hide();
	}	
*/

    @FXML
	public void cancelBtnOnAction(ActionEvent event) {
        cancelBtn.getScene().getWindow().hide();
	}	


}
