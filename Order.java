package PhaseTwo;
public class Order {
	int orderId;
	 Customer customer;
	 LinkedList<Product> products = new LinkedList<>();
	 double totalPrice;
	 String orderDate;
	 String status;

	public Order(int orderId, Customer customer, LinkedList<Product> products, double totalPrice, String orderDate,
			String status) {
		this.orderId = orderId;
		this.customer = customer;
		this.products = products;
		this.totalPrice = totalPrice;
		this.orderDate = orderDate;
		this.status = status;
	}

	public int getOrderId() {
		return orderId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public LinkedList<Product> getProducts() {
		return products;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}