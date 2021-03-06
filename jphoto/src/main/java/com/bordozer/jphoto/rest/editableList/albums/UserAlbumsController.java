package com.bordozer.jphoto.rest.editableList.albums;

import com.bordozer.jphoto.core.general.user.userAlbums.UserPhotoAlbum;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.user.UserPhotoAlbumService;
import com.bordozer.jphoto.core.services.user.UserService;
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
@RequestMapping("/rest//users/{userId}/albums")
public class UserAlbumsController {

    @Autowired
    private UserPhotoAlbumService userPhotoAlbumService;

    @Autowired
    private UrlUtilsService urlUtilsService;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<UserAlbumDTO> userAlbums(final @PathVariable("userId") int userId) {
        return getUserAlbumDTOs(userId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    public UserAlbumDTO createUserAlbum(@RequestBody final UserAlbumDTO dto) {
        doSaveUserAlbum(dto);
        return dto;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{entryId}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    public UserAlbumDTO saveUserAlbum(@RequestBody final UserAlbumDTO dto) {
        doSaveUserAlbum(dto);
        return dto;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{entryId}")
    @ResponseBody
    public boolean deleteUserAlbum(final @PathVariable("entryId") int entryId) {
        return userPhotoAlbumService.delete(entryId);
    }

    private List<UserAlbumDTO> getUserAlbumDTOs(final int userId) {

        final List<UserAlbumDTO> result = newArrayList();

        final List<UserPhotoAlbum> albums = userPhotoAlbumService.loadAllForEntry(userId);

        for (final UserPhotoAlbum album : albums) {
            final UserAlbumDTO dto = new UserAlbumDTO();
            dto.setEntryId(album.getId());
            dto.setUserId(userId);
            dto.setEntryName(album.getName()); // TODO: escaping
            dto.setAlbumLink(urlUtilsService.getUserPhotoAlbumPhotosLink(userId, album.getId()));
            dto.setAlbumPhotosQty(getPhotosQty(album));

            result.add(dto);
        }

        return result;
    }

    private void doSaveUserAlbum(final UserAlbumDTO dto) {
        final UserPhotoAlbum album = new UserPhotoAlbum();
        album.setId(dto.getEntryId());
        album.setName(dto.getEntryName());
        album.setUser(userService.load(dto.getUserId()));
        album.setDescription(""); // TODO

        userPhotoAlbumService.save(album);

        dto.setEntryId(album.getId());
        dto.setAlbumPhotosQty(getPhotosQty(album));
    }

    private int getPhotosQty(final UserPhotoAlbum album) {
        return userPhotoAlbumService.getUserPhotoAlbumPhotosQty(album.getId());
    }

    private Language getLanguage() {
        return EnvironmentContext.getLanguage();
    }
}
