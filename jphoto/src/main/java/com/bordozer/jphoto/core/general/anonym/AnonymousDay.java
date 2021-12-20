package com.bordozer.jphoto.core.general.anonym;

import com.bordozer.jphoto.core.services.utils.DateUtilsService;

import java.util.Date;

public class AnonymousDay {

    private final Date date;

    private final DateUtilsService dateUtilsService;

    public AnonymousDay(final Date date, final DateUtilsService dateUtilsService) {
        this.date = dateUtilsService.getFirstSecondOfDay(date);
        this.dateUtilsService = dateUtilsService; // TODo
    }

    public Date getDate() {
        return date;
    }

    public int getDay() {
        return dateUtilsService.getDay(date);
    }

    public int getMonth() {
        return dateUtilsService.getMonth(date);
    }

    public int getYear() {
        return dateUtilsService.getYear(date);
    }

    @Override
    public String toString() {
        return String.format("Anonymous Day: %s", dateUtilsService.formatDate(date));
    }
}
