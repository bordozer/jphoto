package com.bordozer.jphoto.core.services.photo;

import com.bordozer.jphoto.core.services.dao.PhotoCommentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("photoCommentArchService")
public class PhotoCommentArchServiceImpl extends PhotoCommentServiceImpl implements PhotoCommentArchService {

    @Autowired
    private PhotoCommentDao photoCommentDaoArch;

    protected PhotoCommentDao getPhotoCommentDao() {
        return photoCommentDaoArch;
    }
}
