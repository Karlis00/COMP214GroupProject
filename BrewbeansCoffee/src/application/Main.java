package application;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

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
	GridPane mainPane = new GridPane();
	Alert a = new Alert(AlertType.NONE);

	DatabaseData data;

	@Override
	public void start(Stage primaryStage) throws Exception {
		// try block for exception
		data = new DatabaseData();

		GridPane pane = new GridPane();

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
		mainPane.setVgap(10);
		mainPane.setHgap(10);

		pane.setStyle("-fx-background-color: white");

		Image banner = new Image(new FileInputStream("./src/Banner.png"));
		ImageView bannerView = new ImageView(banner);
		bannerView.setFitHeight(65);
		bannerView.setFitWidth(250);

		pane.add(bannerView, 0, 0);

		GridPane menuPane = new GridPane();
		pane.add(menuPane, 0, 1);
		menuPane.setStyle("-fx-padding: 10 0 30 30");

		pane.add(mainPane, 1, 1);

		Label lblMenu = new Label("Menu");
		lblMenu.setUnderline(true);

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
		Font boldfont = Font.font("Regular", FontWeight.BOLD, 12);

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
					runUpdateStatus();
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
		Scene scene = new Scene(pane, 1000, 500);
		primaryStage.setTitle("BB Coffee");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	private void runTaxCal() {
		mainPane.getChildren().clear();

		Button btnCal = new Button("Calculate tax");

		mainPane.add(new Label("State: "), 0, 0);
		mainPane.add(new Label("Subtotal: "), 0, 1);

		TextField txtState = new TextField();
		this.addTextLimiter(txtState, 2);
		TextField txtSubtotal = new TextField();

		TextField txtShipNumber = new TextField();

		mainPane.add(txtState, 1, 0);
		mainPane.add(new Text("(example: VA, NC, SC)"), 2, 0);

		mainPane.add(txtSubtotal, 1, 1);
		mainPane.add(btnCal, 1, 3);

		btnCal.setOnAction((event) -> {
			try {
				String state = txtState.getText().trim().toUpperCase();
				Double subtotal = Double.parseDouble(txtSubtotal.getText().trim());

				a.setAlertType(AlertType.INFORMATION);
				a.setHeaderText("Tax: " + data.getTaxCost(state, subtotal));
				a.show();
			} catch (Exception e) {
				e.printStackTrace();
				a.setAlertType(AlertType.ERROR);
				a.setHeaderText(e.getMessage());
				a.show();
			}

		});

	}

	public static void addTextLimiter(final TextField tf, final int maxLength) {
		tf.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue,
					final String newValue) {
				if (tf.getText().length() > maxLength) {
					String s = tf.getText().substring(0, maxLength);
					tf.setText(s);
				}
			}
		});
	}

	private void runUpdateStatus() {

		mainPane.getChildren().clear();

		Button btnAdd = new Button("Update Order Status");
		mainPane.add(new Label("Basket ID: "), 0, 0);
		mainPane.add(new Label("Date: "), 0, 1);
		mainPane.add(new Label("Shipper: "), 0, 2);
		mainPane.add(new Label("Ship Number: "), 0, 3);

		ComboBox cbBasketId = new ComboBox();
		List<String> basketIdList = data.getBacketIdList();
		cbBasketId.getItems().addAll(basketIdList);
		cbBasketId.setValue(basketIdList.isEmpty() ? "" : basketIdList.get(0));
		// TextField txtBasketId = new TextField();

		DatePicker datepicker = new DatePicker();
		datepicker.setValue(LocalDate.now());
		// txtDate = new TextField();
		TextField txtShipper = new TextField();
		this.addTextLimiter(txtShipper, 5);

		TextField txtShipNumber = new TextField();

		mainPane.add(cbBasketId, 1, 0);
		mainPane.add(datepicker, 1, 1);
		mainPane.add(txtShipper, 1, 2);
		mainPane.add(new Text("(max: 5 character)"), 2, 2);

		mainPane.add(txtShipNumber, 1, 3);
		mainPane.add(btnAdd, 1, 4);

		btnAdd.setOnAction((event) -> {
			Date date = Date.valueOf(datepicker.getValue());
			try {
				data.updateProduct(cbBasketId.getValue().toString(), date, txtShipper.getText(),
						txtShipNumber.getText());

				a.setAlertType(AlertType.INFORMATION);
				a.setHeaderText("Update Success");
				a.show();
			} catch (Exception e) {
				e.printStackTrace();
				a.setAlertType(AlertType.ERROR);
				a.setHeaderText(e.getMessage());
				a.show();
			}
		});

	}

	private void runShoppingBasket() {
		mainPane.getChildren().clear();

		Button btnCheck = new Button("Check Stock");
		mainPane.add(btnCheck, 0, 0);

	}

	private TableView getBasketTableView(String basketId) throws SQLException {
		TableView tableView = new TableView();
		tableView.setEditable(true);
		tableView.setMinWidth(400);

//		TableColumn<BasketTableModel, String> column1 = new TableColumn<>("Basket Item ID");
//		column1.setMinWidth(30);
//		column1.setCellValueFactory(new PropertyValueFactory<>("id"));

		TableColumn<BasketTableModel, String> column2 = new TableColumn<>("Basket Item");
		column2.setMinWidth(200);
		column2.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn<BasketTableModel, String> column3 = new TableColumn<>("Quantity");
		column3.setMinWidth(30);
		column3.setCellValueFactory(new PropertyValueFactory<>("quantity"));

		TableColumn<BasketTableModel, String> column4 = new TableColumn<>("Price");
		column4.setMinWidth(30);
		column4.setCellValueFactory(new PropertyValueFactory<>("price"));

		TableColumn<BasketTableModel, String> column5 = new TableColumn<>("Stock");
		column5.setMinWidth(30);
		column5.setCellValueFactory(new PropertyValueFactory<>("stock"));

		
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
//		tableView.getColumns().add(column1);
		tableView.getColumns().add(column2);
		tableView.getColumns().add(column3);
		tableView.getColumns().add(column4);
		tableView.getColumns().add(column5);

		tableView.getItems().addAll(data.getItemByBasketId(basketId));

		return tableView;
	}

	private void runReport1() {
		mainPane.getChildren().clear();

		Button btnAdd = new Button("Check In Stock");
		mainPane.add(new Label("Basket ID: "), 0, 0);

		ComboBox cbBasketId = new ComboBox();
		List<String> basketIdList = data.getBacketIdList();
		cbBasketId.getItems().addAll(basketIdList);

		mainPane.add(cbBasketId, 1, 0);
		mainPane.add(btnAdd, 2, 0);

		cbBasketId.setOnAction((event) -> {
			try {
//				ListView<String> listview = new ListView<String>();
//				listview.getItems().addAll(model.getItemByBasketId(cbBasketId.getValue().toString()));
				mainPane.add(getBasketTableView(cbBasketId.getValue().toString()), 0, 1, 3, 1);
			} catch (SQLException e) {
				e.printStackTrace();
				a.setAlertType(AlertType.ERROR);
				a.setHeaderText(e.getMessage());
				a.show();
			}

		});

		btnAdd.setOnAction((event) -> {
			if (cbBasketId.getValue() == null) {
				a.setAlertType(AlertType.ERROR);
				a.setHeaderText("Please select a Backet ID");
				a.show();
			} else {

				try {
					String msg = data.checkInStock(cbBasketId.getValue().toString());

					a.setAlertType(AlertType.INFORMATION);
					a.setHeaderText(msg);
					a.show();
				} catch (Exception e) {
					e.printStackTrace();
					a.setAlertType(AlertType.ERROR);
					a.setHeaderText(e.getMessage());
					a.show();
				}
			}
		});

	}

	private void runReport2() {
		mainPane.getChildren().clear();

	}

	private void runShowProduct() {
		mainPane.getChildren().clear();

		ListView<String> lstvCoffeeProduct = new ListView<String>();
		mainPane.add(new Label("Coffee"), 0, 0);
		mainPane.add(lstvCoffeeProduct, 0, 1);

		lstvCoffeeProduct.getItems().addAll(data.getProductList());
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

			try {
				data.addProduct(productName, productDescription, filename, price, status);

				a.setAlertType(AlertType.INFORMATION);
				a.setHeaderText("Update Success");
				a.show();
			} catch (SQLException e) {
				e.printStackTrace();
				a.setAlertType(AlertType.ERROR);
				a.setHeaderText(e.getMessage());
				a.show();
			}
		});

	}

	public static void main(String[] args) {
		launch(args);
	}
}
