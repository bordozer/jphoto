package com.bordozer.jphoto.core.services.archiving;

import com.bordozer.jphoto.core.general.photo.PhotoComment;
import com.bordozer.jphoto.core.interfaces.ArchivableDAO;
import com.bordozer.jphoto.core.interfaces.ArchivableEntry;
import com.bordozer.jphoto.core.services.dao.PhotoCommentDaoImpl;
import org.springframework.stereotype.Component;

@Component("photoCommentDaoArch")
public class PhotoCommentDaoArchImpl extends PhotoCommentDaoImpl implements ArchivableDAO {

    public final static String TABLE_COMMENTS_ARCHIVE = "comments_archive";

    @Override
    protected String getTableName() {
        return TABLE_COMMENTS_ARCHIVE;
    }

    @Override
    public boolean archive(final ArchivableEntry entry) {
        final PhotoComment photoComment = (PhotoComment) entry;
        photoComment.setId(0);

        return saveToDB(photoComment);
    }
}
