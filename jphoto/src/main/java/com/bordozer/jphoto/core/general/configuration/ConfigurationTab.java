package com.bordozer.jphoto.core.general.configuration;

public enum ConfigurationTab {

    ALL("all", "ConfigurationTab: All tabs"), CHANGES_ONLY("changes", "ConfigurationTab: Changes only"), SYSTEM("system", "ConfigurationTab: System"), PORTAL_PAGE("archiving", "ConfigurationTab: Portal page"), CANDIDATES("candidates", "ConfigurationTab: Candidates"), MEMBERS("users", "ConfigurationTab: Members"), AVATAR("avatar", "ConfigurationTab: Avatar"), PHOTO_UPLOAD("photoUpload", "ConfigurationTab: Photo upload"), PHOTOS("photoLists", "ConfigurationTab: Photo lists"), PHOTO_CARD("photoCard", "ConfigurationTab: Photo card"), COMMENTS("photoComments", "ConfigurationTab: Photo comments"), PHOTO_VOTING("photoVoting", "ConfigurationTab: Photo voting"), PHOTO_RATING("photoRating", "ConfigurationTab: Photo rating"), RANK_VOTING("rankVoting", "ConfigurationTab: Voting for members' ranks in a category"), CACHE("cache", "ConfigurationTab: Cache"), ADMIN("admin", "ConfigurationTab: Admin"), EMAILING("emailing", "ConfigurationTab: Emailing"), ARCHIVING("archiving", "ConfigurationTab: Archiving");

    private final String key;
    private final String name;

    private ConfigurationTab(final String key, final String name) {
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public static ConfigurationTab getDefaultConfigurationTab() {
        return SYSTEM;
    }

    public static ConfigurationTab getByKey(final String tab) {
        for (final ConfigurationTab configurationTab : ConfigurationTab.values()) {
            if (configurationTab.getKey().equals(tab)) {
                return configurationTab;
            }
        }

        return null;
    }
}
