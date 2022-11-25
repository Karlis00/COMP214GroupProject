package application;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
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


public class Main extends Application {
	public String currentPage = "";
	//create the pane
	GridPane pane = new GridPane();
	GridPane mainPane = new GridPane();		
	
	@Override
	public void start(Stage primaryStage) {
		//try block for exception
		try {
			

			
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
			Button btnOrderView = new Button("Order (Tax Calculation)");
			Button btnUpdateOrderView = new Button("Update Order");
			Button[] menuBtns = new Button[] {btnAddView, btnEditView, btnShowView, btnBasketView, btnOrderView, btnUpdateOrderView};
					
			
			
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

		Label lblCoffeeDummy = new Label("Coffee");
		mainPane.add(lblCoffeeDummy, 0, 0);
		
	}

	private void runEditProduct() {
		mainPane.getChildren().clear();

		Button btnEdit = new Button("Edit");
		mainPane.add(btnEdit, 0, 0);		
	}

	private void runAddProduct() {
		mainPane.getChildren().clear();

		Button btnAdd = new Button("Add Product");
		mainPane.add(btnAdd, 0, 0);
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
