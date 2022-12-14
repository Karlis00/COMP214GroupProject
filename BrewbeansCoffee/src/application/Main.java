package application;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Main extends Application {
	public String currentPage = "";

	static final String TASK_1 = "Task 1: Update Product Description";
	static final String TASK_2 = "Task 2: Add New Product";
	static final String TASK_3 = "Task 3: Calculate Tax";
	static final String TASK_4 = "Task 4: Update Order Status";
	static final String TASK_5 = "Task 5 & 6: Add To Basket & Check Sale Product";
	static final String REPORT_1 = "Report 1: Check In Stock";
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
		columnLeft.setPrefWidth(450);
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
					try {
						try {
							runShowProduct();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
		mainPane.add(new Text("(For example: VA, NC, SC)"), 2, 0);

		mainPane.add(txtSubtotal, 1, 1);
		mainPane.add(btnCal, 1, 3);

		btnCal.setOnAction((event) -> {
			try {
				String state = txtState.getText().trim().toUpperCase();
				Double subtotal = Double.parseDouble(txtSubtotal.getText().trim());

				a.setAlertType(AlertType.INFORMATION);
				a.setHeaderText("Tax: $" + data.getTaxCost(state, subtotal));
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
		mainPane.add(new Text("(Max. 5 characters)"), 2, 2);

		mainPane.add(txtShipNumber, 1, 3);
		mainPane.add(btnAdd, 1, 4);

		btnAdd.setOnAction((event) -> {
			Date date = Date.valueOf(datepicker.getValue());
			try {
				data.updateProduct(cbBasketId.getValue().toString(), date, txtShipper.getText(),
						txtShipNumber.getText());

				a.setAlertType(AlertType.INFORMATION);
				a.setHeaderText("The order status has been updated successfully.");
				a.show();
			} catch (Exception e) {
				e.printStackTrace();
				a.setAlertType(AlertType.ERROR);
				a.setHeaderText(e.getMessage());
				a.show();
			}
		});

	}

	private TableView getBasketTableView(String basketId) throws SQLException {
		TableView tableView = new TableView();
		tableView.setEditable(true);
		tableView.setMinWidth(400);

		TableColumn<TableModel, String> column2 = new TableColumn<>("Basket Item");
		column2.setMinWidth(200);
		column2.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn<TableModel, String> column3 = new TableColumn<>("Quantity");
		column3.setMinWidth(30);
		column3.setCellValueFactory(new PropertyValueFactory<>("quantity"));

		TableColumn<TableModel, String> column4 = new TableColumn<>("Price ($)");
		column4.setMinWidth(30);
		column4.setCellValueFactory(new PropertyValueFactory<>("price"));

		TableColumn<TableModel, String> column5 = new TableColumn<>("Stock");
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

	private TableView getSpendingTableView(List<String> idList) throws SQLException {
		List<TableModel> list = new ArrayList<>();
		for (String id : idList) {
			TableModel model = new TableModel();
			model.setId(id);
			model.setPrice(data.getSpendingByShopperId(id));
			list.add(model);
		}

		TableView tableView = new TableView();
		tableView.setEditable(true);
		tableView.setMinWidth(300);

		TableColumn<TableModel, String> column2 = new TableColumn<>("Shopper ID");
		column2.setMinWidth(100);
		column2.setCellValueFactory(new PropertyValueFactory<>("id"));

		TableColumn<TableModel, String> column4 = new TableColumn<>("Total Spending ($)");
		column4.setMinWidth(100);
		column4.setCellValueFactory(new PropertyValueFactory<>("price"));

		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		tableView.getColumns().add(column2);
		tableView.getColumns().add(column4);

		tableView.getItems().addAll(list);

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

		Button btnList = new Button("List All");
		mainPane.add(new Label("Shopper ID: "), 0, 0);

		ComboBox cbShopperId = new ComboBox();
		List<String> shopperIdList = data.getShopperIdList();

		cbShopperId.getItems().addAll(shopperIdList);

		mainPane.add(cbShopperId, 1, 0);
		mainPane.add(btnList, 2, 0);

		cbShopperId.setOnAction((event) -> {
			try {
				List<String> list = new ArrayList<>();
				list.add(cbShopperId.getValue().toString());
				mainPane.add(this.getSpendingTableView(list), 0, 1, 3, 1);
			} catch (SQLException e) {
				e.printStackTrace();
				a.setAlertType(AlertType.ERROR);
				a.setHeaderText(e.getMessage());
				a.show();
			}

		});

		btnList.setOnAction((event) -> {
			try {
				List<String> list = new ArrayList<>();
				list.addAll(shopperIdList);
				mainPane.add(this.getSpendingTableView(list), 0, 1, 3, 1);
			} catch (Exception e) {
				e.printStackTrace();
				a.setAlertType(AlertType.ERROR);
				a.setHeaderText(e.getMessage());
				a.show();
			}
		});

	}

	private void runShowProduct() throws FileNotFoundException, SQLException {
		mainPane.getChildren().clear();

		TableView<TableModel> tbCoffeeProduct = new TableView<TableModel>();
		tbCoffeeProduct.setMaxHeight(400);
		tbCoffeeProduct.setMaxWidth(297);
		TableColumn<TableModel, Integer> idCol = new TableColumn<>("ID");
		idCol.setMinWidth(20);
		idCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
		TableColumn<TableModel, String> nameCol = new TableColumn<>("Product");
		nameCol.setMinWidth(200);
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		TableColumn<TableModel, Integer> priceCol = new TableColumn<>("Price ($)");
		priceCol.setMinWidth(20);
		priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
		tbCoffeeProduct.getColumns().addAll(idCol, nameCol, priceCol);
		
		mainPane.add(new Label("Coffee"), 0, 0, 3, 1);
		mainPane.add(tbCoffeeProduct, 0, 1, 1, 6);

		tbCoffeeProduct.getItems().addAll(data.getProductList());
		
		mainPane.add(new Label("Quantity"), 1,2);
		mainPane.add(new Label("Size"), 1,3);
		mainPane.add(new Text("(1 or 2)"), 3, 3);
		mainPane.add(new Label("Form"),1,4);
		mainPane.add(new Text("(3 or 4)"), 3, 4);
		TextField txtQuantity = new TextField();
		TextField txtSize = new TextField();
		TextField txtForm = new TextField();
		mainPane.add(txtQuantity, 2,2);
		mainPane.add(txtSize, 2,3);
		mainPane.add(txtForm,2,4);
		tbCoffeeProduct.getSelectionModel().selectFirst();
		
		ImageView coffeeView = new ImageView();
		coffeeView.setFitHeight(180);
		coffeeView.setFitWidth(240);
		coffeeView.setImage(new Image(new FileInputStream("./src/" + tbCoffeeProduct.getSelectionModel().getSelectedItem().getImg())));
		mainPane.add(coffeeView,1,1,2,1);
		Label lblSales = new Label();
		mainPane.add(lblSales,1,0,2,1);
		GridPane.setHalignment(lblSales, HPos.RIGHT);
		GridPane.setValignment(lblSales, VPos.BOTTOM);

		
		tbCoffeeProduct.setOnMouseClicked((event) -> {
			try {
				coffeeView.setImage(new Image(new FileInputStream("./src/" + tbCoffeeProduct.getSelectionModel().getSelectedItem().getImg())));
				lblSales.setText("");
				lblSales.setText(data.checkOnSales(tbCoffeeProduct.getSelectionModel().getSelectedItem().getProductId()));

				if (lblSales.getText().equalsIgnoreCase("ON SALE!")) {
					lblSales.setTextFill(Color.RED);
					}
				else
				{
				lblSales.setTextFill(Color.DARKVIOLET);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		mainPane.add(new Label("Basket ID: "),1,5);
		ComboBox cbBasketId = new ComboBox();
		List<String> basketIdList = data.getBacketIdList();
		cbBasketId.getItems().addAll(basketIdList);

		mainPane.add(cbBasketId,2,5);
		
		Button btnToBasket = new Button("Add to Basket");
		mainPane.add(btnToBasket,2,6);
		
		
		
		btnToBasket.setOnAction((event)->{
			try {
			data.addToBasket(Integer.parseInt(cbBasketId.getValue().toString()), 
					tbCoffeeProduct.getSelectionModel().getSelectedItem().getProductId(), 
					Double.parseDouble(tbCoffeeProduct.getSelectionModel().getSelectedItem().getPrice()),
					Integer.parseInt(txtQuantity.getText()),
					Integer.parseInt(txtSize.getText()),
					Integer.parseInt(txtForm.getText()));
			
			a.setAlertType(AlertType.INFORMATION);
			a.setHeaderText("The product has been added to basket successfully.");
			a.show();
		} catch (Exception e) {
			e.printStackTrace();
			a.setAlertType(AlertType.ERROR);
			a.setHeaderText(e.getMessage());
			a.show();
		}
			
		});

	}

	private void runEditProduct() {
		mainPane.getChildren().clear();
		
		ComboBox cbProductId = new ComboBox();
		List<String> productIdList = data.getProductIdList();
		cbProductId.getItems().addAll(productIdList);

		Button btnEdit = new Button("Update Product");
		//Button btnGetProduct = new Button("Find");
		mainPane.add(new Label("Product ID: "), 0, 0);
		mainPane.add(new Label("Product Name: "), 0, 1);
		mainPane.add(new Label("Description: "), 0, 2);
		//TextField txtProductID = new TextField();
		Label lblProductName = new Label();
		TextArea txtProductDescription = new TextArea();
		txtProductDescription.setWrapText(true);

		mainPane.add(cbProductId, 1, 0);
		mainPane.add(lblProductName, 1, 1);
		mainPane.add(txtProductDescription, 1, 2);

		mainPane.add(btnEdit, 1, 3);
		cbProductId.setOnAction((event) -> {
			List<String>array = data.getProductDetail(cbProductId.getValue().toString());
			lblProductName.setText(array.get(0));
			txtProductDescription.setText(array.get(1));
		});

		btnEdit.setOnAction((event) -> {
		
			try {
			data.setProductDetail(cbProductId.getValue().toString(), txtProductDescription.getText());
			a.setAlertType(AlertType.INFORMATION);
			a.setHeaderText("The product description has been updated successfully.");
			a.show();
		} catch (Exception e) {
			e.printStackTrace();
			a.setAlertType(AlertType.ERROR);
			a.setHeaderText(e.getMessage());
			a.show();
		}
		});
	}

	private void runAddProduct() {
		mainPane.getChildren().clear();

		Button btnAdd = new Button("Add Product");
		mainPane.add(new Label("Product Name: "), 0, 0);
		mainPane.add(new Label("Description: "), 0, 1);
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
				a.setHeaderText("The new product has been added successfully.");
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
