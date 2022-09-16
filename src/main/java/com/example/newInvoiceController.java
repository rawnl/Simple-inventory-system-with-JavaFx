package com.example;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.example.db.DataManager;
import com.example.model.Article;
import com.example.model.Commande;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class newInvoiceController implements Initializable{

    @FXML private JFXTextField barcodeField ;
    @FXML private JFXTextField nameField ;
    @FXML private JFXTextField quantityField ;
    
    @FXML private Label error_msg_label;

    @FXML private TableView<Commande> invoiceTableView;
	//@FXML private TableColumn <Commande, Integer> idArticle;
    @FXML private TableColumn <Commande, String> articleName;
	@FXML private TableColumn <Commande, Double> price;
	@FXML private TableColumn <Commande, Integer> quantity;
    @FXML private TableColumn <Commande, Double> subTotal;
	
	@FXML private TableColumn<String, String> invoicesActions;
    @FXML private Label totalLabel;

    private Article article;
    private ObservableList<Commande> observableList ;

    private ArrayList<Commande> commandes;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        commandes = new ArrayList<Commande>();
        totalLabel.setText("0.0");
        initTableView();
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

    private void initTableView() {
        
        //idArticle.setCellValueFactory(new PropertyValueFactory<Commande,Integer>("idArticle"));
        articleName.setCellValueFactory(new PropertyValueFactory<Commande,String>("articleName"));
		price.setCellValueFactory(new PropertyValueFactory<Commande,Double>("price"));
		quantity.setCellValueFactory(new PropertyValueFactory<Commande,Integer>("quantity"));
        subTotal.setCellValueFactory(new PropertyValueFactory<Commande,Double>("total"));

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
        
        DataManager dataManager = new DataManager();
        boolean itemExisted = false ;

        if(!commandes.isEmpty()){
            for (Commande c : commandes) {
                if(c.getIdArticle() == dataManager.getArticleByBarcode(barcodeField.getText()).getId()){

                    c.setQuantity(c.getQuantity() + Integer.parseInt(quantityField.getText()));
                    c.setTotal(c.getQuantity() * c.getPrice());//(c.getQuantity() + Integer.parseInt(quantityField.getText()));
                    itemExisted = true;

                }
            }
        }
        if( !itemExisted ){
            Commande cmd = new Commande();
            Article currentArticle = dataManager.getArticleByBarcode(barcodeField.getText());
            cmd.setIdArticle(currentArticle.getId());
            cmd.setArticleName(currentArticle.getArticleName());
            cmd.setPrice(currentArticle.getPrice());
            cmd.setQuantity(Integer.parseInt(quantityField.getText()));
            double subTotal = cmd.getQuantity() * currentArticle.getPrice();
            cmd.setTotal(subTotal);
            commandes.add(cmd);    
        }

        fillTableView();
        resetFields();

    }

    public void fillTableView(){

        observableList = FXCollections.observableList(commandes);
        invoiceTableView.setItems(observableList);
        invoiceTableView.refresh();
        double grandTotal = 0;
        for (Commande row : invoiceTableView.getItems()) {
            grandTotal = grandTotal + row.getTotal();
        }
        totalLabel.setText(Double.toString(grandTotal));
    
    }

}
