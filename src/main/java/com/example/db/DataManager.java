package com.example.db;

import com.example.model.Article;
import com.example.model.User;

import java.sql.Connection;
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
	
	// link to btn & test
	 public boolean addArticle(Article article) {
		boolean result = false;
		getConnection();
		try {
			PreStat = connection.prepareStatement("insert into articles (barcode, articleName, price, quantity) values (?, ?, ?, ?) ; ");
			PreStat.setString(1,article.getBarcode());
			PreStat.setString(2,article.getArticleName());
			PreStat.setDouble(3,article.getPrice());
			PreStat.setInt(4,article.getQuantity());
			if(PreStat.executeUpdate() >= 1){
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	// link to btn & test
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

	// link to btn & test
	public boolean EditArticle(Article article) {
		boolean result = false;
		getConnection();
		try {
			PreStat = connection.prepareStatement(" UPDATE articles SET barcode = ?, articleName = ?, price = ?, quantity = ? WHERE Id =  ? ; ");
				
			PreStat.setString(1,article.getBarcode());
			PreStat.setString(2,article.getArticleName());
			PreStat.setDouble(3,article.getPrice());
			PreStat.setInt(4, article.getQuantity());
			PreStat.setInt(5, article.getId());

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
				article.setPrice(res.getDouble("price"));
				article.setQuantity((res.getInt("quantity")));
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
				article.setPrice(res.getDouble("price"));
				article.setQuantity((res.getInt("quantity")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return article;
	}
	
	//fix the name of the category
	public ArrayList<Article> getArticles() {
		ArrayList<Article> articles = new ArrayList<Article>() ;
		getConnection();
		try {
			Stat = connection.createStatement();
			res = Stat.executeQuery("select * from articles ; ");
			while(res.next()){
				Article article = new Article ();
				article.setId(res.getInt("id"));
				article.setBarcode(res.getString("barcode"));
				article.setArticleName(res.getString("articleName"));
				article.setCategory(Integer.toString(res.getInt("categoryId")));
				article.setBrand(res.getString("brand"));
				article.setModel(res.getString("model"));
				article.setPrice(res.getDouble("price"));
				article.setQuantity((res.getInt("quantity")));

				articles.add(article);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return articles;
	}

  /*   

	public static void main(String [] args){
		DataManager manager = new DataManager();
	}
	
	public static Map<String, Integer> countGroupBy(String date){
		Map<String, Integer> myMap = null;
		getConnection();
		try {
			PreStat = connection.prepareStatement("SELECT DMB_next, count(DMB_next) as rowcount FROM Animals where DMB_next >= ? group by DMB_next");
			PreStat.setString(1, date);

			res = PreStat.executeQuery();
			
			myMap = new HashMap<String, Integer>();
			
			while(res.next()){
				myMap.put(res.getString(1), res.getInt(2));
			}			
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return myMap;
	}
/*	
	public static void main(String [] args){
		/*FileOutputStream file = loadImage(1);
		if( file != null){
			System.out.println(file.toString());
		}else{
			System.out.println("error");
		}*/
/*
		LocalDate now = LocalDate.now();  
		//System.out.println(now);
		

		Map<String, Integer> myMap = countGroupBy();
		
		for (Map.Entry<String, Integer> entry : myMap.entrySet()) {
			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		}

	}*/
	
}
