package com.bordozer.jphoto.core.general.configuration;

public enum ConfigurationKey {

    SYSTEM_SESSION_TIMEOUT_IN_MINUTES(110, "60", false, ConfigurationDataType.INTEGER, ConfigurationUnit.MIN, ConfigurationTab.SYSTEM, "Session timeout in minutes"), SYSTEM_AUTO_START_SCHEDULER(106, "1", false, ConfigurationDataType.YES_NO, ConfigurationUnit.EMPTY, ConfigurationTab.SYSTEM, "Auto start scheduler when worker starts"), SYSTEM_LOGIN_MIN_LENGTH(100, "3", false, ConfigurationDataType.INTEGER, ConfigurationUnit.SYMBOL, ConfigurationTab.SYSTEM, "Minimal length of member login"), SYSTEM_LOGIN_MAX_LENGTH(101, "20", false, ConfigurationDataType.INTEGER, ConfigurationUnit.SYMBOL, ConfigurationTab.SYSTEM, "Max length of member login"), SYSTEM_USER_NAME_MIN_LENGTH(102, "8", false, ConfigurationDataType.INTEGER, ConfigurationUnit.SYMBOL, ConfigurationTab.SYSTEM, "Minimal length of member name"), SYSTEM_USER_NAME_MAX_LENGTH(103, "30", false, ConfigurationDataType.INTEGER, ConfigurationUnit.SYMBOL, ConfigurationTab.SYSTEM, "Max length of member name"), SYSTEM_PHOTO_NAME_MAX_LENGTH(104, "100", false, ConfigurationDataType.INTEGER, ConfigurationUnit.SYMBOL, ConfigurationTab.SYSTEM, "Max length of photo name (DB limit is 255)"), SYSTEM_SHOW_UI_MENU_GO_TO_PHOTOS_FOR_OWN_ENTRIES(105, "1", false, ConfigurationDataType.YES_NO, ConfigurationUnit.EMPTY, ConfigurationTab.SYSTEM, "Show menu items 'Go to photos...' for own entries"), SYSTEM_ACTIVITY_LOG_PHOTO_PREVIEWS(108, "0", false, ConfigurationDataType.YES_NO, ConfigurationUnit.EMPTY, ConfigurationTab.SYSTEM, "Log photo preview activity"), SYSTEM_ACTIVITY_LOG_FAVORITE_ACTIONS(109, "0", false, ConfigurationDataType.YES_NO, ConfigurationUnit.EMPTY, ConfigurationTab.SYSTEM, "Log favorite actions activity"), SYSTEM_UI_SHOW_PHOTOS_BY_CATEGORIES_MENU_ITEM(111, "1", false, ConfigurationDataType.YES_NO, ConfigurationUnit.EMPTY, ConfigurationTab.SYSTEM, "Show photos by categories site menu item"), SYSTEM_UI_SHOW_PHOTO_CATEGORIES_TOOLBAR(112, "0", false, ConfigurationDataType.YES_NO, ConfigurationUnit.EMPTY, ConfigurationTab.SYSTEM, "Show photo categories toolbar"), USER_LIST_USERS_ON_PAGE(115, "30", false, ConfigurationDataType.INTEGER, ConfigurationUnit.USER, ConfigurationTab.SYSTEM, "User list: users on page")
    //	, SYSTEM_TEMP( 10900, "1", false, ConfigurationDataType.INTEGER, ConfigurationUnit.ITEM, ConfigurationTab.SYSTEM, "Test missing configuration" )

    , SYSTEM_UI_PORTAL_PAGE_LATEST_PHOTOS_COUNT(113, "12", false, ConfigurationDataType.INTEGER, ConfigurationUnit.PHOTO, ConfigurationTab.PORTAL_PAGE, "Portal page latest photos count"), PHOTO_RATING_PORTAL_PAGE_BEST_PHOTOS_FROM_PHOTOS_THAT_GOT_ENOUGH_MARKS_FOR_N_LAST_DAYS(1000, "3", false, ConfigurationDataType.INTEGER, ConfigurationUnit.DAY, ConfigurationTab.PORTAL_PAGE, "The best portal page photos based on a voting period in last N days"), PORTAL_PAGE_BEST_AUTHORS_RATING_LENGTH(114, "10", false, ConfigurationDataType.INTEGER, ConfigurationUnit.ITEM, ConfigurationTab.PORTAL_PAGE, "Portal page best authors rating length"), SYSTEM_ACTIVITY_PORTAL_PAGE_STREAM_LENGTH(107, "15", false, ConfigurationDataType.INTEGER, ConfigurationUnit.ITEM, ConfigurationTab.PORTAL_PAGE, "Portal page activity stream length"), MEMBERS_FILE_MAX_SIZE_KB(200, "500", false, ConfigurationDataType.INTEGER, ConfigurationUnit.KILOBYTE, ConfigurationTab.MEMBERS, "Max size of uploading file"), MEMBERS_DAILY_FILE_SIZE_LIMIT(201, "0", false, ConfigurationDataType.INTEGER, ConfigurationUnit.KILOBYTE, ConfigurationTab.MEMBERS, "Max summary photos size that allowed to upload daily"), MEMBERS_WEEKLY_FILE_SIZE_LIMIT(202, "3072", false, ConfigurationDataType.INTEGER, ConfigurationUnit.KILOBYTE, ConfigurationTab.MEMBERS, "Max summary photos size that allowed to upload weekly"), MEMBERS_PHOTOS_PER_DAY_LIMIT(203, "0", false, ConfigurationDataType.INTEGER, ConfigurationUnit.PHOTO, ConfigurationTab.MEMBERS, "Photos quantity per day limit"), MEMBERS_PHOTOS_PER_WEEK_LIMIT(204, "10", false, ConfigurationDataType.INTEGER, ConfigurationUnit.PHOTO, ConfigurationTab.MEMBERS, "Photos quantity per week limit")

    //	, CANDIDATES_ADMIN_MUST_CONFIRM_FIRST_N_PHOTOS_OF_CANDIDATE( 300, "0", false, ConfigurationDataType.INTEGER, ConfigurationUnit.PHOTO, ConfigurationTab.CANDIDATES, "Admin must approve first N photos of candidates before they are shown in photo lists" )
    , CANDIDATES_PHOTOS_QTY_TO_BECOME_MEMBER(301, "5", false, ConfigurationDataType.INTEGER, ConfigurationUnit.PHOTO, ConfigurationTab.CANDIDATES, "Candidates must have at least N approved photos to become a member"), CANDIDATES_FILE_MAX_SIZE_KB(302, "300", false, ConfigurationDataType.INTEGER, ConfigurationUnit.KILOBYTE, ConfigurationTab.CANDIDATES, "Max size of uploading file"), CANDIDATES_DAILY_FILE_SIZE_LIMIT(303, "0", false, ConfigurationDataType.INTEGER, ConfigurationUnit.KILOBYTE, ConfigurationTab.CANDIDATES, "Max summary photos size that allowed to upload daily"), CANDIDATES_WEEKLY_FILE_SIZE_LIMIT(304, "0", false, ConfigurationDataType.INTEGER, ConfigurationUnit.KILOBYTE, ConfigurationTab.CANDIDATES, "Max summary photos size that allowed to upload weekly"), CANDIDATES_PHOTOS_PER_DAY_LIMIT(305, "1", false, ConfigurationDataType.INTEGER, ConfigurationUnit.PHOTO, ConfigurationTab.CANDIDATES, "Photos quantity per day limit"), CANDIDATES_PHOTOS_PER_WEEK_LIMIT(306, "5", false, ConfigurationDataType.INTEGER, ConfigurationUnit.PHOTO, ConfigurationTab.CANDIDATES, "Photos quantity per week limit"), CANDIDATES_CAN_COMMENT_PHOTOS(307, "0", false, ConfigurationDataType.YES_NO, ConfigurationUnit.EMPTY, ConfigurationTab.CANDIDATES, "Candidates can comment photos"), CANDIDATES_CAN_VOTE_FOR_PHOTOS(308, "1", false, ConfigurationDataType.YES_NO, ConfigurationUnit.EMPTY, ConfigurationTab.CANDIDATES, "Candidates can vote for photos"), CANDIDATES_CAN_VOTE_FOR_USER_RANKS_IN_GENRE(309, "0", false, ConfigurationDataType.YES_NO, ConfigurationUnit.EMPTY, ConfigurationTab.CANDIDATES, "Candidates can vote for ranks in photo categories of other members"), CANDIDATES_PHOTO_VOTING_HIGHEST_MARK(310, "2", false, ConfigurationDataType.INTEGER, ConfigurationUnit.MARK, ConfigurationTab.CANDIDATES, "The highest available mark in photo voting"), CANDIDATES_PHOTO_VOTING_LOWEST_MARK(311, "-1", false, ConfigurationDataType.INTEGER, ConfigurationUnit.MARK, ConfigurationTab.CANDIDATES, "The lowest available mark in photo voting"), PHOTO_UPLOAD_FILE_ALLOWED_EXTENSIONS(400, "image/jpeg", true, ConfigurationDataType.ARRAY_OF_STRINGS, ConfigurationUnit.EMPTY, ConfigurationTab.PHOTO_UPLOAD, "Allowed uploaded photo file formats separated by comma"), PHOTO_UPLOAD_ADDITIONAL_SIZE_WEEKLY_LIMIT_PER_RANK_KB(401, "100", false, ConfigurationDataType.INTEGER, ConfigurationUnit.KILOBYTE, ConfigurationTab.PHOTO_UPLOAD, "Additional summary photos size for file uploading for each rank in photo category per week"), PHOTO_UPLOAD_MIN_WIDTH(410, "300", true, ConfigurationDataType.INTEGER, ConfigurationUnit.PIXEL, ConfigurationTab.PHOTO_UPLOAD, "Photo min image width"), PHOTO_UPLOAD_MIN_HEIGHT(411, "300", true, ConfigurationDataType.INTEGER, ConfigurationUnit.PIXEL, ConfigurationTab.PHOTO_UPLOAD, "Photo min image height"), PHOTO_UPLOAD_MAX_WIDTH(402, "1800", true, ConfigurationDataType.INTEGER, ConfigurationUnit.PIXEL, ConfigurationTab.PHOTO_UPLOAD, "Photo max image width"), PHOTO_UPLOAD_MAX_HEIGHT(403, "1200", true, ConfigurationDataType.INTEGER, ConfigurationUnit.PIXEL, ConfigurationTab.PHOTO_UPLOAD, "Photo max image height"), PHOTO_UPLOAD_ANONYMOUS_PERIOD(407, "1", false, ConfigurationDataType.INTEGER, ConfigurationUnit.DAY, ConfigurationTab.PHOTO_UPLOAD, "Anonymous period") // TODO: use an hours instead of days
    , PHOTO_UPLOAD_IGNORE_ANONYMOUS_GLOBAL_SETTINGS_IF_USER_RANK_IN_GENRE_MORE_THEN(409, "10", false, ConfigurationDataType.INTEGER, ConfigurationUnit.RANK, ConfigurationTab.PHOTO_UPLOAD, "Ignore anonymous global settings if user's rank in genre more then"), PHOTO_UPLOAD_AVATAR_MAX_SIZE_KB(406, "500", true, ConfigurationDataType.INTEGER, ConfigurationUnit.KILOBYTE, ConfigurationTab.AVATAR, "Avatar picture max file size"), PHOTO_UPLOAD_AVATAR_MIN_WIDTH(404, "100", true, ConfigurationDataType.INTEGER, ConfigurationUnit.PIXEL, ConfigurationTab.AVATAR, "Avatar min image width"), PHOTO_UPLOAD_AVATAR_MIN_HEIGHT(405, "100", true, ConfigurationDataType.INTEGER, ConfigurationUnit.PIXEL, ConfigurationTab.AVATAR, "Avatar min image height"), PHOTO_UPLOAD_AVATAR_MAX_WIDTH(412, "800", true, ConfigurationDataType.INTEGER, ConfigurationUnit.PIXEL, ConfigurationTab.AVATAR, "Avatar max image width"), PHOTO_UPLOAD_AVATAR_MAX_HEIGHT(413, "800", true, ConfigurationDataType.INTEGER, ConfigurationUnit.PIXEL, ConfigurationTab.AVATAR, "Avatar max image height"), PHOTO_LIST_PHOTO_TOP_QTY(505, "4", false, ConfigurationDataType.INTEGER, ConfigurationUnit.PHOTO, ConfigurationTab.PHOTOS, "Photo list: Photos qty in a TOP list"), PHOTO_LIST_PHOTOS_ON_PAGE(503, "20", false, ConfigurationDataType.INTEGER, ConfigurationUnit.PHOTO, ConfigurationTab.PHOTOS, "Photo list: photos on page by default"), PHOTO_LIST_PHOTOS_ON_PAGE_FOR_MOBILE_DEVICES(504, "10", false, ConfigurationDataType.INTEGER, ConfigurationUnit.PHOTO, ConfigurationTab.PHOTOS, "Photo list: photos on page by default for mobile devices"), PHOTO_LIST_SHOW_PREVIEW_FOOTER(509, "1", false, ConfigurationDataType.YES_NO, ConfigurationUnit.EMPTY, ConfigurationTab.PHOTOS, "Photo list: display photo preview footer"), PHOTO_LIST_SHOW_PHOTO_MENU(506, "1", false, ConfigurationDataType.YES_NO, ConfigurationUnit.EMPTY, ConfigurationTab.PHOTOS, "Photo list: show photo menu"), PHOTO_LIST_SHOW_STATISTIC(507, "1", false, ConfigurationDataType.YES_NO, ConfigurationUnit.EMPTY, ConfigurationTab.PHOTOS, "Photo list: show photo statistic (marks/previews/comments)"), PHOTO_LIST_SHOW_USER_RANK_IN_GENRE(508, "1", false, ConfigurationDataType.YES_NO, ConfigurationUnit.EMPTY, ConfigurationTab.PHOTOS, "Photo list: show user rank in photo category"), RANK_VOTING_FIRST_RANK_POINTS(601, "5", false, ConfigurationDataType.INTEGER, ConfigurationUnit.VOICE, ConfigurationTab.RANK_VOTING, "First rank points"), RANK_VOTING_POINTS_BASE_STEP(602, "6", true, ConfigurationDataType.INTEGER, ConfigurationUnit.VOICE, ConfigurationTab.RANK_VOTING, "Rank voting base step points"), RANK_VOTING_POINTS_COEFFICIENT(603, "0.2", true, ConfigurationDataType.FLOAT, ConfigurationUnit.EMPTY, ConfigurationTab.RANK_VOTING, "Rank voting coefficient"), RANK_VOTING_MAX_GENRE_RANK(604, "25", false, ConfigurationDataType.INTEGER, ConfigurationUnit.RANK, ConfigurationTab.RANK_VOTING, "Max rank in photo category"), RANK_VOTING_MIN_PHOTOS_QTY_IN_GENRE(605, "3", false, ConfigurationDataType.INTEGER, ConfigurationUnit.PHOTO, ConfigurationTab.RANK_VOTING, "Member must have this approved photos quantity in a photo category to make other members can vote for his rank in the category"), RANK_VOTING_RANK_QTY_TO_COLLAPSE(606, "10", false, ConfigurationDataType.INTEGER, ConfigurationUnit.RANK, ConfigurationTab.RANK_VOTING, "Collapse user's rank in category with one icon (User card -> Photos and ranks -> Stars for genres)"), COMMENTS_MIN_LENGTH(700, "4", false, ConfigurationDataType.INTEGER, ConfigurationUnit.SYMBOL, ConfigurationTab.COMMENTS, "Min length of comment"), COMMENTS_MAX_LENGTH(701, "777", false, ConfigurationDataType.INTEGER, ConfigurationUnit.SYMBOL, ConfigurationTab.COMMENTS, "Max length of comment"), COMMENTS_DELAY_AFTER_COMMENT_SEC(702, "5", false, ConfigurationDataType.INTEGER, ConfigurationUnit.SECOND, ConfigurationTab.COMMENTS, "Delay after the last comment before member can leave a new one")
    //	, COMMENTS_USER_MUST_HAVE_N_APPROVED_PHOTOS_BEFORE_CAN_LEAVE_COMMENT( 703, "2", false, ConfigurationDataType.INTEGER, ConfigurationUnit.PHOTO, ConfigurationTab.COMMENTS, "Member must have N approved by com.bordozer.jphoto.admin photos before ability to comment of photos (to be implemented somedays)" )

    , PHOTO_VOTING_HIGHEST_POSITIVE_MARK(800, "5", true, ConfigurationDataType.INTEGER, ConfigurationUnit.MARK, ConfigurationTab.PHOTO_VOTING, "The highest accessible positive mark even if a voting member's own rank in photo category is higher (cutting the highest mark if rank is too hight)"), PHOTO_VOTING_LOWEST_POSITIVE_MARK(801, "3", true, ConfigurationDataType.INTEGER, ConfigurationUnit.MARK, ConfigurationTab.PHOTO_VOTING, "The highest accessible positive mark even if a voting member's own rank in photo category is lower (adding an extra positive balls to the highest mark if rank is too low)"), PHOTO_VOTING_HIGHEST_NEGATIVE_MARK(802, "-2", true, ConfigurationDataType.INTEGER, ConfigurationUnit.MARK, ConfigurationTab.PHOTO_VOTING, "The lowest accessible negative mark even if a voting member's own rank in photo category is lower (adding an extra negative balls to the lowest mark if rank is too low)"), PHOTO_VOTING_LOWEST_NEGATIVE_MARK(803, "-3", true, ConfigurationDataType.INTEGER, ConfigurationUnit.MARK, ConfigurationTab.PHOTO_VOTING, "The lowest accessible negative mark even if a voting member's own rank in photo category is higher (cutting the lowest mark if rank is too hight)"), PHOTO_VOTING_APPRAISAL_CATEGORIES_COUNT(804, "3", true, ConfigurationDataType.INTEGER, ConfigurationUnit.ITEM, ConfigurationTab.PHOTO_VOTING, "Photo appraisal: categories count"), PHOTO_VOTING_ALLOW_MULTIPLE_APPRAISAL_CATEGORIES(805, "0", true, ConfigurationDataType.YES_NO, ConfigurationUnit.EMPTY, ConfigurationTab.PHOTO_VOTING, "Allow appraisal a photo in multiple categories ( define a category more then one time )"), PHOTO_RATING_MIN_MARKS_TO_BE_IN_PHOTO_OF_THE_DAY(1001, "50", false, ConfigurationDataType.INTEGER, ConfigurationUnit.MARK, ConfigurationTab.PHOTO_RATING, "A photo must get this marks in voting period to pretend to be the best photo of the day"), PHOTO_RATING_CALCULATE_MARKS_FOR_THE_BEST_PHOTOS_FOR_LAST_DAYS(1002, "3", false, ConfigurationDataType.INTEGER, ConfigurationUnit.DAY, ConfigurationTab.PHOTO_RATING, "The best photos in photo lists based on a voting period in last N days"), PHOTO_RATING_MIN_MARKS_TO_BE_IN_THE_BEST_PHOTO(1003, "40", false, ConfigurationDataType.INTEGER, ConfigurationUnit.MARK, ConfigurationTab.PHOTO_RATING, "A photo must get this marks in voting period to appear in a top best of photo list"), CACHE_USE_CACHE(900, "1", false, ConfigurationDataType.YES_NO, ConfigurationUnit.EMPTY, ConfigurationTab.CACHE, "Use cache"), CACHE_LENGTH_USER(901, "1000", false, ConfigurationDataType.INTEGER, ConfigurationUnit.ITEM, ConfigurationTab.CACHE, "Member cache length"), CACHE_LENGTH_USER_AVATAR(902, "1000", false, ConfigurationDataType.INTEGER, ConfigurationUnit.ITEM, ConfigurationTab.CACHE, "Member avatars cache length"), CACHE_LENGTH_GENRE(903, "30", false, ConfigurationDataType.INTEGER, ConfigurationUnit.ITEM, ConfigurationTab.CACHE, "Photo category cache length"), CACHE_LENGTH_GENRE_VOTING_CATEGORY(904, "30", false, ConfigurationDataType.INTEGER, ConfigurationUnit.ITEM, ConfigurationTab.CACHE, "Accessible for a photo category voting categories cache length"), CACHE_LENGTH_PHOTO(905, "500", false, ConfigurationDataType.INTEGER, ConfigurationUnit.ITEM, ConfigurationTab.CACHE, "Photo cache length"), CACHE_LENGTH_PHOTO_VOTING_CATEGORY(906, "15", false, ConfigurationDataType.INTEGER, ConfigurationUnit.ITEM, ConfigurationTab.CACHE, "Photo voting category cache length"), CACHE_LENGTH_PHOTO_INFO(907, "500", false, ConfigurationDataType.INTEGER, ConfigurationUnit.ITEM, ConfigurationTab.CACHE, "Photo info cache length"), CACHE_LENGTH_PHOTO_COMMENT(908, "1500", false, ConfigurationDataType.INTEGER, ConfigurationUnit.ITEM, ConfigurationTab.CACHE, "Photo comment cache length"), CACHE_LENGTH_USER_TEAM_MEMBER(909, "1500", false, ConfigurationDataType.INTEGER, ConfigurationUnit.ITEM, ConfigurationTab.CACHE, "User team member cache length"), CACHE_LENGTH_USER_PHOTO_ALBUM(910, "1500", false, ConfigurationDataType.INTEGER, ConfigurationUnit.ITEM, ConfigurationTab.CACHE, "User photo album cache length"), CACHE_LENGTH_USER_GENRE_RANK(911, "1111", false, ConfigurationDataType.INTEGER, ConfigurationUnit.ITEM, ConfigurationTab.CACHE, "User rank in a photo category cache length"), CACHE_LENGTH_RANK_IN_GENRE_POINTS(912, "200", false, ConfigurationDataType.INTEGER, ConfigurationUnit.ITEM, ConfigurationTab.CACHE, "Rank in a photo category points cache length"), CACHE_LENGTH_USER_PHOTOS_BY_GENRES(913, "2000", false, ConfigurationDataType.INTEGER, ConfigurationUnit.ITEM, ConfigurationTab.CACHE, "User photos in a photo category quantity cache length"), PHOTO_CARD_MAX_WIDTH(1010, "1100", false, ConfigurationDataType.INTEGER, ConfigurationUnit.PIXEL, ConfigurationTab.PHOTO_CARD, "Photo card: resize photo image width to"), PHOTO_CARD_MAX_HEIGHT(1011, "900", false, ConfigurationDataType.INTEGER, ConfigurationUnit.PIXEL, ConfigurationTab.PHOTO_CARD, "Photo card: resize photo image height to"), ADMIN_CAN_EDIT_OTHER_USER_DATA(1020, "1", false, ConfigurationDataType.YES_NO, ConfigurationUnit.EMPTY, ConfigurationTab.ADMIN, "Admin can edit other user data"), ADMIN_CAN_EDIT_OTHER_PHOTOS(1021, "1", false, ConfigurationDataType.YES_NO, ConfigurationUnit.EMPTY, ConfigurationTab.ADMIN, "Admin can edit other photos"), ADMIN_CAN_DELETE_OTHER_PHOTOS(1022, "1", false, ConfigurationDataType.YES_NO, ConfigurationUnit.EMPTY, ConfigurationTab.ADMIN, "Admin can delete other photos"), ADMIN_CAN_EDIT_PHOTO_COMMENTS(1024, "1", false, ConfigurationDataType.YES_NO, ConfigurationUnit.EMPTY, ConfigurationTab.ADMIN, "Admin can edit photo comments"), ADMIN_JOB_HISTORY_ITEMS_ON_PAGE(1023, "50", false, ConfigurationDataType.INTEGER, ConfigurationUnit.ITEM, ConfigurationTab.ADMIN, "Job history items on page"), ADMIN_REMOTE_PHOTO_SITE_IMPORT_JOB_IMPORT_NUDE_CONTENT(1025, "0", false, ConfigurationDataType.YES_NO, ConfigurationUnit.EMPTY, ConfigurationTab.ADMIN, "Photosight import: select by default categories that may contains nude content"), ADMIN_PHOTO_PREVIEW_DENSITY(1026, "72", true, ConfigurationDataType.INTEGER, ConfigurationUnit.DPI, ConfigurationTab.ADMIN, "Photo preview density"), ADMIN_PHOTO_PREVIEW_DIMENSION(1027, "300", true, ConfigurationDataType.INTEGER, ConfigurationUnit.PIXEL, ConfigurationTab.ADMIN, "Photo preview dimension"), ADMIN_PHOTO_FILE_IMPORT_DEFAULT_DIR(1028, "", true, ConfigurationDataType.STRING, ConfigurationUnit.EMPTY, ConfigurationTab.ADMIN, "Photo file import default dir"), EMAILING_ENABLED(1300, "0", false, ConfigurationDataType.YES_NO, ConfigurationUnit.EMPTY, ConfigurationTab.EMAILING, "Emailing enabled"), EMAILING_SMTP_DEBUG_MODE(1308, "jphoto2003", false, ConfigurationDataType.YES_NO, ConfigurationUnit.EMPTY, ConfigurationTab.EMAILING, "SMTP debug mode"), EMAILING_ADMIN_EMAIL(1301, "jphoto@pop3.ru", false, ConfigurationDataType.STRING, ConfigurationUnit.EMPTY, ConfigurationTab.EMAILING, "Admin email write to"), EMAILING_NO_REPLY_EMAIL(1302, "jphoto@pop3.ru", false, ConfigurationDataType.STRING, ConfigurationUnit.EMPTY, ConfigurationTab.EMAILING, "No reply email"), EMAILING_SMTP_SERVER(1303, "smtp.qip.ru", false, ConfigurationDataType.STRING, ConfigurationUnit.EMPTY, ConfigurationTab.EMAILING, "SMTP server"), EMAILING_TRANSPORT_PROTOCOL(1309, "smtp", false, ConfigurationDataType.STRING, ConfigurationUnit.EMPTY, ConfigurationTab.EMAILING, "Transport protocol"), EMAILING_SMTP_SERVER_PORT(1304, "2525", false, ConfigurationDataType.INTEGER, ConfigurationUnit.EMPTY, ConfigurationTab.EMAILING, "SMTP server port"), EMAILING_SMTP_SERVER_TIMEOUT(1305, "5000", false, ConfigurationDataType.STRING, ConfigurationUnit.MILLISECOND, ConfigurationTab.EMAILING, "SMTP server timeout, milliseconds"), EMAILING_SMTP_SERVER_USER(1306, "jphoto@pop3.ru", false, ConfigurationDataType.STRING, ConfigurationUnit.EMPTY, ConfigurationTab.EMAILING, "SMTP server user"), EMAILING_SMTP_SERVER_PASSWORD(1307, "jphoto2003", false, ConfigurationDataType.STRING, ConfigurationUnit.EMPTY, ConfigurationTab.EMAILING, "SMTP server password"), ARCHIVING_PREVIEWS(1400, "14", true, ConfigurationDataType.INTEGER, ConfigurationUnit.DAY, ConfigurationTab.ARCHIVING, "Who viewed the photo: DELETE information older then"), ARCHIVING_VOTES(1401, "14", true, ConfigurationDataType.INTEGER, ConfigurationUnit.DAY, ConfigurationTab.ARCHIVING, "Who apprised the photo: archive information older then"), ARCHIVING_PHOTOS(1402, "30", true, ConfigurationDataType.INTEGER, ConfigurationUnit.DAY, ConfigurationTab.ARCHIVING, "Archive photos uploaded earlie then");

    private final int id;
    private final String description;
    private final String defaultValue;
    private final boolean editableInDefaultConfigurationOnly;

    private final ConfigurationDataType dataType;
    private final ConfigurationUnit unit;
    private final ConfigurationTab tab;

    private ConfigurationKey(final int id, final String defaultValue, final boolean editableInDefaultConfigurationOnly, final ConfigurationDataType dataType, final ConfigurationUnit unit, final ConfigurationTab tab, final String description) {
        this.id = id;
        this.editableInDefaultConfigurationOnly = editableInDefaultConfigurationOnly;
        this.description = description;
        this.defaultValue = defaultValue;
        this.dataType = dataType;
        this.unit = unit;
        this.tab = tab;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public boolean isEditableInDefaultConfigurationOnly() {
        return editableInDefaultConfigurationOnly;
    }

    public ConfigurationDataType getDataType() {
        return dataType;
    }

    public ConfigurationUnit getUnit() {
        return unit;
    }

    public ConfigurationTab getTab() {
        return tab;
    }

    public static ConfigurationKey getById(final int id) {
        for (final ConfigurationKey configurationKey : ConfigurationKey.values()) {
            if (configurationKey.getId() == id) {
                return configurationKey;
            }
        }

        return null;
    }
}