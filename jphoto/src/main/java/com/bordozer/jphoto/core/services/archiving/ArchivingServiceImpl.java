package com.bordozer.jphoto.core.services.archiving;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.PhotoComment;
import com.bordozer.jphoto.core.log.LogHelper;
import com.bordozer.jphoto.core.services.dao.ArchivingDao;
import com.bordozer.jphoto.core.services.photo.PhotoCommentService;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("archivingService")
public class ArchivingServiceImpl implements ArchivingService {

    @Autowired
    private PhotoService photoService;

    @Autowired
    private DateUtilsService dateUtilsService;

    @Autowired
    private PhotoCommentService photoCommentService;

    @Autowired
    private ArchivingDao archivingDao;

    @Autowired
    private PhotoCommentDaoArchImpl photoCommentArchDao;

    private final LogHelper log = new LogHelper();

    @Override
    public void archivePhotosPreviewsOlderThen(final int days) {
        archivingDao.deletePhotosPreviewsOlderThen(getArchiveStartDate(days));
    }

    @Override
    public void archivePhotosAppraisalsOlderThen(final int days) {
        // TODO: blu: do not forget implement this
    }

    @Override
    public void archivePhoto(final Photo photo) {

        log.debug(String.format("Archiving photo %s", photo));

        final List<Integer> rootCommentsIds = photoCommentService.loadRootCommentsIds(photo.getId());
        for (final int rootCommentsId : rootCommentsIds) {

            final PhotoComment archivedComment = photoCommentService.load(rootCommentsId);
            archivedComment.setId(0);

            photoCommentArchDao.archive(archivedComment);

            archiveAnswers(rootCommentsId, archivedComment.getId());
        }

        photoCommentService.deletePhotoComments(photo.getId());

        photo.setArchived(true);

        photoService.save(photo);
    }

    private void archiveAnswers(final int originalCommentsId, final int archivedCommentsId) {

        final List<PhotoComment> answers = photoCommentService.loadAnswersOnComment(originalCommentsId);

        for (final PhotoComment answer : answers) {
            final int answerId = answer.getId();

            answer.setId(0);
            answer.setReplyToCommentId(archivedCommentsId);

            photoCommentArchDao.archive(answer);

            archiveAnswers(answerId, answer.getId());
        }
    }

    @Override
    public Date getArchiveStartDate(final int days) {
        return dateUtilsService.getFirstSecondOfTheDayNDaysAgo(days - 1);
    }

    @Override
    public List<Integer> getNotArchivedPhotosIdsUploadedAtOrEarlieThen(final Date time) {
        return archivingDao.getNotArchivedPhotosIdsUploadedAtOrEarlieThen(time);
    }

    public void setPhotoService(final PhotoService photoService) {
        this.photoService = photoService;
    }

    public void setDateUtilsService(final DateUtilsService dateUtilsService) {
        this.dateUtilsService = dateUtilsService;
    }

    public void setPhotoCommentService(final PhotoCommentService photoCommentService) {
        this.photoCommentService = photoCommentService;
    }

    public void setArchivingDao(final ArchivingDao archivingDao) {
        this.archivingDao = archivingDao;
    }

    public void setPhotoCommentArchDao(final PhotoCommentDaoArchImpl photoCommentArchDao) {
        this.photoCommentArchDao = photoCommentArchDao;
    }
}
