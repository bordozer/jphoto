package com.bordozer.jphoto.ui.controllers.users.avatar;

import com.bordozer.jphoto.core.general.img.Dimension;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.log.LogHelper;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.core.services.utils.ImageFileUtilsService;
import com.bordozer.jphoto.core.services.utils.TempFileUtilsService;
import com.bordozer.jphoto.core.services.utils.UrlUtilsService;
import com.bordozer.jphoto.core.services.utils.UserPhotoFilePathUtilsService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.services.breadcrumbs.BreadcrumbsUserService;
import com.bordozer.jphoto.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("members/{userId}/avatar")
public class UserAvatarController {

    private static final String VIEW = "users/avatar/UserAvatar";

    private static final String MODEL_NAME = "userAvatarModel";
    private static final int MAX_DIMENSION = 600;

    @Autowired
    private UserService userService;

    @Autowired
    private UserAvatarValidator userAvatarValidator;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private TempFileUtilsService tempFileUtilsService;

    @Autowired
    private BreadcrumbsUserService breadcrumbsUserService;

    @Autowired
    private UserPhotoFilePathUtilsService userPhotoFilePathUtilsService;

    @Autowired
    private UrlUtilsService urlUtilsService;

    @Autowired
    private ImageFileUtilsService imageFileUtilsService;

    @Autowired
    private TranslatorService translatorService;

    private LogHelper log = new LogHelper();

    @InitBinder
    private void initBinder(final WebDataBinder binder) {
        binder.setValidator(userAvatarValidator);
    }

    @ModelAttribute(MODEL_NAME)
    private UserAvatarModel prepareModel(final @PathVariable("userId") String _userId) {

        securityService.assertUserExists(_userId);

        final int userId = NumberUtils.convertToInt(_userId);

        final User user = userService.load(userId);

        securityService.assertUserCanEditUserData(EnvironmentContext.getCurrentUser(), user);

        final UserAvatarModel model = new UserAvatarModel();
        model.setUser(user);
        model.getPageModel().setPageTitleData(breadcrumbsUserService.setUserAvatarBreadcrumbs(model.getUser()));


        final File userAvatarFile = userPhotoFilePathUtilsService.getUserAvatarFile(user.getId());
        if (userAvatarFile.exists()) {
            model.setCurrentAvatarFile(userAvatarFile);
        }

        model.setDimension(getAvatarDimension(model.getCurrentAvatarFile()));

        return model;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String showAvatar(final @ModelAttribute(MODEL_NAME) UserAvatarModel model) {
        return VIEW;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public String uploadAvatar(@Valid final @ModelAttribute(MODEL_NAME) UserAvatarModel model, final BindingResult result) {

        model.setBindingResult(result);

        if (result.hasErrors()) {
            return VIEW;
        }

        final int userId = model.getUser().getId();

        final MultipartFile multipartFile = model.getAvatarFile();

        try {
            final File tmpAvatarFile = tempFileUtilsService.getTempFile();

            multipartFile.transferTo(tmpAvatarFile);

            userService.saveAvatar(userId, tmpAvatarFile);
        } catch (final IOException e) {
            final Language language = EnvironmentContext.getLanguage();
            result.reject(translatorService.translate("Saving data error", language), translatorService.translate("Can not copy file", language));
            log.error(e);
        }

        return String.format("redirect:%s", urlUtilsService.getEditUserAvatarLink(userId));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/delete/")
    public String deleteAvatar(final @PathVariable("userId") int userId, final @ModelAttribute(MODEL_NAME) UserAvatarModel model) {

        securityService.assertUserCanEditUserData(EnvironmentContext.getCurrentUser(), model.getUser());

        try {
            final boolean isDeleted = userService.deleteAvatar(userId);
            if (isDeleted) {
                model.setAvatarFile(null);
                model.setCurrentAvatarFile(null);
            }
        } catch (final IOException e) {
            log.error(String.format("Error deleting user avatar: %d", userId), e);
        }

        return VIEW;
    }

    private Dimension getAvatarDimension(final File avatarFile) {
        if (avatarFile != null) {
            try {
                final Dimension imageDimension = imageFileUtilsService.getImageDimension(avatarFile);
                return imageFileUtilsService.resizePhotoImage(imageDimension, new Dimension(MAX_DIMENSION, MAX_DIMENSION));
            } catch (IOException e) {
                log.error(String.format("Error reading avatar dimension: '%s'", avatarFile));
            }
        }
        return null; //new Dimension( 1, 1 );
    }
}
