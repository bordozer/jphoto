package rest.portal.page;

public class PortalPageDTO {

	private int id = 1;

	private String weekBegin;
	private String weekEnd;

	private String monthBegin;
	private String monthEnd;

	public int getId() {
		return id;
	}

	public void setId( final int id ) {
		this.id = id;
	}

	public String getWeekBegin() {
		return weekBegin;
	}

	public void setWeekBegin( final String weekBegin ) {
		this.weekBegin = weekBegin;
	}

	public String getWeekEnd() {
		return weekEnd;
	}

	public void setWeekEnd( final String weekEnd ) {
		this.weekEnd = weekEnd;
	}

	public String getMonthBegin() {
		return monthBegin;
	}

	public void setMonthBegin( final String monthBegin ) {
		this.monthBegin = monthBegin;
	}

	public String getMonthEnd() {
		return monthEnd;
	}

	public void setMonthEnd( final String monthEnd ) {
		this.monthEnd = monthEnd;
	}
}
