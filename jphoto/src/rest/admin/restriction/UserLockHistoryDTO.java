package rest.admin.restriction;

public class UserLockHistoryDTO {

	private int id;

	public UserLockHistoryDTO( final int id ) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId( final int id ) {
		this.id = id;
	}
}
