package application;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import java.io.FileInputStream; 
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class Main extends Application {
	static final String DRIVER = "oracle.jdbc.OracleDriver";
	static final String DATABASE_URL = "jdbc:oracle:thin:@ 199.212.26.208:1521:SQLD";
	public String currentPage = "";
	//create the pane
	GridPane pane = new GridPane();
	GridPane mainPane = new GridPane();		
	
	   Connection connection = null; // manages connection
	   Statement statement = null; // query statement
	   ResultSet resultSet = null; // manages results
	
	@Override
	public void start(Stage primaryStage) {
		//try block for exception
		try {
			

			   
			   // load the driver class
			   Class.forName( DRIVER );

			   // establish connection to database                              
			   connection =                                                     
			   DriverManager.getConnection( DATABASE_URL, "COMP214_F22_er_53", "password" );
			

			      
			//loop for the column to set width
			    RowConstraints rowBanner = new RowConstraints();
			    rowBanner.setPrefHeight(200);
			    pane.getRowConstraints().add(rowBanner);
	            ColumnConstraints columnLeft = new ColumnConstraints();
	            columnLeft.setPrefWidth(200);
	            pane.getColumnConstraints().add(columnLeft);
	            ColumnConstraints columnRight = new ColumnConstraints();
	            columnRight.setPrefWidth(800);
	            pane.getColumnConstraints().add(columnRight);

	        
	        //padding and setting gap
			pane.setPadding(new Insets(10));
			mainPane.setPadding(new Insets(30));
			Label notification = new Label();
			
			pane.setStyle("-fx-background-color: white");
			
			
			Image banner = new Image(new FileInputStream("./src/Banner.png"));
			ImageView bannerView = new ImageView(banner);
			bannerView.setFitHeight(130);
			bannerView.setFitWidth(500);

			
			pane.add(bannerView, 0, 0);
			
			GridPane menuPane = new GridPane();			
			pane.add(menuPane, 0, 1);
			menuPane.setStyle("-fx-padding: 10 0 30 30; -fx-grid-lines-visible: true\");");

			pane.add(mainPane, 1, 1);

			
//			//create menu buttons		
			Label lblMenu = new Label("MENU");
			menuPane.add(lblMenu, 0, 0);
			Button btnAddView = new Button("Add Product");
			Button btnEditView = new Button("Edit Product");
			Button btnShowView = new Button("Show Products");
			Button btnBasketView = new Button("Shopping Basket");
			Button btnOrderTaxView = new Button("Order (Tax Calculation)");
			Button btnUpdateOrderView = new Button("Update Order");
			Button[] menuBtns = new Button[] {btnEditView, btnAddView,  btnUpdateOrderView, btnOrderTaxView, btnShowView, btnBasketView};
					
			
			
			//allocate menu buttons into the menu
			int menuRow = 1;
			for (Button b : menuBtns) {
				menuPane.add(b, 0, menuRow);
				menuRow++;
				
				b.setStyle("-fx-background-color: white; -fx-border-color: white; -fx-border-radius: 5; -fx-padding: 5");
				
				b.setOnAction((event)->
				{
					currentPage = b.getText();
					
					switch (currentPage) {
					case "Add Product":
						runAddProduct();
						break;
					case "Edit Product":
						runEditProduct();
						break;
					case "Show Products":
						runShowProduct();
						break;
					case "Shopping Basket":
						runShoppingBasket();
						break;
					case "Order (Tax Calculation)":
						runTaxCal();
						break;
					case "Update Order":
						runUpdateOrder();
						break;
					}
					
				});
			}
			
			
			//Create a scene and place it in the stage
			Scene scene = new Scene(pane);
			primaryStage.setTitle("BB Coffee");
			primaryStage.setScene(scene);
			primaryStage.show();
		} 
		catch(Exception e) {
			e.printStackTrace();
		}

				
	}
	
	private void runUpdateOrder() {
		mainPane.getChildren().clear();

		Button btnUpdate = new Button("Update");
		mainPane.add(btnUpdate, 0, 0);
		
	}

	private void runTaxCal() {
		mainPane.getChildren().clear();

		Button btnCalculate = new Button("Calculate");
		mainPane.add(btnCalculate, 0, 0);
		
	}

	private void runShoppingBasket() {
		mainPane.getChildren().clear();

		Button btnCheck = new Button("Check Stock");
		mainPane.add(btnCheck, 0, 0);
		
	}

	private void runShowProduct() {
		mainPane.getChildren().clear();
		

		   try {
			// create Statement for querying database
			statement = connection.createStatement();
		
			//String query
			String query = "Select * from BB_PRODUCT";
			// query database                                        
			resultSet = statement.executeQuery(query);
			ResultSetMetaData metaData = resultSet.getMetaData();
		    int numberOfColumns = metaData.getColumnCount();
		    String displayHeader = "";
		    String displayText = "";
		    List<String> displayProductName = new ArrayList<String>();
			for ( int i = 1; i <= numberOfColumns; i++ )
		         displayHeader += metaData.getColumnName( i ) + " \n";

		      
		      while ( resultSet.next() ) 
		      {
		        	 displayProductName.add( resultSet.getObject( 2 ) + " \n");

		      }
			
			
			TextArea txtCoffeeProduct = new TextArea();
			Label lblCoffeeDummy = new Label("Coffee");
			mainPane.add(lblCoffeeDummy, 0, 0);
			mainPane.add(txtCoffeeProduct, 0, 1);
			txtCoffeeProduct.setText(displayHeader + displayProductName);
			
		   }
		   catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	private void runEditProduct() {
		mainPane.getChildren().clear();

		Button btnEdit = new Button("Update Product");
		Button btnGetProduct = new Button("Search by ID");
		mainPane.add(new Label ("Product ID: "), 0, 0);
		mainPane.add(new Label ("Product Name: "), 0, 1);
		mainPane.add(new Label ("Product Description: "), 0, 2);
		TextField txtProductID = new TextField();
		TextField txtProductName = new TextField();
		TextArea txtProductDescription = new TextArea();
		txtProductDescription.setWrapText(true);

		mainPane.add(txtProductID, 1, 0);
		mainPane.add(btnGetProduct, 2, 0);
		mainPane.add(txtProductName, 1, 1);
		mainPane.add(txtProductDescription, 1, 2);

		mainPane.add(btnEdit, 1, 3);
		btnGetProduct.setOnAction((event)->{});
		
		btnEdit.setOnAction((event)->{});	
	}

	private void runAddProduct() {
		mainPane.getChildren().clear();
		
		Button btnAdd = new Button("Add Product");
		mainPane.add(new Label ("Product Name: "), 0, 0);
		mainPane.add(new Label ("Product Description: "), 0, 1);
		mainPane.add(new Label ("Image Filename: "), 0, 2);
		mainPane.add(new Label ("Price: "), 0, 3);
		mainPane.add(new Label ("Status: "), 0, 4);
		TextField txtProductName = new TextField();
		TextArea txtProductDescription = new TextArea();
		txtProductDescription.setWrapText(true);
		
		TextField txtImageFilename = new TextField();
		TextField txtPrice = new TextField();
		TextField txtStatus = new TextField();
		mainPane.add(txtProductName, 1, 0);
		mainPane.add(txtProductDescription, 1, 1);
		mainPane.add(txtImageFilename, 1, 2);
		mainPane.add(txtPrice, 1, 3);
		mainPane.add(txtStatus, 1, 4);
		mainPane.add(btnAdd, 1, 5);
		btnAdd.setOnAction((event)->{});
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
