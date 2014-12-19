package core.services.dao;

import core.interfaces.Archivable;
import core.interfaces.ArchivableEntry;

public class PhotoCommentDaoArchImpl extends PhotoCommentDaoImpl implements Archivable {

	public final static String TABLE_COMMENTS_ARCHIVE = "comments_archive";

	@Override
	protected String getTableName() {
		return TABLE_COMMENTS_ARCHIVE;
	}

	@Override
	public boolean archive( final ArchivableEntry entry ) {
		return false;
	}
}
