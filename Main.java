package PhaseTwo;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		EcommerceSystem amazon = new EcommerceSystem();
		System.out.println("╭───────────────────────────────╮\r\n" + "│ 	  Welcome to Amazon !    	    │\r\n"
				+ "╰───────────────────────────────╯");
		System.out.println("One moment, we're getting things ready...");
		amazon.loadAllData();
		System.out.println("Data loaded successfully!\n");
		int choice;

		do {

			System.out.println("\n- MAIN MENU -");
			System.out.println("1. Product menu");
			System.out.println("2. Customer menu");
			System.out.println("3. Order menu");
			System.out.println("4. Exit");
	

			choice = scanner.nextInt();
			scanner.nextLine();
			switch (choice) {
			case 1:
				int choice2;
				do {
				    System.out.println("\n- PRODUCT MENU -");
				    System.out.println("1. Add New Product to the stock");
				    System.out.println("2. Remove a product");
				    System.out.println("3. Update Product Stock");
				    System.out.println("4. Search product by ID or Name");
				    System.out.println("5. Display Top 3 Highest Rated Products (by average rating)");
				    System.out.println("6. Display Top 3 Most Reviewed Products (by number of reviews)");
				    System.out.println("7. Display the Common products between 2 customers");
				    System.out.println("8. View Out of Stock Products");
				    System.out.println("9. Products in Price Range (Phase II: AVL Range Query)");
				    System.out.println("10. Back to Main Menu");
				    System.out.print("Enter choice (1-10): ");

				    choice2 = scanner.nextInt();
				    scanner.nextLine();

				    switch (choice2) {
				    case 1:
				        // Add New Product
				        System.out.println("\n*** ADD NEW PRODUCT ***");
				        
				        System.out.print("Enter product name: ");
				        String productName = scanner.nextLine();

				        System.out.print("Enter product price: ");
				        double productPrice = scanner.nextDouble();
				        scanner.nextLine();

				        System.out.print("Enter initial stock: ");
				        int initialStock = scanner.nextInt();
				        scanner.nextLine();
				        
				        // Generate new ID using AVL tree
				        LinkedList<Product> allProducts = amazon.getProducts().getAllElementsInOrder();
				        int newProductId;
				        if (allProducts.empty()) {
				            newProductId = 1;
				        } else {
				            allProducts.findFirst();
				            while (!allProducts.last()) allProducts.findNext();
				            newProductId = allProducts.retrieve().getProductId() + 1;
				        }
				        
				        Product newProduct = new Product(newProductId, productName, productPrice, initialStock);
				        amazon.addProduct(newProduct);
				        System.out.println(" Product added successfully!");
				        System.out.println(" Product ID is : "+newProduct.getProductId() );
				        break;
				        
				    case 2:
				        System.out.println("\n*** REMOVE PRODUCT ***");
				        System.out.print("Enter product ID to remove: ");
				        int removeProductId = scanner.nextInt();
				        scanner.nextLine();

				        if (amazon.removeProduct(removeProductId)) {
				            System.out.println(" Product removed successfully!");
				        } else {
				            System.out.println(" Product not found with ID: " + removeProductId);
				        }
				        break;
				        
				    case 3:
				        System.out.println("\n*** UPDATE PRODUCT STOCK ***");
				        System.out.print("Enter product ID: ");
				        int updateProductId = scanner.nextInt();
				        scanner.nextLine();

				        Product productToUpdate = amazon.searchProductById(updateProductId);
				        if (productToUpdate != null) {
				            System.out.print("Enter new stock quantity: ");
				            int newStock = scanner.nextInt();
				            scanner.nextLine();
				            productToUpdate.setStock(newStock);
				            System.out.println(" Stock updated successfully!");
				        } else {
				            System.out.println(" Product not found!");
				        }
				        break;
				        
				    case 4:
				        // Search Product by ID or Name
				        System.out.println("\n*** SEARCH PRODUCT ***");
				        System.out.println("1. Search by ID (Phase II: Logarithmic O(log n) Search)");
				        System.out.println("2. Search by Name");
				        System.out.print("Choose search method (1-2): ");
				        int searchMethod = scanner.nextInt();
				        scanner.nextLine();

				        if (searchMethod == 1) {
				            // Search by ID
				            System.out.print("Enter product ID: ");
				            
				            if (!scanner.hasNextInt()) {
				                System.out.println("Error: Please enter a valid numeric ID!");
				                scanner.nextLine();
				                break;
				            }
				            
				            int searchId = scanner.nextInt();
				            scanner.nextLine();
				            
				            if (searchId <= 0) {
				                System.out.println("Error: Product ID must be a positive number!");
				                break;
				            }
				            
				            Product foundProduct = amazon.searchProductById(searchId);
				            if (foundProduct != null) {
				                System.out.println("\nProduct Found:");
				                System.out.println("ID: " + foundProduct.getProductId());
				                System.out.println("Name: " + foundProduct.getName());
				                System.out.println("Price: $" + foundProduct.getPrice());
				                System.out.println("Stock: " + foundProduct.getStock());
				                
				                // Simple rating display
				                double avgRating = foundProduct.getAverageRating();
				                if (avgRating > 0) {
				                    System.out.println("Rating: " + avgRating + "/5.0");
				                }
				                
				                // Simple stock status
				                if (foundProduct.getStock() > 0) {
				                    System.out.println("Status: In Stock");
				                } else {
				                    System.out.println("Status: Out of Stock");
				                }
				                
				            } else {
				                System.out.println("Product not found with ID: " + searchId);
				            }
				            
				        } else if (searchMethod == 2) {
				            // Search by Name
				            System.out.print("Enter product name: ");
				            String searchName = scanner.nextLine().trim();
				            
				            if (searchName.isEmpty()) {
				                System.out.println("Error: Product name cannot be empty!");
				                break;
				            }
				            
				            Product p = amazon.searchProductByName(searchName);
				            if (p != null) {
				                System.out.println("\nProduct Found:");
				                System.out.println("ID: " + p.getProductId());
				                System.out.println("Name: " + p.getName());
				                System.out.println("Price: $" + p.getPrice());
				                System.out.println("Stock: " + p.getStock());
				                
				                double avgRating = p.getAverageRating();
				                if (avgRating > 0) {
				                    System.out.println("Rating: " + avgRating + "/5.0");
				                }
				                
				                if (p.getStock() > 0) {
				                    System.out.println("Status: In Stock");
				                } else {
				                    System.out.println("Status: Out of Stock");
				                }
				                
				            } else {
				                System.out.println("Product not found with name: '" + searchName + "'");
				            }
				            
				        } else {
				            System.out.println("Invalid choice! Please enter 1 or 2.");
				        }
				        break;
				        
				    case 5:
				        // Display Top 3 Highest Rated Products (by average rating)
				        System.out.println("\n*** TOP 3 HIGHEST RATED PRODUCTS ***");
				        LinkedList<Product> topRatedProducts = amazon.getTop3Products();

				        if (topRatedProducts.empty()) {
				            System.out.println("No products available or no products with ratings.");
				        } else {
				            System.out.println(" TOP 3 HIGHEST RATED PRODUCTS (by average rating):");
				            topRatedProducts.findFirst();
				            int rank = 1;

				            while (topRatedProducts.retrieve() != null) {
				                Product product = topRatedProducts.retrieve();
				                double avgRating = product.getAverageRating();
				                int reviewCount = product.getReviews().size();

				                System.out.printf("%d. %s - Rating: %.2f/5.0", rank, product.getName(), avgRating);

				                if (reviewCount > 0) {
				                    System.out.printf(" (Based on %d review%s)%n", reviewCount,
				                            reviewCount == 1 ? "" : "s");
				                } else {
				                    System.out.println(" (No reviews yet)");
				                }

				                // Show product details
				                System.out.printf("   ID: %d | Price: $%.2f | Stock: %d%n", product.getProductId(),
				                        product.getPrice(), product.getStock());

				                rank++;
				                if (!topRatedProducts.last()) {
				                    topRatedProducts.findNext();
				                } else {
				                    break;
				                }
				            }
				        }
				        break;
				        
				    case 6:
				        // Display Top 3 Most Reviewed Products (by number of reviews)
				        System.out.println("\n*** TOP 3 MOST REVIEWED PRODUCTS ***");
				        LinkedList<Product> mostReviewedProducts = amazon.getTop3MostReviewedProducts();

				        if (mostReviewedProducts.empty()) {
				            System.out.println("No products available.");
				        } else {
				            System.out.println(" TOP 3 MOST REVIEWED PRODUCTS (by number of reviews):");
				            mostReviewedProducts.findFirst();
				            int rank = 1;

				            while (mostReviewedProducts.retrieve() != null) {
				                Product product = mostReviewedProducts.retrieve();
				                int reviewCount = product.getReviews().size();
				                double avgRating = product.getAverageRating();

				                System.out.printf("%d. %s - %d review%s", rank, product.getName(), reviewCount,
				                        reviewCount == 1 ? "" : "s");

				                if (reviewCount > 0) {
				                    System.out.printf(" (Average Rating: %.2f/5.0)%n", avgRating);
				                } else {
				                    System.out.println(" (No ratings yet)");
				                }

				                System.out.printf("   ID: %d | Price: $%.2f | Stock: %d%n", product.getProductId(),
				                        product.getPrice(), product.getStock());

				                rank++;
				                if (!mostReviewedProducts.last()) {
				                    mostReviewedProducts.findNext();
				                } else {
				                    break;
				                }
				            }
				        }
				        break;
				        
				    case 7:
				        // Common Products Between 2 Customers
				        System.out.println("\n*** COMMON HIGHLY RATED PRODUCTS ***");
				        System.out.print("Enter first customer ID: ");
				        int custId1 = scanner.nextInt();
				        scanner.nextLine();

				        System.out.print("Enter second customer ID: ");
				        int custId2 = scanner.nextInt();
				        scanner.nextLine();

				        // Input validation
				        if (custId1 < 0 || custId2 < 0) {
				            System.out.println("Error: Customer IDs cannot be negative.");
				            break;
				        }

				        // Call your method that returns LinkedList<Product>
				        LinkedList<Product> commonProducts = amazon.getCommonHighlyRatedProducts(custId1, custId2);

				        if (commonProducts.empty()) {
				            System.out.println("No common 5-star products found between these customers.");
				        } else {
				            System.out.println("\nCOMMON 5-STAR PRODUCTS:");
				            commonProducts.findFirst();
				            int count = 1;

				            // Safer loop condition
				            while (true) {
				                Product product = commonProducts.retrieve();

				                System.out.printf("%d. %s%n", count, product.getName());
				                System.out.println("   Both customers rated: 5.0/5.0");
				                System.out.printf("   ID: %d | Price: $%.2f | Stock: %d%n", 
				                        product.getProductId(), product.getPrice(), product.getStock());
				                System.out.println();

				                count++;
				                
				                // Break condition at the start of potential next iteration
				                if (commonProducts.last()) break;
				                commonProducts.findNext();
				            }

				            System.out.printf("Found %d common 5-star product%s%n", count - 1,
				                    (count - 1) == 1 ? "" : "s");
				        }
				        break;
				        
				    case 8:
				        // View Out of Stock Products
				        System.out.println("\n*** OUT OF STOCK PRODUCTS ***");
				        LinkedList<Product> outOfStockProducts = amazon.trackOutOfStockProducts();
				        
				        if (outOfStockProducts.empty()) {
				            System.out.println("All products are in stock!");
				        } else {
				            System.out.println("Out of Stock Products:");
				            outOfStockProducts.findFirst();
				            int count = 1;

				            while (true) {
				                Product product = outOfStockProducts.retrieve();
				                System.out.printf("%d. %s (ID: %d) - Price: $%.2f%n",
				                    count, product.getName(), product.getProductId(), product.getPrice());
				                count++;
				                
				                if (outOfStockProducts.last()) break;
				                outOfStockProducts.findNext();
				            }
				        }
				        break;
				        
				    case 9:
				        // Products in Price Range (Phase II)
				        System.out.println("\n*** PRODUCTS IN PRICE RANGE (Phase II: AVL Range Query) ***");
				        System.out.print("Enter minimum price: ");
				        double minPrice = scanner.nextDouble();
				        System.out.print("Enter maximum price: ");
				        double maxPrice = scanner.nextDouble();
				        scanner.nextLine();

				        // Input validation
				        if (minPrice < 0 || maxPrice < 0) {
				            System.out.println("Error: Prices cannot be negative.");
				            break;
				        }
				        if (minPrice > maxPrice) {
				            System.out.println("Error: Minimum price cannot be greater than maximum price.");
				            break;
				        }

				        LinkedList<Product> productsInRange = amazon.getProductsInPriceRange(minPrice, maxPrice);
				        if (productsInRange.empty()) {
				            System.out.println("No products found in range $" + minPrice + " - $" + maxPrice);
				        } else {
				            System.out.println("\nProducts in price range $" + minPrice + " - $" + maxPrice + ":");
				            productsInRange.findFirst();
				            int count = 1;

				            while (true) {
				                Product product = productsInRange.retrieve();
				                System.out.printf("%d. %s - $%.2f (ID: %d, Stock: %d)%n",
				                    count, product.getName(), product.getPrice(), product.getProductId(), product.getStock());
				                count++;
				                
				                if (productsInRange.last()) break;
				                productsInRange.findNext();
				            }
				            System.out.println("Total products found: " + (count - 1));
				        }
				        break;
				        
				    case 10:
				        System.out.println("Returning to main menu...");
				        break;
				        
				    default:
				        System.out.println("Invalid choice!");
				    }

				} while (choice2 != 10);
				break;
			case 2:
				int choice3;

				do {
					System.out.println("\n- CUSTOMER MENU -");
					System.out.println("1. Register Customer");
					System.out.println("2. View Customer Reviews");
					System.out.println("3. Add review");
					System.out.println("4. Edit review");
					System.out.println("5. Customers Sorted Alphabetically (Phase II)");
					System.out.println("6. Customers Who Reviewed Product (Phase II)");
					System.out.println("7. Back to Main Menu");
					System.out.print("Enter choice (1-7): ");

					choice3 = scanner.nextInt();
					scanner.nextLine();

					switch (choice3) {
					case 1:
						// Register Customer
						System.out.println("\n*** REGISTER CUSTOMER ***");
						
						System.out.print("Enter customer name: ");
						String customerName = scanner.nextLine();

						System.out.print("Enter customer email: ");
						String customerEmail = scanner.nextLine();
						
						// Generate new customer ID
						LinkedList<Customer> allCustomers = amazon.getCustomers().getAllElementsInOrder();
						int customerId;
						if (allCustomers.empty()) {
						    customerId = 1;
						} else {
						    allCustomers.findFirst();
						    while (!allCustomers.last()) allCustomers.findNext();
						    customerId = allCustomers.retrieve().getCustomerId() + 1;
						}
						
						Customer newCustomer = new Customer(customerId, customerName, customerEmail);
						amazon.registerCustomer(customerId, customerName, customerEmail);
						System.out.println(" Your ID is: " + newCustomer.getCustomerId());
						break;

					case 2:
						// View Customer Reviews
						System.out.println("\n*** VIEW CUSTOMER REVIEWS ***");
						System.out.print("Enter customer ID: ");
						int viewCustomerId = scanner.nextInt();
						scanner.nextLine();

						Customer customer = amazon.searchCustomerById(viewCustomerId);
						if (customer != null) {
							LinkedList<Review> customerReviews = amazon.extractReviewsFromCustomer(viewCustomerId);

							if (customerReviews.empty()) {
								System.out.println(" No reviews found for this customer.");
							} else {
								System.out.println("\n REVIEWS BY CUSTOMER: " + customer.getName());
								customerReviews.findFirst();
								int reviewCount = 1;

								while (customerReviews.retrieve() != null) {
									Review review = customerReviews.retrieve();
									Product reviewedProduct = amazon
											.searchProductById(review.getProduct().getProductId());

									String productName = "Unknown Product";
									if (reviewedProduct != null) {
										productName = reviewedProduct.getName();
									}

									System.out.printf("%d. Product: %s (ID: %d)%n", reviewCount, productName,
											review.getProduct().getProductId());
									System.out.printf("   Rating: %d/5%n", review.getRating());
									System.out.printf("   Comment: %s%n", review.getComment());
									System.out.println();

									reviewCount++;
									if (!customerReviews.last()) {
										customerReviews.findNext();
									} else {
										break;
									}
								}
							}
						} else {
							System.out.println(" Customer not found with ID: " + viewCustomerId);
						}
						break;

					case 3:
						// Add Review
						System.out.println("\n*** ADD REVIEW ***");

						// verify customer exists
						System.out.print("Enter customer ID: ");
						int reviewCustomerId = scanner.nextInt();
						scanner.nextLine();

						Customer reviewCustomer = amazon.searchCustomerById(reviewCustomerId);
						if (reviewCustomer == null) {
							System.out.println(" Customer not found with ID: " + reviewCustomerId);
							break;
						}
						// verify product exists
						System.out.print("Enter product ID: ");
						int reviewProductId = scanner.nextInt();
						scanner.nextLine();

						Product reviewProduct = amazon.searchProductById(reviewProductId);
						if (reviewProduct == null) {
							System.out.println(" Product not found with ID: " + reviewProductId);
							break;
						}
						// get rating between 1 and 5
						System.out.print("Enter rating (1-5): ");
						int rating = scanner.nextInt();
						scanner.nextLine();

						if (rating < 1 || rating > 5) {
							System.out.println(" Invalid rating! Please enter a rating between 1 and 5");
							break;
						}
						// get comment
						System.out.print("Enter review comment: ");
						String comment = scanner.nextLine();

						// Check if customer has reviewed this product before
						LinkedList<Review> customerReviews = amazon.extractReviewsFromCustomer(reviewCustomerId);
						boolean alreadyReviewed = false;

						if (!customerReviews.empty()) {
							customerReviews.findFirst();
							while (customerReviews.retrieve() != null) {
								Review existingReview = customerReviews.retrieve();
								if (existingReview.getProduct().getProductId() == reviewProductId) {
									alreadyReviewed = true;
									break;
								}
								if (!customerReviews.last()) {
									customerReviews.findNext();
								} else {
									break;
								}
							}
						}

						if (alreadyReviewed) {
							System.out.println(" You have already reviewed this product!");
							break;
						}

						// Generate new review ID
						LinkedList<Review> allReviews = amazon.getReviews().getAllElementsInOrder();
						int reviewId;
						if (allReviews.empty()) {
						    reviewId = 1;
						} else {
						    allReviews.findFirst();
						    while (!allReviews.last()) allReviews.findNext();
						    reviewId = allReviews.retrieve().getReviewId() + 1;
						}

						amazon.addReview(reviewId, reviewProductId, reviewCustomerId, rating, comment);
						System.out.println(" Review added successfully!");
						System.out.println(" Your Review ID Is :" + reviewId);
						break;

					case 4:
						// Edit Review
						System.out.println("\n*** EDIT REVIEW ***");
						System.out.print("Enter customer ID: ");
						int editCustomerId = scanner.nextInt();
						scanner.nextLine();

						Customer editCustomer = amazon.searchCustomerById(editCustomerId);
						if (editCustomer == null) {
							System.out.println(" Customer not found with ID: " + editCustomerId);
							break;
						}

						System.out.print("Enter product ID: ");
						int editProductId = scanner.nextInt();
						scanner.nextLine();

						Product editProduct = amazon.searchProductById(editProductId);
						if (editProduct == null) {
							System.out.println(" Product not found with ID: " + editProductId);
							break;
						}

						Review existingReview = amazon.getReview(editCustomerId, editProductId);
						if (existingReview == null) {
							System.out.println(" No review found for this product by this customer.");
							break;
						}

						System.out.println("Current Review:");
						System.out.printf("Product: %s%n", editProduct.getName());
						System.out.printf("Current Rating: %d/5%n", existingReview.getRating());
						System.out.printf("Current Comment: %s%n", existingReview.getComment());

						System.out.print("Enter new rating (1-5): ");
						int newRating = scanner.nextInt();
						scanner.nextLine();

						if (newRating < 1 || newRating > 5) {
							System.out.println(" Invalid rating! Please enter a rating between 1 and 5");
							break;
						}

						System.out.print("Enter new comment: ");
						String newComment = scanner.nextLine();

						amazon.editReview(existingReview.getReviewId(), newRating, newComment);
						System.out.println(" Review updated successfully!");
						break;

					case 5:
						// Customers Sorted Alphabetically (Phase II)
						System.out.println("\n*** CUSTOMERS SORTED ALPHABETICALLY (Phase II) ***");
						LinkedList<Customer> sortedCustomers = amazon.getCustomersSortedAlphabetically();

						if (sortedCustomers.empty()) {
							System.out.println("No customers available.");
						} else {
							System.out.println("CUSTOMERS (ALPHABETICAL ORDER):");
							sortedCustomers.findFirst();
							int count = 1;

							while (true) {
								Customer cust = sortedCustomers.retrieve();
								System.out.printf("%d. %s (ID: %d, Email: %s)%n",
									count, cust.getName(), cust.getCustomerId(), cust.getEmail());
								count++;
								
								if (sortedCustomers.last()) break;
								sortedCustomers.findNext();
							}
						}
						break;

					case 6:
						// Customers Who Reviewed Product (Phase II)
						System.out.println("\n*** CUSTOMERS WHO REVIEWED PRODUCT (Phase II) ***");
						System.out.print("Enter product ID: ");
						int productId = scanner.nextInt();
						scanner.nextLine();

						LinkedList<Customer> reviewers = amazon.getCustomersWhoReviewedProduct(productId);
						if (reviewers.empty()) {
							System.out.println("No customers have reviewed this product.");
						} else {
							Product product = amazon.searchProductById(productId);
							String productName = (product != null) ? product.getName() : "Unknown Product";
							
							System.out.println("\nCUSTOMERS WHO REVIEWED: " + productName);
							reviewers.findFirst();
							int count = 1;

							while (true) {
								Customer cust = reviewers.retrieve();
								System.out.printf("%d. %s (ID: %d, Email: %s)%n",
									count, cust.getName(), cust.getCustomerId(), cust.getEmail());
								count++;
								
								if (reviewers.last()) break;
								reviewers.findNext();
							}
						}
						break;

					case 7:
						System.out.println("Returning to main menu...");
						break;
					default:
						System.out.println("Invalid choice!");
					}

				} while (choice3 != 7);
				break;
			case 3:
				int choice4;

				do {
					System.out.println("\n- ORDER MENU -");
					System.out.println("1. Place an Order");
					System.out.println("2. Cancel Order");
					System.out.println("3. Update Order Status");
					System.out.println("4. Search Order by ID");
					System.out.println("5. Display All Orders between two dates (Phase II: In-order Traversal)");
					System.out.println("6. Back to main menu");

					System.out.print("Enter choice (1-6): ");

					choice4 = scanner.nextInt();

					switch (choice4) {

					case 1: // Place an Order

						System.out.println("\nAvailable Products:");
						amazon.displayAvailableProducts();

						System.out.print("Enter Customer ID: ");
						int customerId = scanner.nextInt();

						LinkedList<Product> selectedProducts = new LinkedList<>();
						String more;
						do {
							System.out.print("Enter Product ID to add to order: ");
							int prodId = scanner.nextInt();
							Product selected = amazon.searchProductById(prodId);
							if (selected != null && selected.getStock() > 0) {
								selectedProducts.insert(selected);
								System.out.println("Product added.");
							} else {
								System.out.println("Product not found or out of stock.");
							}
							System.out.print("Add another product? (yes/no): ");
							more = scanner.next().toLowerCase();
						} while (more.equals("yes"));

						System.out.print("Enter Order Date (yyyy-MM-dd): ");
						String orderDate = scanner.next();
						
						// Generate new order ID
						LinkedList<Order> allOrders = amazon.getOrders().getAllElementsInOrder();
						int orderId;
						if (allOrders.empty()) {
						    orderId = 1;
						} else {
						    allOrders.findFirst();
						    while (!allOrders.last()) allOrders.findNext();
						    orderId = allOrders.retrieve().getOrderId() + 1;
						}
						
						Order newOrder = amazon.createOrder(orderId, customerId, selectedProducts, orderDate);
						if (newOrder != null) {
							System.out.println(" Order placed successfully!");
						
							System.out.println("Order Summary:");
							System.out.println("Order ID: " + newOrder.getOrderId());
							System.out.println("Customer: " + newOrder.getCustomer().getName());
							System.out.println("Date: " + newOrder.getOrderDate());
							System.out.println("Status: " + newOrder.getStatus());
							System.out.println("Total Price: $" + newOrder.getTotalPrice());
							System.out.println("Products:");
							LinkedList<Product> prods = newOrder.getProducts();
							if (!prods.empty()) {
								prods.findFirst();
								while (!prods.last()) {
									Product p = prods.retrieve();
									System.out.println("- " + p.getName() + " ($" + p.getPrice() + ")");
									prods.findNext();
								}
								Product last = prods.retrieve();
								System.out.println("- " + last.getName() + " ($" + last.getPrice() + ")");
							}
						} else {
							System.out.println(" Failed to place order.");
						}
						break;

					case 2: // Cancel Order
						System.out.print("Enter Order ID to cancel: ");
						int cancelId = scanner.nextInt();
						amazon.cancelOrder(cancelId);
						break;

					case 3: // Update Order Status
						System.out.print("Enter Order ID to update: ");
						int orderIdToUpdate = scanner.nextInt();
						scanner.nextLine();

						System.out.print("Enter new status (e.g., Pending, Shipped, Delivered, Cancelled): ");
						String newStatus = scanner.nextLine();

						amazon.updateOrderStatus(orderIdToUpdate, newStatus);
						break;

					case 4: // Search Order by ID
						System.out.print("Enter Order ID to search: ");
						int searchId = scanner.nextInt();

						Order foundOrder = amazon.searchOrderById(searchId);
						if (foundOrder != null) {
							System.out.println("\nOrder Found:");
							System.out.println("Order ID: " + foundOrder.getOrderId());
							System.out.println("Customer: " + foundOrder.getCustomer().getName());
							System.out.println("Date: " + foundOrder.getOrderDate());
							System.out.println("Status: " + foundOrder.getStatus());
							System.out.println("Total Price: $" + foundOrder.getTotalPrice());
							System.out.println("Products:");
							LinkedList<Product> prods = foundOrder.getProducts();
							if (!prods.empty()) {
								prods.findFirst();
								while (!prods.last()) {
									Product p = prods.retrieve();
									System.out.println("- " + p.getName() + " ($" + p.getPrice() + ")");
									prods.findNext();
								}
								Product last = prods.retrieve();
								System.out.println("- " + last.getName() + " ($" + last.getPrice() + ")");
							}
						} else {
							System.out.println("Order not found.");
						}
						break;

					case 5: // Display All Orders Between Two Dates (Phase II)
						System.out.print("Enter start date (yyyy-MM-dd): ");
						String startDate = scanner.next();
						System.out.print("Enter end date (yyyy-MM-dd): ");
						String endDate = scanner.next();

						LinkedList<Order> ordersInRange = amazon.getOrdersBetweenDates(startDate, endDate);
						if (!ordersInRange.empty()) {
							System.out.println("\nOrders between " + startDate + " and " + endDate + ":");
							ordersInRange.findFirst();
							while (!ordersInRange.last()) {
								Order o = ordersInRange.retrieve();
								System.out.println("Order ID: " + o.getOrderId() + " | Date: " + o.getOrderDate()
										+ " | Status: " + o.getStatus() + " | Total: $" + o.getTotalPrice());
								ordersInRange.findNext();
							}
							Order last = ordersInRange.retrieve();
							System.out.println("Order ID: " + last.getOrderId() + " | Date: " + last.getOrderDate()
									+ " | Status: " + last.getStatus() + " | Total: $" + last.getTotalPrice());
						} else {
							System.out.println("No orders found in this range.");
						}
						break;

					case 6:
						System.out.println("Returning to main menu...");
						break;
					default:
						System.out.println("Invalid choice!");
					}

				} while (choice4 != 6);
				break;
			
			case 4:
				 System.out.println("Saving data before exit...");
				    amazon.saveAllData();
				System.out.println("Goodbye!");
				break;
			default:
				System.out.println("Invalid choice! Try again.");
			}

		} while (choice != 4);

	}
}