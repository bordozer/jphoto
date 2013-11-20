package admin.jobs.enums;

import utils.TranslatorUtils;

public enum SavedJobType {

	PREVIEW_GENERATION( 4, "Photo previews generation", JobListTab.SYSTEM_JOBS, "previews", "previewGeneration.png" )
	, REINDEX( 11, "Recreate indexes and constraints", JobListTab.SYSTEM_JOBS, "reindex", "reindex.png" )
	, USER_MEMBERSHIP( 7, "User membership recalculation", JobListTab.RECALCULATION, "membership", "userMembershipRecalculation.png" )
	, USER_GENRES_RANKS_RECALCULATING( 5, "Members ranks in genres recalculation", JobListTab.RECALCULATION, "genreRank", "userGenreRankRecalculation.png" )
	, PHOTO_RATING( 10, "Photo rating for period recalculation", JobListTab.RECALCULATION, "photoRating", "photoRatingRecalculation.png" )
	, USER_GENERATION( 1, "Users generation", JobListTab.OTHER_DATA_GENERATION, "data/users", "userGeneration.png" )
	, PHOTOS_IMPORT( 12, "Photos import", JobListTab.OTHER_DATA_GENERATION, "data/photosImport", "photosImport.png" )
	, PHOTO_STORAGE_SYNCHRONIZATION( 14, "Photo storage synchronization", JobListTab.OTHER_DATA_GENERATION, "data/photoStorage", "synchronization.png" )
	, ACTIONS_GENERATION( 3, "Photo actions: voting", JobListTab.PHOTO_ACTIONS, "data/voting", "actionGeneration.png" )
	, ACTIONS_GENERATION_COMMENTS( 13, "Photo actions: comments", JobListTab.PHOTO_ACTIONS, "data/comments", "commentGeneration.png" )
	, ACTIONS_GENERATION_VIEWS( 2, "Photo actions: previews", JobListTab.PHOTO_ACTIONS, "data/previews", "viewGeneration.png" )
	, RANK_VOTING_GENERATION( 6, "Voting for users ranks in categories generation", JobListTab.OTHER, "data/rankVoting", "rankVotingGeneration.png" )
	, FAVORITES_GENERATION( 8, "Favorite entries generation", JobListTab.OTHER, "data/favorites", "favoritesGeneration.png" )
	, ACTIVITY_STREAM_CLEAN_UP( 15, "Activity stream clean-up", JobListTab.CLEAN_UP, "activityStreamCleanup", "activityStreamCleanup.png" )
	, JOB_EXECUTION_HISTORY_CLEAN_UP( 16, "Job execution history clean-up", JobListTab.CLEAN_UP, "jobExecutionHistoryCleanup", "jobExecutionHistoryCleanup.png" )
	, JOB_CHAIN( 9, "Chain job", JobListTab.CHAINS, "chain", "chain.png" )
	;

	private final int id;
	private final String name;
	private final JobListTab jobListTab;
	private final String prefix;
	private final String icon;

	private SavedJobType( final int id, final String name, final JobListTab jobListTab, final String prefix, final String icon ) {
		this.id = id;
		this.name = name;
		this.jobListTab = jobListTab;
		this.prefix = prefix;
		this.icon = icon;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getNameTranslated() {
		return TranslatorUtils.translate( name );
	}

	public JobListTab getJobListTab() {
		return jobListTab;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getIcon() {
		return icon;
	}

	public static SavedJobType getById( final int id ) {
		for ( final SavedJobType savedJobType : SavedJobType.values() ) {
			if ( savedJobType.getId() == id ) {
				return savedJobType;
			}
		}
		return null;
	}
}
