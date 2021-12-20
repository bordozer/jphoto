package com.bordozer.jphoto.core.services.utils;

import com.bordozer.jphoto.core.general.photo.Photo;
import org.apache.commons.collections4.Predicate;

import java.io.FilenameFilter;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public interface PredicateUtilsService {

    String BEAN_NAME = "predicateUtilsService";

    Predicate<Photo> getPhotoUploadDatePredicate(Date date);

    Predicate<Photo> getPhotoForPeriodPredicate(int days);

    FilenameFilter getFileFilter();

    FilenameFilter getDirFilter();

    boolean contains(Collection<?> collection, Object o);

    boolean contains(Map map, Object o);
}
