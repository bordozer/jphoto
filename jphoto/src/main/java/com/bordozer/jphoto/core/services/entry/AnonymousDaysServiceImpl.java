package com.bordozer.jphoto.core.services.entry;

import com.bordozer.jphoto.core.general.anonym.AnonymousDay;
import com.bordozer.jphoto.core.services.dao.AnonymousDaysDao;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("anonymousDaysService")
public class AnonymousDaysServiceImpl implements AnonymousDaysService {

    @Autowired
    private AnonymousDaysDao anonymousDaysDao;

    @Autowired
    private DateUtilsService dateUtilsService;

    @Override
    public boolean addAnonymousDay(final AnonymousDay day) {
        return anonymousDaysDao.addAnonymousDay(day);
    }

    @Override
    public List<AnonymousDay> loadAll() {
        return anonymousDaysDao.loadAll();
    }

    @Override
    public void deleteAnonymousDay(final AnonymousDay day) {
        anonymousDaysDao.deleteAnonymousDay(day);
    }

    @Override
    public boolean isDayAnonymous(final Date date) {
        return anonymousDaysDao.isDayAnonymous(new AnonymousDay(date, dateUtilsService));
    }

    @Override
    public void saveAnonymousDayAjax(final String date) { // TODO: move to AjaxService
        addAnonymousDay(new AnonymousDay(dateUtilsService.parseDate(date), dateUtilsService));
    }

    @Override
    public void deleteAnonymousDayAjax(final String date) { // TODO: move to AjaxService
        deleteAnonymousDay(new AnonymousDay(dateUtilsService.parseDate(date), dateUtilsService));
    }
}
