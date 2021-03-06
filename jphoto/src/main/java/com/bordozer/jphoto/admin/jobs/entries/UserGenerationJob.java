package com.bordozer.jphoto.admin.jobs.entries;

import com.bordozer.jphoto.admin.jobs.JobRuntimeEnvironment;
import com.bordozer.jphoto.admin.jobs.enums.SavedJobType;
import com.bordozer.jphoto.core.enums.SavedJobParameterKey;
import com.bordozer.jphoto.core.enums.UserGender;
import com.bordozer.jphoto.core.general.base.CommonProperty;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.userAlbums.UserPhotoAlbum;
import com.bordozer.jphoto.core.general.user.userTeam.UserTeam;
import com.bordozer.jphoto.core.general.user.userTeam.UserTeamMember;
import com.bordozer.jphoto.core.log.LogHelper;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.translator.message.TranslatableMessage;
import com.bordozer.jphoto.core.services.user.UserPhotoAlbumService;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.core.services.user.UserTeamService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.core.services.utils.PredicateUtilsService;
import com.bordozer.jphoto.core.services.utils.RandomUtilsService;
import com.bordozer.jphoto.ui.controllers.users.edit.UserEditDataController;
import com.bordozer.jphoto.utils.fakeUser.AbstractFakeMember;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public class UserGenerationJob extends AbstractJob {

    private final static int MAX_ITERATIONS = 1000;
    private final static int MAX_USER_ALBUM_QTY = 7;

    private String avatarsDir;
    public static final String AVATAR_SUBDIR_MALE = "male";
    public static final String AVATAR_SUBDIR_FEMALE = "female";

    private final String[] albumsNames = {"First attempts", "My cat", "Beauty", "She", "Random", "Just an album", "Black album", "General", "Nature", "Macro", "In studio"
            , "Sun light", "Fairy Tales", "The ocean", "Emotions", "Moonlight", "Silver Lightning", "Loneliness", "The best", "My best", "Collection"};

    public UserGenerationJob(final JobRuntimeEnvironment jobEnvironment) {
        super(new LogHelper(), jobEnvironment);
    }

    @Override
    protected void runJob() throws Throwable {

        int counter = 0;

        final File rootAvatarDir = new File(avatarsDir);
        List<File> malesAvatarImages = newArrayList();
        List<File> femalesAvatarImages = newArrayList();

        final boolean isAvatarDirExists = StringUtils.isNotEmpty(avatarsDir) && rootAvatarDir.exists();
        if (isAvatarDirExists) {
            final PredicateUtilsService predicateUtilsService = services.getPredicateUtilsService();

            final File maleAvatarsSubDir = new File(rootAvatarDir, AVATAR_SUBDIR_MALE);
            if (maleAvatarsSubDir.exists()) {
                final File[] maleAvatars = maleAvatarsSubDir.listFiles(predicateUtilsService.getFileFilter());
                malesAvatarImages = Arrays.asList(maleAvatars);
            }

            final File femaleAvatarsSubDir = new File(rootAvatarDir, AVATAR_SUBDIR_FEMALE);
            if (femaleAvatarsSubDir.exists()) {
                final File[] femaleAvatars = femaleAvatarsSubDir.listFiles(predicateUtilsService.getFileFilter());
                femalesAvatarImages = Arrays.asList(femaleAvatars);
            }
        }

        final UserService userService = services.getUserService();

        while (!isFinished()) {

            final AbstractFakeMember fakeMember = services.getFakeUserService().getRandomFakeMember();
            final User user = services.getFakeUserService().getRandomUser(fakeMember);

            if (!isRegistrationDataBusy(user, beingProcessedUsers)) {
                try {
                    final boolean isCreated = userService.createUser(user, UserEditDataController.DEFAULT_USER_PASSWORD);
                    if (!isCreated) {
                        //						final String message = services.getTranslatorService().translate( "Error creating user $1", getLanguage(), user.getNameEscaped() );
                        final TranslatableMessage translatableMessage = new TranslatableMessage("Error creating user $1", services)
                                .string(user.getNameEscaped());
                        addJobRuntimeLogMessage(translatableMessage);

                        getLog().error(String.format("Error creating user %s", user));
                        continue;
                    }

                    beingProcessedUsers.add(user);

                    addUserTeamMembers(user, fakeMember);

                    addUserPhotoAlbums(user);

                    if (isAvatarDirExists) {
                        final File randomAvatarFile = getRandomAvatarFile(user.getGender() == UserGender.MALE ? malesAvatarImages : femalesAvatarImages);
                        if (randomAvatarFile != null) {
                            userService.saveAvatar(user.getId(), randomAvatarFile);
                        }
                    }

                    getLog().info(String.format("User generated: %s", user));

                    increment();
                    counter = 0;

                } catch (DuplicateKeyException e) {
                    getLog().info("User generating DuplicateKeyException - IGNORED");
                }
            }

            counter++;

            if (counter > MAX_ITERATIONS) {
                getLog().info("User generating: max iteration number reached...");
                break;
            }

            if (hasJobFinishedWithAnyResult()) {
                break;
            }
        }

        getLog().info("User generating finished");

    }

    private void addUserTeamMembers(final User user, final AbstractFakeMember fakeMember) {
        final UserTeamService userTeamService = services.getUserTeamService();

        final UserTeam userTeam = services.getFakeUserService().getRandomUserTeam(user, fakeMember);
        for (final UserTeamMember userTeamMember : userTeam.getUserTeamMembers()) {
            if (userTeamService.loadUserTeamMemberByName(user.getId(), userTeamMember.getName()) != null) {
                userTeamService.save(userTeamMember);
            }
        }
    }

    private void addUserPhotoAlbums(final User user) {

        log.debug(String.format("Creating user photo albums: %s", user));

        final UserPhotoAlbumService userPhotoAlbumService = services.getUserPhotoAlbumService();

        final DateUtilsService dateUtilsService = services.getDateUtilsService();

        final RandomUtilsService randomUtilsService = services.getRandomUtilsService();
        final int randomAlbumQty = randomUtilsService.getRandomInt(0, MAX_USER_ALBUM_QTY);

        for (int i = 0; i < randomAlbumQty; i++) {
            final String albumName = String.format("%s - %d", randomUtilsService.getRandomStringArrayElement(albumsNames), randomUtilsService.getRandomInt(1000, 9999));

            if (doesUserHaveAlbumWithTheName(user, albumName)) {
                continue;
            }

            final UserPhotoAlbum userphotoAlbum = new UserPhotoAlbum();
            userphotoAlbum.setUser(user);
            userphotoAlbum.setName(albumName);
            userphotoAlbum.setDescription(String.format("Generated automatically by User generation job, %s %s", dateUtilsService.formatDate(dateUtilsService.getCurrentDate()), dateUtilsService.formatTime(dateUtilsService.getCurrentTime())));

            log.debug(String.format("User %s: photo albums '%s' is about tobe created", user, userphotoAlbum));

            userPhotoAlbumService.save(userphotoAlbum);
        }
    }

    private boolean doesUserHaveAlbumWithTheName(final User user, final String name) {
        final UserPhotoAlbumService userPhotoAlbumService = services.getUserPhotoAlbumService();

        final List<UserPhotoAlbum> userPhotoAlbums = userPhotoAlbumService.loadAllForEntry(user.getId());
        for (final UserPhotoAlbum album : userPhotoAlbums) {
            if (album.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    private File getRandomAvatarFile(final List<File> avatarFileNames) {
        return services.getRandomUtilsService().getRandomGenericListElement(avatarFileNames);
    }

    private boolean isRegistrationDataBusy(final User user, final List<User> users) {
        for (User existingUser : users) {
            if (existingUser.getLogin().equals(user.getLogin()) || existingUser.getName().equals(user.getName()) || existingUser.getEmail().equals(user.getEmail())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public SavedJobType getJobType() {
        return SavedJobType.USER_GENERATION;
    }

    @Override
    public Map<SavedJobParameterKey, CommonProperty> getParametersMap() {
        final DateUtilsService dateUtilsService = services.getDateUtilsService();

        final Map<SavedJobParameterKey, CommonProperty> parametersMap = newHashMap();

        parametersMap.put(SavedJobParameterKey.PARAM_USER_QTY, new CommonProperty(SavedJobParameterKey.PARAM_USER_QTY.getId(), totalJopOperations));
        parametersMap.put(SavedJobParameterKey.PARAM_AVATARS_DIR, new CommonProperty(SavedJobParameterKey.PARAM_AVATARS_DIR.getId(), avatarsDir));

        return parametersMap;
    }

    @Override
    public String getJobParametersDescription() {
        final TranslatorService translatorService = services.getTranslatorService();

        final StringBuilder builder = new StringBuilder();

        builder.append(translatorService.translate("User generate job parameter: Users to generate", getLanguage())).append(": ").append(totalJopOperations).append("<br />");
        builder.append(translatorService.translate("User generate job parameter: Avatars dir", getLanguage())).append(": ").append(avatarsDir);

        return builder.toString();
    }

    @Override
    public void initJobParameters(final Map<SavedJobParameterKey, CommonProperty> jobParameters) {
        totalJopOperations = jobParameters.get(SavedJobParameterKey.PARAM_USER_QTY).getValueInt();
        avatarsDir = jobParameters.get(SavedJobParameterKey.PARAM_AVATARS_DIR).getValue();
    }

    public void setAvatarsDir(final String avatarsDir) {
        this.avatarsDir = avatarsDir;
    }
}
