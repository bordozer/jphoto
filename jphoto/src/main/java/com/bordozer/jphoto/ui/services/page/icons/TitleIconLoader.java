package com.bordozer.jphoto.ui.services.page.icons;

import com.bordozer.jphoto.admin.services.scheduler.ScheduledTasksExecutionService;
import com.bordozer.jphoto.core.enums.PrivateMessageType;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.entry.PrivateMessageService;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.NerdKey;
import com.bordozer.jphoto.core.services.translator.TranslationData;
import com.bordozer.jphoto.core.services.translator.TranslationEntry;
import com.bordozer.jphoto.core.services.translator.TranslationEntryMissed;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.utils.UserUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.CollectionUtils;
import org.quartz.SchedulerException;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

public class TitleIconLoader {

    public static List<AbstractTitleIcon> getTitleIcons(final Services services, final ScheduledTasksExecutionService scheduledTasksExecutionService) {
        final List<AbstractTitleIcon> result = newArrayList();

        addScheduledIcon(result, services, scheduledTasksExecutionService);

        addUnreadCommentsCountIcon(result, services);

        addUnreadPrivateMessagesIcon(result, services);

        addUntranslatedMessagesIcons(result, services);

        return result;
    }

    private static void addScheduledIcon(final List<AbstractTitleIcon> result, final Services services, final ScheduledTasksExecutionService scheduledTasksExecutionService) {

        if (!services.getSecurityService().isSuperAdminUser(getCurrentUser())) {
            return;
        }

        boolean running;
        try {
            running = scheduledTasksExecutionService.isRunning();
        } catch (final SchedulerException e) {
            running = false;
        }

        if (!running) {
            result.add(new SchedulerTitleIcon(services));
        }
    }

    private static void addUnreadCommentsCountIcon(final List<AbstractTitleIcon> result, final Services services) {
        final int unreadCommentsCount = services.getPhotoCommentService().getUnreadCommentsQty(getCurrentUser().getId());
        if (unreadCommentsCount > 0) {
            result.add(new UnreadCommentsCountTitleIcon(unreadCommentsCount, services));
        }
    }

    private static void addUnreadPrivateMessagesIcon(final List<AbstractTitleIcon> result, final Services services) {

        final PrivateMessageService privateMessageService = services.getPrivateMessageService();
        final int userId = getCurrentUser().getId();

        if (UserUtils.isCurrentUserLoggedUser()) {
            for (final PrivateMessageType messageType : PrivateMessageType.values()) {

                if (messageType == PrivateMessageType.USER_PRIVATE_MESSAGE_OUT) {
                    continue;
                }

                final int messagesCount = privateMessageService.getNewReceivedPrivateMessagesCount(userId, messageType);

                if (messagesCount > 0) {
                    result.add(new UnreadPrivateMessagesTitleIcon(messageType, messagesCount, services));
                }
            }
        }
    }

    private static void addUntranslatedMessagesIcons(final List<AbstractTitleIcon> result, final Services services) {

        if (services.getSecurityService().isSuperAdminUser(getCurrentUser())) {

            final Map<NerdKey, TranslationData> untranslatedMap = getTranslatorService(services).getUntranslatedMap();

            for (final Language language : Language.getUILanguages()) {
                int untranslatedMessagesCount = 0;

                for (final NerdKey nerdKey : untranslatedMap.keySet()) {

                    final TranslationData translationData = untranslatedMap.get(nerdKey);
                    final List<TranslationEntry> translations = newArrayList(translationData.getTranslations());
                    CollectionUtils.filter(translations, new Predicate<TranslationEntry>() {
                        @Override
                        public boolean evaluate(final TranslationEntry translationEntry) {
                            return translationEntry instanceof TranslationEntryMissed && translationEntry.getLanguage() == language;
                        }
                    });
                    untranslatedMessagesCount += translations.size();
                }

                if (untranslatedMessagesCount == 0) {
                    continue;
                }

                result.add(new UntranslatedMessagesTitleIcon(language, untranslatedMessagesCount, services));
            }
        }
    }

    private static TranslatorService getTranslatorService(final Services services) {
        return services.getTranslatorService();
    }

    private static User getCurrentUser() {
        return EnvironmentContext.getCurrentUser();
    }
}
