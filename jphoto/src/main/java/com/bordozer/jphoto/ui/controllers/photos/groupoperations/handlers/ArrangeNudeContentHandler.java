package com.bordozer.jphoto.ui.controllers.photos.groupoperations.handlers;

import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.group.PhotoGroupOperationType;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.controllers.photos.groupoperations.GroupOperationResult;
import com.bordozer.jphoto.ui.controllers.photos.groupoperations.PhotoGroupOperationEntry;
import com.bordozer.jphoto.ui.controllers.photos.groupoperations.PhotoGroupOperationEntryProperty;
import com.bordozer.jphoto.ui.controllers.photos.groupoperations.PhotoGroupOperationModel;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

public class ArrangeNudeContentHandler extends AbstractGroupOperationHandler {

    public ArrangeNudeContentHandler(final PhotoGroupOperationModel model, final Services services) {
        super(model, services);
    }

    @Override
    public PhotoGroupOperationType getPhotoGroupOperation() {
        return PhotoGroupOperationType.ARRANGE_NUDE_CONTENT;
    }

    @Override
    protected void setPhotoOperationAllowance(final PhotoGroupOperationEntry groupOperationEntry) {
        super.setPhotoOperationAllowance(groupOperationEntry);

        if (groupOperationEntry.isGroupOperationAccessible()) {
            final Photo photo = groupOperationEntry.getPhoto();
            final Genre genre = getGenre(photo);

            final String genreNameTranslated = services.getTranslatorService().translateGenre(genre, getUser().getLanguage());
            if (genre.isContainsNudeContent()) {
                groupOperationEntry.setPhotoOperationAllowanceMessage(getTranslatorService().translate("The photo has category '$1' and must have 'Nude content' option.", getUser().getLanguage(), genreNameTranslated));
                //				groupOperationEntry.setPhotoOperationAllowanceMessage( getTranslatorService().translate( "You can not remove 'Nude content' option for photo with category '$1'.<br />The category contains nude obviously.", genre.getName() ) );
                //				groupOperationEntry.setGroupOperationAccessible( false );

                return;
            }

            if (!photo.isContainsNudeContent() && !genre.isCanContainNudeContent()) {
                groupOperationEntry.setPhotoOperationAllowanceMessage(getTranslatorService().translate("You can not set 'Nude content' option for photo with category '$1'.<br />Nude content is not allowed for the category.<br />If the photo does contains nude, then remove the photo to appropriate category.", getUser().getLanguage(), genreNameTranslated));
                groupOperationEntry.setGroupOperationAccessible(false);

                return;
            }
        }
    }

    @Override
    protected void setGroupOperationEntryProperties(final PhotoGroupOperationEntry groupOperationEntry) {
        final Photo photo = groupOperationEntry.getPhoto();

        final Map<String, PhotoGroupOperationEntryProperty> map = model.getPhotoGroupOperationEntryPropertiesMap();

        final Genre genre = getGenre(photo);
        final String key = getDefaultEntryKey(photo);
        final String entryName = String.format("%s%s", getTranslatorService().translate("Group operations: Nude content", getUser().getLanguage()), (!genre.isCanContainNudeContent() ? "<br />&nbsp;&nbsp;Nude content is not allowed for the photo's category and can be only unset" : ""));
        final PhotoGroupOperationEntryProperty entryProperty = new PhotoGroupOperationEntryProperty(photo.getId(), ENTRY_ID, entryName);
        entryProperty.setValue(photo.isContainsNudeContent());

        map.put(key, entryProperty);

        model.setPhotoGroupOperationEntryPropertiesMap(map);
    }

    @Override
    protected List<GroupOperationResult> performPhotoOperation(final Photo photo, final PhotoGroupOperationEntryProperty entryProperty) {

        final List<GroupOperationResult> operationResults = newArrayList();

        final boolean isPhotoNudeContentChecked = entryProperty.isValue();

        final Genre genre = getGenre(photo);
        if (isPhotoNudeContentChecked && !genre.isCanContainNudeContent()) {
            operationResults.add(GroupOperationResult.warning(String.format("Option 'Contains nude content' can no be set for '%s' because nude content is not allowed for it's category '%s'. You have to move the photo to another category, if the photo contains nude."
                    , getPhoyoLink(photo), services.getTranslatorService().translateGenre(genre, getUser().getLanguage()))));
			/*if ( photo.isContainsNudeContent() ) {
				photo.setContainsNudeContent( false );
				services.getPhotoService().save( photo );
			}*/
            return operationResults;
        }

        final boolean isPhotoContainsNudeContent = photo.isContainsNudeContent();

        if (isPhotoNudeContentChecked && !isPhotoContainsNudeContent) {
            photo.setContainsNudeContent(true);

            services.getPhotoService().save(photo);

            operationResults.add(GroupOperationResult.successful(String.format("Option 'Contains nude content' has been set for %s", getPhoyoLink(photo))));
        }

        if (!isPhotoNudeContentChecked && isPhotoContainsNudeContent) {
            photo.setContainsNudeContent(false);

            services.getPhotoService().save(photo);

            operationResults.add(GroupOperationResult.successful(String.format("Option 'Contains nude content' has been remove for %s", getPhoyoLink(photo))));
        }

        return operationResults;
    }

    private String getPhoyoLink(final Photo photo) {
        return services.getEntityLinkUtilsService().getPhotoCardLink(photo, getUser().getLanguage());
    }

    private Genre getGenre(final Photo photo) {
        return services.getGenreService().load(photo.getGenreId());
    }

}
