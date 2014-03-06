package admin.controllers.reports.users;

public class UserRegistrationData {

	private int users;

	public UserRegistrationData( final int users ) {
		this.users = users;
	}

	public int getUsers() {
		return users;
	}

	public void setUsers( final int users ) {
		this.users = users;
	}


	public void increaseUsersCount() {
		users++;
	}

	@Override
	public String toString() {
		return String.format( "%d", users );
	}
}
