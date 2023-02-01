package models;

public class Book {
	private String id, title, author, category;
	public int quantity;
	
	public Book(String id, String title, String author, String category, int quantity) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.category = category;
		this.quantity = quantity;
	}

	public Book() {
		id = "Xin chào ";
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	@Override
	public String toString() {
		return "Book [id=" + id + ", title=" + title + ", author=" + author + ", category=" + category + "]";
	}
	
}
