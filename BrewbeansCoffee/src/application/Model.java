package application;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class Model {

	static final String DRIVER = "oracle.jdbc.OracleDriver";
	static final String DATABASE_URL = "jdbc:oracle:thin:@199.212.26.208:1521:SQLD";

	Connection connection = null; // manages connection
	Statement statement = null; // query statement
	CallableStatement cStatement = null; // callable query statement
	ResultSet resultSet = null; // manages results

	public Model() throws ClassNotFoundException, SQLException {
		Class.forName(DRIVER);
		connection = DriverManager.getConnection(DATABASE_URL, "COMP214_F22_er_53", "password");
		statement = connection.createStatement();
	}

	public List<String> getTaxList() {
		try {
			List<String> array = new ArrayList<>();
			String query = "Select * from bb_tax order by 1 desc";
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				array.add(resultSet.getString(2));
			}
			return array;
		}

		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Double getTaxCost(String state, Double subtotal) {
		try {
			cStatement = connection.prepareCall("CALL tax_cost_sp(?, ?, ?)");
			cStatement.setString(1, state);
			cStatement.setDouble(2, subtotal);
			cStatement.registerOutParameter(3, Types.DOUBLE);
			cStatement.executeQuery();
			return cStatement.getDouble(3);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0.0;
	}

	public void addProduct(String productName, String productDescription, String filename, Double price,
			Integer status) {
		try {
			cStatement = connection.prepareCall("CALL prod_add_sp(?, ?, ?, ?, ?)");
			cStatement.setString(1, productName);
			cStatement.setString(2, productDescription);
			cStatement.setString(3, filename);
			cStatement.setDouble(4, price);
			cStatement.setInt(5, status);
			cStatement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
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

		return null;
	}
	
	public void updateProduct (String basketId, String date, String shipper, String shipNumber) {
		try {
		cStatement = connection.prepareCall("CALL status_ship_sp(?, ?, ?, ?)");
		cStatement.setString(1, basketId);
		cStatement.setString(2, date);
		cStatement.setString(3, shipper);
		cStatement.setString(4, shipNumber);
		cStatement.executeQuery();
	} catch (SQLException e) {
		e.printStackTrace();
	}
	}
}