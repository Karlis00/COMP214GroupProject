package application;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class DatabaseData {

	static final String DRIVER = "oracle.jdbc.OracleDriver";
	static final String DATABASE_URL = "jdbc:oracle:thin:@199.212.26.208:1521:SQLD";

	Connection connection = null; // manages connection
	Statement statement = null; // query statement
	CallableStatement cStatement = null; // callable query statement
	ResultSet resultSet = null; // manages results

	public DatabaseData() throws ClassNotFoundException, SQLException {
		Class.forName(DRIVER);
		connection = DriverManager.getConnection(DATABASE_URL, "COMP214_F22_er_53", "password");
		statement = connection.createStatement();
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

	public List<String> getProductList() {

		List<String> array = new ArrayList<>();

		try {
			String query = "Select * from BB_PRODUCT order by 1 desc";
			resultSet = statement.executeQuery(query);
			ResultSetMetaData metaData = resultSet.getMetaData();
			while (resultSet.next()) {
				array.add(resultSet.getObject(2).toString());
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
}
