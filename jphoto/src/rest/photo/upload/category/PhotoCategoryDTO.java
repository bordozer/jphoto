package rest.photo.upload.category;

public class PhotoCategoryDTO {

	private int categoryId;
	private String name;

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId( final int categoryId ) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName( final String name ) {
		this.name = name;
	}
}
