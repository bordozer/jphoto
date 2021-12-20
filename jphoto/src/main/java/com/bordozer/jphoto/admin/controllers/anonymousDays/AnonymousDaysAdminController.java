package com.bordozer.jphoto.admin.controllers.anonymousDays;

import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.services.entry.AnonymousDaysService;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.ui.services.breadcrumbs.BreadcrumbsAdminService;
import com.bordozer.jphoto.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin/anonymousDays")
public class AnonymousDaysAdminController {

    public static final String MODEL_NAME = "anonymousDaysAdminModel";

    private static final String VIEW = "/admin/anonymousDays/AnonymousDaysAdmin";

    @Autowired
    private AnonymousDaysService anonymousDaysService;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private BreadcrumbsAdminService breadcrumbsAdminService;

    @Autowired
    private DateUtilsService dateUtilsService;

    @ModelAttribute(MODEL_NAME)
    public AnonymousDaysAdminModel prepareModel() {
        return new com.bordozer.jphoto.admin.controllers.anonymousDays.AnonymousDaysAdminModel();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String anonymousDaysForCurrentYear(final @ModelAttribute(MODEL_NAME) com.bordozer.jphoto.admin.controllers.anonymousDays.AnonymousDaysAdminModel model) {

        initModel(model, getCurrentYear());

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{year}/")
    public String anonymousDaysForCustomYear(final @PathVariable("year") String _year, final @ModelAttribute(MODEL_NAME) com.bordozer.jphoto.admin.controllers.anonymousDays.AnonymousDaysAdminModel model) {

        int year = NumberUtils.convertToInt(_year);
        if (year == 0) {
            year = getCurrentYear();
        }

        initModel(model, year);

        return VIEW;
    }

    private void initModel(final AnonymousDaysAdminModel model, final int year) {
        model.setPageTitleData(breadcrumbsAdminService.getAnonymousDaysBreadcrumbs());
        model.setAnonymousDays(anonymousDaysService.loadAll());
        model.setAnonymousPeriod(configurationService.getInt(ConfigurationKey.PHOTO_UPLOAD_ANONYMOUS_PERIOD));
        model.setAnonymousDaysForYear(year);

        model.setCurrentYear(getCurrentYear());
        model.setCalendarMinDate(dateUtilsService.getFirstSecondOfYear(year));
        model.setCalendarMaxDate(dateUtilsService.getLastSecondOfYear(year));
    }

    private int getCurrentYear() {
        return dateUtilsService.getYear(dateUtilsService.getCurrentDate());
    }
}
