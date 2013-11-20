package controllers.anonymousDays;

import core.general.anonym.AnonymousDay;

import java.util.List;

public class AnonymousDaysModel {

	private List<AnonymousDay> anonymousDays;
	private int anonymousPeriod;

	public List<AnonymousDay> getAnonymousDays() {
		return anonymousDays;
	}

	public void setAnonymousDays( final List<AnonymousDay> anonymousDays ) {
		this.anonymousDays = anonymousDays;
	}

	public int getAnonymousPeriod() {
		return anonymousPeriod;
	}

	public void setAnonymousPeriod( final int anonymousPeriod ) {
		this.anonymousPeriod = anonymousPeriod;
	}
}
