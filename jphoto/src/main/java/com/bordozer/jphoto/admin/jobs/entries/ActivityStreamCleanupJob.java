package com.bordozer.jphoto.admin.jobs.entries;

import com.bordozer.jphoto.admin.jobs.JobRuntimeEnvironment;
import com.bordozer.jphoto.admin.jobs.enums.SavedJobType;
import com.bordozer.jphoto.core.enums.SavedJobParameterKey;
import com.bordozer.jphoto.core.general.base.CommonProperty;
import com.bordozer.jphoto.core.log.LogHelper;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.ui.activity.ActivityType;
import com.bordozer.jphoto.utils.ListUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public class ActivityStreamCleanupJob extends AbstractJob {

    private int leaveActivityForDays;
    private List<ActivityType> activityTypes;

    public ActivityStreamCleanupJob(final JobRuntimeEnvironment jobEnvironment) {
        super(new LogHelper(), jobEnvironment);
    }

    @Override
    protected void runJob() throws Throwable {
        final Date timeFrame = services.getDateUtilsService().getFirstSecondOfTheDayNDaysAgo(leaveActivityForDays);

        services.getActivityStreamService().deleteEntriesOlderThen(activityTypes, timeFrame);

        increment();
    }

    @Override
    public Map<SavedJobParameterKey, CommonProperty> getParametersMap() {
        final Map<SavedJobParameterKey, CommonProperty> parametersMap = newHashMap();

        parametersMap.put(SavedJobParameterKey.DAYS, new CommonProperty(SavedJobParameterKey.DAYS.getId(), leaveActivityForDays));

        final List<Integer> statusIds = ListUtils.convertIdentifiableListToListOfIds(activityTypes);
        parametersMap.put(SavedJobParameterKey.ENTRY_TYPES, CommonProperty.createFromIntegerList(SavedJobParameterKey.ENTRY_TYPES.getId(), statusIds, services.getDateUtilsService()));

        return parametersMap;
    }

    @Override
    public void initJobParameters(final Map<SavedJobParameterKey, CommonProperty> jobParameters) {
        totalJopOperations = 1;
        leaveActivityForDays = jobParameters.get(SavedJobParameterKey.DAYS).getValueInt();

        final List<Integer> statusIds = jobParameters.get(SavedJobParameterKey.ENTRY_TYPES).getValueListInt();
        activityTypes = newArrayList();
        for (final int statusId : statusIds) {
            activityTypes.add(ActivityType.getById(statusId));
        }
    }

    @Override
    public String getJobParametersDescription() {
        final Language language = getLanguage();

        final StringBuilder builder = new StringBuilder();

        builder.append(services.getTranslatorService().translate("ActivityStreamCleanupJob: Delete activities older then $1 days", language, String.valueOf(leaveActivityForDays)));
        builder.append("<br />");

        builder.append(services.getTranslatorService().translate("ActivityStreamCleanupJob: Activity types to delete", language)).append(": ");
        if (activityTypes.size() == ActivityType.values().length) {
            builder.append(services.getTranslatorService().translate("ActivityStreamCleanupJob: All activity types", language));
        } else {
            for (final ActivityType activityType : activityTypes) {
                final String name = services.getTranslatorService().translate(activityType.getName(), language);
                final String img = String.format("<img src='%s/jobtype/%s' height='16' title='%s'> "
                        , services.getUrlUtilsService().getSiteImagesPath(), activityType.getIcon(), name);
                builder.append(img).append(services.getTranslatorService().translate(name, language)).append("<br />");
            }
        }

        return builder.toString();
    }

    @Override
    public SavedJobType getJobType() {
        return SavedJobType.ACTIVITY_STREAM_CLEAN_UP;
    }

    public int getLeaveActivityForDays() {
        return leaveActivityForDays;
    }

    public void setLeaveActivityForDays(final int leaveActivityForDays) {
        this.leaveActivityForDays = leaveActivityForDays;
    }

    public void setActivityTypes(final List<ActivityType> activityTypes) {
        this.activityTypes = activityTypes;
    }

    public List<ActivityType> getActivityTypes() {
        return activityTypes;
    }
}
