package com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport;

import com.bordozer.jphoto.admin.controllers.jobs.edit.AbstractAdminJobModel;
import com.bordozer.jphoto.admin.controllers.jobs.edit.DateRangableController;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.importParameters.AbstractImportParameters;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.importParameters.FileSystemImportParameters;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.importParameters.RemoteSitePhotosImportParameters;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;
import com.bordozer.jphoto.admin.jobs.entries.AbstractJob;
import com.bordozer.jphoto.admin.jobs.entries.PhotosImportJob;
import com.bordozer.jphoto.admin.jobs.enums.DateRangeType;
import com.bordozer.jphoto.admin.jobs.enums.SavedJobType;
import com.bordozer.jphoto.admin.jobs.general.JobDateRange;
import com.bordozer.jphoto.core.enums.SavedJobParameterKey;
import com.bordozer.jphoto.core.enums.UserGender;
import com.bordozer.jphoto.core.exceptions.BaseRuntimeException;
import com.bordozer.jphoto.core.general.base.CommonProperty;
import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.photo.PhotoImageLocationType;
import com.bordozer.jphoto.core.general.user.UserMembershipType;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.translatable.GenericTranslatableList;
import com.bordozer.jphoto.utils.NumberUtils;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

@SessionAttributes({PhotosImportController.JOB_MODEL_NAME})
@Controller
@RequestMapping("/admin/jobs/data/photosImport")
public class PhotosImportController extends DateRangableController {

    public static final String JOB_MODEL_NAME = "photosImportModel";

    private static final String START_VIEW = "admin/jobs/edit/photosImport/Start";

    @Autowired
    private PhotosImportValidator photosImportValidator;

    @Autowired
    private DateUtilsService dateUtilsService;

    @Autowired
    private TranslatorService translatorService;

    @Autowired
    private ConfigurationService configurationService;

    @InitBinder
    protected void initBinder(final WebDataBinder binder) {
        binder.setValidator(photosImportValidator);
    }

    @ModelAttribute(JOB_MODEL_NAME)
    public PhotosImportModel initModel() {
        final PhotosImportModel model = new PhotosImportModel();

        final PhotosImportSource importSource = PhotosImportSource.PHOTOSIGHT;
        model.setImportSource(importSource);
        model.setImportSourceId(importSource.getId());

        prepareDateRange(model);

        model.setUserMembershipTypeTranslatableList(GenericTranslatableList.userMembershipTypeTranslatableList(EnvironmentContext.getLanguage(), translatorService));
        model.setUserGenderTranslatableList(GenericTranslatableList.userGenderTranslatableList(EnvironmentContext.getLanguage(), translatorService));
        model.setPhotosImportSourceTranslatableList(GenericTranslatableList.photosImportSourceTranslatableList(EnvironmentContext.getLanguage(), translatorService));

        return model;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public ModelAndView showForm(final @ModelAttribute(JOB_MODEL_NAME) PhotosImportModel model) {
        return doShowForm(model, SavedJobType.PHOTOS_IMPORT);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public ModelAndView postForm(final @Valid @ModelAttribute(JOB_MODEL_NAME) PhotosImportModel model, final BindingResult result) {
        return doPostForm(model, result);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save/")
    public ModelAndView saveJob(final @Valid @ModelAttribute(JOB_MODEL_NAME) PhotosImportModel model, final BindingResult result) {
        return doSaveJob(model, result);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{savedJobId}/edit/")
    public ModelAndView editEntry(final @PathVariable("savedJobId") int savedJobId, final @ModelAttribute(JOB_MODEL_NAME) PhotosImportModel model, final HttpServletRequest request) {
        return processEditing(savedJobId, model, request);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{savedJobId}/delete/")
    public ModelAndView deleteEntry(final @PathVariable("savedJobId") int savedJobId, final @ModelAttribute(JOB_MODEL_NAME) PhotosImportModel model) {
        return deleteAndReturnView(savedJobId, model);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/progress/{jobId}/")
    public ModelAndView showProgress(final @PathVariable("jobId") int jobId, @ModelAttribute(JOB_MODEL_NAME) AbstractAdminJobModel model) {
        return getProgressOrFinishView(model, jobId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/stop/{jobId}/")
    public ModelAndView stopJob(final @PathVariable("jobId") int jobId, final @ModelAttribute(JOB_MODEL_NAME) AbstractAdminJobModel model) {
        stopJobWithChildByUserDemand(jobId);
        return getProgressOrFinishView(model, jobId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/finish/")
    public ModelAndView finishJob(@ModelAttribute(JOB_MODEL_NAME) AbstractAdminJobModel model) {
        return getFinishView(model);
    }

    @Override
    protected void showFormCustomAction(final AbstractAdminJobModel model) {

        final PhotosImportModel aModel = (PhotosImportModel) model;

        aModel.setImportComments(true);
        aModel.setPictureDir(configurationService.getString(ConfigurationKey.ADMIN_PHOTO_FILE_IMPORT_DEFAULT_DIR));
        aModel.setRemotePhotoSiteImport_importNudeContentByDefault(configurationService.getBoolean(ConfigurationKey.ADMIN_REMOTE_PHOTO_SITE_IMPORT_JOB_IMPORT_NUDE_CONTENT));
        aModel.setBreakImportIfAlreadyImportedPhotoFound(true);
        aModel.setPhotoImageImportStrategyTypeId(PhotoImageLocationType.WEB.getId());
    }

    @Override
    protected void initJobFromModel(final AbstractAdminJobModel model) {

        final PhotosImportJob job = (PhotosImportJob) model.getJob();
        final PhotosImportModel aModel = (PhotosImportModel) model;

        final PhotosImportSource importSource = aModel.getImportSource();
        job.setImportSource(importSource);

        final AbstractImportParameters importParameters;

        switch (importSource) {
            case FILE_SYSTEM:
                initDateRangeJob(aModel, job);

                final int photoQtyLimit = NumberUtils.convertToInt(aModel.getPhotoQtyLimit());
                final String pictureDir = aModel.getPictureDir();
                final boolean deletePictureAfterImport = aModel.isDeletePictureFromDiskAfterImport();
                final int assignAllGeneratedPhotosToUserId = aModel.getAssignAllGeneratedPhotosToUserId();

                final Date dateFrom = dateUtilsService.parseDate(aModel.getDateFrom());
                final Date dateTo = dateUtilsService.parseDate(aModel.getDateTo());
                final DateRangeType dateRangeType = DateRangeType.getById(NumberUtils.convertToInt(aModel.getDateRangeTypeId()));
                final int timePeriod = NumberUtils.convertToInt(aModel.getTimePeriod());
                final JobDateRange jobDateRange = new JobDateRange(dateRangeType, dateFrom, dateTo, timePeriod, dateUtilsService);

                importParameters = new FileSystemImportParameters(pictureDir, photoQtyLimit, deletePictureAfterImport, assignAllGeneratedPhotosToUserId, jobDateRange, EnvironmentContext.getLanguage());

                job.setTotalJopOperations(photoQtyLimit > 0 ? photoQtyLimit : AbstractJob.OPERATION_COUNT_UNKNOWN);
                break;
            case PHOTOSIGHT:
            case PHOTO35:
            case NATURELIGHT:
                final List<String> remotePhotoSiteUserIds = newArrayList();
                final String remotePhotoSiteUserIdsText = aModel.getRemotePhotoSiteUserIds();
                final String[] ids = remotePhotoSiteUserIdsText.split(",");
                for (final String idTxt : ids) {
                    remotePhotoSiteUserIds.add(idTxt.trim());
                }

                final UserGender userGender = UserGender.getById(NumberUtils.convertToInt(aModel.getUserGenderId()));
                final UserMembershipType membershipType = UserMembershipType.getById(NumberUtils.convertToInt(aModel.getUserMembershipId()));
                final boolean importComments = aModel.isImportComments();
                final int delayBetweenRequests = NumberUtils.convertToInt(aModel.getDelayBetweenRequest());
                final int pageQty = NumberUtils.convertToInt(aModel.getPageQty());
                final PhotoImageLocationType photoImageLocationType = PhotoImageLocationType.getById(aModel.getPhotoImageImportStrategyTypeId());

                final List<String> reqRemotePhotoSiteCategories = aModel.getRemotePhotoSiteCategories();
                final List<RemotePhotoSiteCategory> remotePhotoSiteCategories = Lists.transform(reqRemotePhotoSiteCategories, new Function<String, RemotePhotoSiteCategory>() {
                    @Override
                    public RemotePhotoSiteCategory apply(final String id) {
                        return RemotePhotoSiteCategory.getById(importSource, NumberUtils.convertToInt(id));
                    }
                });
                job.setRemotePhotoSiteCategories(remotePhotoSiteCategories);

                importParameters = new RemoteSitePhotosImportParameters(importSource, remotePhotoSiteUserIds, userGender, membershipType, importComments, delayBetweenRequests
                        , pageQty, EnvironmentContext.getCurrentUser().getLanguage(), aModel.isBreakImportIfAlreadyImportedPhotoFound(), remotePhotoSiteCategories, photoImageLocationType);

                job.setTotalJopOperations(pageQty > 0 ? pageQty : AbstractJob.OPERATION_COUNT_UNKNOWN);
                break;
            default:
                throw new BaseRuntimeException(String.format("Unsupported PhotoImportSource: %s", importSource));
        }

        job.setImportParameters(importParameters);
    }

    @Override
    protected void initModelFromSavedJob(final AbstractAdminJobModel model, final int savedJobId) {
        final PhotosImportModel aModel = (PhotosImportModel) model;

        final Map<SavedJobParameterKey, CommonProperty> savedJobParametersMap = savedJobService.getSavedJobParametersMap(savedJobId);

        final PhotosImportSource importSource = PhotosImportSource.getById(savedJobParametersMap.get(SavedJobParameterKey.PHOTOS_IMPORT_SOURCE).getValueInt());

        aModel.setImportSource(importSource);
        aModel.setImportSourceId(importSource.getId());

        switch (importSource) {
            case FILE_SYSTEM:
                final String pictureDir = savedJobParametersMap.get(SavedJobParameterKey.PARAM_PICTURES_DIR).getValue();
                final int photos = savedJobParametersMap.get(SavedJobParameterKey.PARAM_PHOTOS_QTY).getValueInt();
                final boolean deletePictureAfterImport = savedJobParametersMap.get(SavedJobParameterKey.DELETE_PICTURE_AFTER_IMPORT).getValueBoolean();
                final int userId = savedJobParametersMap.get(SavedJobParameterKey.PARAM_USER_ID).getValueInt();

                aModel.setPictureDir(pictureDir);
                aModel.setPhotoQtyLimit(photos > 0 ? String.valueOf(photos) : StringUtils.EMPTY);
                aModel.setDeletePictureFromDiskAfterImport(deletePictureAfterImport);
                aModel.setAssignAllGeneratedPhotosToUserId(userId);
                aModel.setAssignAllGeneratedPhotosToUser(userService.load(userId));

                initModelDateRange(aModel, savedJobParametersMap);
                break;
            case PHOTOSIGHT:
            case PHOTO35:
            case NATURELIGHT:
                final String remotePhotoSiteUserId = savedJobParametersMap.get(SavedJobParameterKey.PARAM_USER_ID).getValue();
                final String genderId = savedJobParametersMap.get(SavedJobParameterKey.USER_GENDER_ID).getValue();
                final String membershipId = savedJobParametersMap.get(SavedJobParameterKey.USER_MEMBERSHIP_ID).getValue();
                final boolean importComments = savedJobParametersMap.get(SavedJobParameterKey.IMPORT_REMOTE_PHOTO_SITE_COMMENTS).getValueBoolean();
                final String pageQty = savedJobParametersMap.get(SavedJobParameterKey.IMPORT_PAGE_QTY).getValue();
                final String delayBetweenRequests = savedJobParametersMap.get(SavedJobParameterKey.DELAY_BETWEEN_REQUESTS).getValue();

                aModel.setRemotePhotoSiteUserIds(remotePhotoSiteUserId);
                aModel.setUserGenderId(genderId);
                aModel.setUserMembershipId(membershipId);
                aModel.setImportComments(importComments);
                aModel.setPageQty(pageQty);
                aModel.setDelayBetweenRequest(delayBetweenRequests);

                final List<String> categories = savedJobParametersMap.get(SavedJobParameterKey.REMOTE_PHOTO_SITE_CATEGORIES).getValueListString();
                aModel.setRemotePhotoSiteCategories(categories);
                break;
            default:
                throw new BaseRuntimeException(String.format("Unsupported PhotoImportSource: %s", importSource));
        }
    }

    @Override
    protected String getStartViewName() {
        return START_VIEW;
    }
}
