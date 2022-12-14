package com.example;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import com.example.db.DataManager;
import com.example.model.Client;
import com.example.model.Facture;
import com.example.model.User;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

public class FacturesController implements Initializable{

    @FXML private StackPane stackPane;
	@FXML private AnchorPane mainAnchorPane;
    
    @FXML private Label currentTab;
	
    @FXML private TextField search; 

    @FXML private Circle circle;
	@FXML private Label username;

    @FXML private TextField invoicesItemsTotal;

    @FXML private Pagination invoicesPagination;
	//private final static int rowsPerPage = 11 ;

    @FXML private Button invoicesBtn;
	@FXML private ImageView invoicesIcon;

    @FXML private TableView<Facture> invoicesTableView;
	@FXML private TableColumn <Facture, Integer> invoiceNumber;
	@FXML private TableColumn <Facture, Date> invoiceDate;
	@FXML private TableColumn <Facture, Client> invoiceClient;
	@FXML private TableColumn <Facture, User> invoiceUser;
	@FXML private TableColumn <Facture, Double> invoiceTotal;
	@FXML private TableColumn<Facture, String> invoicesActions;

    private ObservableList<Facture> invoicesObsList;
	private Facture facture;
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
		initTableView();
		initActionIcons();
		UpdateInvoicesTableView();
		currentTab.setText("Liste des factures");
    }

	public void initIcons() {

		File userIconFile =  new File("src/main/resources/com/example/img/user.png");
		Image userImage = new Image(userIconFile.toURI().toString());
		circle.setFill(new ImagePattern(userImage));
	
	}

    public void initTableView() {

		invoiceNumber.setCellValueFactory(new PropertyValueFactory<Facture,Integer>("number"));
		invoiceDate.setCellValueFactory(new PropertyValueFactory<Facture,Date>("date"));
		invoiceClient.setCellValueFactory(new PropertyValueFactory<Facture,Client>("client"));
		invoiceUser.setCellValueFactory(new PropertyValueFactory<Facture,User>("user"));
		invoiceTotal.setCellValueFactory(new PropertyValueFactory<Facture,Double>("total"));

	}

	public void initActionIcons(){
        
        Callback<TableColumn<Facture, String>, TableCell<Facture, String>> cellFoctory = (TableColumn<Facture, String> param) -> {
			final TableCell<Facture, String> cell = new TableCell<Facture, String>() {
				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setGraphic(null);
						setText(null);
					} else {

						ImageView iconDelete = new ImageView();
						File FileDeleteIcon =  new File("src/main/resources/com/example/img/bin.png");
						Image deleteImg = new Image(FileDeleteIcon.toURI().toString(),25,25, false,true);

						iconDelete.setImage(deleteImg);		
						iconDelete.setStyle(" -fx-cursor: hand ; -fx-fill:#C90202;");
						iconDelete.setOnMouseClicked((MouseEvent event) -> {       
							facture = invoicesTableView.getSelectionModel().getSelectedItem();
							/* 
							if(displayConfirmationBox(AlertType.CONFIRMATION, "Suppression", "Vous etes sur le point de supprimer cet article. Etes vous sur ?")){
								DataManager dataManager = new DataManager();
								dataManager.DeleteArticle(article.getId());
								displayMessage(AlertType.INFORMATION, "Suppression", "Article supprim?? avec succes");
								UpdateTableView();
							}
							*/
						});
					
						ImageView iconEdit = new ImageView();
						File FileEditIcon =  new File("src/main/resources/com/example/img/edit.png");
						Image editImg = new Image(FileEditIcon.toURI().toString(), 25, 25, false, true);
									
						iconEdit.setImage(editImg);
						iconEdit.setStyle(" -fx-cursor: hand ; -fx-fill:#C90202; ");
						iconEdit.setOnMouseClicked((MouseEvent event) -> {
							facture = invoicesTableView.getSelectionModel().getSelectedItem();
							/* 
							displayEditForm(facture);
							*/
						});

						ImageView iconPdf = new ImageView();
						File filePdfIcon =  new File("src/main/resources/com/example/img/pdf.png");
						Image pdfImg = new Image(filePdfIcon.toURI().toString(),25,25, false,true);

						iconPdf.setImage(pdfImg);		
						iconPdf.setStyle(" -fx-cursor: hand ; -fx-fill:#C90202;");
						iconPdf.setOnMouseClicked((MouseEvent event) -> {       
							facture = invoicesTableView.getSelectionModel().getSelectedItem();
							// generate PDF
						});

						HBox managebtn = new HBox(iconEdit, iconDelete,iconPdf); 
						managebtn.setStyle("-fx-alignment:center");
						HBox.setMargin(iconDelete, new Insets(2, 2, 0, 3));
						HBox.setMargin(iconEdit, new Insets(2, 2, 0, 3));
						HBox.setMargin(iconPdf, new Insets(2, 2, 0, 3));
						setGraphic(managebtn);
						setText(null);
					}
				}
			};
            return cell;
    	};
        invoicesActions.setCellFactory(cellFoctory);
        invoicesTableView.setItems(invoicesObsList);

    }

	public void UpdateInvoicesTableView() {

		DataManager dataManager = new DataManager();
		ArrayList<Facture> factures = new ArrayList<Facture>();
		factures = dataManager.getFactures();

		invoicesObsList = FXCollections.observableList(factures);
        invoicesTableView.setItems(invoicesObsList);
		invoicesItemsTotal.setText(((Integer)(invoicesTableView.getItems().size())).toString());

	}

    public void setupUserInfo(User user) {
		setUser(user);
		username.setText(user.getUsername());
		username.setAlignment(Pos.CENTER);
		/*
		DataManager dataManager = new DataManager();
		File userIconFile = new File("user.png");
		dataManager.loadImage(userIconFile, user.getId());
		Image userImage = new Image(userIconFile.toURI().toString());
		circle.setFill(new ImagePattern(userImage));
		*/
	}

    public void enableSearch(){
		FilteredList<Facture> filteredList = new FilteredList<>(invoicesObsList, e -> true);
		search.setOnKeyReleased(e -> {
			search.textProperty().addListener((observableValue, oldValue, newValue) -> {
				filteredList.setPredicate((Predicate<? super Facture>) a ->{
					if(newValue == null || newValue.isEmpty()){
						return true;
					}
					String lowerCaseFilter = newValue.toLowerCase();
					if(Integer.toString(a.getNumber()).contains(newValue) ){
						return true;
					}
					else if (a.getClient().getName().contains(newValue)){
						return true;
					}else if(a.getUser() != null && a.getUser().getName().contains(newValue)){
						return true;
					}else if(a.getDate().toString().contains(newValue) ){
						return true;
					}else if(Double.toString(a.getTotal()).contains(newValue)){
						return true;
					}
					return false;
				});
			});
			SortedList<Facture> sortedList = new SortedList<>(filteredList);
			sortedList.comparatorProperty().bind(invoicesTableView.comparatorProperty());
			invoicesTableView.setItems(sortedList);
		});
	}

	public void displayNewInvoice() throws IOException{
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("newInvoice.fxml"));
		Parent root = (Parent) loader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root));

		newInvoiceController newInvoiceController = loader.getController();
		newInvoiceController.setUser(user);

		stage.initModality(Modality.WINDOW_MODAL); 
		Stage primaryStage = (Stage)(mainAnchorPane.getScene().getWindow()); 
		stage.initOwner(primaryStage);
		stage.show();
		
		stage.setOnCloseRequest((EventHandler<WindowEvent>) new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				UpdateInvoicesTableView();
			}
		});
    }
    
    @FXML
	public void toDashboard(ActionEvent event) throws IOException {
		
		/* 
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
		AnchorPane pane = loader.load();

		HomeController HomeController = loader.getController();
	    HomeController.setupUserInfo(getUser());

		mainAnchorPane.getChildren().clear();
        mainAnchorPane.getChildren().add(pane);
		*/

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
	public void toSettings(ActionEvent event) throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("settings.fxml"));
		Parent root = (Parent) loader.load();

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);

		SettingsController settingsController = loader.getController();
		settingsController.setupUserInfo(user);

		stage.show();

	}
	
}
