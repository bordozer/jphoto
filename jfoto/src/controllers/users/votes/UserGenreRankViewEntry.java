package controllers.users.votes;

public class UserGenreRankViewEntry {

	private String column1;
	private String column2;
	private String column3;

	private boolean statusChangeEntry;

	public String getColumn1() {
		return column1;
	}

	public void setColumn1( final String column1 ) {
		this.column1 = column1;
	}

	public String getColumn2() {
		return column2;
	}

	public void setColumn2( final String column2 ) {
		this.column2 = column2;
	}

	public String getColumn3() {
		return column3;
	}

	public void setColumn3( final String column3 ) {
		this.column3 = column3;
	}

	public boolean isStatusChangeEntry() {
		return statusChangeEntry;
	}

	public void setStatusChangeEntry( final boolean statusChangeEntry ) {
		this.statusChangeEntry = statusChangeEntry;
	}
}
