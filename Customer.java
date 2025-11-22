package PhaseTwo;
public class Customer {

	int customerId;
	 String name;
	 String email;
	 LinkedList<Order> orders;

	public Customer(int customerId, String name, String email) {

		this.customerId = customerId;
		this.name = name;
		this.email = email;
		this.orders = new LinkedList<Order>(); // initialize an empty list

	}

	public void addOrder(Order order) {
		if (order != null)
			orders.insert(order);
	
	}

	public LinkedList<Order> viewOrderHistory() {
		return this.orders;
	}

	public int getCustomerId() {
		return customerId;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public LinkedList<Order> getOrders() {
		return orders;
	}

}