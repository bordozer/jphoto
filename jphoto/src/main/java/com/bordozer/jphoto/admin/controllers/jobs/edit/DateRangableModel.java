package com.bordozer.jphoto.admin.controllers.jobs.edit;

public class DateRangableModel extends AbstractAdminJobModel {

    public static final String DATE_RANGE_TYPE_ID_FORM_CONTROL = "dateRangeTypeId";

    public final static String DATE_FROM_FORM_CONTROL = "dateFrom";
    public final static String DATE_TO_FORM_CONTROL = "dateTo";

    public static final String TIME_PERIOD_FORM_CONTROL = "timePeriod";

    private String dateRangeTypeId;

    private String dateFrom;
    private String dateTo;

    private String timePeriod;

    public String getDateRangeTypeId() {
        return dateRangeTypeId;
    }

    public void setDateRangeTypeId(final String dateRangeTypeId) {
        this.dateRangeTypeId = dateRangeTypeId;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(final String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(final String dateTo) {
        this.dateTo = dateTo;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(final String timePeriod) {
        this.timePeriod = timePeriod;
    }
}
