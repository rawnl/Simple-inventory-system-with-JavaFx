package com.example;

import java.net.URL;
import java.util.ResourceBundle;

import com.example.db.DataManager;
import com.example.model.Article;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class newInvoiceController implements Initializable{

    @FXML private JFXTextField barcodeField ;
    @FXML private JFXTextField nameField ;
    @FXML private JFXTextField quantityField ;
    
    @FXML private Label error_msg_label;

    @FXML private TableView<String> invoiceTableView;
	@FXML private TableColumn <String, String> articleName;
	@FXML private TableColumn <String, Double> price;
	@FXML private TableColumn <String, Integer> quantity;
    @FXML private TableColumn <String, Double> subTotal;
	
	@FXML private TableColumn<String, String> invoicesActions;
    @FXML private Label totalLabel;

    private Article article;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        /*  
        barcodeField.setOnKeyPressed(new EventHandler<KeyEvent>(){

            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().equals(KeyCode.ENTER) && barcodeField.getText() != ""){
                    getItemByBarcode(barcodeField.getText());
                } 
            }
            
        });
*/
/* 
        nameField.setOnKeyPressed(new EventHandler<KeyEvent>(){

            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().equals(KeyCode.ENTER) && nameField.getText() != ""){
                    getItemByName(nameField.getText());
                } 
            }
            
        });
*/
        barcodeField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                System.out.println(oldValue+"====="+newValue);
                getItemByBarcode(newValue);
                if(newValue.equals("")){
                    error_msg_label.setText("");

                }               
            }
        });

        nameField.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                System.out.println(oldValue+"====="+newValue);
                getItemByName(newValue);
                if(newValue.equals("")){
                    error_msg_label.setText("");
                }
                  
            }
        });

        quantityField.setTextFormatter(new TextFormatter<>(change ->
        (change.getControlNewText().matches("([1-9][0-9]*)?")) ? change : null));
        
    }

    public void getItemByBarcode(String barcode){
        DataManager dataManager = new DataManager();
        article = dataManager.getArticleByBarcode(barcode);

        if(article != null ){
            if(article.getQuantity() > 0){
                nameField.setText(article.getArticleName());
                quantityField.setText("1");
                error_msg_label.setText("");

            }else{
                error_msg_label.setText("Cet article est en rupture de stock");
            }
        }else{
            nameField.setText("");
            quantityField.setText("");
            error_msg_label.setText("Il n'existe aucun article avec ce code à barre !");
        }
    }

    public void getItemByName(String name){
        DataManager dataManager = new DataManager();
        article = dataManager.getArticleByName(name);
        if(article != null ){
            if(article.getQuantity() > 0){
                barcodeField.setText(article.getBarcode());
                quantityField.setText("1");
                error_msg_label.setText("");
            }else{
                error_msg_label.setText("Cet article est en rupture de stock");
            }
        }else{
            barcodeField.setText("");
            quantityField.setText("");
            error_msg_label.setText("Il n'existe aucun article avec cette désignation");
        }
    }
    
    public void increaseQuantity(){
        if(quantityField.getText() != "" ){
            int qt = Integer.parseInt(quantityField.getText());
            qt++;
            quantityField.setText(Integer.toString(qt));
        }
    }

    public void decreaseQuantity(){
        if(quantityField.getText() != "" ){
            int qt = Integer.parseInt(quantityField.getText());
            qt--;
            quantityField.setText(Integer.toString(qt));
        }
    }

    public void resetFields(){
        article = null;
        nameField.setText("");
        barcodeField.setText("");
        quantityField.setText("");
    }

    public void insertRow(){
        System.out.println("création d'un nouvel article");
        System.out.println("création d'une nouvelle comande");
        System.out.println("Ajout à la table");
        resetFields();
    }
}
