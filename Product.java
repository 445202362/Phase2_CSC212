package PhaseTwo;



public class Product {
	int productId;
	 String name;
	 double price;
	 int stock;
	 LinkedList<Review> reviews;

	Product(int productId, String name, double price, int stock) {
		this.productId = productId;
		this.name = name;
		this.price = price;
		this.stock = stock;
		this.reviews = new LinkedList<>();
	}

	public void addReview(Review review) {
		if (reviews == null) {
			reviews = new LinkedList<>();
		}
		reviews.insert(review);
	}

	public double getAverageRating() {
		if (reviews == null || reviews.empty())
			return 0.0;

		double sum = 0;
		int count = 0;

		reviews.findFirst();
		while (!reviews.last()) {
			sum += reviews.retrieve().getRating();
			count++;
			reviews.findNext();
		}
		sum += reviews.retrieve().getRating();
		count++;

		return sum / count;
	}

	public int getProductId() {
		return productId;
	}

	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}

	public int getStock() {
		return stock;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public LinkedList<Review> getReviews() {
		return reviews;
	}

	@Override
	public String toString() {
		return String.format("Product{id=%d, name='%s', price=%.2f, stock=%d}", productId, name, price, stock);
	}
}

