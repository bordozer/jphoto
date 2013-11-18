package core.general.data;

import java.util.Date;

public class TimeRange {

	private Date timeFrom;
	private Date timeTo;

	public TimeRange( final Date timeFrom, final Date timeTo ) {
		this.timeFrom = timeFrom;
		this.timeTo = timeTo;
	}

	public Date getTimeFrom() {
		return timeFrom;
	}

	public void setTimeFrom( final Date timeFrom ) {
		this.timeFrom = timeFrom;
	}

	public Date getTimeTo() {
		return timeTo;
	}

	public void setTimeTo( final Date timeTo ) {
		this.timeTo = timeTo;
	}
}
