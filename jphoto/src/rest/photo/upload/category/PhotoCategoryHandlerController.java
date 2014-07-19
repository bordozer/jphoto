package rest.photo.upload.category;

import core.general.genre.Genre;
import core.general.photo.Photo;
import core.services.entry.GenreService;
import core.services.photo.PhotoService;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ui.context.EnvironmentContext;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "photos/{photoId}/edit/category/{categoryId}/" )
public class PhotoCategoryHandlerController {

	@Autowired
	private GenreService genreService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private TranslatorService translatorService;

	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	@ResponseBody
	public PhotoCategoryHandlerDTO renderPhotoCategory( final @PathVariable( "photoId" ) int photoId, final @PathVariable( "categoryId" ) int categoryId ) {

		final PhotoCategoryHandlerDTO dto = new PhotoCategoryHandlerDTO();

		dto.setPhotoId( photoId );
		dto.setPhotoCategoryDTOs( getPhotoCategoryDTOs() );

		final PhotoUploadNudeContentDTO nudeContentDTO = getNudeContentDTO( categoryId, photoId );
		if ( photoId > 0 ) {
			final Photo photo = photoService.load( photoId );
			dto.setPhotoContainsNude( photo.isContainsNudeContent() );
		}
		dto.setNudeContentDTO( nudeContentDTO );

		final Photo photo = photoService.load( photoId );
		if ( photo != null ) {
			dto.setSelectedCategoryId( photo.getGenreId() );
		}

		return dto;
	}

	@RequestMapping( method = RequestMethod.PUT, value = "/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	@ResponseBody
	public PhotoCategoryHandlerDTO onChangePhotoCategory( @RequestBody final PhotoCategoryHandlerDTO dto ) {

		// TODO: set photo upload allowance
		dto.setNudeContentDTO( getNudeContentDTO( dto.getSelectedCategoryId(), dto.getPhotoId() ) );

		return dto;
	}

	public PhotoUploadNudeContentDTO getNudeContentDTO( final int categoryId, final int photoId ) {

		final Genre genre = genreService.load( categoryId );

		final PhotoUploadNudeContentDTO dto = new PhotoUploadNudeContentDTO();

		dto.setGenreCanContainsNude( genre.isCanContainNudeContent() );
		dto.setGenreObviouslyContainsNude( genre.isContainsNudeContent() );

		dto.setYesTranslated( translatorService.translate( "Photo edit: Category always contains nude", EnvironmentContext.getLanguage() ) );
		dto.setNoTranslated( translatorService.translate( "Photo edit: Category can not contains nude", EnvironmentContext.getLanguage() ) );

		return dto;
	}

	private List<PhotoCategoryDTO> getPhotoCategoryDTOs() {
		final Language language = EnvironmentContext.getLanguage();

		final List<PhotoCategoryDTO> result = newArrayList();

		final List<Genre> genres = genreService.loadAllSortedByNameForLanguage( language );
		for ( final Genre genre : genres ) {
			final PhotoCategoryDTO wrapper = new PhotoCategoryDTO();
			wrapper.setCategoryId( genre.getId() );
			wrapper.setName( translatorService.translateGenre( genre, language ) );

			result.add( wrapper );
		}

		return result;
	}
}
