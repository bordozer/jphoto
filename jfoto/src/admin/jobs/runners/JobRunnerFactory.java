package admin.jobs.runners;

import admin.jobs.enums.JobRunMode;

public class JobRunnerFactory {

	public AbstractJobRunner getInstance( final JobRunMode jobRunMode ) {
		switch ( jobRunMode ) {
			case SERIALLY:
				return new InSeriesJobRunner();
			case PARALLEL:
				return new AsSeparateThreadJobRunner();
		}

		throw new IllegalArgumentException( String.format( "Unsupported JobRunMode: %s", jobRunMode ) );
	}
}
