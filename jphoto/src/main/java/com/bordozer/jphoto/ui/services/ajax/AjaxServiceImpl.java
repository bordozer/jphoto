package com.bordozer.jphoto.ui.services.ajax;

import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.AbstractRemotePhotoSitePageContentDataExtractor;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.AbstractRemotePhotoSiteUrlHelper;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteImportStrategy;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteUserDTO;
import com.bordozer.jphoto.admin.jobs.enums.JobExecutionStatus;
import com.bordozer.jphoto.admin.jobs.general.JobProgressDTO;
import com.bordozer.jphoto.admin.services.jobs.JobExecutionHistoryEntry;
import com.bordozer.jphoto.admin.services.jobs.JobExecutionHistoryService;
import com.bordozer.jphoto.core.enums.FavoriteEntryType;
import com.bordozer.jphoto.core.enums.PrivateMessageType;
import com.bordozer.jphoto.core.enums.RestrictionType;
import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.message.PrivateMessage;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.PhotoComment;
import com.bordozer.jphoto.core.general.restriction.RestrictionTimeUnit;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.interfaces.Restrictable;
import com.bordozer.jphoto.core.log.LogHelper;
import com.bordozer.jphoto.core.services.entry.ActivityStreamService;
import com.bordozer.jphoto.core.services.entry.FavoritesService;
import com.bordozer.jphoto.core.services.entry.PrivateMessageService;
import com.bordozer.jphoto.core.services.notification.NotificationService;
import com.bordozer.jphoto.core.services.photo.PhotoCommentService;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.security.RestrictionService;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.core.services.utils.EntityLinkUtilsService;
import com.bordozer.jphoto.core.services.utils.UrlUtilsService;
import com.bordozer.jphoto.core.services.utils.UserPhotoFilePathUtilsService;
import com.bordozer.jphoto.rest.users.picker.UserDTO;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.dtos.AjaxResultDTO;
import com.bordozer.jphoto.ui.dtos.CommentDTO;
import com.bordozer.jphoto.ui.dtos.ComplaintMessageDTO;
import com.bordozer.jphoto.ui.dtos.PrivateMessageSendingDTO;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuType;
import com.bordozer.jphoto.ui.services.menu.entry.items.comment.ComplaintReasonType;
import com.bordozer.jphoto.utils.StringUtilities;
import com.bordozer.jphoto.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

@Service("ajaxService")
public class AjaxServiceImpl implements AjaxService {

    @Autowired
    private UserService userService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private EntityLinkUtilsService entityLinkUtilsService;

    @Autowired
    private UrlUtilsService urlUtilsService;

    @Autowired
    private FavoritesService favoritesService;

    @Autowired
    private TranslatorService translatorService;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private ActivityStreamService activityStreamService;

    @Autowired
    private DateUtilsService dateUtilsService;

    @Autowired
    private PrivateMessageService privateMessageService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private PhotoCommentService photoCommentService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserPhotoFilePathUtilsService userPhotoFilePathUtilsService;

    @Autowired
    private RestrictionService restrictionService;

    @Autowired
    private JobExecutionHistoryService jobExecutionHistoryService;

    private final LogHelper log = new LogHelper();

    @Override
    public AjaxResultDTO sendComplaintMessageAjax(final ComplaintMessageDTO complaintMessageDTO) {
        final int complaintEntityTypeId = complaintMessageDTO.getComplaintEntityTypeId();
        final int entryId = complaintMessageDTO.getEntryId();
        final int fromUserId = complaintMessageDTO.getFromUserId();
        final int complaintReasonTypeId = complaintMessageDTO.getComplaintReasonTypeId();
        final String customDescription = complaintMessageDTO.getCustomDescription();

        final EntryMenuType entryMenuType = EntryMenuType.getById(complaintEntityTypeId);
        final ComplaintReasonType complaintReasonType = ComplaintReasonType.getById(complaintReasonTypeId);

        final AjaxResultDTO resultDTO = AjaxResultDTO.successResult();
        return resultDTO;
    }

    @Override
    public RemotePhotoSiteUserDTO getRemoteUserDTO(final String _remoteUserId, final String _importSourceId) {

        if (StringUtils.isEmpty(_remoteUserId)) {
            final RemotePhotoSiteUserDTO remotePhotoSiteUserDTO = new RemotePhotoSiteUserDTO("0");
            remotePhotoSiteUserDTO.setRemoteUserFound(false);
            return remotePhotoSiteUserDTO;
        }

        final RemotePhotoSiteUserDTO remotePhotoSiteUserDTO = new RemotePhotoSiteUserDTO(_remoteUserId);

        final String userId = String.valueOf(_remoteUserId);

        final PhotosImportSource importSource = PhotosImportSource.getById(_importSourceId);

        final AbstractRemotePhotoSiteUrlHelper remoteContentHelper = AbstractRemotePhotoSiteUrlHelper.getInstance(importSource);
        final String remoteUserName = remoteContentHelper.extractUserNameFromRemoteSite(userId);
        final String remoteUserCardUrl = remoteContentHelper.getUserCardUrl(userId);

        remotePhotoSiteUserDTO.setRemoteUserName(remoteUserName);
        remotePhotoSiteUserDTO.setRemoteUserCardUrl(remoteUserCardUrl);

        final boolean photosightUserFound = StringUtils.isNotEmpty(remoteUserName);
        remotePhotoSiteUserDTO.setRemoteUserFound(photosightUserFound);

        if (photosightUserFound) {
            remotePhotoSiteUserDTO.setRemoteUserPhotosCount(AbstractRemotePhotoSitePageContentDataExtractor.getInstance(importSource).extractRemotePhotoSiteUserPhotosCount(_remoteUserId));
        }

        final String userLogin = RemotePhotoSiteImportStrategy.createLoginForRemotePhotoSiteUser(userId);
        final User user = userService.loadByLogin(userLogin);
        final boolean userExistsInTheSystem = user != null;
        remotePhotoSiteUserDTO.setRemoteUserExistsInTheSystem(userExistsInTheSystem);

        if (userExistsInTheSystem) {
            remotePhotoSiteUserDTO.setUserCardLink(entityLinkUtilsService.getUserCardLink(user, EnvironmentContext.getLanguage()));
            remotePhotoSiteUserDTO.setPhotosCount(photoService.getPhotosCountByUser(user.getId()));
            remotePhotoSiteUserDTO.setUserPhotosUrl(urlUtilsService.getPhotosByUserLink(user.getId()));
            remotePhotoSiteUserDTO.setUserGender(user.getGender());
            remotePhotoSiteUserDTO.setUserMembershipType(user.getMembershipType());
        }

        return remotePhotoSiteUserDTO;
    }

    @Override
    public AjaxResultDTO addEntryToFavoritesAjax(final int userId, final int favoriteEntryId, final int entryTypeId) {
        boolean isSuccessful;
        String message = "";

        // TODO: change a test if exists
        if (!UserUtils.isLoggedUser(userId)) {
            final AjaxResultDTO ajaxResultDTO = new AjaxResultDTO();
            ajaxResultDTO.setSuccessful(false);
            ajaxResultDTO.setMessage(translatorService.translate("You are not logged in", EnvironmentContext.getLanguage()));

            return ajaxResultDTO;
        }

        // TODO: make a test
        if (!UserUtils.isTheUserThatWhoIsCurrentUser(userId)) {
            final AjaxResultDTO ajaxResultDTO = new AjaxResultDTO();
            ajaxResultDTO.setSuccessful(false);
            ajaxResultDTO.setMessage(translatorService.translate("Wrong account", EnvironmentContext.getLanguage()));

            return ajaxResultDTO;
        }

        if (favoritesService.isEntryInFavorites(userId, favoriteEntryId, entryTypeId)) {
            isSuccessful = false;
            message = translatorService.translate("Entry is already in your favorites", EnvironmentContext.getLanguage());
        } else {
            final FavoriteEntryType entryType = FavoriteEntryType.getById(entryTypeId);
            final Date currentTime = dateUtilsService.getCurrentTime();
            isSuccessful = favoritesService.addEntryToFavorites(userId, favoriteEntryId, currentTime, entryType);
            if (configurationService.getBoolean(ConfigurationKey.SYSTEM_ACTIVITY_LOG_FAVORITE_ACTIONS)) {
                activityStreamService.saveFavoriteAction(userId, favoriteEntryId, currentTime, entryType);
            }
        }

        final AjaxResultDTO ajaxResultDTO = new AjaxResultDTO();
        ajaxResultDTO.setSuccessful(isSuccessful);
        ajaxResultDTO.setMessage(message);

        return ajaxResultDTO;
    }

    @Override
    public AjaxResultDTO removeEntryFromFavoritesAjax(final int userId, final int favoriteEntryId, final int entryTypeId) {
        boolean isSuccessful;
        String message = "";

        // TODO: change a test if exists
        if (!UserUtils.isLoggedUser(userId)) {
            final AjaxResultDTO ajaxResultDTO = new AjaxResultDTO();
            ajaxResultDTO.setSuccessful(false);
            ajaxResultDTO.setMessage(translatorService.translate("You are not logged in", EnvironmentContext.getLanguage()));

            return ajaxResultDTO;
        }

        // TODO: make a test
        if (!UserUtils.isTheUserThatWhoIsCurrentUser(userId)) {
            final AjaxResultDTO ajaxResultDTO = new AjaxResultDTO();
            ajaxResultDTO.setSuccessful(false);
            ajaxResultDTO.setMessage(translatorService.translate("Wrong account", EnvironmentContext.getLanguage()));

            return ajaxResultDTO;
        }

        if (!favoritesService.isEntryInFavorites(userId, favoriteEntryId, entryTypeId)) {
            isSuccessful = false;
            message = translatorService.translate("Entry is NOT in your favorites", EnvironmentContext.getLanguage());
        } else {
            isSuccessful = favoritesService.removeEntryFromFavorites(userId, favoriteEntryId, FavoriteEntryType.getById(entryTypeId));
        }

        final AjaxResultDTO ajaxResultDTO = new AjaxResultDTO();
        ajaxResultDTO.setSuccessful(isSuccessful);
        ajaxResultDTO.setMessage(message);

        return ajaxResultDTO;
    }

    @Override
    public AjaxResultDTO sendPrivateMessageAjax(final PrivateMessageSendingDTO messageDTO) {

        final Language language = EnvironmentContext.getLanguage();

        if (!UserUtils.isCurrentUserLoggedUser()) {
            return AjaxResultDTO.failResult(translatorService.translate("You are not logged in", language));
        }

        final int fromUserId = messageDTO.getFromUserId();
        final User fromUser = userService.load(fromUserId);
        if (fromUser == null) {
            return AjaxResultDTO.failResult(translatorService.translate("Member FROM not found", language));
        }

        if (!UserUtils.isTheUserThatWhoIsCurrentUser(fromUser)) {
            return AjaxResultDTO.failResult(translatorService.translate("Attempt to send the message from another account. It seems you have changed your account after loading of this page, haven't you?", language));
        }

        final int toUserId = messageDTO.getToUserId();
        final User toUser = userService.load(toUserId);
        if (toUser == null) {
            return AjaxResultDTO.failResult(translatorService.translate("Member you are trying to send message not found", language));
        }

        if (UserUtils.isUsersEqual(fromUser, toUser)) {
            return AjaxResultDTO.failResult(translatorService.translate("You can not send message to yourself", language));
        }

        final String privateMessageText = messageDTO.getPrivateMessageText();
        if (StringUtils.isEmpty(privateMessageText)) {
            return AjaxResultDTO.failResult(translatorService.translate("Message text should not be empty", language));
        }

        final PrivateMessage privateMessageOut = new PrivateMessage();
        privateMessageOut.setFromUser(fromUser);
        privateMessageOut.setToUser(toUser);
        privateMessageOut.setMessageText(privateMessageText);
        privateMessageOut.setPrivateMessageType(PrivateMessageType.USER_PRIVATE_MESSAGE_OUT);
        privateMessageOut.setCreationTime(dateUtilsService.getCurrentTime());

        final boolean isSuccessfulOut = privateMessageService.save(privateMessageOut);

        final AjaxResultDTO resultDTO = new AjaxResultDTO();
        resultDTO.setSuccessful(isSuccessfulOut);

        if (!isSuccessfulOut) {
            resultDTO.setMessage(translatorService.translate("Error saving OUT message to DB", language));

            return resultDTO;
        }

        final PrivateMessage privateMessageIn = new PrivateMessage(privateMessageOut);
        privateMessageIn.setPrivateMessageType(PrivateMessageType.USER_PRIVATE_MESSAGE_IN);
        privateMessageIn.setOutPrivateMessageId(privateMessageOut.getId());

        final boolean isSuccessfulIn = privateMessageService.save(privateMessageIn);

        resultDTO.setSuccessful(isSuccessfulIn);

        if (!isSuccessfulIn) {
            resultDTO.setMessage(translatorService.translate("Error saving IN message to DB", language));
            privateMessageService.delete(privateMessageOut.getId());
        }

        if (resultDTO.isSuccessful()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    notificationService.newPrivateMessage(privateMessageOut);
                }
            }).start();
        }

        return resultDTO;
    }

    @Override
    public CommentDTO markCommentAsDeletedAjax(final int userId, final int commentId) { // TODO: move to AJAX service

        if (!securityService.userCanDeletePhotoComment(userId, commentId)) {
            final CommentDTO commentDTO = new CommentDTO(commentId);
            commentDTO.setErrorMessage(translatorService.translate("You do not have permission to delete this comment", EnvironmentContext.getLanguage()));

            return commentDTO;
        }

        final User userWhoIsDeletingComment = EnvironmentContext.getCurrentUser();

        final PhotoComment comment = photoCommentService.load(commentId);

        if (comment.isCommentDeleted()) {
            final CommentDTO commentDTO = new CommentDTO(commentId);
            commentDTO.setErrorMessage(translatorService.translate("The comment has already been deleted", EnvironmentContext.getLanguage()));

            return commentDTO;
        }

        final PhotoComment deletedComment = new PhotoComment(comment);
        deletedComment.setId(commentId);
        deletedComment.setCommentDeleted(true);

        final Photo photo = photoService.load(comment.getPhotoId());

        String userRole = "";
        if (securityService.isSuperAdminUser(userWhoIsDeletingComment.getId())) {
            userRole = "admin";
        } else if (UserUtils.isUserOwnThePhoto(userWhoIsDeletingComment, photo)) {
            userRole = "photo author";
        } else {
            userRole = "comment author";
        }

        final String commentText = String.format("%s ( %s ) deleted this comment: %s"
                , userWhoIsDeletingComment.getNameEscaped(), userRole, dateUtilsService.formatDateTime(dateUtilsService.getCurrentTime()));
        deletedComment.setCommentText(commentText);

        photoCommentService.save(deletedComment);

        return new CommentDTO(commentId, commentText);
    }

    @Override
    public List<UserDTO> userLinkAjax(final String searchString) {

        final List<User> users = userService.searchByPartOfName(searchString);

        final List<UserDTO> userDTOs = newArrayList();

        if (users.size() == 0) {
            return newArrayList();
        }

        for (final User user : users) {
            final UserDTO userDTO = new UserDTO();

            userDTO.setUserId(String.valueOf(user.getId()));
            userDTO.setUserName(user.getName());
            userDTO.setUserNameEscaped(StringUtilities.escapeHtml(user.getName()));
            userDTO.setUserCardLink(entityLinkUtilsService.getUserCardLink(user, EnvironmentContext.getLanguage()));
            userDTO.setUserAvatarUrl(userPhotoFilePathUtilsService.getUserAvatarFileUrl(user.getId()));
            userDTO.setUserGender(translatorService.translate(user.getGender().getName(), EnvironmentContext.getLanguage()));

            userDTOs.add(userDTO);
        }

        return userDTOs;
    }

    @Override
    public long getUserDelayToNextCommentAjax(final int userId) {
        return photoCommentService.getUserDelayToNextComment(userId);
    }

    @Override
    public CommentDTO getCommentDTOAjax(final int commentId) {
        final PhotoComment photoComment = photoCommentService.load(commentId);
        return new CommentDTO(photoComment);
    }

    @Override
    public boolean isUserCanCommentPhotosAjax(final int userId) {
        return photoCommentService.isUserCanCommentPhotos(userId);
    }

    @Override
    public boolean isEntryInFavoritesAjax(final int userWhoIsAddingToFavorites, final int beingAddedEntryId, final int entryTypeId) {
        return favoritesService.isEntryInFavorites(userWhoIsAddingToFavorites, beingAddedEntryId, entryTypeId);
    }

    @Override
    public void setPhotoNudeContent(final int photoId, final boolean isNudeContent) {
        final Photo photo = photoService.load(photoId);
        photo.setContainsNudeContent(isNudeContent);

        photoService.save(photo);
    }

    @Override
    public void restrictEntryForPeriod(final int entryId, final int period, final int unitId, String[] restrictionTypeIds) {

        final RestrictionTimeUnit periodUnit = RestrictionTimeUnit.getById(unitId);

        final Date timeFrom = dateUtilsService.getCurrentTime();
        final Date timeTo;

        switch (periodUnit) {
            case HOUR:
                timeTo = dateUtilsService.getTimeOffsetInHours(timeFrom, period);
                break;
            case DAY:
                timeTo = dateUtilsService.getDatesOffset(timeFrom, period);
                break;
            case MONTH:
                timeTo = dateUtilsService.getTimeOffsetInMonth(timeFrom, period);
                break;
            case YEAR:
                timeTo = dateUtilsService.getTimeOffsetInYear(timeFrom, period);
                break;
            default:
                throw new IllegalArgumentException(String.format("Illegal RestrictionTimeUnit id: %d", unitId));
        }

        for (final String restrictionTypeId : restrictionTypeIds) {
            final RestrictionType restrictionType = RestrictionType.getById(Integer.parseInt(restrictionTypeId));

            final Restrictable entry = getRestrictableEntry(entryId, restrictionType);

            restrictionService.restrictEntry(entry, restrictionType, timeFrom, timeTo);

            log.debug(String.format("Entry restriction: %s, period: '%s', uit: '%s', type of restriction: '%s'", entry, period, periodUnit, restrictionType));
        }
    }

    @Override
    public void restrictEntryForRange(final int entryId, final long _timeFrom, final long _timeTo, final String[] restrictionTypeIds) {

        final long timeZoneOffset = dateUtilsService.getTimeZoneOffset();

        final Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(_timeFrom - timeZoneOffset);

        final Date from = instance.getTime();

        instance.setTimeInMillis(_timeTo - timeZoneOffset);
        final Date to = instance.getTime();

        for (final String restrictionTypeId : restrictionTypeIds) {
            final RestrictionType restrictionType = RestrictionType.getById(Integer.parseInt(restrictionTypeId));

            final Restrictable entry = getRestrictableEntry(entryId, restrictionType);

            restrictionService.restrictEntry(entry, restrictionType, from, to);

            log.debug(String.format("Entry restriction: %s, time from: '%s', time to: '%s', type of restriction: '%s'", entry, _timeFrom, _timeTo, restrictionType));
        }
    }

    @Override
    public String translate(final String nerd) {
        return translatorService.translate(nerd, EnvironmentContext.getLanguage());
    }

    @Override
    public JSONObject translateAll(final JSONObject nerds) throws JSONException {

        final Map<String, String> result = newHashMap();

        final Iterator iterator = nerds.keys();
        while (iterator.hasNext()) {
            final String nerd = (String) iterator.next();
            result.put(nerd, translate(nerds.getString(nerd)));
        }

        return new JSONObject(result);
    }


    @Override
    public JobProgressDTO getJobProgressAjax(final int jobId) {

        final JobProgressDTO result = new JobProgressDTO(translatorService, EnvironmentContext.getLanguage());

        final JobExecutionHistoryEntry historyEntry = jobExecutionHistoryService.load(jobId);
        if (historyEntry == null) {
            result.setJobStatusId(JobExecutionStatus.ERROR.getId());
            log.error(String.format("Job with JobId='%d' not found", jobId));
            return result;
        }

        result.setCurrent(historyEntry.getCurrentJobStep());
        result.setTotal(historyEntry.getTotalJobSteps());
        result.setJobStatusId(historyEntry.getJobExecutionStatus().getId());
        result.setJobActive(historyEntry.getJobExecutionStatus().isActive());
        result.setJobExecutionDuration(dateUtilsService.formatTime(historyEntry.getExecutionDuration()));

        return result;
    }

    @Override
    public void reloadTranslationsAjax() throws DocumentException {
        translatorService.reloadTranslations();
    }

    private Restrictable getRestrictableEntry(final int entryId, final RestrictionType restrictionType) {
        return RestrictionType.FOR_USERS.contains(restrictionType) ? userService.load(entryId) : photoService.load(entryId);
    }
}
