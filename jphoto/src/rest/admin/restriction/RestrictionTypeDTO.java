package rest.admin.restriction;

public class RestrictionTypeDTO {

	private final int id;
	private final String name;

	public RestrictionTypeDTO( final int id, final String name ) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
