package com.bordozer.jphoto.admin.jobs.entries;

import com.bordozer.jphoto.admin.controllers.jobs.edit.action.PhotoStrategyType;
import com.bordozer.jphoto.admin.jobs.JobRuntimeEnvironment;
import com.bordozer.jphoto.admin.jobs.enums.SavedJobType;
import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.PhotoVotingCategory;
import com.bordozer.jphoto.core.general.photo.ValidationResult;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserPhotoVote;
import com.bordozer.jphoto.core.log.LogHelper;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.message.TranslatableMessage;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.core.services.utils.EntityLinkUtilsService;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class PhotoActionGenerationVotingJob extends AbstractPhotoActionGenerationJob {

    public PhotoActionGenerationVotingJob(final JobRuntimeEnvironment jobEnvironment) {
        super(SavedJobType.ACTIONS_GENERATION, new LogHelper(), jobEnvironment);
    }

    @Override
    public boolean doPhotoAction(final Photo photo, final User user) {
		/*if ( ! services.getPhotoService().exists( photo.getId() ) ) {
				return false;
			}*/

        final Date actionTime = getPhotoActionTime(photo.getUploadTime());

        if (services.getPhotoVotingService().isUserVotedForPhoto(user, photo)) {
            return false;
        }

        final ValidationResult validationResult = services.getSecurityService().validateUserCanVoteForPhoto(user, photo, actionTime, Language.RU); // TODO: read language from properties
        if (validationResult.isValidationFailed()) {
            //				getLog().debug( String.format( "Voting fot a photo: %s", validation.getValidationMessage() ) );
            return false;
        }

        final List<PhotoVotingCategory> votingCategoriesForPhotoGenre = services.getVotingCategoryService().getGenreVotingCategories(photo.getGenreId()).getVotingCategories();
        final List<UserPhotoVote> userVotes = newArrayList();

        final int maxAccessibleMark = services.getUserRankService().getUserHighestPositiveMarkInGenre(user.getId(), photo.getGenreId());
        final int minAccessibleMark = services.getUserRankService().getUserLowestNegativeMarkInGenre(user.getId(), photo.getGenreId());

        final PhotoLikeStrategyFactory factory = new PhotoLikeStrategyFactory();
        final AbstractPhotoLikeStrategy strategy = factory.getStrategy();

        final int categoriesCount = services.getConfigurationService().getInt(ConfigurationKey.PHOTO_VOTING_APPRAISAL_CATEGORIES_COUNT);
        final List<PhotoVotingCategory> categories = services.getRandomUtilsService().getRandomNUniqueListElements(votingCategoriesForPhotoGenre, categoriesCount);
        for (final PhotoVotingCategory category : categories) {

            int mark = strategy.getRandomMark();

            if (mark > maxAccessibleMark) {
                mark = maxAccessibleMark;
            }
            if (mark < minAccessibleMark) {
                mark = minAccessibleMark;
            }

            if (mark == 0) {
                continue;
            }

            final UserPhotoVote userVote = new UserPhotoVote(user, photo, category);
            userVote.setMark(mark);
            userVote.setMaxAccessibleMark(maxAccessibleMark);
            userVote.setVotingTime(actionTime);
            userVotes.add(userVote);
        }

        savePhotoPreview(photo, user, actionTime);

        services.getPhotoVotingService().saveUserPhotoVoting(user, photo, actionTime, userVotes);

        final DateUtilsService dateUtilsService = services.getDateUtilsService();
        final EntityLinkUtilsService entityLinkUtilsService = services.getEntityLinkUtilsService();
        final User photoAuthor = services.getUserService().load(photo.getUserId());

        final TranslatableMessage translatableMessage = new TranslatableMessage("User $1 has appraised photo $2 of $3 ( time: $4 )", services)
                .userCardLink(user)
                .addPhotoCardLinkParameter(photo)
                .userCardLink(photoAuthor);
        addJobRuntimeLogMessage(translatableMessage);

        getLog().info(String.format("User %s has appraised photo %s ( time: %s )", user, photo, dateUtilsService.formatDateTime(actionTime)));

        return true;
    }

    private abstract class AbstractPhotoLikeStrategy {

        protected abstract int[] getMarksArray();

        protected abstract PhotoStrategyType getType();

        private int getRandomMark() {
            return services.getRandomUtilsService().getRandomIntegerArrayElement(getMarksArray());
        }
    }

    private class PhotoLikeStrategyFactory {

        private PhotoStrategyType[] strategyTypes = {
                PhotoStrategyType.COULD_BE_WORST, PhotoStrategyType.COULD_BE_BETTER, PhotoStrategyType.NORMAL, PhotoStrategyType.NORMAL, PhotoStrategyType.NORMAL,
                PhotoStrategyType.VERY_GOOD, PhotoStrategyType.VERY_GOOD, PhotoStrategyType.PERFECT
        };

        public AbstractPhotoLikeStrategy getStrategy() {
            final PhotoStrategyType randomStrategyType = services.getRandomUtilsService().getRandomGenericArrayElement(strategyTypes);

            switch (randomStrategyType) {
                case PERFECT:
                    return getPerfectPhotoLikeStrategy();
                case VERY_GOOD:
                    return getVeryGoodPhotoLikeStrategy();
                case NORMAL:
                    return getNormalPhotoLikeStrategy();
                case COULD_BE_BETTER:
                    return getCouldBeBetterPhotoLikeStrategy();
                case COULD_BE_WORST:
                    return getCouldBeWorstPhotoLikeStrategy();
                case UGLY_SHIT:
                    return uglyShitPhotoLikeStrategy();
            }

            throw new IllegalArgumentException(String.format("Illegal photo like strategy type: %s", randomStrategyType));
        }

        public AbstractPhotoLikeStrategy getPerfectPhotoLikeStrategy() {
            return new AbstractPhotoLikeStrategy() {
                @Override
                protected int[] getMarksArray() {
                    return new int[]{2, 3, 3, 3, 3};
                }

                @Override
                protected PhotoStrategyType getType() {
                    return PhotoStrategyType.PERFECT;
                }
            };
        }

        public AbstractPhotoLikeStrategy getVeryGoodPhotoLikeStrategy() {
            return new AbstractPhotoLikeStrategy() {
                @Override
                protected int[] getMarksArray() {
                    return new int[]{2, 2, 2, 3, 3, 3};
                }

                @Override
                protected PhotoStrategyType getType() {
                    return PhotoStrategyType.VERY_GOOD;
                }
            };
        }

        public AbstractPhotoLikeStrategy getNormalPhotoLikeStrategy() {
            return new AbstractPhotoLikeStrategy() {
                @Override
                protected int[] getMarksArray() {
                    return new int[]{0, 1, 1, 1, 1, 2, 2, 2, 2, 3};
                }

                @Override
                protected PhotoStrategyType getType() {
                    return PhotoStrategyType.NORMAL;
                }
            };
        }

        public AbstractPhotoLikeStrategy getCouldBeBetterPhotoLikeStrategy() {
            return new AbstractPhotoLikeStrategy() {
                @Override
                protected int[] getMarksArray() {
                    return new int[]{-1, -1, 0, 0, 1, 1, 1, 1, 2};
                }

                @Override
                protected PhotoStrategyType getType() {
                    return PhotoStrategyType.COULD_BE_BETTER;
                }
            };
        }

        public AbstractPhotoLikeStrategy getCouldBeWorstPhotoLikeStrategy() {
            return new AbstractPhotoLikeStrategy() {
                @Override
                protected int[] getMarksArray() {
                    return new int[]{-2, -2, -2, -1, 0, 0, 1, 1};
                }

                @Override
                protected PhotoStrategyType getType() {
                    return PhotoStrategyType.COULD_BE_WORST;
                }
            };
        }

        public AbstractPhotoLikeStrategy uglyShitPhotoLikeStrategy() {
            return new AbstractPhotoLikeStrategy() {
                @Override
                protected int[] getMarksArray() {
                    return new int[]{-3, -3, -3, -2, -2, -2};
                }

                @Override
                protected PhotoStrategyType getType() {
                    return PhotoStrategyType.UGLY_SHIT;
                }
            };
        }
    }
}
