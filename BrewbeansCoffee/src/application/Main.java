package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.io.FileInputStream;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Main extends Application {
	public String currentPage = "";

	static final String TASK_1 = "Task 1: Update Product Description";
	static final String TASK_2 = "Task 2: Add New Product";
	static final String TASK_3 = "Task 3: Calculate Tax";
	static final String TASK_4 = "Task 4: Update Shipping Status";
	static final String TASK_5 = "Task 5/6: Add Basket & Check Sale";
	static final String REPORT_1 = "Report 1: Check In Stock ";
	static final String REPORT_2 = "Report 2: Calculate Total Spending";

	// create the pane
	GridPane pane = new GridPane();
	GridPane mainPane = new GridPane();
	Model model;

	@Override
	public void start(Stage primaryStage) throws Exception {
		// try block for exception
		model = new Model();
		RowConstraints rowBanner = new RowConstraints();
		rowBanner.setPrefHeight(100);
		pane.getRowConstraints().add(rowBanner);
		ColumnConstraints columnLeft = new ColumnConstraints();
		columnLeft.setPrefWidth(300);
		pane.getColumnConstraints().add(columnLeft);
		ColumnConstraints columnRight = new ColumnConstraints();
		columnRight.setPrefWidth(800);
		pane.getColumnConstraints().add(columnRight);

		// padding and setting gap
		pane.setPadding(new Insets(10));
		mainPane.setPadding(new Insets(30));

		pane.setStyle("-fx-background-color: white");

		Image banner = new Image(new FileInputStream("./src/Banner.png"));
		ImageView bannerView = new ImageView(banner);
		bannerView.setFitHeight(65);
		bannerView.setFitWidth(250);

		pane.add(bannerView, 0, 0);

		GridPane menuPane = new GridPane();
		pane.add(menuPane, 0, 1);
		menuPane.setStyle("-fx-padding: 10 0 30 30; -fx-grid-lines-visible: true\");");

		pane.add(mainPane, 1, 1);

//			//create menu buttons		
		Label lblMenu = new Label("Menu");

		menuPane.add(lblMenu, 0, 3);
		Button btnEditView = new Button(TASK_1);
		Button btnAddView = new Button(TASK_2);
		Button btnUpdateOrderView = new Button(TASK_3);
		Button btnOrderTaxView = new Button(TASK_4);
		Button btnShowView = new Button(TASK_5);
		Button btnReport1View = new Button(REPORT_1);
		Button btnReport2View = new Button(REPORT_2);
		Button[] menuBtns = new Button[] { btnEditView, btnAddView, btnUpdateOrderView, btnOrderTaxView, btnShowView,
				btnReport1View, btnReport2View };

		Font font = Font.font("Regular", FontWeight.LIGHT, 12);
		Font boldfont = Font.font("Regular", FontWeight.BOLD, 14);

		// allocate menu buttons into the menu
		int menuRow = 4;
		for (Button b : menuBtns) {
			menuPane.add(b, 0, menuRow);
			menuRow++;

			b.setFont(font);
			b.setStyle("-fx-background-color: #ffffff; -fx-border-color: white; -fx-border-radius: 5; -fx-padding: 5");

			b.setOnAction((event) -> {

				for (Button btn : menuBtns) {
					btn.setFont(font);
				}

				b.setFont(boldfont);
				currentPage = b.getText();

				switch (currentPage) {
				case TASK_1:
					runEditProduct();
					break;
				case TASK_2:
					runAddProduct();
					break;
				case TASK_3:
					runTaxCal();
					break;
				case TASK_4:
					runUpdateOrder();
					break;
				case TASK_5:
					runShowProduct();
					break;
				case REPORT_1:
					runReport1();
					break;
				case REPORT_2:
					runReport2();
					break;
				}

			});
		}

		// Create a scene and place it in the stage
		Scene scene = new Scene(pane);
		primaryStage.setTitle("BB Coffee");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	private void runTaxCal() {
		mainPane.getChildren().clear();

		Button btnCal = new Button("Calculate tax");

		mainPane.add(new Label("Shopper's Statue: "), 0, 0);
		mainPane.add(new Label("Backet Subtotal: "), 0, 1);

		TextField txtState = new TextField();
		TextField txtSubtotal = new TextField();

		mainPane.add(txtState, 1, 0);
		mainPane.add(txtSubtotal, 1, 1);
		mainPane.add(btnCal, 1, 3);

		Label txtResult = new Label();
		mainPane.add(txtResult, 1, 4);

		btnCal.setOnAction((event) -> {
			String state = txtState.getText();
			Double subtotal = Double.parseDouble(txtSubtotal.getText());
			txtResult.setText("" + model.getTaxCost(state, subtotal));

		});

	}

	private void runUpdateOrder() {

		mainPane.getChildren().clear();

		Button btnAdd = new Button("Update Order Status");
		mainPane.add(new Label("Basket ID: "), 0, 0);
		mainPane.add(new Label("Date: "), 0, 1);
		mainPane.add(new Label("Shipper: "), 0, 2);
		mainPane.add(new Label("Ship Number: "), 0, 3);

		TextField txtBasketId = new TextField();
		TextField txtDate = new TextField();
		TextField txtShipper = new TextField();
		TextField txtShipNumber = new TextField();

		mainPane.add(txtBasketId, 1, 0);
		mainPane.add(txtDate, 1, 1);
		mainPane.add(txtShipper, 1, 2);
		mainPane.add(txtShipNumber, 1, 3);

		mainPane.add(btnAdd, 1, 5);
		btnAdd.setOnAction((event) -> {
			model.updateProduct(txtBasketId.getText(), txtDate.getText(), txtShipper.getText(),
					txtShipNumber.getText());
		});

	}

	private void runShoppingBasket() {
		mainPane.getChildren().clear();

		Button btnCheck = new Button("Check Stock");
		mainPane.add(btnCheck, 0, 0);

	}

	private void runReport1() {
		mainPane.getChildren().clear();

	}

	private void runReport2() {
		mainPane.getChildren().clear();

	}

	private void runShowProduct() {
		mainPane.getChildren().clear();

		ListView<String> lstvCoffeeProduct = new ListView<String>();
		mainPane.add(new Label("Coffee"), 0, 0);
		mainPane.add(lstvCoffeeProduct, 0, 1);

		lstvCoffeeProduct.getItems().addAll(model.getProductList());
	}

	private void runEditProduct() {
		mainPane.getChildren().clear();

		Button btnEdit = new Button("Update Product");
		Button btnGetProduct = new Button("Search by ID");
		mainPane.add(new Label("Product ID: "), 0, 0);
		mainPane.add(new Label("Product Name: "), 0, 1);
		mainPane.add(new Label("Product Description: "), 0, 2);
		TextField txtProductID = new TextField();
		TextField txtProductName = new TextField();
		TextArea txtProductDescription = new TextArea();
		txtProductDescription.setWrapText(true);

		mainPane.add(txtProductID, 1, 0);
		mainPane.add(btnGetProduct, 2, 0);
		mainPane.add(txtProductName, 1, 1);
		mainPane.add(txtProductDescription, 1, 2);

		mainPane.add(btnEdit, 1, 3);
		btnGetProduct.setOnAction((event) -> {
		});

		btnEdit.setOnAction((event) -> {
		});
	}

	private void runAddProduct() {
		mainPane.getChildren().clear();

		Button btnAdd = new Button("Add Product");
		mainPane.add(new Label("Product Name: "), 0, 0);
		mainPane.add(new Label("Product Description: "), 0, 1);
		mainPane.add(new Label("Image Filename: "), 0, 2);
		mainPane.add(new Label("Price: "), 0, 3);
		mainPane.add(new Label("Status: "), 0, 4);
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
		btnAdd.setOnAction((event) -> {

			String productName = txtProductName.getText();
			String productDescription = txtProductDescription.getText();
			String filename = txtImageFilename.getText();
			Double price = Double.parseDouble(txtPrice.getText());
			Integer status = Integer.parseInt(txtStatus.getText());

			model.addProduct(productName, productDescription, filename, price, status);
		});

	}

	public static void main(String[] args) {
		launch(args);
	}
}
