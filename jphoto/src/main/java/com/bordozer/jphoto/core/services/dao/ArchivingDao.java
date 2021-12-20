package com.bordozer.jphoto.core.services.dao;

import java.util.Date;
import java.util.List;

public interface ArchivingDao {

    void deletePhotosPreviewsOlderThen(final Date time);

    List<Integer> getNotArchivedPhotosIdsUploadedAtOrEarlieThen(Date time);
}
