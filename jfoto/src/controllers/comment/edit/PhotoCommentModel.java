package controllers.comment.edit;

import core.general.base.AbstractGeneralModel;

import java.util.Date;

public class PhotoCommentModel extends AbstractGeneralModel {

	public final static String COMMENT_TEXT_FORM_CONTROL = "commentText";
	public final static String COMMENT_TEXTAREA_FORM_CONTROL = "commentTextArea";
	public final static String BEING_EDITED_COMMENT_ID_FORM_CONTROL = "photoCommentId";
	public final static String REPLY_TO_COMMENT_ID_FORM_CONTROL = "replyToCommentId";

	public static final String COMMENTS_END_ANCHOR = "comments_end";

	public static final String COMMENT_DIV_ID = "comment_div_";
	public static final String COMMENT_START_ANCHOR = "comment_start_";
	public static final String COMMENT_END_ANCHOR = "comment_end_";
	public static final String COMMENT_TEXT_DIV_ID = "comment_text_div_id_";

	public final static String PHOTO_COMMENT_FORM_ANCHOR = "commentForm";
	public final static String PHOTO_COMMENT_INFO_DIV = "photoCommentFormInfoDiv";

	public final static String COMMENT_EDIT_ICON_ID = "commentEditIcon";
	public final static String COMMENT_REPLY_ICON_ID = "commentReplyIcon";
	public final static String COMMENT_DELETE_ICON_ID = "commentDeleteIcon";

	public final static String SUBMIT_COMMENT_BUTTON_ID = "postCommentBultton";

	private PhotoCommentInfo photoCommentInfo;
	private long commentDelay;
	private Date userNextCommentTime;

	private int photoCommentId;
	private int photoId;
	private int replyToCommentId;
	private String commentText;

	public PhotoCommentInfo getPhotoCommentInfo() {
		return photoCommentInfo;
	}

	public void setPhotoCommentInfo( final PhotoCommentInfo photoCommentInfo ) {
		this.photoCommentInfo = photoCommentInfo;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText( final String commentText ) {
		this.commentText = commentText;
	}

	public int getPhotoCommentId() {
		return photoCommentId;
	}

	public void setPhotoCommentId( final int photoCommentId ) {
		this.photoCommentId = photoCommentId;
	}

	public int getReplyToCommentId() {
		return replyToCommentId;
	}

	public void setReplyToCommentId( int replyToCommentId ) {
		this.replyToCommentId = replyToCommentId;
	}

	public long getCommentDelay() {
		return commentDelay;
	}

	public void setCommentDelay( final long commentDelay ) {
		this.commentDelay = commentDelay;
	}

	public Date getUserNextCommentTime() {
		return userNextCommentTime;
	}

	public void setUserNextCommentTime( final Date userNextCommentTime ) {
		this.userNextCommentTime = userNextCommentTime;
	}

	public int getPhotoId() {
		return photoId;
	}

	public void setPhotoId( final int photoId ) {
		this.photoId = photoId;
	}
}
