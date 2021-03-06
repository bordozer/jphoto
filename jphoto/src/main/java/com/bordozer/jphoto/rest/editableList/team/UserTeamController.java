package com.bordozer.jphoto.rest.editableList.team;

import com.bordozer.jphoto.core.enums.UserTeamMemberType;
import com.bordozer.jphoto.core.general.user.userTeam.UserTeam;
import com.bordozer.jphoto.core.general.user.userTeam.UserTeamMember;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.core.services.user.UserTeamService;
import com.bordozer.jphoto.core.services.utils.EntityLinkUtilsService;
import com.bordozer.jphoto.core.services.utils.UrlUtilsService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/rest//users/{userId}/team")
public class UserTeamController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserTeamService userTeamService;

    @Autowired
    private UrlUtilsService urlUtilsService;

    @Autowired
    private EntityLinkUtilsService entityLinkUtilsService;

    @Autowired
    private TranslatorService translatorService;

    @RequestMapping(method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<UserTeamMemberDTO> userTeam(final @PathVariable("userId") int userId) {
        return getUserTeamMemberDTOs(userId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    public UserTeamMemberDTO createUserTeamMember(@RequestBody final UserTeamMemberDTO userTeamMemberDTO) {
        doSaveUserTeamMember(userTeamMemberDTO);
        return userTeamMemberDTO;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{userTeamMemberId}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    public UserTeamMemberDTO saveUserTeamMember(@RequestBody final UserTeamMemberDTO userTeamMemberDTO) {
        doSaveUserTeamMember(userTeamMemberDTO);
        return userTeamMemberDTO;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{userTeamMemberId}")
    @ResponseBody
    public boolean deleteUserTeamMember(final @PathVariable("userTeamMemberId") int userTeamMemberId) {
        return userTeamService.delete(userTeamMemberId);
    }

    private void doSaveUserTeamMember(final UserTeamMemberDTO dto) {

        final UserTeamMember teamMember = new UserTeamMember();

        teamMember.setUser(userService.load(dto.getUserId()));
        teamMember.setId(dto.getEntryId());
        teamMember.setName(dto.getEntryName());

        final UserTeamMemberType teamMemberType = UserTeamMemberType.getById(dto.getTeamMemberTypeId());
        teamMember.setTeamMemberType(teamMemberType);

        userTeamService.save(teamMember);

        dto.setEntryId(teamMember.getId());
        dto.setTeamMemberTypeName(translatorService.translate(teamMemberType.getName(), getLanguage()));
        dto.setTeamMemberPhotosQty(userTeamService.getTeamMemberPhotosQty(teamMember.getId()));
    }

    private List<UserTeamMemberDTO> getUserTeamMemberDTOs(final int userId) {

        final int currentUserId = EnvironmentContext.getCurrentUserId();

        final UserTeam userTeam = userTeamService.loadUserTeam(userId);

        final List<UserTeamMemberDTO> result = newArrayList();

        for (final UserTeamMember userTeamMember : userTeam.getUserTeamMembers()) {
            final UserTeamMemberDTO dto = new UserTeamMemberDTO(userTeamMember.getId());

            final String teamMemberName = userTeamMember.getTeamMemberName();
            dto.setEntryName(teamMemberName);

            dto.setUserTeamMemberCardUrl(urlUtilsService.getUserTeamMemberCardLink(currentUserId, userTeamMember.getId()));

            final String memberTypeName = translatorService.translate(userTeamMember.getTeamMemberType().getName(), getLanguage());
            dto.setTeamMemberTypeName(memberTypeName);
            dto.setTeamMemberTypeId(userTeamMember.getTeamMemberType().getId());

            dto.setUserTeamMemberNameTitle(translatorService.translate("$1: $2", getLanguage(), teamMemberName, memberTypeName));
            dto.setTeamMemberPhotosQty(userTeamService.getTeamMemberPhotosQty(userTeamMember.getId()));

            if (userTeamMember.getTeamMemberUser() != null) {
                dto.setSiteMemberLink(entityLinkUtilsService.getUserCardLink(userTeamMember.getTeamMemberUser(), getLanguage()));
            } else {
                dto.setSiteMemberLink("");
            }

            result.add(dto);
        }

        return result;
    }

    private Language getLanguage() {
        return EnvironmentContext.getLanguage();
    }
}
