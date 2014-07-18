package rest.photo.upload.userTeam;

public class TeamMemberTypeDTO {

	private int id;
	private String name;

	public TeamMemberTypeDTO() {
	}

	public TeamMemberTypeDTO( final int id, final String name ) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId( final int id ) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName( final String name ) {
		this.name = name;
	}
}
