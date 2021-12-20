package com.bordozer.jphoto.rest.photo.upload.category.allowance;

import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserStatus;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.message.TranslatableMessage;
import com.bordozer.jphoto.utils.NumberUtils;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public abstract class AbstractPhotoUploadAllowance {

    private final User photoAuthor;
    private User accessor;
    private Genre genre;

    protected Language language;
    protected Services services;

    private List<Photo> uploadThisWeekPhotos;
    private boolean userCanUploadPhoto = true;
    private Date nextPhotoUploadTime;

    private boolean skipGenreSelectionInfo;
    private long fileSize;

    public abstract UserStatus getUserStatus();

    public abstract int getMaxPhotoSize();

    public abstract int getDailyLimitPhotosQty();

    public abstract int getWeeklyLimitPhotosQty();

    public abstract int getDailyLimitUploadSize();

    public abstract int getWeeklyLimitUploadSize();

    public AbstractPhotoUploadAllowance(final User photoAuthor, final User accessor, final Language language, final Services services) {
        this.photoAuthor = photoAuthor;
        this.accessor = accessor;
        this.language = language;
        this.services = services;
    }

    public List<PhotoUploadDescription> getUploadAllowance() {
        final List<PhotoUploadDescription> photoUploadDescriptions = newArrayList();

        addMaxPhotoSizeDescription(photoUploadDescriptions);

        addDailyPhotosQtyDescription(photoUploadDescriptions);

        addWeeklyPhotosQtyDescription(photoUploadDescriptions);

        addDailyPhotosSizeDescription(photoUploadDescriptions);

        addWeeklyPhotosSizeDescription(photoUploadDescriptions);

        final int weeklyLimitUploadSize = getWeeklyLimitUploadSize();

        final int weeklyLimitIncreasingPerRank = services.getConfigurationService().getInt(ConfigurationKey.PHOTO_UPLOAD_ADDITIONAL_SIZE_WEEKLY_LIMIT_PER_RANK_KB);
        if (weeklyLimitUploadSize > 0 && weeklyLimitIncreasingPerRank > 0 && !skipGenreSelectionInfo && userCanUploadPhoto && weeklyLimitUploadSize > 0) {
            final TranslatableMessage translatableMessage = new TranslatableMessage("Each rank in a genre except first one increases your weekly limit on $1 Kb. Please, select a genre to see full photo upload allowance.", services)
                    .addIntegerParameter(weeklyLimitIncreasingPerRank);
            final PhotoUploadDescription description = new PhotoUploadDescription();
            description.setUploadRuleDescription(translatableMessage.build(language));
            photoUploadDescriptions.add(description);
        }

        if (genre != null) {

            addAdditionalWeeklyKbByGenreDescription(photoUploadDescriptions);

            addResultWeeklyPhotosSizeDescription(photoUploadDescriptions);

            if (fileSize > 0 && weeklyLimitUploadSize > 0) {
                addVerdictAboutFileUploading(photoUploadDescriptions);
            }
        }

        if (weeklyLimitUploadSize == 0) {
            addVerdictAboutFileUploading(photoUploadDescriptions);
        }

        return photoUploadDescriptions;
    }

    public float getSummaryFilesSizeUploadedThisWeek() {
        float result = 0;
        for (final Photo photo : uploadThisWeekPhotos) {
            result += photo.getFileSize();
        }
        return result;
    }

    private void addVerdictAboutFileUploading(final List<PhotoUploadDescription> photoUploadDescriptions) {

        if (!userCanUploadPhoto) {
            return;
        }

        final float fileSizeInKb = services.getImageFileUtilsService().getFileSizeInKb(fileSize);

        final TranslatableMessage translatableMessage = new TranslatableMessage("Being uploaded file size is $1 Kb", services)
                .addFloatParameter(fileSizeInKb);

        final int weeklyLimitUploadSize = getWeeklyLimitUploadSize();
        final float allowedToUploadThisWeekSize = weeklyLimitUploadSize > 0 ? getAllowedToUploadThisWeekSize() : 0;
        userCanUploadPhoto &= weeklyLimitUploadSize == 0 || (fileSizeInKb <= allowedToUploadThisWeekSize);

        translatableMessage.string(" ");
        if (userCanUploadPhoto) {
            translatableMessage.translatableString("The file can be uploaded");
        } else {
            final float diff = NumberUtils.round(fileSizeInKb - allowedToUploadThisWeekSize, 1);
            final TranslatableMessage message = new TranslatableMessage("The file can not be uploaded. You need more $1 Kb.", services).addFloatParameter(diff);
            translatableMessage.addTranslatableMessageParameter(message);
        }

        final PhotoUploadDescription description = new PhotoUploadDescription();
        description.setUploadRuleDescription(translatableMessage.build(language));
        description.setPassed(userCanUploadPhoto);

        photoUploadDescriptions.add(description);
    }

    private void addMaxPhotoSizeDescription(final List<PhotoUploadDescription> photoUploadDescriptions) {

        final PhotoUploadDescription uploadDescription = new PhotoUploadDescription();

        final TranslatableMessage translatableMessage = new TranslatableMessage("Max photo's size you can upload is $1 $2.", services)
                .addIntegerParameter(getMaxPhotoSize())
                .translatableString(ConfigurationKey.PHOTO_UPLOAD_ADDITIONAL_SIZE_WEEKLY_LIMIT_PER_RANK_KB.getUnit().getName());

        uploadDescription.setUploadRuleDescription(translatableMessage.build(language));

        photoUploadDescriptions.add(uploadDescription);
    }

    private void addDailyPhotosQtyDescription(final List<PhotoUploadDescription> photoUploadDescriptions) {
        final String period1 = "photo uploading: today";
        final String period2 = "photo uploading: day";
        final int limitPhotosQty = getDailyLimitPhotosQty();
        final List<Integer> uploadedPhotosIds = services.getPhotoUploadService().getUploadedTodayPhotosIds(photoAuthor);

        addPhotoQtyLimitDescription(photoUploadDescriptions, period1, period2, limitPhotosQty, uploadedPhotosIds.size(), services.getDateUtilsService().getFirstSecondOfTomorrow());
    }

    private void addWeeklyPhotosQtyDescription(final List<PhotoUploadDescription> photoUploadDescriptions) {

        if (!userCanUploadPhoto) {
            return;
        }

        final String period1 = "photo uploading: this week";
        final String period2 = "photo uploading: week";
        final int limitPhotosQty = getWeeklyLimitPhotosQty();
        final List<Integer> uploadedPhotosIds = services.getPhotoUploadService().getUploadedThisWeekPhotosIds(photoAuthor);

        addPhotoQtyLimitDescription(photoUploadDescriptions, period1, period2, limitPhotosQty, uploadedPhotosIds.size(), services.getDateUtilsService().getFirstSecondOfNextMonday());
    }

    private void addPhotoQtyLimitDescription(final List<PhotoUploadDescription> photoUploadDescriptions, final String period1, final String period2, final int limitPhotosQty, final int uploadedPhotosQty, final Date nextVotingTime) {
        if (limitPhotosQty > 0) {

            final Language language = accessor.getLanguage();

            final PhotoUploadDescription uploadDescription = new PhotoUploadDescription();

            final TranslatableMessage translatableMessage = new TranslatableMessage("Your status '$5' limit is $1 photo(s) per $2. You uploaded $3 photo(s) $4.", services)
                    .addIntegerParameter(limitPhotosQty)
                    .translatableString(period2)
                    .addIntegerParameter(uploadedPhotosQty)
                    .translatableString(period1)
                    .translatableString(accessor.getUserStatus().getName());
            if (userCanUploadPhoto) {
                translatableMessage.string(" ");
                final int canBeUploadedPhotos = limitPhotosQty - uploadedPhotosQty;
                if (canBeUploadedPhotos > 0) {
                    final TranslatableMessage message = new TranslatableMessage("You can upload $1 photo(s) more $2.", services)
                            .addIntegerParameter(canBeUploadedPhotos)
                            .translatableString(period1);
                    translatableMessage.addTranslatableMessageParameter(message);
                } else {
                    final TranslatableMessage message = new TranslatableMessage("You can not upload photo.", services)
                            //						.translatableString( period2 )
                            ;
                    translatableMessage.addTranslatableMessageParameter(message);
                    uploadDescription.setPassed(false);
                    userCanUploadPhoto = false;
                    setNextPhotoUploadTime(nextVotingTime);
                }
            }

            uploadDescription.setUploadRuleDescription(translatableMessage.build(language));

            photoUploadDescriptions.add(uploadDescription);
        }
    }

    private void addDailyPhotosSizeDescription(final List<PhotoUploadDescription> photoUploadDescriptions) {
        final String period1 = "photo uploading: today";
        final String period2 = "photo uploading: day";
        final int uploadSizeLimit = getDailyLimitUploadSize();
        final long uploadedSummarySize = services.getPhotoUploadService().getUploadedTodayPhotosSummarySize(photoAuthor);

        getPhotoSizeLimitDescription(photoUploadDescriptions, period1, period2, uploadSizeLimit, services.getImageFileUtilsService().getFileSizeInKb(uploadedSummarySize), services.getDateUtilsService().getFirstSecondOfTomorrow());
    }

    private void addWeeklyPhotosSizeDescription(final List<PhotoUploadDescription> photoUploadDescriptions) {

        if (!userCanUploadPhoto) {
            return;
        }

        final String period1 = "photo uploading: this week";
        final String period2 = "photo uploading: week";
        final int uploadSizeLimit = getWeeklyLimitUploadSize();

        final float fileSizeInKb = getUploadedThisWeekInKB();

        getPhotoSizeLimitDescription(photoUploadDescriptions, period1, period2, uploadSizeLimit, fileSizeInKb, services.getDateUtilsService().getFirstSecondOfNextMonday());
    }

    private void getPhotoSizeLimitDescription(final List<PhotoUploadDescription> photoUploadDescriptions, final String period1, final String period2, final int uploadSizeLimit, final float uploadedSummarySize, final Date nextVotingTime) {

        if (uploadSizeLimit > 0) {
            final PhotoUploadDescription uploadDescription = new PhotoUploadDescription();

            final TranslatableMessage translatableMessage = new TranslatableMessage("Your status '$1' limit is $2 $3 per $4. You uploaded $5 $6 $7.", services)
                    .translatableString(accessor.getUserStatus().getName())
                    .addIntegerParameter(uploadSizeLimit)
                    .translatableString(ConfigurationKey.CANDIDATES_DAILY_FILE_SIZE_LIMIT.getUnit().getName())
                    .translatableString(period1)
                    .addFloatParameter(uploadedSummarySize)
                    .translatableString(ConfigurationKey.CANDIDATES_DAILY_FILE_SIZE_LIMIT.getUnit().getName())
                    .translatableString(period1);

            if (userCanUploadPhoto) {
                translatableMessage.string(" ");
                final float canUploadKb = NumberUtils.round(uploadSizeLimit - uploadedSummarySize, 1);
                if (canUploadKb > 0) {
                    final TranslatableMessage message = new TranslatableMessage("You can upload $1 Kb more $2.", services)
                            .addFloatParameter(canUploadKb)
                            .translatableString(period1);
                    translatableMessage.addTranslatableMessageParameter(message);
                } else {
                    final TranslatableMessage message = new TranslatableMessage("You can not upload photo $1.", services)
                            .translatableString(period2);
                    translatableMessage.addTranslatableMessageParameter(message);
                    uploadDescription.setPassed(false);
                    userCanUploadPhoto = false;
                    setNextPhotoUploadTime(nextVotingTime);
                }
            }

            uploadDescription.setUploadRuleDescription(translatableMessage.build(language));

            photoUploadDescriptions.add(uploadDescription);
        }
    }

    private void addAdditionalWeeklyKbByGenreDescription(final List<PhotoUploadDescription> photoUploadDescriptions) {

        if (!userCanUploadPhoto) {
            return;
        }

        if (getWeeklyLimitUploadSize() == 0) {
            return;
        }

        final int additionalWeeklyLimitPerGenreRank = services.getConfigurationService().getInt(ConfigurationKey.PHOTO_UPLOAD_ADDITIONAL_SIZE_WEEKLY_LIMIT_PER_RANK_KB);
        if (additionalWeeklyLimitPerGenreRank > 0) {

            final Language language = accessor.getLanguage();

            final int userRankInGenre = services.getUserRankService().getUserRankInGenre(photoAuthor.getId(), genre.getId());

            final PhotoUploadDescription uploadDescription = new PhotoUploadDescription();

            final TranslatableMessage translatableMessage = new TranslatableMessage("Your rank in genre '$1' is $2.", services)
                    .addPhotosByUserByGenreLinkParameter(accessor, genre)
                    .addIntegerParameter(userRankInGenre)
                    .string(" ");

            if (userRankInGenre > 0) {
                final TranslatableMessage message = new TranslatableMessage("So it gives you possibility to upload on $1 Kb more this week.", services)
                        .addIntegerParameter(getUserRankAdditionalBonuses());
                translatableMessage.addTranslatableMessageParameter(message);
            } else {
                translatableMessage.translatableString("So it is too small yet to give you any bonuses.");
            }

            uploadDescription.setUploadRuleDescription(translatableMessage.build(language));

            photoUploadDescriptions.add(uploadDescription);
        }
    }

    private void addResultWeeklyPhotosSizeDescription(final List<PhotoUploadDescription> photoUploadDescriptions) {

        if (!userCanUploadPhoto) {
            return;
        }

        if (getWeeklyLimitUploadSize() == 0) {
            return;
        }

        final float totalWeekLimitKb = NumberUtils.round(getAllowedToUploadThisWeekSize(), 1);

        final TranslatableMessage translatableMessage = new TranslatableMessage("You can upload $1 Kb this week in category '$2'.", services)
                .addFloatParameter(totalWeekLimitKb)
                .addPhotosByUserByGenreLinkParameter(accessor, genre);

        final PhotoUploadDescription description = new PhotoUploadDescription();
        description.setUploadRuleDescription(translatableMessage.build(language));

        photoUploadDescriptions.add(description);
    }

    private float getAllowedToUploadThisWeekSize() {
        return getUserRankAdditionalBonuses() + getWeeklyLimitUploadSize() - getUploadedThisWeekInKB();
    }

    private int getUserRankAdditionalBonuses() {
        final int userRankInGenre = services.getUserRankService().getUserRankInGenre(photoAuthor.getId(), genre.getId());
        final int additionalWeeklyLimitPerGenreRank = services.getConfigurationService().getInt(ConfigurationKey.PHOTO_UPLOAD_ADDITIONAL_SIZE_WEEKLY_LIMIT_PER_RANK_KB);

        return userRankInGenre * additionalWeeklyLimitPerGenreRank;
    }

    private float getUploadedThisWeekInKB() {
        return services.getImageFileUtilsService().getFileSizeInKb(services.getPhotoUploadService().getUploadedThisWeekPhotosSummarySize(photoAuthor));
    }

    private void setNextPhotoUploadTime(final Date nextTime) {
        if (nextPhotoUploadTime == null || nextPhotoUploadTime.getTime() < nextTime.getTime()) {
            nextPhotoUploadTime = nextTime;
        }
    }

    public User getPhotoAuthor() {
        return photoAuthor;
    }

    public List<Photo> getUploadThisWeekPhotos() {
        return uploadThisWeekPhotos;
    }

    public void setUploadThisWeekPhotos(final List<Photo> uploadThisWeekPhotos) {
        this.uploadThisWeekPhotos = uploadThisWeekPhotos;
    }

    public boolean isUserCanUploadPhoto() {
        return userCanUploadPhoto;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(final Genre genre) {
        this.genre = genre;
    }

    public Date getNextPhotoUploadTime() {
        return nextPhotoUploadTime;
    }

    public boolean isSkipGenreSelectionInfo() {
        return skipGenreSelectionInfo;
    }

    public void setSkipGenreSelectionInfo(final boolean skipGenreSelectionInfo) {
        this.skipGenreSelectionInfo = skipGenreSelectionInfo;
    }

    public void setFileSize(final long fileSize) {
        this.fileSize = fileSize;
    }

    public long getFileSize() {
        return fileSize;
    }
}
