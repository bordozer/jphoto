package admin.controllers.restriction;

import core.general.photo.Photo;
import core.general.user.User;
import core.services.photo.PhotoService;
import core.services.translator.TranslatorService;
import core.services.user.UserService;
import org.jabsorb.JSONRPCBridge;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import rest.admin.restriction.RestrictionTypeDTO;
import ui.context.ApplicationContextHelper;
import ui.context.EnvironmentContext;
import ui.translatable.GenericTranslatableEntry;
import ui.translatable.GenericTranslatableList;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@RequestMapping( "restriction" )
@Controller
public class RestrictionController {

	@Autowired
	private UserService userService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private TranslatorService translatorService;

	private static final String MODEL_NAME = "restrictionModel";

	private static final String VIEW = "admin/restriction/Restriction";

	@ModelAttribute( MODEL_NAME )
	public RestrictionModel prepareModel() {
		return new RestrictionModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/members/{userId}/" )
	public String showUserRestrictions( final @PathVariable( "userId" ) int userId, final @ModelAttribute( MODEL_NAME ) RestrictionModel model ) {

		final User user = userService.load( userId );

		model.setEntryId( user.getId() );
		model.setEntryName( user.getNameEscaped() );

		model.setRestrictionTypes( restrictionTypes( GenericTranslatableList.restrictionUserTranslatableList( EnvironmentContext.getLanguage(), translatorService ).getEntries() ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/photos/{photoId}/" )
	public String showPhotoRestrictions( final @PathVariable( "photoId" ) int photoId, final @ModelAttribute( MODEL_NAME ) RestrictionModel model ) {

		final Photo photo = photoService.load( photoId );

		model.setEntryId( photo.getId() );
		model.setEntryName( photo.getNameEscaped() );

		model.setRestrictionTypes( restrictionTypes( GenericTranslatableList.restrictionPhotosTranslatableList( EnvironmentContext.getLanguage(), translatorService ).getEntries() ) );

		return VIEW;
	}

	private JSONArray restrictionTypes( final List<GenericTranslatableEntry> restrictionTypes ) {
		final List<JSONObject> jsonObjects = newArrayList();
		for ( final GenericTranslatableEntry restrictionType : restrictionTypes ) {
			jsonObjects.add( new JSONObject( new RestrictionTypeDTO( restrictionType.getId(), restrictionType.getName() ) ) );
		}

		return new JSONArray( jsonObjects );
	}
}
