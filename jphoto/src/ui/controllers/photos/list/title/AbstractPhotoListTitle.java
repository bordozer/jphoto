package ui.controllers.photos.list.title;

import core.general.data.PhotoListCriterias;
import core.services.system.Services;
import core.services.translator.Language;
import core.services.translator.message.TranslatableMessage;
import core.services.utils.DateUtilsService;
import core.services.utils.sql.PhotoSqlHelperServiceImpl;

import java.util.Date;

public abstract class AbstractPhotoListTitle {

	protected PhotoListCriterias criterias;
	protected Language language;
	protected Services services;

	public abstract String getPhotoListTitle();

	public abstract String getPhotoListDescription();

	public AbstractPhotoListTitle( final PhotoListCriterias criterias, final Language language, final Services services ) {
		this.criterias = criterias;
		this.language = language;
		this.services = services;
	}

	protected String getTitle( final String nerd ) {
		final TranslatableMessage translatableMessage = new TranslatableMessage( nerd, services );

		addUser( translatableMessage );

		addGenre( translatableMessage );

		addUploadingDateRange( translatableMessage );

		addVotingDateRange( translatableMessage );

		addVotingCategoryTitle( translatableMessage );

		return translatableMessage.build( getLanguage() );
	}

	protected String addDescription( final String nerd ) {
		final TranslatableMessage translatableMessage = new TranslatableMessage( nerd, services );

		addUser( translatableMessage );

		addGenre( translatableMessage );

		addMarks( translatableMessage );

		addUploadingDateRange( translatableMessage );

		addVotingDateRange( translatableMessage );

		addVotingCategoryTitle( translatableMessage );

		addVotingCategoryDescription( translatableMessage );

		addMembershipType( translatableMessage );

		return translatableMessage.build( getLanguage() );
	}

	private void addUser( final TranslatableMessage translatableMessage ) {

		if ( criterias.getUser() == null ) {
			return;
		}

		translatableMessage
			.worldSeparator()
			.translatableString( "Top best photo list title: of member" )
			.worldSeparator()
			.addUserCardLinkParameter( criterias.getUser() )
		;
	}

	private void addGenre( final TranslatableMessage translatableMessage ) {

		if ( criterias.getGenre() == null ) {
			return;
		}

		translatableMessage
			.worldSeparator()
			.translatableString( "Top best photo list title: in category" )
			.worldSeparator()
			.addGenreNameParameter( criterias.getGenre() )
		;
	}

	private void addMarks( final TranslatableMessage translatableMessage ) {

		if ( criterias.getMinimalMarks() <= PhotoSqlHelperServiceImpl.MIN_POSSIBLE_MARK ) {
			return;
		}

		translatableMessage
			.worldSeparator()
			.translatableString( "Top best photo list title: that got at least" )
			.worldSeparator()
			.addIntegerParameter( criterias.getMinimalMarks() )
			.worldSeparator()
			.translatableString( "ROD PLURAL marks" )
		;
	}

	private void addUploadingDateRange( final TranslatableMessage translatableMessage ) {
		final DateUtilsService dateUtilsService = services.getDateUtilsService();

		final Date timeFrom = criterias.getUploadDateFrom();
		final Date timeTo = criterias.getUploadDateTo();

		if ( dateUtilsService.isEmptyTime( timeFrom ) || dateUtilsService.isEmptyTime( timeTo ) ) {
			return;
		}

		final Date dateFrom = dateUtilsService.getFirstSecondOfDay( timeFrom );
		final Date dateTo = dateUtilsService.getFirstSecondOfDay( timeTo );

		/*if ( dateUtilsService.isNotEmptyTime( timeFrom ) && dateFrom.getTime() == dateTo.getTime() ) {
			translatableMessage
				.worldSeparator()
				.translatableString( "Top best photo list title: for date" )
				.worldSeparator()
				.dateTimeFormatted( dateFrom )
			;
		}*/

		translatableMessage
			.worldSeparator()
			.translatableString( "Top best photo list title: uploaded from to" )
			.worldSeparator()
			.dateFormatted( dateFrom )
			.string( " - " )
			.dateFormatted( dateTo )
		;
	}

	private void addVotingDateRange( final TranslatableMessage translatableMessage ) {
		final DateUtilsService dateUtilsService = services.getDateUtilsService();

		final Date timeFrom = criterias.getVotingTimeFrom();
		final Date timeTo = criterias.getVotingTimeTo();

		if ( dateUtilsService.isEmptyTime( timeFrom ) || dateUtilsService.isEmptyTime( timeTo ) ) {
			return;
		}

		final Date dateFrom = dateUtilsService.getFirstSecondOfDay( timeFrom );
		final Date dateTo = dateUtilsService.getFirstSecondOfDay( timeTo );

		final boolean isPeriodInOneDay = dateFrom.getTime() == dateTo.getTime();
		if ( isPeriodInOneDay ) {
			translatableMessage
			.worldSeparator()
			.translatableString( "Top best photo list title: appraised at" )
			.worldSeparator()
			.dateFormatted( dateFrom )
			;

			return;
		}

		translatableMessage
			.worldSeparator()
			.translatableString( "Top best photo list title: appraised from to" )
			.worldSeparator()
			.dateFormatted( dateFrom )
			.string( " - " )
			.dateFormatted( dateTo )
		;
	}

	private void addVotingCategoryTitle( final TranslatableMessage translatableMessage ) {

		if ( criterias.getVotedUser() == null ) {
			return;
		}

		translatableMessage
			.worldSeparator()
			.translatableString( "Top best photo list title: appraised by member" )
			.worldSeparator()
			.addUserCardLinkParameter( criterias.getVotedUser() )
		;
	}

	private void addVotingCategoryDescription( final TranslatableMessage translatableMessage ) {

		/*if ( criterias.getVotedUser() == null ) {
			return;
		}

		translatableMessage
			.worldSeparator()
			.translatableString( "Top best photo list title: appraised by member" )
			.worldSeparator()
			.addUserCardLinkParameter( criterias.getVotedUser() )
		;*/

		if ( criterias.getVotingCategory() == null ) {
			return;
		}

		translatableMessage
			.worldSeparator()
			.translatableString( "Top best photo list description: has appraised as" )
			.worldSeparator()
			.addPhotoVotingCategoryParameterParameter( criterias.getVotingCategory() )
		;
	}

	private void addMembershipType( final TranslatableMessage translatableMessage ) {
		if ( criterias.getMembershipType() == null ) {
			return;
		}

		translatableMessage
			.worldSeparator()
			.translatableString( "Top best photo list description: with membership type" )
			.worldSeparator()
			.translatableString( criterias.getMembershipType().getNamePlural() )
		;
	}

	protected Language getLanguage() {
		return language;
	}
}
