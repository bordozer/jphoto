package services;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.PhotoComment;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.security.SecurityServiceImpl;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.unit.common.AbstractTestCase;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class CommentAuthorVisibilityTest extends AbstractTestCase {

    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String MUST_BE_TRUE_BUT_FALSE = "Must be TRUE but FALSE";
    private static final String MUST_BE_FALSE_BUT_TRUE = "Must be FALSE but TRUE";

    private final User commentAuthor = new User(222);
    private final User photoAuthor = new User(444);
    private final User justUser = new User(555);

    private Photo photo;
    private PhotoComment comment;

    @Before
    public void setup() {
        super.setup();

        photo = new Photo();
        photo.setId(777);
        photo.setUserId(111);
        photo.setUserId(photoAuthor.getId());
        photo.setAnonymousPosting(true);
        photo.setUploadTime(dateUtilsService.parseTime("2014-01-12 12:00:00", FORMAT));

        comment = new PhotoComment();
        comment.setId(123);
        comment.setCommentAuthor(commentAuthor);
        comment.setPhotoId(photo.getId());
    }

    @Test
    public void commentOfAbstractUserWhenAnonymousPeriodIsNotPassedTest() {

        final Date viewTime = dateUtilsService.parseTime("2014-01-12 13:00:00", FORMAT);
        final Date anonymousPeriodExpirationTime = dateUtilsService.parseTime("2014-01-12 14:00:00", FORMAT);

        final SecurityServiceImpl securityService = getSecurityService(anonymousPeriodExpirationTime);

        assertFalse(String.format(MUST_BE_FALSE_BUT_TRUE), securityService.isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod(comment, commentAuthor, viewTime));
        assertFalse(String.format(MUST_BE_FALSE_BUT_TRUE), securityService.isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod(comment, photoAuthor, viewTime));
        assertFalse(String.format(MUST_BE_FALSE_BUT_TRUE), securityService.isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod(comment, SUPER_ADMIN_1, viewTime));
        assertFalse(String.format(MUST_BE_FALSE_BUT_TRUE), securityService.isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod(comment, justUser, viewTime));

        comment.setCommentAuthor(photoAuthor);
        assertTrue(String.format(MUST_BE_TRUE_BUT_FALSE), securityService.isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod(comment, justUser, viewTime));
    }

    @Test
    public void commentOfPhotoAuthorWhenAnonymousPeriodIsNotPassedTest() {

        final Date viewTime = dateUtilsService.parseTime("2014-01-12 13:00:00", FORMAT);
        final Date anonymousPeriodExpirationTime = dateUtilsService.parseTime("2014-01-12 14:00:00", FORMAT);
        comment.setCommentAuthor(photoAuthor);

        final SecurityServiceImpl securityService = getSecurityService(anonymousPeriodExpirationTime);

        assertTrue(String.format(MUST_BE_TRUE_BUT_FALSE), securityService.isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod(comment, commentAuthor, viewTime));
        assertFalse(String.format(MUST_BE_FALSE_BUT_TRUE), securityService.isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod(comment, photoAuthor, viewTime));
        assertFalse(String.format(MUST_BE_FALSE_BUT_TRUE), securityService.isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod(comment, SUPER_ADMIN_1, viewTime));
        assertTrue(String.format(MUST_BE_TRUE_BUT_FALSE), securityService.isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod(comment, justUser, viewTime));
    }

    @Test
    public void anonymousPeriodIsPassedTest() {

        final Date anonymousPeriodExpirationTime = dateUtilsService.parseTime("2014-01-12 15:00:00", FORMAT);
        final Date viewTime = dateUtilsService.parseTime("2014-01-12 14:00:00", FORMAT);

        final SecurityServiceImpl securityService = getSecurityService(anonymousPeriodExpirationTime);

        assertFalse(String.format(MUST_BE_FALSE_BUT_TRUE), securityService.isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod(comment, commentAuthor, viewTime));
        assertFalse(String.format(MUST_BE_FALSE_BUT_TRUE), securityService.isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod(comment, photoAuthor, viewTime));
        assertFalse(String.format(MUST_BE_FALSE_BUT_TRUE), securityService.isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod(comment, SUPER_ADMIN_1, viewTime));
        assertFalse(String.format(MUST_BE_FALSE_BUT_TRUE), securityService.isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod(comment, justUser, viewTime));
    }

    private SecurityServiceImpl getSecurityService(final Date photoAnonymousPeriodExpirationTime) {

        final UserService userService = EasyMock.createMock(UserService.class);
        EasyMock.expect(userService.load(SUPER_ADMIN_1.getId())).andReturn(SUPER_ADMIN_1).anyTimes();
        EasyMock.expect(userService.load(commentAuthor.getId())).andReturn(commentAuthor).anyTimes();
        EasyMock.expect(userService.load(photoAuthor.getId())).andReturn(photoAuthor).anyTimes();
        EasyMock.expect(userService.load(justUser.getId())).andReturn(justUser).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.replay(userService);


        final PhotoService photoService = EasyMock.createMock(PhotoService.class);
        EasyMock.expect(photoService.load(photo.getId())).andReturn(photo).anyTimes();
        EasyMock.expect(photoService.getPhotoAnonymousPeriodExpirationTime(photo)).andReturn(photoAnonymousPeriodExpirationTime).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.replay(photoService);

        final SecurityServiceImpl securityService = new SecurityServiceImpl();

        securityService.setUserService(userService);
        securityService.setPhotoService(photoService);

        return securityService;
    }
}
