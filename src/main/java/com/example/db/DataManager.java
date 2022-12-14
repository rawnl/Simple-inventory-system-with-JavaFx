package com.example.db;

import com.example.model.Article;
import com.example.model.Category;
import com.example.model.Client;
import com.example.model.Commande;
import com.example.model.Facture;
import com.example.model.User;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class DataManager {
	
	private static Connection connection;//3306
    private static String url="jdbc:mysql://localhost:3306/bd_librairie?useLegacyDatetimeCode=false&serverTimezone=CET";
    
    private static Statement Stat ;
    private static PreparedStatement PreStat;
    private static ResultSet res ;
    
    public static void  getConnection(){ 
		String user="root";
		String password = "mysqlpassword";
		
    	try{ 
			Class.forName("com.mysql.cj.jdbc.Driver");//.newInstance(); 
			connection= DriverManager.getConnection(url,user,password ); 
			
			System.out.println("Database connected .");
		 }
		 catch(SQLException | ClassNotFoundException e){ //| InstantiationException | IllegalAccessException | ClassNotFoundException
			 System.out.println("Driver is missing !");
		 }
	 }

	public User Login(String username, String password){
		User user = null ;
		getConnection();
		try {
			PreStat = connection.prepareStatement("select * from users where username = ? and password = ? ; ");
			PreStat.setString(1,username);
			PreStat.setString(2,password);
			res = PreStat.executeQuery();
			if(res.next()){
				user = new User ();
				user.setId(res.getInt("id"));
				user.setName(res.getString("name"));
				user.setUsername(res.getString("username"));
				user.setPassword(res.getString("password"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	// Articles ------------------------
	// test if article already exists
	public boolean addArticle(Article article) {
		boolean result = false;
		getConnection();
		try {
			PreStat = connection.prepareStatement("insert into articles (barcode, articleName, categoryId, brand, model, price, quantity) values (?, ?, ?, ?, ?, ?, ?) ; ");
			PreStat.setString(1,article.getBarcode());
			PreStat.setString(2,article.getArticleName());
			PreStat.setInt(3,article.getCategory().getId());
			PreStat.setString(4,article.getBrand());
			PreStat.setString(5,article.getModel());
			
			PreStat.setDouble(6,article.getPrice());
			PreStat.setInt(7,article.getQuantity());
			if(PreStat.executeUpdate() >= 1){
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean DeleteArticle(int id) {
		boolean result = false ;
		getConnection();
		try {
			PreStat = connection.prepareStatement("delete from articles where id = ? ; ");
			PreStat.setInt(1,id);
			if(PreStat.executeUpdate() >= 1){
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean EditArticle(Article article) {
		boolean result = false;
		getConnection();
		try {
			PreStat = connection.prepareStatement(" UPDATE articles SET barcode = ?, articleName = ?, categoryId = ?, brand = ?, model = ?, price = ?, quantity = ? WHERE Id =  ? ; ");
				
			PreStat.setString(1,article.getBarcode());
			PreStat.setString(2,article.getArticleName());

			PreStat.setInt(3, article.getCategory().getId());
			PreStat.setString(4,article.getBrand());
			PreStat.setString(5,article.getModel());


			PreStat.setDouble(6,article.getPrice());
			PreStat.setInt(7, article.getQuantity());
			PreStat.setInt(8, article.getId());

			if(PreStat.executeUpdate() >= 1){
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public Article getArticleById(int id) {
		Article article = null ;
		getConnection();
		try {
			PreStat = connection.prepareStatement("select * from articles where id = ? ; ");
			PreStat.setInt(1,id);
			res = PreStat.executeQuery();
			if(res.next()){
				article = new Article ();
				article.setId(res.getInt("id"));
				article.setBarcode(res.getString("barcode"));
				article.setArticleName(res.getString("articleName"));
				article.setBrand(res.getString("brand"));
				article.setModel(res.getString("model"));
				article.setPrice(res.getDouble("price"));
				article.setQuantity((res.getInt("quantity")));
				article.setCategory(getCategory(res.getInt("categoryId")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return article;
	}

	// link to input & test
	public Article getArticleByBarcode(String barcode) {
		Article article = null ;
		getConnection();
		try {
			PreStat = connection.prepareStatement("select * from articles where barcode = ? ; ");
			PreStat.setString(1,barcode);
			res = PreStat.executeQuery();
			if(res.next()){
				article = new Article ();
				article.setId(res.getInt("id"));
				article.setBarcode(res.getString("barcode"));
				article.setArticleName(res.getString("articleName"));
				article.setBrand(res.getString("brand"));
				article.setModel(res.getString("model"));
				article.setPrice(res.getDouble("price"));
				article.setQuantity((res.getInt("quantity")));
				article.setCategory(getCategory(res.getInt("categoryId")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return article;
	}
	
	public Article getArticleByName(String articleName) {
		Article article = null ;
		getConnection();
		try {
			PreStat = connection.prepareStatement("select * from articles where articleName = ? ; ");
			PreStat.setString(1,articleName);
			res = PreStat.executeQuery();
			if(res.next()){
				article = new Article ();
				article.setId(res.getInt("id"));
				article.setBarcode(res.getString("barcode"));
				article.setArticleName(res.getString("articleName"));
				article.setBrand(res.getString("brand"));
				article.setModel(res.getString("model"));
				article.setPrice(res.getDouble("price"));
				article.setQuantity((res.getInt("quantity")));
				article.setCategory(getCategory(res.getInt("categoryId")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return article;
	}

	public ArrayList<Article> getArticles() {
		ArrayList<Article> articles = new ArrayList<Article>() ;
		getConnection();
		try {
			Stat = connection.createStatement();
			res = Stat.executeQuery("select * from articles, categories where articles.categoryId = categories.id ; ");
			while(res.next()){
				Article article = new Article ();
				article.setId(res.getInt("articles.id"));
				article.setBarcode(res.getString("barcode"));
				article.setArticleName(res.getString("articleName"));
				article.setPrice(res.getDouble("price"));
				article.setQuantity((res.getInt("quantity")));
				article.setBrand(res.getString("brand"));
				article.setModel(res.getString("model"));
				//article.setCategory(getCategory(res.getInt("categoryId")));
				article.setCategory(new Category(res.getInt("categoryId"), res.getString("categoryName")));
				articles.add(article);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		return articles;
	}
 
	// Cat??gories ------------------------
	public Category getCategory(int id) {
		Category category = null ;
		getConnection();
		try {
			PreStat = connection.prepareStatement("select * from categories where id = ? ; ");
			PreStat.setInt(1,id);
			res = PreStat.executeQuery();
			if(res.next()){
				category = new Category ();
				category.setId(res.getInt("id"));
				category.setName(res.getString("categoryName"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return category;
	}

	public Category getCategoryByName(String name) {
		Category category = null ;
		getConnection();
		try {
			PreStat = connection.prepareStatement("select * from categories where categoryName = ? ; ");
			PreStat.setString(1,name);
			res = PreStat.executeQuery();
			if(res.next()){
				category = new Category ();
				category.setId(res.getInt("id"));
				category.setName(res.getString("categoryName"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return category;
	}

	public boolean addCategory(Category category) {
        boolean result = false;
		getConnection();
		try {
			PreStat = connection.prepareStatement("insert into categories (categoryName) values (?) ; ");
			PreStat.setString(1,category.getName());
			
			if(PreStat.executeUpdate() >= 1){
				result = true;
			}
			connection.close();
			res.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
    }

	public ArrayList<Category> getCategories() {
		ArrayList<Category> categories = new ArrayList<Category>() ;
		getConnection();
		try {
			Stat = connection.createStatement();
			res = Stat.executeQuery("select * from categories ; ");
			while(res.next()){
				Category category = new Category ();
				
				category.setId(res.getInt("id"));
				category.setName(res.getString("categoryName"));
				//System.out.println(category.getName());
				categories.add(category);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return categories;
	}

	// Factures ------------------------
	public Facture addFacture(Facture facture) {
		getConnection();
		try {
			PreStat = connection.prepareStatement("insert into factures (clientId, userId, date, total) values (?, ?, ?, ?) ; ", Statement.RETURN_GENERATED_KEYS);
			PreStat.setInt(1,facture.getClient().getId());
			PreStat.setInt(2,facture.getUser().getId());
			PreStat.setDate(3, facture.getDate());
			PreStat.setDouble(4,facture.getTotal());
			PreStat.executeUpdate();
			res = PreStat.getGeneratedKeys();
			if(res.next() && res != null){
				facture.setNumber(res.getInt(1));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return facture;
		
	}

    public ArrayList<Facture> getFactures() {
        ArrayList<Facture> factures = new ArrayList<Facture>() ;
		getConnection();
		try {
			Stat = connection.createStatement();
			res = Stat.executeQuery("select * from factures, clients, users where factures.clientId = clients.id and factures.userId = users.id ; ");
			while(res.next()){
				Facture facture = new Facture ();
				facture.setNumber((res.getInt("factures.id")));
				facture.setClient(new Client(res.getInt("clients.id"),res.getString("clients.name"),res.getString("clients.phone"),res.getString("clients.address")));
				facture.setDate(res.getDate("factures.date"));
				facture.setUser(new User(res.getInt("users.id"),res.getString("users.name"),res.getString("users.username"),res.getString("users.password")));
				
				factures.add(facture);
				
				System.out.println(facture.getNumber());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		return factures;
    }

	public double getTodaysTotal(){
		double total = 0;
		getConnection();
		try {
			PreStat = connection.prepareStatement(" select * from factures where date = ? ; ");
			Date todaysDate = new Date(System.currentTimeMillis());
			PreStat.setDate(1,todaysDate);
			res = PreStat.executeQuery();
			while(res.next()){
				total = total + res.getDouble("total");
				System.out.println(total);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		return total;
	}

	// Commandes ------------------------
	public boolean addCommande(Commande commande){
		boolean result = false;
			getConnection();
			try {
				PreStat = connection.prepareStatement("insert into commandes (idFacture, idArticle, quantity) values (?, ?, ?) ; ");
				
				PreStat.setInt(1,commande.getIdFacture());			
				PreStat.setInt(2,commande.getIdArticle());
				PreStat.setInt(3,commande.getQuantity());
	
				if(PreStat.executeUpdate() >= 1){
					result = true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return result;
		}
	
	// Clients ------------------------
	public Client getClient(int id){
		Client client = null ;
		getConnection();
		try {
			PreStat = connection.prepareStatement("select * from clients where id = ? ; ");
			PreStat.setInt(1,id);
			res = PreStat.executeQuery();
			if(res.next()){
				client = new Client ();
				client.setId(res.getInt("id"));
				client.setName(res.getString("name"));
				client.setPhone(res.getString("phone"));
				client.setAddress(res.getString("address"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return client;
	}
	
	public ArrayList<Client> getClients(){
		ArrayList<Client> clients = new ArrayList<Client>() ;
		getConnection();
		try {
			Stat = connection.createStatement();
			res = Stat.executeQuery("select * from clients ; ");
			while(res.next()){
				Client client = new Client ();
				client.setId(res.getInt("id"));
				client.setName(res.getString("name"));
				client.setPhone(res.getString("phone"));
				client.setAddress(res.getString("address"));
				clients.add(client);
				System.out.println(client);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		return clients;
	}
  		
}
