package core.services.photo;

import core.services.archiving.PhotoCommentDaoArchImpl;
import org.springframework.beans.factory.annotation.Autowired;

public class PhotoCommentArchServiceImpl extends PhotoCommentServiceImpl implements PhotoCommentArchService {

	@Autowired
	private PhotoCommentDaoArchImpl photoCommentDaoArch;

	protected PhotoCommentDaoArchImpl getPhotoCommentDao() {
		return photoCommentDaoArch;
	}
}
