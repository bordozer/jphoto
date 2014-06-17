package ui.controllers.photos.list.title;

import core.general.data.PhotoListCriterias;
import core.services.system.Services;
import core.services.translator.Language;
import core.services.translator.message.TranslatableMessage;
import core.services.utils.DateUtilsService;
import core.services.utils.sql.PhotoSqlHelperServiceImpl;
import ui.context.EnvironmentContext;

import java.util.Date;

public abstract class AbstractPhotoListTitle {

	protected PhotoListCriterias criterias;
	protected Services services;

	public abstract String getPhotoListTitle();

	public abstract String getPhotoListDescription();

	public AbstractPhotoListTitle( final PhotoListCriterias criterias, final Services services ) {
		this.criterias = criterias;
		this.services = services;
	}

	protected String getTitle( final String nerd ) {
		final TranslatableMessage translatableMessage = new TranslatableMessage( nerd, services );

		addUser( translatableMessage );

		addGenre( translatableMessage );

		return translatableMessage.build( getLanguage() );
	}

	protected String addDescription( final String nerd ) {
		final TranslatableMessage translatableMessage = new TranslatableMessage( nerd, services );

		addUser( translatableMessage );

		addGenre( translatableMessage );

		addMarks( translatableMessage );

		addVotingDateRange( translatableMessage );

		return translatableMessage.build( getLanguage() );
	}

	public Language getLanguage() {
		return EnvironmentContext.getLanguage();
	}

	private void addGenre( final TranslatableMessage translatableMessage ) {
		if ( criterias.getGenre() != null ) {
			translatableMessage
				.worldSeparator()
				.translatableString( "Top best photo list title: in category" )
				.worldSeparator()
				.addGenreNameParameter( criterias.getGenre() )
			;
		}
	}

	private void addUser( final TranslatableMessage translatableMessage ) {
		if ( criterias.getUser() != null ) {
			translatableMessage
				.worldSeparator()
				.translatableString( "Top best photo list title: of member" )
				.worldSeparator()
				.addUserCardLinkParameter( criterias.getUser() )
			;
		}
	}

	private void addMarks( final TranslatableMessage translatableMessage ) {
		if ( criterias.getMinimalMarks() > PhotoSqlHelperServiceImpl.MIN_POSSIBLE_MARK ) {
			translatableMessage
				.worldSeparator()
				.translatableString( "Top best photo list title: that got at least" )
				.string( " " )
				.addIntegerParameter( criterias.getMinimalMarks() )
				.worldSeparator()
				.translatableString( "ROD PLURAL marks" )
			;
		}
	}

	private void addVotingDateRange( final TranslatableMessage translatableMessage ) {
		final DateUtilsService dateUtilsService = services.getDateUtilsService();

		final Date votingTimeFrom = criterias.getVotingTimeFrom();
		final Date votingTimeTo = criterias.getVotingTimeTo();

		if ( dateUtilsService.isEmptyTime( votingTimeFrom ) || dateUtilsService.isEmptyTime( votingTimeTo ) ) {
			return;
		}

		final Date votingDateFrom = dateUtilsService.getFirstSecondOfDay( votingTimeFrom );
		final Date votingDateTo = dateUtilsService.getFirstSecondOfDay( votingTimeTo );

		if ( votingDateTo.getTime() == dateUtilsService.getFirstSecondOfToday().getTime() ) {
			translatableMessage
				.worldSeparator()
				.translatableString( "Top best photo list title: for the last" )
				.worldSeparator()
				.addIntegerParameter( dateUtilsService.getDifferenceInDays( votingDateFrom, votingDateTo ) )
				.worldSeparator()
				.translatableString( "ROD PLURAL days" )
			;
		}
	}
}
