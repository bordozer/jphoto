package rest.photo.list;

import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.entry.GenreService;
import core.services.photo.PhotoService;
import core.services.security.SecurityService;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.user.UserService;
import core.services.utils.DateUtilsService;
import core.services.utils.EntityLinkUtilsService;
import core.services.utils.UrlUtilsService;
import core.services.utils.UserPhotoFilePathUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import ui.context.EnvironmentContext;
import ui.services.security.SecurityUIService;

import java.util.Date;

public abstract class AbstractPhotoListEntryController {

	@Autowired
	protected PhotoService photoService;

	@Autowired
	protected UserService userService;

	@Autowired
	protected GenreService genreService;

	@Autowired
	protected EntityLinkUtilsService entityLinkUtilsService;

	@Autowired
	protected UrlUtilsService urlUtilsService;

	@Autowired
	protected DateUtilsService dateUtilsService;

	@Autowired
	protected TranslatorService translatorService;

	@Autowired
	protected UserPhotoFilePathUtilsService userPhotoFilePathUtilsService;

	@Autowired
	protected SecurityUIService securityUIService;

	@Autowired
	protected SecurityService securityService;

	protected String getPhotoUploadDate( final Photo photo, final Language language ) {
		final Date uploadTime = photo.getUploadTime();

		final String shownTime = dateUtilsService.isItToday( uploadTime ) ? dateUtilsService.formatTimeShort( uploadTime ) : dateUtilsService.formatDateTimeShort( uploadTime );

		return String.format( "<a href='%s' title='%s'>%s</a>"
				, urlUtilsService.getPhotosUploadedOnDateUrl( uploadTime )
				, translatorService.translate( "Photo preview: show all photos uploaded at $1", language, dateUtilsService.formatDate( uploadTime ) )
				, shownTime
		);
	}

	protected String getPhotoCategory( final int genreId, Language language ) {
		final Genre genre = genreService.load( genreId );
		return entityLinkUtilsService.getPhotosByGenreLink( genre, language );
	}

	protected Language getLanguage() {
		return EnvironmentContext.getLanguage();
	}

	protected String getPhotoPreview( final Photo photo, final User accessor, final boolean doesPreviewHasToBeHidden, final Language language, final String photoPreviewUrl ) {

		if ( doesPreviewHasToBeHidden ) {
			return String.format( "<img src='%s/hidden_picture.png' class='photo-preview-image' title='%s'/>"
					, urlUtilsService.getSiteImagesPath()
					, translatorService.translate( "Photo preview: The photo is within anonymous period", language )
			);
		}

		if ( securityUIService.isPhotoHasToBeHiddenBecauseOfNudeContent( photo, accessor ) ) {
			return String.format( "<a href='%s' title='%s'><img src='%s/nude_content.jpg' class='photo-preview-image block-border'/></a>"
					, urlUtilsService.getPhotoCardLink( photo.getId() )
					, String.format( "%s ( %s )", photo.getNameEscaped(), translatorService.translate( "Photo preview: Nude content", language ) )
					, urlUtilsService.getSiteImagesPath()
			);
		}

		return String.format( "<a href='%s' title='%s'><img src='%s' class='photo-preview-image block-border'/></a>"
				, urlUtilsService.getPhotoCardLink( photo.getId() )
				, photo.getNameEscaped()
				, photoPreviewUrl
		);
	}
}
