package com.example;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import com.example.db.DataManager;
import com.example.model.Article;
import com.example.model.Category;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;


public class HomeController implements Initializable{

   	@FXML private StackPane stackPane;
	@FXML private AnchorPane mainAnchorPane;
   
	@FXML private GridPane dashboardPane;
	@FXML private GridPane statsPane;
	@FXML private GridPane settingsPane;	
	
   	@FXML private Label currentTab;

	@FXML private Circle circle;
	@FXML private Label username;
	
	@FXML private Button homeBtn;
	@FXML private ImageView homeIcon;
	
	@FXML private Button invoicesBtn;
	@FXML private ImageView invoicesIcon;

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
   
	// to fix/update and test
    @FXML
    private void switchToFactures() throws IOException {
       System.out.println("switching to factures .....");
    }

	// to fix/update and test
    @FXML
    private void switchToArticles() throws IOException {
       System.out.println("switching to articles .....");
    }

    @Override
	public void initialize(URL arg0, ResourceBundle arg1){
		initIcons();
		initTableView();
		initActionIcons();
		UpdateTableView();
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
		/* 
		category.setCellFactory(new Callback<TableColumn<Article,Category>, TableCell<Article,Category>>() {
 
			@Override
			public TableCell<Article,Category> call(TableColumn<Article,Category> param) {
				return new TableCell<Article,Category>(){
					@Override
					protected void updateItem(Category item, boolean empty){
						if(item != null){
							System.out.println(item);
							setText(item.getName());
						}else{
							System.out.println(item);
						}

					}
				};
			}
		});
		*/
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
							//displayDeleteDialog();
						});
					
						ImageView iconEdit = new ImageView();
						File FileEditIcon =  new File("src/main/resources/com/example/img/edit.png");
						Image editImg = new Image(FileEditIcon.toURI().toString(), 25, 25, false, true);
									
						iconEdit.setImage(editImg);
						iconEdit.setStyle(" -fx-cursor: hand ; -fx-fill:#C90202; ");
						iconEdit.setOnMouseClicked((MouseEvent event) -> {
							article = tableView.getSelectionModel().getSelectedItem();
							//displayEditForm(article);
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
	
	/* delete
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
							displayAlert("success","Opération effectuée avec succes.");
						}else{
							displayAlert("alert","Une erreur c'est produite lors de la suppression de cet article.");
						}
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
   */
   	/* edit
	public void displayEditForm(Animal animal){
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../ui/editForm.fxml"));
		Parent root;
		try {
			root = (Parent) loader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			EditController editController = loader.getController();
			editController.configureBackground();
			editController.setAnimal(animal);
													
			stage.initModality(Modality.WINDOW_MODAL); // APPLICATION_MODAL
			Stage primaryStage = (Stage)(mainAnchorPane.getScene().getWindow()); 
													
			stage.initOwner(primaryStage);
			editController.fillForm();	
			editController.setDynamic();		
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
   */
	
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

   	// To test after fixing addArticle
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
   
	
   	/* to test
   @FXML
	public void toDashboard() throws IOException {
		currentTab.setText("Lappins EL BENNA / Accueil");
		dashboardPane.toFront();
		dashboardPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY,Insets.EMPTY)));
		System.out.println("to dasshboard");
	}
   */
	
	/* to test
	@FXML
	public void toStats() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../ui/stats.fxml"));
		AnchorPane pane = loader.load();

		StatsController StatsController = loader.getController();
		StatsController.setupUserInfo(getUser());

		mainAnchorPane.getChildren().clear();
        mainAnchorPane.getChildren().add(pane);
	}
   */
	
   	/* to test 
	@FXML
	public void toSettings() throws IOException {
		currentTab.setText("Lappins EL BENNA / Paramètres");
		settingsPane.toFront();
		settingsPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY,Insets.EMPTY)));
		System.out.println("to settings");
	}*/

}
