package com.bordozer.jphoto.core.services.utils;

import com.bordozer.jphoto.core.general.photo.Photo;
import org.apache.commons.collections4.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

@Service("predicateUtilsService")
public class PredicateUtilsServiceImpl implements PredicateUtilsService {

    @Autowired
    private DateUtilsService dateUtilsService;

    @Override
    public Predicate<Photo> getPhotoUploadDatePredicate(final Date date) {
        return new Predicate<Photo>() {
            @Override
            public boolean evaluate(Photo photo) {
                return dateUtilsService.extractDate(photo.getUploadTime()).getTime() == date.getTime();
            }
        };

    }

    @Override
    public Predicate<Photo> getPhotoForPeriodPredicate(final int days) {
        return new Predicate<Photo>() {
            @Override
            public boolean evaluate(Photo photo) {
                final Date dateNDaysAgo = dateUtilsService.getDatesOffsetFromCurrentDate(-days);
                final Date uploadDate = dateUtilsService.extractDate(photo.getUploadTime());

                return uploadDate.getTime() >= dateNDaysAgo.getTime();
            }
        };
    }

    @Override
    public FilenameFilter getFileFilter() {
        return new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return new File(dir, name).isFile();
            }
        };
    }

    @Override
    public FilenameFilter getDirFilter() {
        return new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return new File(dir, name).isDirectory();
            }
        };
    }

    @Override
    public boolean contains(Collection<?> collection, Object o) {
        return collection.contains(o);
    }

    @Override
    public boolean contains(Map map, Object o) {
        return map.containsKey(o);
    }
}
