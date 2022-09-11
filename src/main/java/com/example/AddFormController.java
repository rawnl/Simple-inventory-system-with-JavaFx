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
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

public class AddFormController implements Initializable{
    
    //@FXML private Label error_msg;

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

    //private ArrayList<Integer,String> newCategory = new ArrayList<>();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
        // init categories list --> use of ComboBox
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
	
	//
	private void initCategories() {
		DataManager dataManager = new DataManager();
	
		ObservableList<Category> categoriesObsList = FXCollections.observableList(dataManager.getCategories());
		categoryField.setItems(categoriesObsList);
		/*  
		categoryField.setConverter(new StringConverter<Category>() {

			@Override
			public String toString(Category category) {	
				return category.getName();	
			}
		
			@Override
			public Category fromString(String string) {
				return categoryField.getItems().stream().filter(ap -> 
					ap.getName().equals(string)).findFirst().orElse(null);
			}
			
		});

		categoryField.valueProperty().addListener((obs, oldval, newval) -> {
			if(newval != null)
				System.out.println("Selected airport: " + newval.getName() 
					+ ". ID: " + newval.getId());
		});
	*/
	}
	

	//ajouter les autres champs
	@FXML 
	public void addOnAction(ActionEvent event) {
		System.out.println("addOnAction ... ");
		if(validateForm()) {
			System.out.println("validForm true ... ");

			DataManager dataManager = new DataManager();
			
			String barcode = barCodeField.getText();
			String name = nameField.getText();
			
			System.out.println("selected cat id : "+categoryField.valueProperty().get().getId());
			
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
				//displayMessage("success","Opération effectuée avec succès.");
				System.out.println("succes");
			}else {
				System.out.println("echec");			
				//displayMessage("echec","Echec d'ajout !\nVeuillez vérifier que les informations fournies sont correctes et non pas dupliquées.");
			}
		}else {
			System.out.println("validForm false ... ");
			//addListeners();
			if(validTextField(barCodeField) == false ) {
				//barcode_field_msg.setText("Ce champs est obligatoire. Veuillez le remplir");
				System.out.println("barcode error");
			}
			if(validTextField(nameField) == false) {
				//name_field_msg.setText("Ce champs est obligatoire. Veuillez le remplir");
				System.out.println("name error");					
			}
			if(validTextField(priceField) == false ) {
				//price_field_msg.setText("Ce champs est obligatoire. Veuillez le remplir");
				System.out.println("price error");
			}
			if(validTextField(quantityField) == false ) {
				//quantity_field_msg.setText("Ce champs est obligatoire. Veuillez le remplir");
				System.out.println("quantity error");
			}
		}
	}

/* 
    public void displayMessage(String msgType, String msg) {
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		
		JFXDialog dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		dialog.setOverlayClose(false);
		
		JFXButton okBtn = new JFXButton("OK");
		okBtn.setStyle("-fx-background-color: #C90202; -fx-border-radius: 1em; -fx-text-fill: white ;");
		okBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent)->{
			dialog.close();
			if(msgType == "success") {
				closeBtn.fire();
			}
		});
		
		dialogLayout.setBody(new Text(msg));
		dialogLayout.setActions(okBtn);
		dialog.show();
	}
*/

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

/* 
	public void addListeners() {
		ChangeListener<Boolean> changeEvent = new ChangeListener<Boolean>() {
			@Override
		    public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
		    {
		        cage_number_msg.setText("");
				birth_date_msg.setText("");
				MB_msg.setText("");
				DI_msg.setText("");
				DMB_msg.setText("");	        
		    }
		};
		
		cage_number_field.focusedProperty().addListener(changeEvent);
		birth_date_field.focusedProperty().addListener(changeEvent);
		MB_field.focusedProperty().addListener(changeEvent);
		DI_field.focusedProperty().addListener(changeEvent);
		DMB_field.focusedProperty().addListener(changeEvent);
	}
*/

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
