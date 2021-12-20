# SQL Manager 2007 for MySQL 4.5.0.7
# ---------------------------------------
# Host     : localhost
# Port     : 3306
# Database : jphoto_v1


/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

SET FOREIGN_KEY_CHECKS = 0;

#
# Structure for the `users` table :
#

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users`
(
    `id`                              int(11)      NOT NULL AUTO_INCREMENT,
    `name`                            varchar(100) NOT NULL,
    `login`                           varchar(20)  NOT NULL,
    `email`                           varchar(30)  NOT NULL,
    `dateOfBirth`                     date         NOT NULL DEFAULT '1000-01-01',
    `homeSite`                        varchar(100)          DEFAULT '',
    `photosOnPage`                    tinyint(4)   NOT NULL DEFAULT '4',
    `registerTime`                    timestamp    NOT NULL DEFAULT '1970-01-01 03:00:01',
    `membershipType`                  tinyint(4)   NOT NULL DEFAULT '1',
    `userStatus`                      tinyint(4)   NOT NULL,
    `gender`                          tinyint(1)   NOT NULL,
    `emailNotificationOptions`        varchar(100)          DEFAULT NULL,
    `defaultPhotoCommentsAllowanceId` tinyint(1)   NOT NULL,
    `defaultPhotoVotingAllowanceId`   tinyint(1)            DEFAULT NULL,
    `showNudeContent`                 tinyint(1)            DEFAULT NULL,
    `selfDescription`                 text,
    `languageId`                      tinyint(4)   NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `idx_id` (`id`) USING BTREE,
    UNIQUE KEY `idx_name` (`name`) USING BTREE,
    UNIQUE KEY `idx_login` (`login`) USING BTREE,
    UNIQUE KEY `idx_email` (`email`) USING BTREE,
    UNIQUE KEY `name` (`name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3286
  DEFAULT CHARSET = utf8
  AVG_ROW_LENGTH = 203;

#
# Structure for the `activityStream` table :
#

DROP TABLE IF EXISTS `activityStream`;

CREATE TABLE `activityStream`
(
    `id`           int(11)    NOT NULL AUTO_INCREMENT,
    `activityType` tinyint(4) NOT NULL,
    `activityTime` timestamp  NOT NULL DEFAULT '1970-01-01 03:00:01',
    `userId`       int(11)             DEFAULT NULL,
    `photoId`      int(11)             DEFAULT NULL,
    `activityXML`  text       NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `id` (`id`),
    KEY `userId` (`userId`),
    KEY `photoId` (`photoId`),
    KEY `activityTime` (`activityTime`),
    CONSTRAINT `fk_activityStream_userId_users_id` FOREIGN KEY (`userId`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3254225
  DEFAULT CHARSET = utf8;

#
# Structure for the `anonymousDay` table :
#

DROP TABLE IF EXISTS `anonymousDay`;

CREATE TABLE `anonymousDay`
(
    `anonymousDayDate` timestamp NOT NULL DEFAULT '1970-01-01 03:00:01',
    PRIMARY KEY (`anonymousDayDate`),
    UNIQUE KEY `anonymousDayDate` (`anonymousDayDate`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

#
# Structure for the `genres` table :
#

DROP TABLE IF EXISTS `genres`;

CREATE TABLE `genres`
(
    `id`                    int(11)      NOT NULL AUTO_INCREMENT,
    `name`                  varchar(255) NOT NULL DEFAULT '',
    `minmarksforbest`       tinyint(4)            DEFAULT NULL,
    `description`           text,
    `canContainNudeContent` tinyint(1)   NOT NULL DEFAULT '0',
    `containsNudeContent`   tinyint(1)   NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `idx_id` (`id`) USING BTREE,
    UNIQUE KEY `idx_name` (`name`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 23
  DEFAULT CHARSET = utf8
  AVG_ROW_LENGTH = 910;

#
# Structure for the `photos` table :
#

DROP TABLE IF EXISTS `photos`;

CREATE TABLE `photos`
(
    `id`                                    int(11)      NOT NULL AUTO_INCREMENT,
    `userId`                                int(11)      NOT NULL,
    `name`                                  varchar(255) NOT NULL DEFAULT '',
    `genreId`                               int(11)      NOT NULL,
    `keywords`                              varchar(255) NOT NULL DEFAULT '',
    `description`                           text,
    `photoImageSource`                      varchar(255) NOT NULL DEFAULT '',
    `photoPreviewName`                      varchar(255) NOT NULL,
    `fileSize`                              int(11)               DEFAULT '0',
    `uploadTime`                            timestamp    NOT NULL DEFAULT '1970-01-01 03:00:01',
    `containsNudeContent`                   tinyint(1)   NOT NULL DEFAULT '0',
    `bgcolor`                               varchar(7)            DEFAULT NULL,
    `commentsAllowance`                     tinyint(1)   NOT NULL,
    `notificationEmailAboutNewPhotoComment` tinyint(1)            DEFAULT NULL,
    `votingAllowance`                       tinyint(1)   NOT NULL,
    `isAnonymousPosting`                    tinyint(1)            DEFAULT '0',
    `userGenreRank`                         tinyint(4)            DEFAULT NULL,
    `importId`                              bigint(20)            DEFAULT NULL,
    `image_width`                           smallint(6)  NOT NULL,
    `image_height`                          smallint(6)  NOT NULL,
    `imageLocationTypeId`                   tinyint(4)   NOT NULL,
    `imageSourceId`                         tinyint(4)   NOT NULL,
    `importData`                            text,
    `isArchived`                            tinyint(1)            DEFAULT '0',
    PRIMARY KEY (`id`, `keywords`),
    UNIQUE KEY `idx_id` (`id`) USING BTREE,
    KEY `fk_photos_genreId_genres_id` (`genreId`),
    KEY `fk_photos_userId_users_id` (`userId`),
    KEY `idx_uploadTime` (`uploadTime`) USING BTREE,
    CONSTRAINT `fk_photos_genreId_genres_id` FOREIGN KEY (`genreId`) REFERENCES `genres` (`id`),
    CONSTRAINT `fk_photos_userId_users_id` FOREIGN KEY (`userId`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 364368
  DEFAULT CHARSET = utf8;

#
# Structure for the `comments` table :
#

DROP TABLE IF EXISTS `comments`;

CREATE TABLE `comments`
(
    `id`               int(11)   NOT NULL AUTO_INCREMENT,
    `photoId`          int(11)   NOT NULL,
    `authorId`         int(11)   NOT NULL,
    `replyToCommentId` int(11)            DEFAULT NULL,
    `commentText`      text      NOT NULL,
    `creationTime`     timestamp NOT NULL DEFAULT '1970-01-01 03:00:01',
    `readtime`         timestamp NULL     DEFAULT '1970-01-01 03:00:01',
    `deleted`          tinyint(1)         DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `idx_id` (`id`) USING BTREE,
    KEY `fk_comments_photoId_photos_id` (`photoId`),
    KEY `fk_comments_authorId_users_id` (`authorId`),
    CONSTRAINT `fk_comments_authorId_users_id` FOREIGN KEY (`authorId`) REFERENCES `users` (`id`),
    CONSTRAINT `fk_comments_photoId_photos_id` FOREIGN KEY (`photoId`) REFERENCES `photos` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1337585
  DEFAULT CHARSET = utf8
  AVG_ROW_LENGTH = 5461;


DROP TABLE IF EXISTS `comments_archive`;

CREATE TABLE `comments_archive`
(
    `id`               int(11)   NOT NULL AUTO_INCREMENT,
    `photoId`          int(11)   NOT NULL,
    `authorId`         int(11)   NOT NULL,
    `replyToCommentId` int(11)            DEFAULT NULL,
    `commentText`      text      NOT NULL,
    `creationTime`     timestamp NOT NULL DEFAULT '1970-01-01 03:00:01',
    `readtime`         timestamp NULL     DEFAULT '1970-01-01 03:00:01',
    `deleted`          tinyint(1)         DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `idx_id` (`id`) USING BTREE,
    KEY `fk_comments_photoId_photos_id_arch` (`photoId`),
    KEY `fk_comments_authorId_users_id_arch` (`authorId`),
    CONSTRAINT `fk_comments_authorId_users_id_arch` FOREIGN KEY (`authorId`) REFERENCES `users` (`id`),
    CONSTRAINT `fk_comments_photoId_photos_id_arch` FOREIGN KEY (`photoId`) REFERENCES `photos` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 18023
  DEFAULT CHARSET = utf8
  AVG_ROW_LENGTH = 5461;


#
# Structure for the `favorites` table :
#

DROP TABLE IF EXISTS `favorites`;

CREATE TABLE `favorites`
(
    `id`              int(11)   NOT NULL AUTO_INCREMENT,
    `userId`          int(11)   NOT NULL,
    `favoriteEntryId` int(11)   NOT NULL,
    `created`         timestamp NOT NULL DEFAULT '1970-01-01 03:00:01',
    `entryType`       int(2)    NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `id` (`id`) USING BTREE,
    KEY `userId` (`userId`) USING BTREE,
    KEY `photoId` (`favoriteEntryId`) USING BTREE,
    KEY `userIdPhotoId` (`userId`, `favoriteEntryId`) USING BTREE,
    CONSTRAINT `favoritePhoto_fk` FOREIGN KEY (`userId`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 29742
  DEFAULT CHARSET = utf8;

#
# Structure for the `votingCategories` table :
#

DROP TABLE IF EXISTS `votingCategories`;

CREATE TABLE `votingCategories`
(
    `id`          int(11)     NOT NULL AUTO_INCREMENT,
    `name`        varchar(20) NOT NULL,
    `description` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `idx_id` (`id`) USING BTREE,
    UNIQUE KEY `idx_name` (`name`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 32
  DEFAULT CHARSET = utf8
  AVG_ROW_LENGTH = 2730;

#
# Structure for the `genreVotingCategories` table :
#

DROP TABLE IF EXISTS `genreVotingCategories`;

CREATE TABLE `genreVotingCategories`
(
    `id`               int(11) NOT NULL AUTO_INCREMENT,
    `genreId`          int(11) NOT NULL,
    `votingCategoryId` int(11) NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `id` (`id`) USING BTREE,
    KEY `genreId` (`genreId`) USING BTREE,
    KEY `votingCategoryId` (`votingCategoryId`) USING BTREE,
    CONSTRAINT `genreVotingCategories_fk` FOREIGN KEY (`genreId`) REFERENCES `genres` (`id`),
    CONSTRAINT `genreVotingCategories_fk1` FOREIGN KEY (`votingCategoryId`) REFERENCES `votingCategories` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 301
  DEFAULT CHARSET = utf8
  AVG_ROW_LENGTH = 154;

#
# Structure for the `job` table :
#

DROP TABLE IF EXISTS `job`;

CREATE TABLE `job`
(
    `id`       int(11)      NOT NULL AUTO_INCREMENT,
    `name`     varchar(255) NOT NULL,
    `isActive` tinyint(1)   NOT NULL DEFAULT '0',
    `jobType`  tinyint(4)   NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_id` (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 26
  DEFAULT CHARSET = utf8;

#
# Structure for the `jobExecutionHistory` table :
#

DROP TABLE IF EXISTS `jobExecutionHistory`;

CREATE TABLE `jobExecutionHistory`
(
    `id`                    int(11)    NOT NULL AUTO_INCREMENT,
    `jobTypeId`             tinyint(4) NOT NULL,
    `parametersDescription` text,
    `startTime`             timestamp  NOT NULL DEFAULT '1970-01-01 03:00:01',
    `endTime`               timestamp  NULL     DEFAULT '1970-01-01 03:00:01',
    `jobStatusId`           int(11)    NOT NULL,
    `jobMessage`            longtext,
    `savedJobId`            int(11)             DEFAULT NULL,
    `currentJobStep`        int(11)             DEFAULT NULL,
    `totalJobSteps`         int(11)             DEFAULT NULL,
    `schedulerTaskId`       smallint(6)         DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `id` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1322759
  DEFAULT CHARSET = utf8;

#
# Structure for the `jobExecutionHistoryParameters` table :
#

DROP TABLE IF EXISTS `jobExecutionHistoryParameters`;

CREATE TABLE `jobExecutionHistoryParameters`
(
    `id`                    int(11)    NOT NULL AUTO_INCREMENT,
    `jobExecutionHistoryId` int(11)    NOT NULL,
    `pKey`                  tinyint(4) NOT NULL,
    `pValue`                varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_id` (`id`) USING BTREE,
    KEY `jobExecutionHistoryId` (`jobExecutionHistoryId`),
    CONSTRAINT `[jobExecutionHistoryId]_fk[num_for_dup]` FOREIGN KEY (`jobExecutionHistoryId`) REFERENCES `jobExecutionHistory` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 7475741
  DEFAULT CHARSET = utf8;

#
# Structure for the `jobParameters` table :
#

DROP TABLE IF EXISTS `jobParameters`;

CREATE TABLE `jobParameters`
(
    `id`     int(11)    NOT NULL AUTO_INCREMENT,
    `jobId`  int(11)    NOT NULL,
    `pKey`   tinyint(4) NOT NULL,
    `pValue` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_id` (`id`) USING BTREE,
    KEY `fk_jobParameters_jobId_job_id` (`jobId`),
    CONSTRAINT `fk_jobParameters_jobId_job_id` FOREIGN KEY (`jobId`) REFERENCES `job` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 179
  DEFAULT CHARSET = utf8;

#
# Structure for the `userPhotoAlbum` table :
#

DROP TABLE IF EXISTS `userPhotoAlbum`;

CREATE TABLE `userPhotoAlbum`
(
    `id`          int(11)      NOT NULL AUTO_INCREMENT,
    `userId`      int(11)      NOT NULL,
    `name`        varchar(100) NOT NULL,
    `description` varchar(1000) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_id` (`id`) USING BTREE,
    UNIQUE KEY `idx_userId_name` (`userId`, `name`),
    KEY `fk_userPhotoAlbum_userId_users_id` (`userId`),
    CONSTRAINT `fk_userPhotoAlbum_userId_users_id` FOREIGN KEY (`userId`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 10870
  DEFAULT CHARSET = utf8;

#
# Structure for the `photoAlbums` table :
#

DROP TABLE IF EXISTS `photoAlbums`;

CREATE TABLE `photoAlbums`
(
    `photoId`      int(11) NOT NULL,
    `photoAlbumId` int(11) NOT NULL,
    UNIQUE KEY `idx_photoId_photoAlbumId` (`photoId`, `photoAlbumId`) USING BTREE,
    KEY `fk_photoAlbums_photoAlbumId_userPhotoAlbum_id` (`photoAlbumId`),
    CONSTRAINT `fk_photoAlbums_photoAlbumId_userPhotoAlbum_id` FOREIGN KEY (`photoAlbumId`) REFERENCES `userPhotoAlbum` (`id`),
    CONSTRAINT `fk_photoAlbums_photoId_photos_id` FOREIGN KEY (`photoId`) REFERENCES `photos` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

#
# Structure for the `photoAwards` table :
#

DROP TABLE IF EXISTS `photoAwards`;

CREATE TABLE `photoAwards`
(
    `id`       int(11)   NOT NULL AUTO_INCREMENT,
    `photoId`  int(11)   NOT NULL,
    `awardId`  int(11)   NOT NULL,
    `timeFrom` timestamp NOT NULL DEFAULT '1970-01-01 03:00:01',
    `timeTo`   timestamp NOT NULL DEFAULT '1970-01-01 03:00:01',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_id` (`id`) USING BTREE,
    KEY `idx_photoId` (`photoId`) USING BTREE,
    KEY `idx_photoId_awardId_timeFrom_timeTo` (`photoId`, `awardId`, `timeFrom`, `timeTo`) USING BTREE,
    CONSTRAINT `fk_photoAwards_photoId_photos_id` FOREIGN KEY (`photoId`) REFERENCES `photos` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

#
# Structure for the `photoPreview` table :
#

DROP TABLE IF EXISTS `photoPreview`;

CREATE TABLE `photoPreview`
(
    `id`          int(11)   NOT NULL AUTO_INCREMENT,
    `photoId`     int(11)   NOT NULL,
    `userId`      int(11)   NOT NULL,
    `previewTime` timestamp NOT NULL DEFAULT '1970-01-01 03:00:01',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_id` (`id`) USING BTREE,
    KEY `idx_photoId` (`photoId`) USING BTREE,
    CONSTRAINT `photoPreview_photoId` FOREIGN KEY (`photoId`) REFERENCES `photos` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2077722
  DEFAULT CHARSET = utf8;

#
# Structure for the `photoRatings` table :
#

DROP TABLE IF EXISTS `photoRatings`;

CREATE TABLE `photoRatings`
(
    `photoId`        int(11)   NOT NULL,
    `timeFrom`       timestamp NOT NULL DEFAULT '1970-01-01 03:00:01',
    `timeTo`         timestamp NOT NULL DEFAULT '1970-01-01 03:00:01',
    `ratingPosition` int(11)   NOT NULL,
    `summaryMark`    int(11)            DEFAULT NULL,
    PRIMARY KEY (`photoId`, `timeFrom`, `timeTo`),
    KEY `idx_photoId` (`photoId`) USING BTREE,
    KEY `idx_timeFrom_timeTo` (`timeFrom`, `timeTo`) USING BTREE,
    CONSTRAINT `fk_photoRatings_photoId_photos_id` FOREIGN KEY (`photoId`) REFERENCES `photos` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

#
# Structure for the `userTeam` table :
#

DROP TABLE IF EXISTS `userTeam`;

CREATE TABLE `userTeam`
(
    `id`               int(11)    NOT NULL AUTO_INCREMENT,
    `userId`           int(11)    NOT NULL,
    `teamMemberUserId` int(11)    NOT NULL,
    `teamMemberName`   varchar(100) DEFAULT NULL,
    `teamMemberTypeId` tinyint(4) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_id` (`id`) USING BTREE,
    UNIQUE KEY `idx_userId_teamMemberName` (`userId`, `teamMemberName`) USING BTREE,
    KEY `idx_userId` (`userId`) USING BTREE,
    KEY `idx_teamMemberUserId` (`teamMemberUserId`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 45
  DEFAULT CHARSET = utf8;

#
# Structure for the `photoTeam` table :
#

DROP TABLE IF EXISTS `photoTeam`;

CREATE TABLE `photoTeam`
(
    `photoId`          int(11) NOT NULL,
    `userTeamMemberId` int(11) NOT NULL,
    `description`      varchar(1000) DEFAULT NULL,
    PRIMARY KEY (`photoId`, `userTeamMemberId`),
    KEY `idx_photoId` (`photoId`) USING BTREE,
    KEY `idx_userTeamMemberId` (`userTeamMemberId`) USING BTREE,
    CONSTRAINT `fk_photoTeam_photoId_photos_id` FOREIGN KEY (`photoId`) REFERENCES `photos` (`id`),
    CONSTRAINT `fk_photoTeam_userTeamMemberId_userTeam_id` FOREIGN KEY (`userTeamMemberId`) REFERENCES `userTeam` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

#
# Structure for the `photoVoting` table :
#

DROP TABLE IF EXISTS `photoVoting`;

CREATE TABLE `photoVoting`
(
    `id`                int(11)   NOT NULL AUTO_INCREMENT,
    `userId`            int(11)   NOT NULL,
    `photoId`           int(11)   NOT NULL,
    `votingCategoryId`  int(11)   NOT NULL,
    `mark`              int(2)    NOT NULL,
    `votingTime`        timestamp NOT NULL DEFAULT '1970-01-01 03:00:01',
    `maxAccessibleMark` tinyint(4)         DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_id` (`id`) USING BTREE,
    KEY `fk_photoVoting_votingCategoryId_votingCategories_id` (`votingCategoryId`),
    KEY `idx_votingTime` (`votingTime`) USING BTREE,
    KEY `idx_userId` (`userId`) USING BTREE,
    KEY `idx_photoId` (`photoId`) USING BTREE,
    CONSTRAINT `fk_photoVoting_photoId_photos_id` FOREIGN KEY (`photoId`) REFERENCES `photos` (`id`),
    CONSTRAINT `fk_photoVoting_userId_users_id` FOREIGN KEY (`userId`) REFERENCES `users` (`id`),
    CONSTRAINT `fk_photoVoting_votingCategoryId_votingCategories_id` FOREIGN KEY (`votingCategoryId`) REFERENCES `votingCategories` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3735033
  DEFAULT CHARSET = utf8;

#
# Structure for the `photoVotingSummary` table :
#

DROP TABLE IF EXISTS `photoVotingSummary`;

CREATE TABLE `photoVotingSummary`
(
    `id`                    int(11) NOT NULL AUTO_INCREMENT,
    `photoId`               int(11) NOT NULL,
    `photoVotingCategoryId` int(11) NOT NULL,
    `photoSummaryMark`      int(11) DEFAULT NULL,
    `photoSummaryVoices`    int(11) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_id` (`id`) USING BTREE,
    UNIQUE KEY `idx_photoId_photoVotingCategoryId` (`photoId`, `photoVotingCategoryId`) USING BTREE,
    CONSTRAINT `fk_photoVotingSummary_photoId_photos_id` FOREIGN KEY (`photoId`) REFERENCES `photos` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1049647
  DEFAULT CHARSET = utf8;

#
# Structure for the `privateMessage` table :
#

DROP TABLE IF EXISTS `privateMessage`;

CREATE TABLE `privateMessage`
(
    `id`                  int(11)    NOT NULL AUTO_INCREMENT,
    `fromUserId`          int(11)             DEFAULT NULL,
    `toUserId`            int(11)             DEFAULT NULL,
    `messageTypeId`       tinyint(1) NOT NULL,
    `messageText`         text,
    `createTime`          timestamp  NOT NULL DEFAULT '1970-01-01 03:00:01',
    `readTime`            timestamp  NULL     DEFAULT '1970-01-01 03:00:01',
    `outPrivateMessageId` int(11)             DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `id` (`id`),
    KEY `forUserId` (`toUserId`),
    KEY `fromUserId` (`fromUserId`),
    CONSTRAINT `[OwnerNameToUserId]_fk[num_for_dup]` FOREIGN KEY (`toUserId`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 11047
  DEFAULT CHARSET = utf8;

#
# Structure for the `restriction` table :
#

DROP TABLE IF EXISTS `restriction`;

CREATE TABLE `restriction`
(
    `id`                  int(11)   NOT NULL AUTO_INCREMENT,
    `entryId`             int(11)   NOT NULL,
    `restrictionTypeId`   int(11)   NOT NULL,
    `restrictionTimeFrom` timestamp NULL DEFAULT '1970-01-01 03:00:01',
    `restrictionTimeTo`   timestamp NULL DEFAULT '1970-01-01 03:00:01',
    `restrictionMessage`  text,
    `restrictionComment`  text,
    `active`              tinyint(1)     DEFAULT NULL,
    `creatingTime`        timestamp NULL DEFAULT '1970-01-01 03:00:01',
    `createdUserId`       int(11)   NOT NULL,
    `cancelledUserId`     int(11)        DEFAULT NULL,
    `cancellingTime`      timestamp NULL DEFAULT '1970-01-01 03:00:01',
    PRIMARY KEY (`id`),
    UNIQUE KEY `id` (`id`),
    KEY `entryId` (`entryId`, `restrictionTypeId`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 40
  DEFAULT CHARSET = utf8;

#
# Structure for the `schedulerTasks` table :
#

DROP TABLE IF EXISTS `schedulerTasks`;

CREATE TABLE `schedulerTasks`
(
    `id`       int(11)      NOT NULL AUTO_INCREMENT,
    `name`     varchar(255) NOT NULL,
    `taskType` tinyint(4)   NOT NULL,
    `jobId`    int(11)      NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_id` (`id`) USING BTREE,
    UNIQUE KEY `idx_name` (`name`) USING BTREE,
    KEY `fk_schedulerTasks_jobId_job_id` (`jobId`),
    CONSTRAINT `fk_schedulerTasks_jobId_job_id` FOREIGN KEY (`jobId`) REFERENCES `job` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 21
  DEFAULT CHARSET = utf8;

#
# Structure for the `schedulerTasksProperties` table :
#

DROP TABLE IF EXISTS `schedulerTasksProperties`;

CREATE TABLE `schedulerTasksProperties`
(
    `id`              int(11) NOT NULL AUTO_INCREMENT,
    `schedulerTaskId` int(11) NOT NULL,
    `propertyKey`     int(11) NOT NULL,
    `propertyValue`   varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_id` (`id`) USING BTREE,
    KEY `fk_schedulerTasksProperties_schedulerTaskId_schedulerTasks_id` (`schedulerTaskId`),
    CONSTRAINT `fk_schedulerTasksProperties_schedulerTaskId_schedulerTasks_id` FOREIGN KEY (`schedulerTaskId`) REFERENCES `schedulerTasks` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2229
  DEFAULT CHARSET = utf8;

#
# Structure for the `systemConfiguration` table :
#

DROP TABLE IF EXISTS `systemConfiguration`;

CREATE TABLE `systemConfiguration`
(
    `id`          int(11)      NOT NULL AUTO_INCREMENT,
    `name`        varchar(255) NOT NULL,
    `description` text,
    `isdefault`   tinyint(1)   NOT NULL DEFAULT '0',
    `isactive`    tinyint(1)   NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_id` (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 54
  DEFAULT CHARSET = utf8;

#
# Structure for the `systemConfigurationKeys` table :
#

DROP TABLE IF EXISTS `systemConfigurationKeys`;

CREATE TABLE `systemConfigurationKeys`
(
    `id`                    int(11)     NOT NULL AUTO_INCREMENT,
    `systemConfigurationId` int(11)     NOT NULL,
    `configurationKeyId`    smallint(6) NOT NULL,
    `value`                 varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_id` (`id`) USING BTREE,
    UNIQUE KEY `idx_systemConfigurationId_configurationKeyId` (`systemConfigurationId`, `configurationKeyId`) USING BTREE,
    CONSTRAINT `fk_systemConfigurationId_systemConfiguration_id` FOREIGN KEY (`systemConfigurationId`) REFERENCES `systemConfiguration` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 20171
  DEFAULT CHARSET = utf8;

#
# Structure for the `translations` table :
#

DROP TABLE IF EXISTS `translations`;

CREATE TABLE `translations`
(
    `id`          int(11)       NOT NULL AUTO_INCREMENT,
    `entryTypeId` int(11)       NOT NULL,
    `entryId`     int(11)       NOT NULL,
    `languageId`  tinyint(4)    NOT NULL,
    `translation` varchar(1000) NOT NULL,
    PRIMARY KEY (`entryTypeId`, `entryId`, `languageId`),
    UNIQUE KEY `id` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 85
  DEFAULT CHARSET = utf8;

#
# Structure for the `upgradeLog` table :
#

DROP TABLE IF EXISTS `upgradeLog`;

CREATE TABLE `upgradeLog`
(
    `id`              int(11)      NOT NULL AUTO_INCREMENT,
    `upgradeTaskName` varchar(200) NOT NULL,
    `performanceTime` timestamp    NOT NULL DEFAULT '1970-01-01 03:00:01',
    `taskResult`      tinyint(1)   NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_id` (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

#
# Structure for the `userSecurity` table :
#

DROP TABLE IF EXISTS `userSecurity`;

CREATE TABLE `userSecurity`
(
    `userId`               int(11)     NOT NULL,
    `userPassword`         varchar(60) NOT NULL,
    `lastLoginTime`        timestamp   NULL DEFAULT '1970-01-01 03:00:01',
    `lastLoginIP`          varchar(32)      DEFAULT NULL,
    `authorizationKey`     varchar(1000)    DEFAULT NULL,
    `lastUserActivityTime` timestamp   NULL DEFAULT '1970-01-01 03:00:01',
    PRIMARY KEY (`userId`),
    UNIQUE KEY `userId` (`userId`),
    CONSTRAINT `fk_UserSecurity_UserId` FOREIGN KEY (`userId`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

#
# Structure for the `usersRankHistory` table :
#

DROP TABLE IF EXISTS `usersRankHistory`;

CREATE TABLE `usersRankHistory`
(
    `userId`     int(11)   NOT NULL,
    `genreId`    int(11)   NOT NULL,
    `rank`       int(11)            DEFAULT NULL,
    `assignTime` timestamp NOT NULL DEFAULT '1970-01-01 03:00:01',
    KEY `userId` (`userId`),
    KEY `genreId` (`genreId`),
    KEY `idx_userId_genreId` (`userId`, `genreId`),
    CONSTRAINT `fk_usersRankHistory_genreId` FOREIGN KEY (`genreId`) REFERENCES `genres` (`id`),
    CONSTRAINT `[OwnerName]_fk[num_for_dup]` FOREIGN KEY (`userId`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

#
# Structure for the `usersRanksByGenres` table :
#

DROP TABLE IF EXISTS `usersRanksByGenres`;

CREATE TABLE `usersRanksByGenres`
(
    `id`      int(11)    NOT NULL AUTO_INCREMENT,
    `userId`  int(11)    NOT NULL,
    `genreId` int(11)    NOT NULL,
    `rank`    tinyint(4) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_id` (`id`) USING BTREE,
    UNIQUE KEY `fk_usersRanksByGenres_userId_genreid` (`userId`, `genreId`),
    KEY `fk_usersRanksByGenres_userId_users_id` (`userId`),
    KEY `fk_usersRanksByGenres_genreId_genres_id` (`genreId`),
    CONSTRAINT `fk_usersRanksByGenres_genreId_genres_id` FOREIGN KEY (`genreId`) REFERENCES `genres` (`id`),
    CONSTRAINT `fk_usersRanksByGenres_userId_users_id` FOREIGN KEY (`userId`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2674
  DEFAULT CHARSET = utf8;

#
# Structure for the `usersRanksByGenresVoting` table :
#

DROP TABLE IF EXISTS `usersRanksByGenresVoting`;

CREATE TABLE `usersRanksByGenresVoting`
(
    `id`                 int(11)    NOT NULL AUTO_INCREMENT,
    `userId`             int(11)    NOT NULL,
    `voterId`            int(11)    NOT NULL,
    `genreId`            int(11)    NOT NULL,
    `points`             tinyint(4) NOT NULL,
    `userRankWhenVoting` tinyint(4) NOT NULL,
    `votingtime`         timestamp  NOT NULL DEFAULT '1970-01-01 03:00:01',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_id` (`id`) USING BTREE,
    KEY `fk_usersRanksByGenresVoting_userId_users_id` (`userId`),
    KEY `fk_usersRanksByGenresVoting_genreId_genres_id` (`genreId`),
    KEY `fk_usersRanksByGenresVoting_voterId_users_id` (`voterId`),
    CONSTRAINT `fk_usersRanksByGenresVoting_genreId_genres_id` FOREIGN KEY (`genreId`) REFERENCES `genres` (`id`),
    CONSTRAINT `fk_usersRanksByGenresVoting_userId_users_id` FOREIGN KEY (`userId`) REFERENCES `users` (`id`),
    CONSTRAINT `fk_usersRanksByGenresVoting_voterId_users_id` FOREIGN KEY (`voterId`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 231570
  DEFAULT CHARSET = utf8;



/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;



INSERT INTO `votingCategories` (`name`, `description`)
VALUES ('Categoriy-1', ''),
       ('Categoriy-2', ''),
       ('Categoriy-3', ''),
       ('Categoriy-4', '');

COMMIT;

DELETE
FROM favorites;
DELETE
FROM comments;

DELETE
FROM `activityStream`;

DELETE
FROM `photoPreview`;
DELETE
FROM `photoVoting`;
DELETE
FROM `photoVotingSummary`;
DELETE
FROM `photoAwards`;
DELETE
FROM `photoRatings`;
DELETE
FROM `photoAwards`;
DELETE
FROM `photoAlbums`;
DELETE
FROM `photoTeam`;
DELETE
FROM photos;

DELETE
FROM `privateMessage`;
DELETE
FROM `usersRanksByGenresVoting`;
DELETE
FROM `usersRanksByGenres`;
DELETE
FROM `usersRankHistory`;
DELETE
FROM `userPhotoAlbum`;
DELETE
FROM `userTeam`;
DELETE
FROM `userSecurity`;
DELETE
FROM users;



DELETE
FROM `schedulerTasksProperties`;
DELETE
FROM `schedulerTasks`;

DELETE
FROM `jobParameters`;
DELETE
FROM `job`;

DELETE
FROM jobExecutionHistoryParameters;
DELETE
FROM jobExecutionHistory;


DELETE
FROM `systemConfigurationKeys`;
DELETE
FROM `systemConfiguration`;


UPDATE users
SET users.`notificationEmailAboutNewPhotosOfFavoriteMembers`   = 0,
    users.`notificationMessageAboutNewPhotosOfFavoriteMembers` = 0,
    users.`defaultNotificationEmailAboutNewPhotoComment`       = 0,
    users.`defaultPhotoCommentsAllowanceId`                    = 3;

UPDATE photos
SET photos.`notificationEmailAboutNewPhotoComment` = 1;

UPDATE photos
SET photos.`votingAllowance` = 2;
UPDATE users
SET defaultPhotoVotingAllowanceId = 2;


--
delete all user 's photos -->
DELETE
FROM `comments`
WHERE `photoId` IN (
	SELECT p.id
    FROM photos p
    WHERE p.`userId` = 7807
);
DELETE FROM `photos` WHERE `userId` = 7807;
--delete all user' s photos <
--

/* delete photos with ZERO upload time */
DELETE
FROM `photoAlbums`
WHERE photoAlbums.`photoId` IN (
    SELECT photos.id
    FROM `photos`
    WHERE photos.uploadTime = 0
);

DELETE
FROM `photos`
WHERE photos.uploadTime = 0;
/* / delete photos with ZERO upload time */


UPDATE photos
SET photos.bgcolor = CONCAT("#", photos.bgcolor)
WHERE photos.`bgcolor` <> ""
