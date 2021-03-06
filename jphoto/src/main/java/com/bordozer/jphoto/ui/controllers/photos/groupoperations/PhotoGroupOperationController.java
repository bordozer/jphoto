package com.bordozer.jphoto.ui.controllers.photos.groupoperations;

import com.bordozer.jphoto.core.general.photo.group.PhotoGroupOperationType;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.controllers.photos.groupoperations.handlers.AbstractGroupOperationHandler;
import com.bordozer.jphoto.ui.services.breadcrumbs.BreadcrumbsPhotoGalleryService;
import com.bordozer.jphoto.utils.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@SessionAttributes(PhotoGroupOperationController.MODEL_NAME)
@Controller
@RequestMapping("photos/groupOperations")
public class PhotoGroupOperationController {

    public static final String MODEL_NAME = "photoGroupOperationModel";

    private static final String VIEW = "photos/groupoperations/GroupOperations";
    private static final String VIEW_DONE = "photos/groupoperations/GroupOperationsDone";
    private static final String VIEW_ERROR = "photos/groupoperations/GroupOperationsError";

    @Autowired
    private PhotoGroupOperationValidator photoGroupOperationValidator;

    @Autowired
    private BreadcrumbsPhotoGalleryService breadcrumbsPhotoGalleryService;

    @Autowired
    private Services services;

    @InitBinder
    protected void initBinder(final WebDataBinder binder) {
        binder.setValidator(photoGroupOperationValidator);
    }

    @ModelAttribute(MODEL_NAME)
    public PhotoGroupOperationModel prepareModel() {
        return new PhotoGroupOperationModel();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public String showSelectedPhotos(final @ModelAttribute(MODEL_NAME) PhotoGroupOperationModel model, final HttpServletRequest request) {

        model.clear();

        model.setUser(EnvironmentContext.getCurrentUser());

        final String groupOperationId = model.getPhotoGroupOperationId();

        final BindingResult result = new BindException(model, MODEL_NAME);
        photoGroupOperationValidator.validateGroupOperationSelected(model, result);
        photoGroupOperationValidator.validateAtLeastOnePhotoSelected(model, result);
        model.setBindingResult(result);

        if (result.hasErrors()) {
            model.setPageTitleData(breadcrumbsPhotoGalleryService.getPhotoGroupOperationErrorBreadcrumbs());
            return VIEW_ERROR;
        }

        final PhotoGroupOperationType groupOperationType = PhotoGroupOperationType.getById(Integer.parseInt(groupOperationId));
        model.setPhotoGroupOperationType(groupOperationType);

        model.setReturnUrl(request.getHeader("referer"));

        model.setPageTitleData(breadcrumbsPhotoGalleryService.getPhotoGroupOperationBreadcrumbs(groupOperationType));

        final AbstractGroupOperationHandler groupOperationHandler = AbstractGroupOperationHandler.getInstance(groupOperationType, model, services);
        groupOperationHandler.fillModel();

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/confirm/")
    public String addToAlbum(@Valid final @ModelAttribute(MODEL_NAME) PhotoGroupOperationModel model, final BindingResult result, final HttpServletRequest request) {

        model.setBindingResult(result);
        if (result.hasErrors()) {
            model.setPageTitleData(breadcrumbsPhotoGalleryService.getPhotoGroupOperationErrorBreadcrumbs());
            return VIEW;
        }

        final List<PhotoGroupOperationEntryProperty> selectedEntries = newArrayList();
        for (final String photoId : model.getSelectedPhotoIds()) {
            final String[] entryIds = request.getParameterValues(photoId);
            for (final String entryId : entryIds) {
                final String parameter = request.getParameter(String.format("photo-%s-entry-%s", photoId, entryId));

                final PhotoGroupOperationEntryProperty entryProperty = new PhotoGroupOperationEntryProperty(NumberUtils.convertToInt(photoId), NumberUtils.convertToInt(entryId), "");
                entryProperty.setValue(StringUtils.isNotEmpty(parameter) && !"false".equals(parameter));

                selectedEntries.add(entryProperty);
            }
        }

        final AbstractGroupOperationHandler groupOperationHandler = AbstractGroupOperationHandler.getInstance(model.getPhotoGroupOperationType(), model, services);
        model.setOperationResults(groupOperationHandler.performGroupOperations(selectedEntries));

        return VIEW_DONE;
    }
}
