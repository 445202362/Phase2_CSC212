package PhaseTwo;

import java.io.*;
import java.util.Scanner;

public class EcommerceSystem {
	// Phase II: AVL Trees as REQUIRED by PDF
	private AVL<Product> products;
	private AVL<Product> productsByPrice;
	private AVL<Customer> customers;
	private AVL<Order> orders;
	private AVL<Review> reviews;

	public EcommerceSystem() {
		products = new AVL<>();
		productsByPrice = new AVL<>();
		customers = new AVL<>();
		orders = new AVL<>();
		reviews = new AVL<>();
	}

	// Getters for the AVL trees
	public AVL<Product> getProducts() {
		return products;
	}

	public AVL<Product> getProductsByPrice() {
		return productsByPrice; // NEW: Getter for price-sorted tree
	}

	public AVL<Customer> getCustomers() {
		return customers;
	}

	public AVL<Order> getOrders() {
		return orders;
	}

	public AVL<Review> getReviews() {
		return reviews;
	}

	// == REVIEW OPERATIONS ==

	// Extract reviews from a specific customer
	public LinkedList<Review> extractReviewsFromCustomer(int customerId) {
		LinkedList<Review> customerReviews = new LinkedList<>();
		inOrderReviewTraversalByCustomer(reviews.getRoot(), customerId, customerReviews);
		return customerReviews;
	}

	private void inOrderReviewTraversalByCustomer(AVLNode<Review> node, int customerId, LinkedList<Review> result) {
		if (node == null)
			return;
		inOrderReviewTraversalByCustomer(node.left, customerId, result);
		if (node.data.getCustomer().getCustomerId() == customerId) {
			result.insert(node.data);
		}
		inOrderReviewTraversalByCustomer(node.right, customerId, result);
	}

	// Add review
	public Review addReview(int reviewId, int productId, int customerId, int rating, String comment) {
		if (rating < 1 || rating > 5) {
			System.out.println("Rating must be between 1 and 5 !");
			return null;
		}

		Product product = searchProductById(productId);
		Customer customer = searchCustomerById(customerId);

		if (product == null || customer == null) {
			System.out.println("Product or Customer not found");
			return null;
		}

		Review review = new Review(reviewId, product, customer, rating, comment);
		reviews.insert(reviewId, review);
		product.addReview(review);

		System.out.println("Review added successfully!");
		return review;
	}

	// Edit review
	public void editReview(int reviewId, int rating, String comment) {
		Review oldReview = reviews.get(reviewId);
		if (oldReview != null) {
			Review updatedReview = new Review(reviewId, oldReview.getProduct(), oldReview.getCustomer(), rating,
					comment);
			reviews.removeKey(reviewId);
			reviews.insert(reviewId, updatedReview);
			System.out.println("Review " + reviewId + " updated successfully!");
		} else {
			System.out.println("Review with ID " + reviewId + " not found!");
		}
	}

	// Get review
	public Review getReview(int customerId, int productId) {
		LinkedList<Review> allReviews = new LinkedList<>();
		inOrderTraversal(reviews.getRoot(), allReviews);

		if (!allReviews.empty()) {
			allReviews.findFirst();
			do {
				Review review = allReviews.retrieve();
				if (review.getCustomer().getCustomerId() == customerId
						&& review.getProduct().getProductId() == productId) {
					return review;
				}
				if (allReviews.last())
					break;
				allReviews.findNext();
			} while (true);
		}
		return null;
	}

	// == CUSTOMER OPERATIONS ==

	// Search a customer id - O(log n) as REQUIRED
	public Customer searchCustomerById(int customerId) {
		return customers.get(customerId);
	}

	// Register customer - O(log n) as REQUIRED
	public Customer registerCustomer(int customerId, String name, String email) {
		if (customers.get(customerId) != null) {
			System.out.println("Customer with ID " + customerId + " already exists!");
			return null;
		}

		Customer newCustomer = new Customer(customerId, name, email);
		customers.insert(customerId, newCustomer);
		System.out.println("Customer '" + name + "' registered successfully!");
		return newCustomer;
	}

	// == PRODUCT OPERATIONS ==

	// Get common 5 star reviews
	public LinkedList<Product> getCommonHighlyRatedProducts(int custId1, int custId2) {
		LinkedList<Product> common = new LinkedList<>();

		// Get 5-star products for customer 1 using traversal
		LinkedList<Integer> cust1Loves = new LinkedList<>();
		LinkedList<Review> allReviews = new LinkedList<>();
		inOrderTraversal(reviews.getRoot(), allReviews);

		if (!allReviews.empty()) {
			allReviews.findFirst();
			do {
				Review r = allReviews.retrieve();
				if (r.getCustomer().getCustomerId() == custId1 && r.getRating() == 5) {
					cust1Loves.insert(r.getProduct().getProductId());
				}
				if (allReviews.last())
					break;
				allReviews.findNext();
			} while (true);
		}

		// Find matches in customer 2 using same traversal
		if (!allReviews.empty()) {
			allReviews.findFirst();
			do {
				Review r = allReviews.retrieve();
				if (r.getCustomer().getCustomerId() == custId2 && r.getRating() == 5) {
					int productId = r.getProduct().getProductId();

					// Check if customer 1 also loved this product
					boolean cust1AlsoLoves = false;
					if (!cust1Loves.empty()) {
						cust1Loves.findFirst();
						do {
							if (cust1Loves.retrieve() == productId) {
								cust1AlsoLoves = true;
								break;
							}
							if (cust1Loves.last())
								break;
							cust1Loves.findNext();
						} while (true);
					}

					// Check if already added
					boolean alreadyAdded = false;
					if (!common.empty()) {
						common.findFirst();
						do {
							if (common.retrieve().getProductId() == productId) {
								alreadyAdded = true;
								break;
							}
							if (common.last())
								break;
							common.findNext();
						} while (true);
					}

					if (cust1AlsoLoves && !alreadyAdded) {
						common.insert(r.getProduct());
					}
				}
				if (allReviews.last())
					break;
				allReviews.findNext();
			} while (true);
		}

		return common;
	}

	// Top three products by number of 5-star ratings
	public LinkedList<Product> getTop3Products() {
		LinkedList<Product> allProducts = products.getAllElementsInOrder();

		if (allProducts.empty())
			return new LinkedList<>();

		// First, collect all products with their 5-star counts in a simple array
		int productCount = allProducts.size();
		Product[] productArray = new Product[productCount];
		int[] fiveStarCounts = new int[productCount];

		// Fill the arrays
		allProducts.findFirst();
		for (int i = 0; i < productCount; i++) {
			productArray[i] = allProducts.retrieve();

			// Count 5-star ratings
			LinkedList<Review> reviews = productArray[i].getReviews();
			if (!reviews.empty()) {
				reviews.findFirst();
				do {
					if (reviews.retrieve().getRating() == 5) {
						fiveStarCounts[i]++;
					}
					if (reviews.last())
						break;
					reviews.findNext();
				} while (true);
			}

			if (!allProducts.last())
				allProducts.findNext();
		}

		// Simple selection of top 3 based on 5-star counts
		Product[] topProducts = new Product[3];

		for (int i = 0; i < productCount; i++) {
			if (fiveStarCounts[i] > 0) {
				for (int pos = 0; pos < 3; pos++) {
					if (topProducts[pos] == null) {
						topProducts[pos] = productArray[i];
						break;
					} else {
						// Find 5-star count for current top product
						int currentTopCount = 0;
						LinkedList<Review> currentReviews = topProducts[pos].getReviews();
						if (!currentReviews.empty()) {
							currentReviews.findFirst();
							do {
								if (currentReviews.retrieve().getRating() == 5) {
									currentTopCount++;
								}
								if (currentReviews.last())
									break;
								currentReviews.findNext();
							} while (true);
						}

						if (fiveStarCounts[i] > currentTopCount) {
							for (int j = 2; j > pos; j--) {
								topProducts[j] = topProducts[j - 1];
							}
							topProducts[pos] = productArray[i];
							break;
						}
					}
				}
			}
		}

		// Build result list
		LinkedList<Product> result = new LinkedList<>();
		for (Product p : topProducts) {
			if (p != null)
				result.insert(p);
		}

		return result;
	}

	// Track out of stock products
	public LinkedList<Product> trackOutOfStockProducts() {
		LinkedList<Product> outOfStock = new LinkedList<>();
		LinkedList<Product> allProducts = new LinkedList<>();
		inOrderTraversal(products.getRoot(), allProducts);

		if (!allProducts.empty()) {
			allProducts.findFirst();
			do {
				Product product = allProducts.retrieve();
				if (product.getStock() <= 0) {
					outOfStock.insert(product);
				}
				if (allProducts.last())
					break;
				allProducts.findNext();
			} while (true);
		}
		return outOfStock;
	}

	// Add product - O(log n) as REQUIRED
	public void addProduct(Product product) {
		products.insert(product.getProductId(), product);
		productsByPrice.insert((int) (product.getPrice() * 100), product);
	}

	// Remove product
	public boolean removeProduct(int productId) {
		Product product = products.get(productId);
		if (product != null) {
			// Remove from both trees
			products.removeKey(productId);
			productsByPrice.removeKey((int) (product.getPrice() * 100));
			return true;
		}
		return false;
	}

	// Update a product - O(log n) as REQUIRED
	public void updateProduct(int productId, String name, double price, int stock) {
		Product product = products.get(productId);
		if (product != null) {
			// Remove from price-sorted tree with old price
			productsByPrice.removeKey((int) (product.getPrice() * 100));

			// Update product details
			product.setName(name);
			product.setPrice(price);
			product.setStock(stock);

			// Re-insert with new price
			productsByPrice.insert((int) (price * 100), product);
		}
	}

	// Search a product id - O(log n) as REQUIRED
	public Product searchProductById(int productId) {
		return products.get(productId);
	}

	// Search product name
	public Product searchProductByName(String name) {
		LinkedList<Product> allProducts = new LinkedList<>();
		inOrderTraversal(products.getRoot(), allProducts);

		if (!allProducts.empty()) {
			allProducts.findFirst();
			do {
				Product product = allProducts.retrieve();
				if (product.getName().equalsIgnoreCase(name)) {
					return product;
				}
				if (allProducts.last())
					break;
				allProducts.findNext();
			} while (true);
		}
		return null;
	}

	// Product average rating by id
	public double getAverageRating(int productId) {
		Product product = searchProductById(productId);
		return (product != null) ? product.getAverageRating() : 0.0;
	}

	// Display available products
	public void displayAvailableProducts() {
		LinkedList<Product> allProducts = new LinkedList<>();
		inOrderTraversal(products.getRoot(), allProducts);

		if (allProducts.empty()) {
			System.out.println("No products available in the system.");
			return;
		}

		System.out.println("\n" + "=".repeat(70));
		System.out.println("               AVAILABLE PRODUCTS (In Stock)");
		System.out.println("=".repeat(70));
		System.out.printf("%-6s%12s    %8s   %-30s\n", "ID", "Price", "Stock", "Product Name");
		System.out.println("-".repeat(70));

		boolean found = false;

		if (!allProducts.empty()) {
			allProducts.findFirst();
			do {
				Product p = allProducts.retrieve();
				if (p.getStock() > 0) {
					found = true;
					String productName = p.getName();
					if (productName.length() > 30) {
						productName = productName.substring(0, 27) + "...";
					}
					System.out.printf("%-6d%12.2f%8d   %-30s\n", p.getProductId(), p.getPrice(), p.getStock(),
							productName);
				}
				if (allProducts.last())
					break;
				allProducts.findNext();
			} while (true);
		}

		System.out.println("=".repeat(70));
		if (!found) {
			System.out.println("No available products found");
		}
	}

	// == ORDER OPERATIONS ==

	// Create order
	public Order createOrder(int orderId, int customerId, LinkedList<Product> orderProducts, String orderDate) {
		Customer customer = searchCustomerById(customerId);
		if (customer == null) {
			System.out.println("Customer not found.");
			return null;
		}

		double totalPrice = 0;
		if (!orderProducts.empty()) {
			orderProducts.findFirst();
			while (!orderProducts.last()) {
				totalPrice += orderProducts.retrieve().getPrice();
				orderProducts.findNext();
			}
			totalPrice += orderProducts.retrieve().getPrice();
		}

		Order newOrder = new Order(orderId, customer, orderProducts, totalPrice, orderDate, "Pending");
		orders.insert(orderId, newOrder);
		customer.addOrder(newOrder);
		return newOrder;
	}

	// Cancel order
	public void cancelOrder(int orderId) {
		Order order = orders.get(orderId);
		if (order != null) {
			order.setStatus("Cancelled");
			System.out.println("Order " + orderId + " cancelled successfully!");
		} else {
			System.out.println("Order with ID " + orderId + " not found!");
		}
	}

	// Update order
	public void updateOrderStatus(int orderId, String newStatus) {
		Order order = orders.get(orderId);
		if (order != null) {
			order.setStatus(newStatus);
			System.out.println("Order " + orderId + " status updated to: " + newStatus);
		} else {
			System.out.println("Order with ID " + orderId + " not found!");
		}
	}

	// Search an order id - O(log n) as REQUIRED
	public Order searchOrderById(int orderId) {
		return orders.get(orderId);
	}

	// Get all orders between two dates
	public LinkedList<Order> getAllOrdersBetweenDates(String startDate, String endDate) {
		LinkedList<Order> ordersInRange = new LinkedList<>();
		LinkedList<Order> allOrders = new LinkedList<>();
		inOrderTraversal(orders.getRoot(), allOrders);

		if (!allOrders.empty()) {
			allOrders.findFirst();
			do {
				Order order = allOrders.retrieve();
				String orderDate = order.getOrderDate();
				if (orderDate.compareTo(startDate) >= 0 && orderDate.compareTo(endDate) <= 0) {
					ordersInRange.insert(order);
				}
				if (allOrders.last())
					break;
				allOrders.findNext();
			} while (true);
		}
		return ordersInRange;
	}

	// == PHASE II ADVANCED QUERIES USING BST/AVL TRAVERSAL ==

	// 1. Find All Orders Between Two Dates (use in-order traversal) - AS REQUIRED
	public LinkedList<Order> getOrdersBetweenDates(String startDate, String endDate) {
		LinkedList<Order> result = new LinkedList<>();
		inOrderDateRange(orders.getRoot(), startDate, endDate, result);
		return result;
	}

	private void inOrderDateRange(AVLNode<Order> node, String startDate, String endDate, LinkedList<Order> result) {
		if (node == null)
			return;

		// In-order traversal as REQUIRED by PDF
		inOrderDateRange(node.left, startDate, endDate, result);

		String orderDate = node.data.getOrderDate();
		if (orderDate.compareTo(startDate) >= 0 && orderDate.compareTo(endDate) <= 0) {
			result.insert(node.data);
		}

		inOrderDateRange(node.right, startDate, endDate, result);
	}

	public LinkedList<Product> getProductsInPriceRange(double minPrice, double maxPrice) {
		LinkedList<Product> result = new LinkedList<>();
		rangeQueryByPrice(productsByPrice.getRoot(), minPrice, maxPrice, result); // CHANGED: uses productsByPrice
		return result;
	}

	private void rangeQueryByPrice(AVLNode<Product> node, double minPrice, double maxPrice,
			LinkedList<Product> result) {
		if (node == null)
			return;

		double currentPrice = node.data.getPrice();

		// If current price > min, check left subtree (lower prices)
		if (currentPrice > minPrice) {
			rangeQueryByPrice(node.left, minPrice, maxPrice, result);
		}

		// If current node is in range, add it
		if (currentPrice >= minPrice && currentPrice <= maxPrice) {
			result.insert(node.data);
		}

		// If current price < max, check right subtree (higher prices)
		if (currentPrice < maxPrice) {
			rangeQueryByPrice(node.right, minPrice, maxPrice, result);
		}
	}

	// 3. Show the Top 3 Most Reviewed Products - USING traversal
	public LinkedList<Product> getTop3MostReviewedProducts() {
		LinkedList<Product> allProducts = new LinkedList<>();
		inOrderTraversal(products.getRoot(), allProducts);

		if (allProducts.empty())
			return new LinkedList<>();

		Product first = null, second = null, third = null;
		int count1 = -1, count2 = -1, count3 = -1;

		allProducts.findFirst();
		do {
			Product product = allProducts.retrieve();
			int reviewCount = product.getReviews().size();

			if (reviewCount > count1) {
				third = second;
				count3 = count2;
				second = first;
				count2 = count1;
				first = product;
				count1 = reviewCount;
			} else if (reviewCount > count2) {
				third = second;
				count3 = count2;
				second = product;
				count2 = reviewCount;
			} else if (reviewCount > count3) {
				third = product;
				count3 = reviewCount;
			}

			if (allProducts.last())
				break;
			allProducts.findNext();
		} while (true);

		LinkedList<Product> topProducts = new LinkedList<>();
		if (first != null)
			topProducts.insert(first);
		if (second != null)
			topProducts.insert(second);
		if (third != null)
			topProducts.insert(third);

		return topProducts;
	}

	// 4. List All Customers Sorted Alphabetically - AS REQUIRED
	public LinkedList<Customer> getCustomersSortedAlphabetically() {
		LinkedList<Customer> allCustomers = new LinkedList<>();
		inOrderTraversal(customers.getRoot(), allCustomers);

		// Since customers are stored by ID, we need to sort by name after traversal
		return sortCustomersByName(allCustomers);
	}

	private LinkedList<Customer> sortCustomersByName(LinkedList<Customer> customersList) {
		if (customersList.empty())
			return new LinkedList<>();

		// Convert to array for sorting
		Customer[] customerArray = new Customer[customersList.size()];
		customersList.findFirst();
		for (int i = 0; i < customerArray.length; i++) {
			customerArray[i] = customersList.retrieve();
			if (!customersList.last())
				customersList.findNext();
		}

		// Bubble sort by name (since we already used in-order traversal as required)
		for (int i = 0; i < customerArray.length - 1; i++) {
			for (int j = 0; j < customerArray.length - i - 1; j++) {
				if (customerArray[j].getName().compareToIgnoreCase(customerArray[j + 1].getName()) > 0) {
					Customer temp = customerArray[j];
					customerArray[j] = customerArray[j + 1];
					customerArray[j + 1] = temp;
				}
			}
		}

		LinkedList<Customer> sorted = new LinkedList<>();
		for (int i = 0; i < customerArray.length; i++) {
			sorted.insert(customerArray[i]);
		}

		return sorted;
	}

	// 5. Given a Product ID, Display All Customers Who Reviewed It - USING
	// traversal
	public LinkedList<Customer> getCustomersWhoReviewedProduct(int productId) {
		LinkedList<Customer> reviewers = new LinkedList<>();
		inOrderReviewTraversal(reviews.getRoot(), productId, reviewers);
		return reviewers;
	}

	private void inOrderReviewTraversal(AVLNode<Review> node, int productId, LinkedList<Customer> reviewers) {
		if (node == null)
			return;

		inOrderReviewTraversal(node.left, productId, reviewers);

		if (node.data.getProduct().getProductId() == productId) {
			Customer customer = node.data.getCustomer();
			if (!containsCustomer(reviewers, customer.getCustomerId())) {
				reviewers.insert(customer);
			}
		}

		inOrderReviewTraversal(node.right, productId, reviewers);
	}

	private boolean containsCustomer(LinkedList<Customer> customers, int customerId) {
		if (customers.empty())
			return false;
		customers.findFirst();
		do {
			if (customers.retrieve().getCustomerId() == customerId) {
				return true;
			}
			if (customers.last())
				break;
			customers.findNext();
		} while (true);
		return false;
	}

	// == DATA LOADING AND SAVING ==

	public void loadAllData() {
		System.out.println("Loading all data...");
		loadCustomers();
		loadProducts();
		loadOrders();
		loadReviews();
		System.out.println("Data loading completed!");
	}

	private void loadCustomers() {
		try (BufferedReader br = new BufferedReader(new FileReader("customers.csv"))) {
			String line = br.readLine(); // Skip header
			int count = 0;

			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");
				if (data.length >= 3) {
					int customerId = Integer.parseInt(data[0].trim());
					String name = data[1].trim();
					String email = data[2].trim();

					Customer customer = new Customer(customerId, name, email);
					customers.insert(customerId, customer);
					count++;
				}
			}
			System.out.println("Loaded " + count + " customers");
		} catch (IOException e) {
			System.err.println("Error loading customers: " + e.getMessage());
		}
	}

	private void loadProducts() {
		try (BufferedReader br = new BufferedReader(new FileReader("products.csv"))) {
			String line = br.readLine(); // Skip header
			int count = 0;

			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");
				if (data.length >= 4) {
					int productId = Integer.parseInt(data[0].trim());
					String name = data[1].trim();
					double price = Double.parseDouble(data[2].trim());
					int stock = Integer.parseInt(data[3].trim());

					Product product = new Product(productId, name, price, stock);
					products.insert(productId, product);
					productsByPrice.insert((int) (price * 100), product);
					count++;
				}
			}
			System.out.println("Loaded " + count + " products");
		} catch (IOException e) {
			System.err.println("Error loading products: " + e.getMessage());
		}
	}

	private void loadOrders() {
		try (BufferedReader br = new BufferedReader(new FileReader("orders.csv"))) {
			String line = br.readLine(); // Skip header
			int count = 0;

			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");
				if (data.length >= 6) {
					int orderId = Integer.parseInt(data[0].trim());
					int customerId = Integer.parseInt(data[1].trim());
					String productIdsStr = data[2].trim().replace("\"", "");
					double totalPrice = Double.parseDouble(data[3].trim());
					String orderDate = data[4].trim();
					String status = data[5].trim();

					Customer customer = searchCustomerById(customerId);
					if (customer != null) {
						LinkedList<Product> orderProducts = new LinkedList<>();
						String[] productIds = productIdsStr.split(";");

						for (String productIdStr : productIds) {
							int productId = Integer.parseInt(productIdStr.trim());
							Product product = searchProductById(productId);
							if (product != null) {
								orderProducts.insert(product);
							}
						}

						Order order = new Order(orderId, customer, orderProducts, totalPrice, orderDate, status);
						orders.insert(orderId, order);
						customer.addOrder(order);
						count++;
					}
				}
			}
			System.out.println("Loaded " + count + " orders");
		} catch (IOException e) {
			System.err.println("Error loading orders: " + e.getMessage());
		}
	}

	private void loadReviews() {
		try (BufferedReader br = new BufferedReader(new FileReader("reviews.csv"))) {
			String line = br.readLine(); // Skip header
			int count = 0;

			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");
				if (data.length >= 5) {
					int reviewId = Integer.parseInt(data[0].trim());
					int productId = Integer.parseInt(data[1].trim());
					int customerId = Integer.parseInt(data[2].trim());
					int rating = Integer.parseInt(data[3].trim());
					String comment = data[4].trim().replace("\"", "");

					Product product = searchProductById(productId);
					Customer customer = searchCustomerById(customerId);

					if (product != null && customer != null) {
						Review review = new Review(reviewId, product, customer, rating, comment);
						reviews.insert(reviewId, review);
						product.addReview(review);
						count++;
					}
				}
			}
			System.out.println("Loaded " + count + " reviews");
		} catch (IOException e) {
			System.err.println("Error loading reviews: " + e.getMessage());
		}
	}

	// == CSV WRITING METHODS ==

	public void saveAllData() {
		System.out.println("Saving all data...");
		saveCustomers();
		saveProducts();
		saveOrders();
		saveReviews();
		System.out.println("Data saving completed!");
	}

	private void saveCustomers() {
		try (PrintWriter pw = new PrintWriter(new FileWriter("customers.csv"))) {
			pw.println("customerId,name,email");

			LinkedList<Customer> allCustomers = new LinkedList<>();
			inOrderTraversal(customers.getRoot(), allCustomers);
			if (!allCustomers.empty()) {
				allCustomers.findFirst();
				do {
					Customer customer = allCustomers.retrieve();
					pw.printf("%d,%s,%s\n", customer.getCustomerId(), customer.getName(), customer.getEmail());
					if (allCustomers.last())
						break;
					allCustomers.findNext();
				} while (true);
			}
			System.out.println("Customers saved successfully!");
		} catch (IOException e) {
			System.err.println("Error saving customers: " + e.getMessage());
		}
	}

	private void saveProducts() {
		try (PrintWriter pw = new PrintWriter(new FileWriter("products.csv"))) {
			pw.println("productId,name,price,stock");

			LinkedList<Product> allProducts = new LinkedList<>();
			inOrderTraversal(products.getRoot(), allProducts);
			if (!allProducts.empty()) {
				allProducts.findFirst();
				do {
					Product product = allProducts.retrieve();
					pw.printf("%d,%s,%.2f,%d\n", product.getProductId(), product.getName(), product.getPrice(),
							product.getStock());
					if (allProducts.last())
						break;
					allProducts.findNext();
				} while (true);
			}
			System.out.println("Products saved successfully!");
		} catch (IOException e) {
			System.err.println("Error saving products: " + e.getMessage());
		}
	}

	private void saveOrders() {
		try (PrintWriter pw = new PrintWriter(new FileWriter("orders.csv"))) {
			pw.println("orderId,customerId,productIds,totalPrice,orderDate,status");

			LinkedList<Order> allOrders = new LinkedList<>();
			inOrderTraversal(orders.getRoot(), allOrders);
			if (!allOrders.empty()) {
				allOrders.findFirst();
				do {
					Order order = allOrders.retrieve();

					StringBuilder productIds = new StringBuilder();
					LinkedList<Product> orderProducts = order.getProducts();

					if (!orderProducts.empty()) {
						orderProducts.findFirst();
						do {
							Product product = orderProducts.retrieve();
							productIds.append(product.getProductId());
							if (!orderProducts.last()) {
								productIds.append(";");
							}
							if (orderProducts.last())
								break;
							orderProducts.findNext();
						} while (true);
					}

					pw.printf("%d,%d,\"%s\",%.2f,%s,%s\n", order.getOrderId(), order.getCustomer().getCustomerId(),
							productIds.toString(), order.getTotalPrice(), order.getOrderDate(), order.getStatus());

					if (allOrders.last())
						break;
					allOrders.findNext();
				} while (true);
			}
			System.out.println("Orders saved successfully!");
		} catch (IOException e) {
			System.err.println("Error saving orders: " + e.getMessage());
		}
	}

	private void saveReviews() {
		try (PrintWriter pw = new PrintWriter(new FileWriter("reviews.csv"))) {
			pw.println("reviewId,productId,customerId,rating,comment");

			LinkedList<Review> allReviews = new LinkedList<>();
			inOrderTraversal(reviews.getRoot(), allReviews);
			if (!allReviews.empty()) {
				allReviews.findFirst();
				do {
					Review review = allReviews.retrieve();
					pw.printf("%d,%d,%d,%d,\"%s\"\n", review.getReviewId(), review.getProduct().getProductId(),
							review.getCustomer().getCustomerId(), review.getRating(), review.getComment());
					if (allReviews.last())
						break;
					allReviews.findNext();
				} while (true);
			}
			System.out.println("Reviews saved successfully!");
		} catch (IOException e) {
			System.err.println("Error saving reviews: " + e.getMessage());
		}
	}

	// == HELPER TRAVERSAL METHODS ==
	private <T> void inOrderTraversal(AVLNode<T> node, LinkedList<T> result) {
		if (node == null)
			return;
		inOrderTraversal(node.left, result);
		result.insert(node.data);
		inOrderTraversal(node.right, result);
	}
}

