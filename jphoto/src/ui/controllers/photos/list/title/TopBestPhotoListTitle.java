package ui.controllers.photos.list.title;

import core.general.data.PhotoListCriterias;
import core.services.system.Services;
import core.services.translator.message.TranslatableMessage;
import core.services.utils.DateUtilsService;
import core.services.utils.sql.PhotoSqlHelperServiceImpl;

import java.util.Date;

public class TopBestPhotoListTitle extends AbstractPhotoListTitle {

	public TopBestPhotoListTitle( final PhotoListCriterias criterias, final Services services ) {
		super( criterias, services );
	}

	@Override
	public String getPhotoListTitle() {
		final TranslatableMessage translatableMessage = new TranslatableMessage( "Top best photo list title: TOP best photos", services );

		addUser( translatableMessage );

		addGenre( translatableMessage );

		return translatableMessage.build( getLanguage() );
	}

	@Override
	public String getPhotoListDescription() {
		final TranslatableMessage translatableMessage = new TranslatableMessage( "Top best photo list title: description", services );

		addUser( translatableMessage );

		addGenre( translatableMessage );

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

		final DateUtilsService dateUtilsService = services.getDateUtilsService();

		final Date votingTimeFrom = criterias.getVotingTimeFrom();
		final Date votingTimeTo = criterias.getVotingTimeTo();

		if ( dateUtilsService.isEmptyTime( votingTimeFrom ) || dateUtilsService.isEmptyTime( votingTimeTo ) ) {
			return translatableMessage.build( getLanguage() );
		}

		final Date votingDateFrom = dateUtilsService.getFirstSecondOfDay( votingTimeFrom );
		final Date votingDateTo = dateUtilsService.getFirstSecondOfDay( votingTimeTo );

		/*if ( dateUtilsService.isNotEmptyTime( votingTimeFrom ) && votingDateFrom.getTime() == votingDateTo.getTime() ) {
			translatableMessage
				.worldSeparator()
				.dateTimeFormatted( votingTimeFrom )
			;
		}*/

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

		return translatableMessage.build( getLanguage() );
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
}
