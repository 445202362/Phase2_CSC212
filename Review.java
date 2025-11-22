package PhaseTwo;
public class Review {

	int reviewId;
	 Product product;
	 Customer customer;
	int rating;
	String comment;

	public Review(int reviewId, Product product, Customer customer, int rating, String comment) {

		this.reviewId = reviewId;
		this.product = product;
		this.customer = customer;
		this.rating = rating;
		this.comment = comment;
	}

	public int getReviewId() {
		return reviewId;
	}

	public Product getProduct() {
		return product;
	}

	public Customer getCustomer() {
		return customer;
	}

	public int getRating() {
		return rating;
	}

	public String getComment() {
		return comment;
	}

	public String toString() {
		return "Review:\n" + "Review ID = " + reviewId + "\n" + "Product ID = " + product.getProductId() + "\n"
				+ "Customer ID = " + customer.getCustomerId() + "\n" + "Rating = " + rating + "\n" + "Comment = "
				+ comment;
	}
}