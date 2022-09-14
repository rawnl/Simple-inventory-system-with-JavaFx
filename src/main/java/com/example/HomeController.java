package com.example;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import com.example.db.DataManager;
import com.example.model.Article;
import com.example.model.Category;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;


public class HomeController implements Initializable{

   	@FXML private StackPane stackPane;
	@FXML private AnchorPane mainAnchorPane;
   
	@FXML private GridPane dashboardPane;
	//@FXML private GridPane statsPane;
	@FXML private GridPane facturesPane;	
	
   	@FXML private Label currentTab;
	@FXML private Label todaysTotal;

	@FXML private Circle circle;
	@FXML private Label username;
	
	@FXML private Button homeBtn;
	@FXML private ImageView homeIcon;

	@FXML private Button addBtn;
	@FXML private ImageView addIcon;

	@FXML private TextField search; 

	@FXML private TableView<Article> tableView;
	@FXML private TableColumn <Article, Integer> id;
	@FXML private TableColumn <Article, String> articleName;
   	@FXML private TableColumn <Article, String> barcode;
	@FXML private TableColumn <Article, Category> category;
	@FXML private TableColumn <Article, String> brand; 
	@FXML private TableColumn <Article, String> model;
	@FXML private TableColumn <Article, Double> price;
	@FXML private TableColumn <Article, Integer> quantity;
	@FXML private TableColumn<Article, String> actions;

	@FXML private TextField total;
	
	@FXML private Pagination pagination;
	private final static int rowsPerPage = 11 ;

	private ObservableList<Article> obsList;
	
	private Article article;
	private User user;
   

    @Override
	public void initialize(URL arg0, ResourceBundle arg1){
		initIcons();
		initTableView();
		initActionIcons();
		UpdateTableView();
		updateTodaysTotal();
		pagination.setPageFactory(this::createPage);
		currentTab.setText("Liste des articles");
	}
 
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	// Make use of setupUserInfo
	public void initIcons() {

		File userIconFile =  new File("src/main/resources/com/example/img/user.png");
		Image userImage = new Image(userIconFile.toURI().toString());
		circle.setFill(new ImagePattern(userImage));
	
	}
	
	// fix the category column
	public void initTableView() {
		id.setCellValueFactory(new PropertyValueFactory<Article,Integer>("id"));
		articleName.setCellValueFactory(new PropertyValueFactory<Article,String>("articleName"));
		barcode.setCellValueFactory(new PropertyValueFactory<Article,String>("barcode"));
		category.setCellValueFactory(new PropertyValueFactory<Article,Category>("category"));
		brand.setCellValueFactory(new PropertyValueFactory<Article,String>("brand"));
		model.setCellValueFactory(new PropertyValueFactory<Article,String>("model"));
		price.setCellValueFactory(new PropertyValueFactory<Article,Double>("price"));
		quantity.setCellValueFactory(new PropertyValueFactory<Article,Integer>("quantity"));

	}

	public void initActionIcons(){
        //add cell of button edit 
        Callback<TableColumn<Article, String>, TableCell<Article, String>> cellFoctory = (TableColumn<Article, String> param) -> {
			// make cell containing buttons
			final TableCell<Article, String> cell = new TableCell<Article, String>() {
				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					//that cell created only on non-empty rows
					if (empty) {
						//add button
						setGraphic(null);
						setText(null);
					} else {

						ImageView iconDelete = new ImageView();
						File FileDeleteIcon =  new File("src/main/resources/com/example/img/bin.png");
						Image deleteImg = new Image(FileDeleteIcon.toURI().toString(),25,25, false,true);

						iconDelete.setImage(deleteImg);		
						iconDelete.setStyle(" -fx-cursor: hand ; -fx-fill:#C90202;");
						iconDelete.setOnMouseClicked((MouseEvent event) -> {       
							article = tableView.getSelectionModel().getSelectedItem();
							if(displayConfirmationBox(AlertType.CONFIRMATION, "Suppression", "Vous êtes sur le point de supprimer cet article.\n Êtes-vous sûr?")){
								DataManager dataManager = new DataManager();
								dataManager.DeleteArticle(article.getId());
								displayMessage(AlertType.INFORMATION, "Suppression", "Article supprimé avec succes");
								UpdateTableView();
							}
						});
					
						ImageView iconEdit = new ImageView();
						File FileEditIcon =  new File("src/main/resources/com/example/img/edit.png");
						Image editImg = new Image(FileEditIcon.toURI().toString(), 25, 25, false, true);
									
						iconEdit.setImage(editImg);
						iconEdit.setStyle(" -fx-cursor: hand ; -fx-fill:#C90202; ");
						iconEdit.setOnMouseClicked((MouseEvent event) -> {
							article = tableView.getSelectionModel().getSelectedItem();
							displayEditForm(article);
						});
						HBox managebtn = new HBox(iconEdit, iconDelete); 
						managebtn.setStyle("-fx-alignment:center");
						HBox.setMargin(iconDelete, new Insets(2, 2, 0, 3));
						HBox.setMargin(iconEdit, new Insets(2, 2, 0, 3));
						setGraphic(managebtn);
						setText(null);
					}
				}
			};
            return cell;
    	};
        actions.setCellFactory(cellFoctory);
        tableView.setItems(obsList);
    }
	
	// to test and fix
	public void enableSearch(){
		FilteredList<Article> filteredList = new FilteredList<>(obsList, e -> true);
		search.setOnKeyReleased(e -> {
			search.textProperty().addListener((observableValue, oldValue, newValue) -> {
				filteredList.setPredicate((Predicate<? super Article>) a ->{
					if(newValue == null || newValue.isEmpty()){
						return true;
					}
					String lowerCaseFilter = newValue.toLowerCase();
					if(Integer.toString(a.getId()).contains(newValue) ){
						return true;
					}
					else if (a.getArticleName().contains(newValue)){
						return true;
					}else if(a.getBarcode() != null && a.getBarcode().contains(newValue)){
						return true;
					}else if(a.getCategory().getName().contains(newValue) ){
						return true;
					}else if(a.getBrand().contains(newValue)){
						return true;
					}else if (a.getModel().contains(newValue)){
						return true;
					
               }else if (Double.toString(a.getPrice()).contains(newValue)){
						return true ;
					}else if(Integer.toString(a.getQuantity()).contains(newValue) ){
						return true;
					}
					return false;
				});
			});
			SortedList<Article> sortedList = new SortedList<>(filteredList);
			sortedList.comparatorProperty().bind(tableView.comparatorProperty());
			tableView.setItems(sortedList);
		});
	}
	
	public Node createPage(int pageIndex){
		int fromIndex = pageIndex * rowsPerPage;
		int toIndex = Math.min(fromIndex+rowsPerPage, Integer.parseInt(total.getText()));
		tableView.setItems(FXCollections.observableArrayList(obsList.subList(fromIndex, toIndex)));
		return tableView;
	}
	
	// display delete dialog --> to get rid of
	/* 
	public void displayDeleteDialog(){
		FXMLLoader loader = new FXMLLoader(getClass().getResource("deleteDialog.fxml"));
		Parent root;
		try {
			root = (Parent) loader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
		
			DeleteController deleteController = loader.getController();
			//deleteController.configureBackground();
													
			stage.initModality(Modality.WINDOW_MODAL); 
			Stage primaryStage = (Stage)(mainAnchorPane.getScene().getWindow()); 
													
			stage.initOwner(primaryStage);
			//deleteController.setDynamic();		
			stage.show();

			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					if(deleteController.getAnswer()){
						DataManager dataManager = new DataManager();
						if(dataManager.DeleteArticle(article.getId())){
							UpdateTableView();
							//displayAlert("success","Opération effectuée avec succes.");
							System.out.println("**success");
						}else{
							System.out.println("**echec");
							//displayAlert("alert","Une erreur c'est produite lors de la suppression de cet article.");
						}
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
   	*/

	public void displayEditForm(Article article){
		FXMLLoader loader = new FXMLLoader(getClass().getResource("editForm.fxml"));
		Parent root;
		try {
			
			root = (Parent) loader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			
			EditController editController = loader.getController();

			editController.setArticle(article);
													
			stage.initModality(Modality.WINDOW_MODAL); // APPLICATION_MODAL
			Stage primaryStage = (Stage)(mainAnchorPane.getScene().getWindow()); 										
			stage.initOwner(primaryStage);

			editController.fillForm();		
			stage.show();
						
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					UpdateTableView();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
   	/* display alert
  	public void displayAlert(String type, String message){
	FXMLLoader loader = new FXMLLoader(getClass().getResource("../ui/alert.fxml"));
	Parent root;
	try {
		root = (Parent) loader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
	
		AlertController alertController = loader.getController();
		alertController.configureBackground();
		
		alertController.setMessage(message);	

		if(type.equals("success")){
			alertController.changeIcon();
		}

		stage.initModality(Modality.WINDOW_MODAL); 
		Stage primaryStage = (Stage)(mainAnchorPane.getScene().getWindow()); 
												
		stage.initOwner(primaryStage);
		alertController.setDynamic();		
		stage.show();

	} catch (IOException e) {
		e.printStackTrace();
	}
}
*/

	public void UpdateTableView() {
		DataManager dataManager = new DataManager();
		ArrayList<Article> articles = new ArrayList<Article>();
		articles = dataManager.getArticles();
		obsList = FXCollections.observableList(articles);
        tableView.setItems(obsList);
		total.setText(((Integer)(tableView.getItems().size())).toString());
		int maxPages = Integer.parseInt(total.getText()) / rowsPerPage;
		pagination.setPageCount(maxPages);
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
	
	public void AddOnAction(ActionEvent event) throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("addForm.fxml"));
		Parent root = (Parent) loader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		AddFormController addFormController = loader.getController();
		//addFormController.configureBackground();
		
		stage.initModality(Modality.WINDOW_MODAL); // APPLICATION_MODAL
		Stage primaryStage = (Stage)(mainAnchorPane.getScene().getWindow()); 
		
		stage.initOwner(primaryStage);
	
		//addFormController.setDynamic();		
		stage.show();
		
		stage.setOnCloseRequest((EventHandler<WindowEvent>) new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				UpdateTableView();
			}
		});
		
	}
   
	public void displayMessage(AlertType alertType, String title, String msg) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.showAndWait();
	}
	
	public boolean displayConfirmationBox(AlertType alertType, String title, String msg){
		
		Alert alert = new Alert(alertType);
		
		ButtonType yesBtn = new ButtonType("Oui", ButtonData.YES);
		ButtonType noBtn = new ButtonType("Non", ButtonData.NO);
		
		alert.getButtonTypes().setAll(yesBtn, noBtn);

		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(msg);
		//alert.showAndWait();
		Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
            if (result.get() == yesBtn ){
				return true;
            }else{
				return false;
			}
	}
   	
	public void updateTodaysTotal(){
		DataManager dataManager = new DataManager();
		String totalDuJour =  Double.toString(dataManager.getTodaysTotal());
		todaysTotal.setText(totalDuJour);
	}
	// update total
    
	@FXML
	public void toDashboard() throws IOException {
		currentTab.setText("Liste des articles");
		dashboardPane.toFront();
		dashboardPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY,Insets.EMPTY)));
		System.out.println("to dasshboard");
	}
   
	/* 
	@FXML
	public void toFactures() throws IOException {
		currentTab.setText("Liste des factures");
		facturesPane.toFront();
		facturesPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY,Insets.EMPTY)));
		System.out.println("to factures");
	}
	*/
	
	@FXML
	public void toFactures(ActionEvent event) throws IOException {
		/*FXMLLoader loader = new FXMLLoader(getClass().getResource("invoices.fxml"));
		AnchorPane pane = loader.load();

		FacturesController facturesController = loader.getController();
		facturesController.setupUserInfo(getUser());

		mainAnchorPane.getChildren().clear();
        mainAnchorPane.getChildren().add(pane);

		*/

		FXMLLoader loader = new FXMLLoader(getClass().getResource("invoices.fxml"));
		Parent root = (Parent) loader.load();
		

		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		
		//stage.setMaximized(true);
		//stage.setScene(new Scene(root));
		FacturesController facturesController = loader.getController();
		
		facturesController.setupUserInfo(user);
		facturesController.enableSearch();
		stage.show();

//		login.getScene().getWindow().hide();
	}
   
}
