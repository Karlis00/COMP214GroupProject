package application;

public class TableModel {
	private String id;
	private int productId;
	private String name;
	private String quantity;
	private String price;
	private String stock;
	private String img;

	public TableModel() {
		super();
	}

	public TableModel(String id, String name, String quantity, String price, String stock) {
		super();
		this.id = id;
		this.name = name;
		this.quantity = quantity;
		this.price = price;
		this.stock = stock;
	}
	
	public TableModel(int productId, String name, String price, String img) {
		super();
		this.productId = productId;
		this.name = name;
		this.price = price;
		this.img = img;
	}

	public String getId() {
		return id;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

}
