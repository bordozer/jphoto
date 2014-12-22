package core.services.archiving;

import core.general.photo.PhotoComment;
import core.interfaces.ArchivableDAO;
import core.interfaces.ArchivableEntry;
import core.services.dao.PhotoCommentDaoImpl;

public class PhotoCommentDaoArchImpl extends PhotoCommentDaoImpl implements ArchivableDAO {

	public final static String TABLE_COMMENTS_ARCHIVE = "comments_archive";

	@Override
	protected String getTableName() {
		return TABLE_COMMENTS_ARCHIVE;
	}

	@Override
	public boolean archive( final ArchivableEntry entry ) {
		final PhotoComment photoComment = ( PhotoComment ) entry;
		photoComment.setId( 0 );

		return saveToDB( photoComment );
	}
}
