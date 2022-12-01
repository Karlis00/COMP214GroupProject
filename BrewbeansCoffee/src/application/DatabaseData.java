package application;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DatabaseData {

	static final String DRIVER = "oracle.jdbc.OracleDriver";
	static final String DATABASE_URL = "jdbc:oracle:thin:@199.212.26.208:1521:SQLD";

	Connection connection = null; // manages connection
	Statement statement = null; // query statement
	CallableStatement cStatement = null; // callable query statement
	PreparedStatement pStatement = null; // prepared query statement
	ResultSet resultSet = null; // manages results

	public DatabaseData() throws ClassNotFoundException, SQLException {
		Class.forName(DRIVER);
		connection = DriverManager.getConnection(DATABASE_URL, "COMP214_F22_er_57", "password");
		statement = connection.createStatement();
	}

	public List<String> getProductIdList() {
		List<String> array = new ArrayList<>();
		try {
			String query = "Select idproduct from bb_product order by 1";
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				array.add(resultSet.getString(1));
			}
			return array;
		}

		catch (SQLException e) {
			e.printStackTrace();
		}
		return array;
	}
	
	public List<String> getBacketIdList() {
		List<String> array = new ArrayList<>();
		try {
			String query = "Select idbasket from bb_basket order by 1 desc";
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				array.add(resultSet.getString(1));
			}
			return array;
		}

		catch (SQLException e) {
			e.printStackTrace();
		}
		return array;
	}
	
	public List<String> getShopperIdList() {
		List<String> array = new ArrayList<>();
		try {
			String query = "Select idshopper from bb_shopper order by 1 desc";
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				array.add(resultSet.getString(1));
			}
			return array;
		}

		catch (SQLException e) {
			e.printStackTrace();
		}
		return array;
	}

	public Double getTaxCost(String state, Double subtotal) throws SQLException {
		cStatement = connection.prepareCall("CALL tax_cost_sp(?, ?, ?)");
		cStatement.setString(1, state);
		cStatement.setDouble(2, subtotal);
		cStatement.registerOutParameter(3, Types.DOUBLE);
		cStatement.executeQuery();
		return cStatement.getDouble(3);
	}

	public String checkInStock(String basketid) throws SQLException {
		cStatement = connection.prepareCall("CALL ck_instock_sp(?,?)");
		cStatement.setString(1, basketid);
		cStatement.registerOutParameter(2, Types.VARCHAR);
		cStatement.executeQuery();
		return cStatement.getString(2);
	}

	public List<String> getStateList() {
		List<String> array = new ArrayList<>();
		try {
			String query = "Select state from bb_tax order by 1 desc";
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				array.add(resultSet.getString(2));
			}
			return array;
		}

		catch (SQLException e) {
			e.printStackTrace();
		}
		return array;
	}

	public void addProduct(String productName, String productDescription, String filename, Double price, Integer status)
			throws SQLException {
		cStatement = connection.prepareCall("CALL prod_add_sp(?, ?, ?, ?, ?)");
		cStatement.setString(1, productName);
		cStatement.setString(2, productDescription);
		cStatement.setString(3, filename);
		cStatement.setDouble(4, price);
		cStatement.setInt(5, status);
		cStatement.executeQuery();
	}

	public ObservableList<TableModel> getProductList() {

		ObservableList<TableModel> array = FXCollections.observableArrayList();

		try {
			String query = "Select IDPRODUCT, PRODUCTNAME, PRICE, PRODUCTIMAGE from BB_PRODUCT order by 1";
			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				array.add(new TableModel(resultSet.getInt("IDPRODUCT"), resultSet.getString("PRODUCTNAME"), resultSet.getString("PRICE"), resultSet.getString("PRODUCTIMAGE")));
			}
			return array;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return array;
	}

	public void updateProduct(String basketId, Date date, String shipper, String shipNumber) throws SQLException {
		cStatement = connection.prepareCall("CALL status_ship_sp(?, ?, ?, ?)");
		cStatement.setString(1, basketId);
		cStatement.setDate(2, date);
		cStatement.setString(3, shipper);
		cStatement.setString(4, shipNumber);
		cStatement.executeQuery();
	}

	public List<TableModel> getItemByBasketId(String basketId) throws SQLException {
		List<TableModel> array = new ArrayList<>();
		String query = "select bb_basketitem.idbasketitem, bb_product.productname, bb_basketitem.quantity, bb_basketitem.price, bb_product.stock "
				+ " from bb_basketitem "
				+ "left join bb_product on bb_basketitem.idproduct = bb_product.idproduct "
				+ "where bb_basketitem.idbasket = " + basketId + " order by 1 desc";

		resultSet = statement.executeQuery(query);
		resultSet.getMetaData();
		while (resultSet.next()) {
			array.add(new TableModel(resultSet.getString(1), resultSet.getString(2),
					resultSet.getString(3), resultSet.getString(4), resultSet.getString(5)));
		}
		return array;
	}
	
	public String getSpendingByShopperId(String shopperId) throws SQLException {

		cStatement = connection.prepareCall("{? = call tot_purch_sf(?)}");
		cStatement.registerOutParameter(1, Types.DECIMAL);
		cStatement.setString(2, shopperId);
		cStatement.execute();
		return cStatement.getString(1);
		
	}


public List<String> getProductDetail(String id) {

	List<String> array = new ArrayList<>();

	try {
		pStatement = connection.prepareStatement("Select PRODUCTNAME, DESCRIPTION from BB_PRODUCT WHERE IDPRODUCT = ?");
		pStatement.setString(1, id);

		resultSet = pStatement.executeQuery();

		while (resultSet.next()) {
			array.add(resultSet.getObject(1).toString());
			array.add(resultSet.getObject(2).toString());
		}
		return array;
	} catch (SQLException e) {
		e.printStackTrace();
	}

	return array;
}

public void setProductDetail(String id, String des) {


	try {
		
		cStatement = connection.prepareCall("CALL upd_description_sp(? ,?)");
		cStatement.setInt(1, Integer.parseInt(id));
		cStatement.setString(2, des);
		cStatement.executeQuery();

	} catch (SQLException e) {
		e.printStackTrace();
	}


}

public void addToBasket(int basketId, int productId, double price, int quantity, int size, int form) {
try {
		
		cStatement = connection.prepareCall("CALL basket_add_sp(? ,? , ?, ?, ?, ?)");
		cStatement.setInt(1, basketId);
		cStatement.setInt(2, productId);
		cStatement.setDouble(3, price);
		cStatement.setInt(4, quantity);
		cStatement.setInt(5, size);
		cStatement.setInt(6, form);

		cStatement.executeQuery();

	} catch (SQLException e) {
		e.printStackTrace();
	}
}

public String checkOnSales(int productId) throws SQLException {
	pStatement = connection.prepareCall("SELECT ck_sale_sf(? ,?) FROM DUAL");
	pStatement.setInt(1, productId);
	Date now = new Date(Calendar.getInstance().getTimeInMillis());
	pStatement.setDate(2, now);
	resultSet = pStatement.executeQuery();
	resultSet.next();
	return resultSet.getObject(1).toString();
	}


}