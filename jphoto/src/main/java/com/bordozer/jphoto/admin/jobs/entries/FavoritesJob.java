package com.bordozer.jphoto.admin.jobs.entries;

import com.bordozer.jphoto.admin.jobs.JobRuntimeEnvironment;
import com.bordozer.jphoto.admin.jobs.enums.SavedJobType;
import com.bordozer.jphoto.core.enums.FavoriteEntryType;
import com.bordozer.jphoto.core.enums.SavedJobParameterKey;
import com.bordozer.jphoto.core.general.base.CommonProperty;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.log.LogHelper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public class FavoritesJob extends AbstractJob {

    private List<FavoriteEntryType> favoriteEntries;

    private final static Integer MAX_ITERATIONS = 1000;

    public FavoritesJob(final JobRuntimeEnvironment jobEnvironment) {
        super(new LogHelper(), jobEnvironment);
    }

    @Override
    protected void runJob() throws Throwable {
        final FavoriteGeneratorFactory factory = new FavoriteGeneratorFactory();

        int emptyIterations = 0;
        while (!isFinished()) {

            final FavoriteEntryType favoriteType = services.getRandomUtilsService().getRandomFavoriteType(favoriteEntries);
            final User userWhoIsAddingToFavorites = services.getRandomUtilsService().getRandomUser(beingProcessedUsers);

            final AbstractFavoriteEntryGenerator entryGenerator = factory.getInstance(favoriteType, userWhoIsAddingToFavorites);

            if (entryGenerator.generateFavoriteEntry()) {
                increment();
                emptyIterations = 0;

            } else {
                emptyIterations++;
            }

            if (hasJobFinishedWithAnyResult()) {
                break;
            }

            if (emptyIterations > MAX_ITERATIONS) {
                getLog().info(String.format("%s empty iterations. Stopped.", MAX_ITERATIONS));
                break;
            }
        }
    }

    @Override
    public SavedJobType getJobType() {
        return SavedJobType.FAVORITES_GENERATION;
    }

    @Override
    public Map<SavedJobParameterKey, CommonProperty> getParametersMap() {

        final Map<SavedJobParameterKey, CommonProperty> parametersMap = newHashMap();

        parametersMap.put(SavedJobParameterKey.PARAM_ACTIONS_QTY, new CommonProperty(SavedJobParameterKey.PARAM_ACTIONS_QTY.getId(), totalJopOperations));
        parametersMap.put(SavedJobParameterKey.PARAM_PHOTOS_QTY, new CommonProperty(SavedJobParameterKey.PARAM_PHOTOS_QTY.getId(), photoQtyLimit));
        parametersMap.put(SavedJobParameterKey.PARAM_FAVORITE_ENTRIES, CommonProperty.createFromIdentifiable(SavedJobParameterKey.PARAM_FAVORITE_ENTRIES.getId(), favoriteEntries));

        return parametersMap;
    }

    @Override
    public String getJobParametersDescription() {
        final StringBuilder builder = new StringBuilder();

        builder.append(services.getTranslatorService().translate("FavoritesJob: Total job steps", getLanguage())).append(": ").append(totalJopOperations);
        for (final FavoriteEntryType favoriteEntry : favoriteEntries) {
            builder.append("<br />- ").append(favoriteEntry.getName());
        }

        return builder.toString();
    }

    @Override
    public void initJobParameters(final Map<SavedJobParameterKey, CommonProperty> jobParameters) {
        totalJopOperations = jobParameters.get(SavedJobParameterKey.PARAM_ACTIONS_QTY).getValueInt();
        photoQtyLimit = jobParameters.get(SavedJobParameterKey.PARAM_PHOTOS_QTY).getValueInt();

        final List<Integer> favoriteEntriesIds = jobParameters.get(SavedJobParameterKey.PARAM_FAVORITE_ENTRIES).getValueListInt();
        favoriteEntries = newArrayList();
        for (final Integer favoriteEntriesId : favoriteEntriesIds) {
            favoriteEntries.add(FavoriteEntryType.getById(favoriteEntriesId));
        }
    }

    public void setFavoriteEntries(final List<FavoriteEntryType> favoriteEntries) {
        this.favoriteEntries = favoriteEntries;
    }

    private class FavoriteGeneratorFactory {
        public AbstractFavoriteEntryGenerator getInstance(final FavoriteEntryType favoriteEntryType, final User user) {
            switch (favoriteEntryType) {
                case FAVORITE_PHOTOS:
                    return new FavoritePhotoEntryGenerator(user);
                case FAVORITE_MEMBERS:
                    return new FavoriteUserEntryGenerator(user);
                case FRIENDS:
                    return new FriendEntryGenerator(user);
                case BLACKLIST:
                    return new BlacklistEntryGenerator(user);
                case BOOKMARKED_PHOTOS:
                    return new BookmarkEntryGenerator(user);
                case NEW_COMMENTS_NOTIFICATION:
                    return new NewCommentNotificationEntryGenerator(user);
                case NEW_PHOTO_NOTIFICATION:
                    return new NewPhotoNotificationEntryGenerator(user);
                case MEMBERS_INVISIBILITY_LIST:
                    return new PhotoListPhotoVisibilityEntryGenerator(user);
            }
            throw new IllegalArgumentException(String.format("Illegal favorite entry: %s", favoriteEntryType));
        }
    }

    private abstract class AbstractFavoriteEntryGenerator {
        protected final User user;

        abstract boolean generateFavoriteEntry();

        protected boolean addFavoriteUser(final FavoriteEntryType favoriteEntryType) {
            final User randomUser = services.getRandomUtilsService().getRandomUserButNotThisOne(user, FavoritesJob.this.beingProcessedUsers);

            if (randomUser == null) {
                return false;
            }

            if (services.getFavoritesService().isEntryInFavorites(user.getId(), randomUser.getId(), favoriteEntryType.getId())) {
                return false;
            }

            final Date currentTime = services.getDateUtilsService().getCurrentTime();

            services.getFavoritesService().addEntryToFavorites(user.getId(), randomUser.getId(), currentTime, favoriteEntryType);
            getLog().info(String.format("Member %s has added member %s to %s", user, randomUser, favoriteEntryType));

            services.getUsersSecurityService().saveLastUserActivityTime(user.getId(), currentTime);

            return true;
        }

        protected boolean addFavoritePhoto(final FavoriteEntryType favoriteEntryType) {
            final Photo randomPhoto = services.getRandomUtilsService().getRandomPhotoButNotOfUser(user, FavoritesJob.this.beingProcessedPhotosIds);

            if (randomPhoto == null) {
                return false;
            }

            if (services.getFavoritesService().isEntryInFavorites(user.getId(), randomPhoto.getId(), favoriteEntryType.getId())) {
                return false;
            }

            final Date currentTime = services.getDateUtilsService().getCurrentTime();

            services.getFavoritesService().addEntryToFavorites(user.getId(), randomPhoto.getId(), currentTime, favoriteEntryType);
            getLog().info(String.format("User %s has added photo %sd to %s", user, randomPhoto, favoriteEntryType));

            services.getUsersSecurityService().saveLastUserActivityTime(user.getId(), currentTime);

            return true;
        }

        protected AbstractFavoriteEntryGenerator(final User user) {
            this.user = user;
        }
    }

    private class FavoriteUserEntryGenerator extends AbstractFavoriteEntryGenerator {

        protected FavoriteUserEntryGenerator(final User user) {
            super(user);
        }

        @Override
        public boolean generateFavoriteEntry() {
            return addFavoriteUser(FavoriteEntryType.FAVORITE_MEMBERS);
        }
    }

    private class FriendEntryGenerator extends AbstractFavoriteEntryGenerator {

        protected FriendEntryGenerator(final User user) {
            super(user);
        }

        @Override
        public boolean generateFavoriteEntry() {
            return addFavoriteUser(FavoriteEntryType.FRIENDS);
        }
    }

    private class BlacklistEntryGenerator extends AbstractFavoriteEntryGenerator {

        protected BlacklistEntryGenerator(final User user) {
            super(user);
        }

        @Override
        public boolean generateFavoriteEntry() {
            return addFavoriteUser(FavoriteEntryType.BLACKLIST);
        }
    }

    private class NewPhotoNotificationEntryGenerator extends AbstractFavoriteEntryGenerator {

        protected NewPhotoNotificationEntryGenerator(final User user) {
            super(user);
        }

        @Override
        public boolean generateFavoriteEntry() {
            return addFavoriteUser(FavoriteEntryType.NEW_PHOTO_NOTIFICATION);
        }
    }

    private class FavoritePhotoEntryGenerator extends AbstractFavoriteEntryGenerator {

        public FavoritePhotoEntryGenerator(final User user) {
            super(user);
        }

        @Override
        public boolean generateFavoriteEntry() {
            return addFavoritePhoto(FavoriteEntryType.FAVORITE_PHOTOS);
        }
    }

    private class BookmarkEntryGenerator extends AbstractFavoriteEntryGenerator {

        protected BookmarkEntryGenerator(final User user) {
            super(user);
        }

        @Override
        public boolean generateFavoriteEntry() {
            return addFavoritePhoto(FavoriteEntryType.BOOKMARKED_PHOTOS);
        }
    }

    private class NewCommentNotificationEntryGenerator extends AbstractFavoriteEntryGenerator {

        protected NewCommentNotificationEntryGenerator(final User user) {
            super(user);
        }

        @Override
        public boolean generateFavoriteEntry() {
            return addFavoritePhoto(FavoriteEntryType.NEW_COMMENTS_NOTIFICATION);
        }
    }

    private class PhotoListPhotoVisibilityEntryGenerator extends AbstractFavoriteEntryGenerator {

        protected PhotoListPhotoVisibilityEntryGenerator(final User user) {
            super(user);
        }

        @Override
        public boolean generateFavoriteEntry() {
            return addFavoritePhoto(FavoriteEntryType.MEMBERS_INVISIBILITY_LIST);
        }
    }
}
