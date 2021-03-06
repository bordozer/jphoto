package com.bordozer.jphoto.ui.controllers.photos.groupoperations.handlers;

import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.group.PhotoGroupOperationType;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.utils.EntityLinkUtilsService;
import com.bordozer.jphoto.ui.controllers.photos.groupoperations.GenreEntry;
import com.bordozer.jphoto.ui.controllers.photos.groupoperations.GroupOperationResult;
import com.bordozer.jphoto.ui.controllers.photos.groupoperations.PhotoGroupOperationEntry;
import com.bordozer.jphoto.ui.controllers.photos.groupoperations.PhotoGroupOperationEntryProperty;
import com.bordozer.jphoto.ui.controllers.photos.groupoperations.PhotoGroupOperationModel;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

public class MoveToGenreHandler extends AbstractGroupOperationHandler {

    protected MoveToGenreHandler(final PhotoGroupOperationModel model, final Services services) {
        super(model, services);
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

        for (final Genre genre : genres) {
            final String genreNameTranslated = getTranslatorService().translateGenre(genre, getLanguage());
            final String genreNameShown = String.format("%s%s", genreNameTranslated, genre.isCanContainNudeContent() ? getTranslatorService().translate("Move to genre: ( nude flag )", getLanguage()) : StringUtils.EMPTY);

            genreEntries.add(new GenreEntry(genre.getId(), genreNameShown));
        }

        model.setGenreEntries(genreEntries);
    }

    @Override
    protected void setGroupOperationEntryProperties(final PhotoGroupOperationEntry groupOperationEntry) {
        final EntityLinkUtilsService entityLinkUtilsService = services.getEntityLinkUtilsService();

        final Photo photo = groupOperationEntry.getPhoto();
        final Genre genre = services.getGenreService().load(photo.getGenreId());

        final Map<String, PhotoGroupOperationEntryProperty> map = model.getPhotoGroupOperationEntryPropertiesMap();

        final String key = getDefaultEntryKey(photo);

        final String nudeContentWarning = photo.isContainsNudeContent() ? getTranslatorService().translate("<br />&nbsp;Nude content!", getLanguage()) : StringUtils.EMPTY;
        final String name = getTranslatorService().translate("Move<br />&nbsp;Current category: '$1'$2", getLanguage(), entityLinkUtilsService.getPhotosByGenreLink(genre, getUser().getLanguage()), nudeContentWarning);
        final PhotoGroupOperationEntryProperty entryProperty = new PhotoGroupOperationEntryProperty(photo.getId(), ENTRY_ID, name);
        entryProperty.setValue(true);

        map.put(key, entryProperty);

        model.setPhotoGroupOperationEntryPropertiesMap(map);
    }

    @Override
    protected List<GroupOperationResult> performPhotoOperation(final Photo photo, final PhotoGroupOperationEntryProperty entryProperty) {
        final EntityLinkUtilsService entityLinkUtilsService = services.getEntityLinkUtilsService();

        final List<GroupOperationResult> operationResults = newArrayList();

        final boolean isSelectedForMoving = entryProperty.isValue();
        if (!isSelectedForMoving) {
            operationResults.add(GroupOperationResult.skipped(String.format("Photo '%s' is skipped", entityLinkUtilsService.getPhotoCardLink(photo, getLanguage()))));

            return operationResults;
        }

        final PhotoService photoService = services.getPhotoService();

        final int moveToGenreId = model.getMoveToGenreId();
        final Genre genre = services.getGenreService().load(moveToGenreId);

        if (photo.getGenreId() == moveToGenreId) {
            operationResults.add(GroupOperationResult.skipped(getTranslatorService().translate("Photo '$1' already has category '$2'. Skipped.", getLanguage(), entityLinkUtilsService.getPhotoCardLink(photo, getLanguage()), entityLinkUtilsService.getPhotosByGenreLink(genre, getLanguage()))));
            return operationResults;
        }

        if (photo.isContainsNudeContent() && !genre.isCanContainNudeContent()) {
            operationResults.add(GroupOperationResult.warning(getTranslatorService().translate("Photo '$1' contains nude content, but this is not allowed for category '$2'.", getLanguage()
                    , entityLinkUtilsService.getPhotoCardLink(photo, getLanguage()), entityLinkUtilsService.getPhotosByGenreLink(genre, getUser().getLanguage()))));
            return operationResults;
        }

        if (!photoService.movePhotoToGenreWithNotification(photo.getId(), moveToGenreId, getUser())) {
            operationResults.add(GroupOperationResult.error("Photo data saving error"));
            return operationResults;
        }

        operationResults.add(GroupOperationResult.successful(getTranslatorService().translate("Photo '$1' has been moved to '$2'", getLanguage()
                , entityLinkUtilsService.getPhotoCardLink(photo, getLanguage()), entityLinkUtilsService.getPhotosByGenreLink(genre, getUser().getLanguage()))));

        return operationResults;
    }
}
