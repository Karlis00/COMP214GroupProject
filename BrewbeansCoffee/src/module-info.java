module BrewbeansCoffee {
	requires javafx.controls;
	requires java.sql;
	requires javafx.graphics;
	requires javafx.base;
    requires javafx.fxml;
	
	opens application to javafx.graphics, javafx.fxml, javafx.base;

}
