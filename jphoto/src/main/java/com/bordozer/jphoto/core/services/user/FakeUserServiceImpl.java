package com.bordozer.jphoto.core.services.user;

import com.bordozer.jphoto.core.general.user.EmailNotificationType;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserMembershipType;
import com.bordozer.jphoto.core.general.user.UserStatus;
import com.bordozer.jphoto.core.general.user.userTeam.UserTeam;
import com.bordozer.jphoto.core.general.user.userTeam.UserTeamMember;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.core.services.utils.RandomUtilsService;
import com.bordozer.jphoto.utils.fakeUser.AbstractFakeMember;
import com.bordozer.jphoto.utils.fakeUser.FakeMakeUpMaster;
import com.bordozer.jphoto.utils.fakeUser.FakeModel;
import com.bordozer.jphoto.utils.fakeUser.FakePhotographer;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

@Service("fakeUserService")
public class FakeUserServiceImpl implements FakeUserService {

    private final static int userMemberShipArray[] = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3};
    private final static int teamMembersQtyArray[] = {0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 4, 5};

    @Autowired
    private UserService userService;

    @Autowired
    private RandomUtilsService randomUtilsService;

    @Autowired
    private DateUtilsService dateUtilsService;

    @Override
    public User getRandomUser() {
        return initUserFromFakeMember(getRandomFakeMember());
    }

    @Override
    public User getRandomUser(final AbstractFakeMember fakeMember) {
        return initUserFromFakeMember(fakeMember);
    }

    @Override
    public AbstractFakeMember getRandomFakeMember() {
        final int randomMembershipTypeId = userMemberShipArray[randomUtilsService.getRandomInt(0, userMemberShipArray.length - 1)];

        return getRandomFakeMember(UserMembershipType.getById(randomMembershipTypeId));
    }

    public AbstractFakeMember getRandomFakeMember(final UserMembershipType membershipType) {
        AbstractFakeMember fakeMember = null;

        switch (membershipType) {
            case AUTHOR:
                fakeMember = new FakePhotographer();
                break;
            case MODEL:
                fakeMember = new FakeModel();
                break;
            case MAKEUP_MASTER:
                fakeMember = new FakeMakeUpMaster();
                break;
            default:
                throw new IllegalArgumentException(String.format("Incorrect Membership Type Id: %s", membershipType));
        }

        fakeMember.setRandomUtilsService(randomUtilsService);

        return fakeMember;
    }

    @Override
    public UserTeam getRandomUserTeam(final User user) {
        return new UserTeam(user, getRandomUserTeamMembers(user, getRandomFakeMember()));
    }

    @Override
    public UserTeam getRandomUserTeam(final User user, final AbstractFakeMember fakeMember) {
        return new UserTeam(user, getRandomUserTeamMembers(user, fakeMember));
    }

    private List<UserTeamMember> getRandomUserTeamMembers(final User user, final AbstractFakeMember fakeMember) {

        fakeMember.setRandomUtilsService(randomUtilsService);
        fakeMember.setFakeUserService(this);

        final int teamMembersQty = randomUtilsService.getRandomIntegerArrayElement(teamMembersQtyArray);

        final List<UserTeamMember> userTeamMembers = newArrayList();

        final List<User> users = userService.loadAll();

        if (users.size() == 0) {
            return Collections.emptyList();
        }

        for (int i = 0; i < teamMembersQty; i++) {
            final UserTeamMember userTeamMember = fakeMember.getRandomUserTeamMember(user, users);

            if (userTeamMember == null) {
                continue;
            }

            userTeamMembers.add(userTeamMember);
        }

        return userTeamMembers;
    }

    private User initUserFromFakeMember(final AbstractFakeMember fakeMember) {
        final int randomInt = randomUtilsService.getRandomInt(14, 99999);

        final User user = new User();

        final String userName = fakeMember.getName();
        user.setLogin(userName);
        user.setName(userName);
        user.setGender(fakeMember.getGender());

        user.setMembershipType(fakeMember.getMembershipType());

        user.setEmail(getRandomEmail());

        user.setPhotosOnPage(-1);

        final int age = randomUtilsService.getRandomInt(14, 75);
        final Date yearsOffsetFromCurrentDate = dateUtilsService.getYearsOffsetFromCurrentDate(-age);
        final Date monthsOffsetFromCurrentDate = dateUtilsService.getMonthsOffset(yearsOffsetFromCurrentDate, -age);
        final Date daysOffsetFromCurrentDate = dateUtilsService.getDatesOffset(monthsOffsetFromCurrentDate, -age);
        user.setDateOfBirth(daysOffsetFromCurrentDate);

        user.setHomeSite(String.format("http://www.user%s.someserver.com", randomInt));

        user.setUserStatus(UserStatus.CANDIDATE);
        user.setRegistrationTime(dateUtilsService.getCurrentTime());

        final List<EmailNotificationType> emailNotificationTypes = Arrays.asList(EmailNotificationType.values());
        final int emailOptionsRandomQty = randomUtilsService.getRandomInt(0, EmailNotificationType.values().length - 1);
        final List<EmailNotificationType> randomEmailNotificationTypes = randomUtilsService.getRandomNUniqueListElements(emailNotificationTypes, emailOptionsRandomQty);
        user.setEmailNotificationTypes(newHashSet(randomEmailNotificationTypes));

        user.setDefaultPhotoCommentsAllowance(randomUtilsService.getRandomPhotoAllowance());
        user.setDefaultPhotoVotingAllowance(randomUtilsService.getRandomPhotoAllowance());

        user.setLanguage(getRandomLanguage());

        return user;
    }

    private Language getRandomLanguage() {
        final Set<Language> activeLanguages = newHashSet(Language.RU, Language.UA); // TODO: read from property

        CollectionUtils.filter(activeLanguages, language -> language != Language.NERD);

        return randomUtilsService.getRandomGenericSetElement(activeLanguages);
    }

    private String getRandomEmail() {
        String randomEmail;
        boolean isUserFound;
        int i = 0;
        do {
            randomEmail = String.format("%s%s@jphoto.ua", "email", randomUtilsService.getRandomInt(14, 99999));
            isUserFound = userService.loadByEmail(randomEmail) != null;

            if (i > 100) {
                return "CAN_NOT_CREATE_EMAIL";
            }
            i++;
        } while (isUserFound);
        return randomEmail;
    }
}
