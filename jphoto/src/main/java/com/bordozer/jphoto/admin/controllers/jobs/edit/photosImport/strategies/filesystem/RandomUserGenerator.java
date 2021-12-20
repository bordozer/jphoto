package com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.filesystem;

import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.LocalCategory;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteImportStrategy;
import com.bordozer.jphoto.core.exceptions.BaseRuntimeException;
import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserMembershipType;
import com.bordozer.jphoto.core.log.LogHelper;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.utils.RandomUtilsService;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;

public class RandomUserGenerator extends AbstractUserGenerator {

    private static final int MAX_ATTEMPTS_TO_FIND_USER_FOR_GENRE = 50;

    private final Map<User, Set<Genre>> userPhotosByGenresCacheMap = newHashMap();
    private final Set<Genre> genres;

    private final List<User> beingProcessedUsers;
    private final Services services;

    protected final LogHelper log = new LogHelper();

    private final static EnumSet<LocalCategory> allowedForModelsAndMakeupMastersGenres = EnumSet.of(LocalCategory.ADVERTISING, LocalCategory.CHILDREN, LocalCategory.GLAMOUR, LocalCategory.MODELS, LocalCategory.NUDE, LocalCategory.PORTRAIT, LocalCategory.WEDDING, LocalCategory.HDR);

    public RandomUserGenerator(final Set<Genre> genres, final List<User> beingProcessedUsers, final Services services) {
        this.services = services;
        this.beingProcessedUsers = beingProcessedUsers;

        log.debug("Loading users photos by genres map");
        //			userPhotosByGenresMap = getUserCardGenreInfoMap( genres );

        this.genres = genres;
    }

    @Override
    public User getUser(final Genre genre) {
        return getRandomUserForGenre(genre);
    }

    private User getRandomUserForGenre(final Genre genre) {
        final UserSelectionHelper helper = new UserSelectionHelper(services);

        int i = 0;

        while (true) {
            i++;

            final User user = getRandomNonPhotosightUser(0);

            final int userPhotosQty = services.getPhotoService().getPhotosCountByUser(user.getId());

            if (userPhotosQty == 0) {
                // no photo at all yet. This randomly selected user is about to upload his first photo in this genre
                if (!isGenreSuitableForUserMembershipType(user.getMembershipType(), genre)) {
                    log.debug(String.format("Trying to pick up suitable user for uploading photo in category %s: Randomly selected user %s has no photos yet, but his membership type is not suitable for the genre. Trying to find another one...", genre, user));
                    continue;
                }

                return user; // there are no photos and the genre is suitable for user's membership type
            }

            // already has photo(s) int this or another genre(s)
            final int photosByGenre = services.getPhotoService().getPhotosCountByUserAndGenre(user.getId(), genre.getId());
            if (photosByGenre > 0) {
                // already has photo(s) in this photo category - user suitable for the category and can upload another photo of this category
                // TODO: respect photo upload allowances
				/*final AbstractPhotoUploadAllowance uploadAllowance = UploadDescriptionFactory.getInstance( user, user, Language.EN, services );
				if ( ! uploadAllowance.isUserCanUploadPhoto() ) {
					log.debug( String.format( "Trying to pick up suitable user for uploading photo in category %s: Randomly selected user %s has uploaded photos in the suitable genres, but the user does not have photo upload allowances. Trying to find another one...", genre, user ) );
					continue;
				}*/

                return user;
            }

            // possible the user has photos from similar genres
            final Set<Genre> userGenres = getGenresWhereUserHasPhotos(genres, user);

            for (final Genre genreWhereUserHasPhotos : userGenres) {
                final boolean genresSimilar = helper.isGenresSimilar(genreWhereUserHasPhotos, genre);
                if (genresSimilar) {
                    // user has photo from similar genre - user fits our genre
                    log.debug(String.format("Trying to pick up suitable user for uploading photo in category %s: Randomly selected user %s is suitable enough for", genre, user));
                    return user;
                }
            }

            // user has photos but not in the genre or similar to it - skipping this user and try to select another one
            if (i > MAX_ATTEMPTS_TO_FIND_USER_FOR_GENRE) {
                break;
            }
        }

        log.debug(String.format("Can not find a suitable user for uploading photo in category '%s'. An any random user is going to be selected.", genre.getName()));

        return getRandomNonPhotosightUser(0); // This genre might have been missed in configuration...
    }

    private boolean isGenreSuitableForUserMembershipType(final UserMembershipType membershipType, final Genre genre) {
        switch (membershipType) {
            case AUTHOR:
                return true;
            case MODEL:
            case MAKEUP_MASTER:
                return allowedForModelsAndMakeupMastersGenres.contains(LocalCategory.getByName(genre.getName()));
        }

        throw new IllegalArgumentException(String.format("Illegal membershipType: %s", membershipType));
    }

    private User getRandomNonPhotosightUser(final int counter) {

        if (counter > 100) {
            throw new BaseRuntimeException("Max iteration reached. Can not find non-photosight user. Check if at least one not imported user exists.");
        }

        final RandomUtilsService randomUtilsService = services.getRandomUtilsService();

        final User randomUser = randomUtilsService.getRandomUser(beingProcessedUsers);
        if (randomUser.getLogin().startsWith(RemotePhotoSiteImportStrategy.REMOTE_PHOTO_SITE_USER_LOGIN_PREFIX)) {
            return getRandomNonPhotosightUser(counter + 1);
        }

        return randomUser;
    }

    private Set<Genre> getGenresWhereUserHasPhotos(final Set<Genre> genres, final User user) {

        final Set<Genre> userGenresWhereHeHasPhotos = userPhotosByGenresCacheMap.get(user);
        if (userGenresWhereHeHasPhotos != null) {
            return userGenresWhereHeHasPhotos;
        }

        final Set<Genre> result = newHashSet();

        // TODO: use photoService.getUserPhotoGenres()
        for (final Genre genre : genres) {
            final int photosInGenre = services.getPhotoService().getPhotosCountByUserAndGenre(user.getId(), genre.getId());
            if (photosInGenre > 0) {
                result.add(genre);
            }
        }

        userPhotosByGenresCacheMap.put(user, result);

        return result;
    }
}
