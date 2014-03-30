package admin.jobs.entries;

import admin.jobs.JobRuntimeEnvironment;
import admin.jobs.enums.SavedJobType;
import core.enums.PhotoActionAllowance;
import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.general.user.UserStatus;
import core.log.LogHelper;
import core.services.translator.Language;
import core.services.translator.message.TranslatableMessage;
import core.services.utils.DateUtilsService;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class PhotoActionGenerationCommentsJob extends AbstractPhotoActionGenerationJob {

	private enum CommentType { COMMENT, ANSWER }

	public PhotoActionGenerationCommentsJob( final JobRuntimeEnvironment jobEnvironment ) {
		super( SavedJobType.ACTIONS_GENERATION_COMMENTS, new LogHelper( PhotoActionGenerationCommentsJob.class ), jobEnvironment );
	}

	@Override
	public boolean doPhotoAction( final Photo photo, final User user ) {
		final Date actionTime = getPhotoActionTime( photo.getUploadTime() );

		final Language language = getLanguage();
		if ( services.getSecurityService().validateUserCanCommentPhoto( user, photo, language ).isValidationFailed() ) {
			return false;
		}

		final PhotoActionAllowance commentsAllowance = photo.getCommentsAllowance();
		if ( commentsAllowance == PhotoActionAllowance.ACTIONS_DENIED ) {
			return false;
		}

		if ( commentsAllowance == PhotoActionAllowance.MEMBERS_ONLY && user.getUserStatus() == UserStatus.CANDIDATE ) {
			return false;
		}

		final PhotoComment comment = new PhotoComment();
		comment.setPhotoId( photo.getId() );
		comment.setCommentAuthor( user );
		comment.setCreationTime( actionTime );
		comment.setCommentText( services.getRandomUtilsService().getRandomStringArrayElement( comments ) );

		if ( isGoingToBeAnswer() ) {
			final int replyToCommentId = getParentComment( photo, user, actionTime );
			if ( replyToCommentId > 0 ) {
				comment.setReplyToCommentId( replyToCommentId );
			}
		}

		savePhotoPreview( photo, user, actionTime );

		services.getPhotoCommentService().save( comment );

		final DateUtilsService dateUtilsService = services.getDateUtilsService();

		final TranslatableMessage translatableMessage = new TranslatableMessage( "User $1 has left a comment for photo $2 ( time: $3 )", services )
			.addUserCardLinkParameter( user )
			.addPhotoCardLinkParameter( photo )
			.addFormattedDateTimeParameter( actionTime )
			;
		addJobRuntimeLogMessage( translatableMessage );

		getLog().info( String.format( "User %s has commented photo %s ( time: %s )", user, photo, dateUtilsService.formatDateTime( actionTime ) ) );

		return true;
	}

	private int getParentComment( final Photo photo, final User user, final Date beingCreatedCommentTime ) {
		final List<PhotoComment> comments = services.getPhotoCommentService().loadAll( photo.getId() );

		if ( comments.size() == 0 ) {
			return 0;
		}

		final List<PhotoComment> commentsLeftBeforeBeingCreatedComment = newArrayList();
		for ( final PhotoComment comment : comments ) {
			if ( !comment.getCommentAuthor().equals( user ) && comment.getCreationTime().getTime() < beingCreatedCommentTime.getTime() ) {
				commentsLeftBeforeBeingCreatedComment.add( comment );
			}
		}

		if ( commentsLeftBeforeBeingCreatedComment.size() == 0 ) {
			return 0;
		}

		final int randomParentCommentIndex = services.getRandomUtilsService().getRandomInt( 0, commentsLeftBeforeBeingCreatedComment.size() - 1 );

		return commentsLeftBeforeBeingCreatedComment.get( randomParentCommentIndex ).getId();
	}

	private boolean isGoingToBeAnswer() {
		return services.getRandomUtilsService().getRandomGenericArrayElement( commentTypesArray ) == CommentType.ANSWER;
	}

	final CommentType[] commentTypesArray = {CommentType.COMMENT, CommentType.COMMENT, CommentType.COMMENT, CommentType.ANSWER};
	final String[] comments = {
		"Nice shot", "Perfect!!!", "Very nice :)", "How do you do this?", "This is the best photo I have ever seen", "I don't like this", "Buls shit", "Go on!", "I like this",
		"Would you teach me?", "You are master!", "I don't understand this art", "Could be worst", "Heh, it looks like my ex-girlfriend", "Is it real?", "H.O.T.",
		"Do you give a masterclass?", "Imagine!", "Significantly good", "The idea is stolen from razooma.net", "Who is this model?", "This is your best shot, for sure",
		"Beautiful portrait!!!", "Very beautiful and excellent portrait!", "My dear BER?. Great composition, my best regards!", "Aqui est? ela, a cria??o do homem! poder!",
		"A lot of visual impact, quality work.congratulations :)", "Nice effect",
		"Child of the very Brasil well captured with a superb expression of inocence. She is strongly attached to the environmental problems and concerning their ancestral way of living and forest deflorestation!\nMy warmest regards, dear BER?!!",
		"Interesting work. You are Master!", "Very beautiful colors and details", "Unbelievable beauty!", "Grate fantasia",
		"It is not a macro, I think. So, I do not understand why this photo has been posted into macro photography compitition... However, thanks for sharing a very good photo.",
		"Lovely cat, Lovely photo...I like it...", "This is really a macro photography, I believe.....!!!!", "Anyway, I like.. Nice Photo!!",
		"What a beautiful kitty! I would however suggest either more of a crop to the right or leaving all of the right ear within the frame.",
		"Your portfolio is stunning. Your images are so dramatic. Each one is super and well thought out. I wish I can shoot like you. I have so much to learn.",
		"Beautiful shots! Your work is original and inspiring.", "I'm glad I stopped in... Very nice work. Stop and visit sometime.",
		"Colourful, creative, unique and sensual. What amazing work! Your talent shines through in every capture. Keep up the great work!",
		"Breathtaking, inspiring, sensual and evocative. Norm, your work is in a class of it's own. Look forward to seeing more of your creations!",
		"Norm, your portfolio is impressive! Regards...", "Excellent portfolio, Norm, it is beautiful work. Congratulations.",
		"Wonderful work you are displaying here. I really like your lighting.", "More or less cool. But more less then more... :)", "I love your photos!", "Speechless!!!",
		"Great body of work. Much to be admired for sure.",
		"You are great artist It's amazing the way you treat the photographs to create such an atmosphere in them. You have great eye to create art. Nice technique, great ideas behind them and a wonderful dept in images appears which create a class of your own.Congrats to you and to them for the wonderful body of work that you have presented here. I look forward to seeing more. Thanks to share it with us.",
		"I've noticed your photos and your name but this was the first time that I got to your portfolio. I should have known it is very good but really it was a revelation for me. Very good. The saturation versions only prove that there is no ultimate only way. Conclusively - very impressive portfolio.",
		"Your portfolio is an inspiration - Thank you.", "Your eye is unique. You can't purchase that in the local camera shop.", "My Mom said this is bull's shit.",
		"You are very talented.. Congratulations", "You have many interesting photos. It's hard to pick my favorite. Very creative and artistic. Well done.",
		"I really don't tend to think to record such information. Camera info is recorded, some of the uploaded shots have exif information some don't, Lighting is just what is needed, and then tweaked in post. Aperture thought of for what I'm seeing, etc. Understanding what camera or settings someone else has used to take a photo doesn't help often because you aren't in an identical situation - learn to see and think about the shot and you can adapt to situations yourself."
	};
}
