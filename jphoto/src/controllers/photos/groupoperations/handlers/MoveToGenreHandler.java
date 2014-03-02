package controllers.photos.groupoperations.handlers;

import controllers.photos.groupoperations.*;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.photo.group.PhotoGroupOperationType;
import core.services.photo.PhotoService;
import core.services.security.Services;
import core.services.utils.EntityLinkUtilsService;
import org.apache.commons.lang.StringUtils;
import utils.TranslatorUtils;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

public class MoveToGenreHandler extends AbstractGroupOperationHandler {

	protected MoveToGenreHandler( final PhotoGroupOperationModel model, final Services services ) {
		super( model, services );
	}

	@Override
	protected PhotoGroupOperationType getPhotoGroupOperation() {
		return PhotoGroupOperationType.MOVE_TO_GENRE;
	}

	@Override
	public void fillModel() {
		super.fillModel();

		final List<Genre> genres = services.getGenreService().loadAll();
		final List<GenreEntry> genreEntries = newArrayList();

		for ( final Genre genre : genres ) {
			final String genreName = String.format( "%s%s", genre.getName(), genre.isCanContainNudeContent() ? TranslatorUtils.translate( "( nude )" ) : StringUtils.EMPTY );

			genreEntries.add( new GenreEntry( genre.getId(), genreName ) );
		}

		model.setGenreEntries( genreEntries );
	}

	@Override
	protected void setGroupOperationEntryProperties( final PhotoGroupOperationEntry groupOperationEntry ) {
		final EntityLinkUtilsService entityLinkUtilsService = services.getEntityLinkUtilsService();

		final Photo photo = groupOperationEntry.getPhoto();
		final Genre genre = services.getGenreService().load( photo.getGenreId() );

		final Map<String, PhotoGroupOperationEntryProperty> map = model.getPhotoGroupOperationEntryPropertiesMap();

		final String key = getDefaultEntryKey( photo );

		final String nudeContentWarning = photo.isContainsNudeContent() ? TranslatorUtils.translate( "<br />&nbsp;Nude content!" ) : StringUtils.EMPTY;
		final String name = TranslatorUtils.translate( "Move<br />&nbsp;Current category: '$1'$2", entityLinkUtilsService.getPhotosByGenreLink( genre ), nudeContentWarning );
		final PhotoGroupOperationEntryProperty entryProperty = new PhotoGroupOperationEntryProperty( photo.getId(), ENTRY_ID, name );
		entryProperty.setValue( true );

		map.put( key, entryProperty );

		model.setPhotoGroupOperationEntryPropertiesMap( map );
	}

	@Override
	protected List<GroupOperationResult> performPhotoOperation( final Photo photo, final PhotoGroupOperationEntryProperty entryProperty ) {
		final EntityLinkUtilsService entityLinkUtilsService = services.getEntityLinkUtilsService();

		final List<GroupOperationResult> operationResults = newArrayList();

		final boolean isSelectedForMoving = entryProperty.isValue();
		if ( ! isSelectedForMoving ) {
			operationResults.add( GroupOperationResult.skipped( String.format( "Photo '%s' is skipped", entityLinkUtilsService.getPhotoCardLink( photo ) ) ) );

			return operationResults;
		}

		final PhotoService photoService = services.getPhotoService();

		final int moveToGenreId = model.getMoveToGenreId();
		final Genre genre = services.getGenreService().load( moveToGenreId );

		if ( photo.getGenreId() == moveToGenreId ) {
			operationResults.add( GroupOperationResult.skipped( TranslatorUtils.translate( "Photo '$1' already has category '$2'. Skipped.", entityLinkUtilsService.getPhotoCardLink( photo ), entityLinkUtilsService.getPhotosByGenreLink( genre ) ) ) );
			return operationResults;
		}

		if ( photo.isContainsNudeContent() && ! genre.isCanContainNudeContent() ) {
			operationResults.add( GroupOperationResult.warning( TranslatorUtils.translate( "Photo '$1' contains nude content, but this is not allowed for category '$2'."
				, entityLinkUtilsService.getPhotoCardLink( photo ), entityLinkUtilsService.getPhotosByGenreLink( genre ) ) ) );
			return operationResults;
		}

		photo.setGenreId( moveToGenreId );
		if ( !photoService.save( photo ) ) {
			operationResults.add( GroupOperationResult.error( "Photo data saving error" ) );
			return operationResults;
		}

		operationResults.add( GroupOperationResult.successful( TranslatorUtils.translate( "Photo '$1' has been moved to '$2'"
			, entityLinkUtilsService.getPhotoCardLink( photo ), entityLinkUtilsService.getPhotosByGenreLink( genre ) ) ) );

		return operationResults;
	}
}
