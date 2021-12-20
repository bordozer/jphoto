package com.bordozer.jphoto.ui.controllers.comment.edit;

import com.bordozer.jphoto.core.general.photo.PhotoComment;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.photo.PhotoCommentService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.services.menu.entry.EntryMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping("photos/{photoId}/comments/")
public class PhotoCommentEditDataController {

    private static final String PHOTO_COMMENT_MODEL = "photoCommentModel";

    private static final String SAVE_VIEW = "comments/CommentSave";

    @Autowired
    private PhotoCommentService photoCommentService;

    @Autowired
    private PhotoCommentValidator photoCommentValidator;

    @Autowired
    private EntryMenuService entryMenuService;

    @Autowired
    private DateUtilsService dateUtilsService;

    @InitBinder
    protected void initBinder(final WebDataBinder binder) {
        binder.setValidator(photoCommentValidator);
    }

    @ModelAttribute(PHOTO_COMMENT_MODEL)
    public PhotoCommentModel prepareModel(final @PathVariable("photoId") int photoId) {
        final PhotoCommentModel model = new PhotoCommentModel();

        model.setPhotoId(photoId);

        return model;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save/")
    public String saveComment(final @Valid @ModelAttribute(PHOTO_COMMENT_MODEL) PhotoCommentModel model, final BindingResult result) {

        model.setBindingResult(result);

        if (result.hasErrors()) {
            return SAVE_VIEW;
        }

        final PhotoComment photoComment = initCommentFromModel(model);

        model.setNew(photoComment.isNew());

        photoCommentService.save(photoComment);

        final PhotoComment reloadedComment = photoCommentService.load(photoComment.getId());  // need to reload to get photoComment's time

        model.setPhotoCommentInfo(photoCommentService.getPhotoCommentInfoWithChild(reloadedComment, entryMenuService.getCommentFullMenuItems(), EnvironmentContext.getCurrentUser()));

        final User currentUser = EnvironmentContext.getCurrentUser();
        model.setCommentDelay(photoCommentService.getUserDelayToNextComment(currentUser.getId()));
        model.setUserNextCommentTime(photoCommentService.getUserNextCommentTime(currentUser.getId()));

        return SAVE_VIEW;
    }

    private PhotoComment initCommentFromModel(final PhotoCommentModel model) {
        final PhotoComment photoComment = new PhotoComment();
        photoComment.setId(model.getPhotoCommentId());
        photoComment.setCommentAuthor(EnvironmentContext.getCurrentUser());
        photoComment.setCreationTime(dateUtilsService.getCurrentTime());
        photoComment.setPhotoId(model.getPhotoId());
        photoComment.setCommentText(model.getCommentText());
        photoComment.setReplyToCommentId(model.getReplyToCommentId());

        return photoComment;
    }
}
