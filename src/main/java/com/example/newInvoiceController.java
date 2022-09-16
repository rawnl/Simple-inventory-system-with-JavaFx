package com.example;

import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.example.db.DataManager;
import com.example.model.Article;
import com.example.model.Client;
import com.example.model.Commande;
import com.example.model.Facture;
import com.example.model.User;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class newInvoiceController implements Initializable{

    @FXML private JFXTextField barcodeField ;
    @FXML private JFXTextField nameField ;
    @FXML private JFXTextField quantityField ;
    @FXML private ChoiceBox clientChoiceBox;
    
    @FXML private Label error_msg_label;

    @FXML private TableView<Commande> invoiceTableView;
	//@FXML private TableColumn <Commande, Integer> idArticle;
    @FXML private TableColumn <Commande, String> articleName;
	@FXML private TableColumn <Commande, Double> price;
	@FXML private TableColumn <Commande, Integer> quantity;
    @FXML private TableColumn <Commande, Double> subTotal;
	@FXML private TableColumn <String, String> leftQt;

	@FXML private TableColumn<String, String> invoicesActions;
    @FXML private Label totalLabel;

    @FXML private JFXButton validBtn;

    private User user;
    private Article article;
    private ObservableList<Commande> observableList ;
    private ArrayList<Commande> commandes;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        commandes = new ArrayList<Commande>();
        observableList = null;
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
/* 
        observableList.addListener(new ListChangeListener<Commande>(){

            @Override
            public void onChanged(javafx.collections.ListChangeListener.Change<? extends Commande> pChange) {
                while(pChange.next()) {
                    if(observableList == null){
                        validBtn.setDisable(true);
                    }else{
                        validBtn.setDisable(false);
                    }
                }
            }
        });
*/        
    }

    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

        Article currentArticle = dataManager.getArticleByBarcode(barcodeField.getText());
        
        if(!commandes.isEmpty()){
            for (Commande c : commandes) {
                if(c.getIdArticle() == currentArticle.getId()){
                    itemExisted = true;
                    int newQt = c.getQuantity() + Integer.parseInt(quantityField.getText());
                    if(newQt > currentArticle.getQuantity()){
                        error_msg_label.setText("La quantité restante de cette article est: "+currentArticle.getQuantity());
                        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), new KeyValue(error_msg_label.textFillProperty(), Color.PINK));
                        Timeline TIMER = new Timeline();
                        TIMER.getKeyFrames().add(keyFrame);
                        TIMER.setCycleCount(Animation.INDEFINITE);
                        TIMER.setAutoReverse(true);
                        TIMER.play(); 
                    }else{
                        c.setQuantity(newQt);
                        c.setTotal(c.getQuantity() * c.getPrice());//(c.getQuantity() + Integer.parseInt(quantityField.getText()));    
                        fillTableView();
                        resetFields();
                    }       
                }
            }
        }
        if( !itemExisted ){
            Commande cmd = new Commande();
            cmd.setQuantity(Integer.parseInt(quantityField.getText()));
            if(cmd.getQuantity() <= currentArticle.getQuantity()){
                cmd.setIdArticle(currentArticle.getId());
                cmd.setArticleName(currentArticle.getArticleName());
                cmd.setPrice(currentArticle.getPrice());
                double subTotal = cmd.getQuantity() * currentArticle.getPrice();
                cmd.setTotal(subTotal);
                commandes.add(cmd);            
                fillTableView();
                resetFields();
            }else{
                error_msg_label.setText("La quantité restante de cette article est: "+currentArticle.getQuantity());
                KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.5), new KeyValue(error_msg_label.textFillProperty(), Color.PINK));
                Timeline TIMER = new Timeline();
                TIMER.getKeyFrames().add(keyFrame);
                TIMER.setCycleCount(Animation.INDEFINITE);
                TIMER.setAutoReverse(true);
                TIMER.play(); 
            }
        }

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

    // set up the client
    public void validBtnOnAction(){
        DataManager dataManager = new DataManager();
        Facture facture = new Facture();
        //facture.setClient(new Client(dataManager.getClient(Integer.parseInt(clientChoiceBox.getValue()))));
        Client client = new Client();
        client.setId(1);
        facture.setClient(client);
        facture.setUser(user);
        facture.setDate(new Date(new java.util.Date().getTime()));
        facture.setTotal(Double.parseDouble(totalLabel.getText()));
        //int idFacture = dataManager.addFacture(facture).getNumber();
        facture.setNumber(dataManager.addFacture(facture).getNumber());

        for (Commande commande : commandes) {
            commande.setIdFacture(facture.getNumber());
            if(dataManager.addCommande(commande)){
                Article cmdArticle = dataManager.getArticleById(commande.getIdArticle());
                cmdArticle.setQuantity(cmdArticle.getQuantity() - commande.getQuantity());
                dataManager.EditArticle(cmdArticle);
            }
        }

    }
}
