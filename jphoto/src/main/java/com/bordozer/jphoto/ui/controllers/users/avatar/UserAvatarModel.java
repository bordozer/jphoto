package com.bordozer.jphoto.ui.controllers.users.avatar;

import com.bordozer.jphoto.core.general.img.Dimension;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.ui.elements.PageModel;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class UserAvatarModel {

    public final static String AVATAR_FILE_FORM_CONTROL = "avatarFile";

    private User user;
    private MultipartFile avatarFile;
    private File currentAvatarFile;

    private BindingResult bindingResult;
    private PageModel pageModel = new PageModel();

    private Dimension dimension;

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public MultipartFile getAvatarFile() {
        return avatarFile;
    }

    public void setAvatarFile(final MultipartFile avatarFile) {
        this.avatarFile = avatarFile;
    }

    public File getCurrentAvatarFile() {
        return currentAvatarFile;
    }

    public void setCurrentAvatarFile(final File currentAvatarFile) {
        this.currentAvatarFile = currentAvatarFile;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }

    public void setBindingResult(final BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

    public PageModel getPageModel() {
        return pageModel;
    }

    public void setPageModel(final PageModel pageModel) {
        this.pageModel = pageModel;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(final Dimension dimension) {
        this.dimension = dimension;
    }
}
