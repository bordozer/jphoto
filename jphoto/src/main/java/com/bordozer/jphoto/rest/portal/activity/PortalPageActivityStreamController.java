package com.bordozer.jphoto.rest.portal.activity;

import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.services.entry.ActivityStreamService;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.ui.activity.AbstractActivityStreamEntry;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/rest/portal-page/activity-stream")
public class PortalPageActivityStreamController {

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private ActivityStreamService activityStreamService;

    @Autowired
    private DateUtilsService dateUtilsService;

    @RequestMapping(method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public ActivityStreamModel activityStream() {
        final Language language = EnvironmentContext.getLanguage();

        final ActivityStreamModel model = new ActivityStreamModel();

        final List<AbstractActivityStreamEntry> activities = activityStreamService.getLastActivities(configurationService.getInt(ConfigurationKey.SYSTEM_ACTIVITY_PORTAL_PAGE_STREAM_LENGTH));

        final List<ActivityStreamEntryDTO> dtos = newArrayList();
        for (final AbstractActivityStreamEntry activity : activities) {
            final ActivityStreamEntryDTO dto = new ActivityStreamEntryDTO();
            dto.setActivityText(activity.getActivityText(language));
            dto.setActivityTime(dateUtilsService.isItToday(activity.getActivityTime()) ? dateUtilsService.formatTimeShort(activity.getActivityTime()) : dateUtilsService.formatDateTimeShort(activity.getActivityTime()));
            dto.setActivityUserLink(activity.getDisplayActivityUserLink().build(language));
            dto.setActivityIcon(activity.getDisplayActivityIcon());

            dtos.add(dto);
        }

        model.setActivityStreamEntryDTOs(dtos);

        return model;
    }
}
