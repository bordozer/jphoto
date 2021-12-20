package com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.photos35;

import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.AbstractRemotePhotoSitePageContentDataExtractor;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.AbstractRemotePhotoSiteUrlHelper;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;
import com.bordozer.jphoto.core.log.LogHelper;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;

public class Photo35UrlHelper extends AbstractRemotePhotoSiteUrlHelper {

    private final AbstractRemotePhotoSitePageContentDataExtractor photosightContentDataExtractor = new Photo35ContentDataExtractor();

    public Photo35UrlHelper() {
        super(new LogHelper());
    }

    @Override
    public PhotosImportSource getPhotosImportSource() {
        return PhotosImportSource.PHOTO35;
    }

    @Override
    public String getUserCardUrl(final String remotePhotoSiteUserId, final int pageNumber) {
        return String.format("http://www.%s.%s/", remotePhotoSiteUserId, getRemotePhotoSiteHost());
    }

    @Override
    public String getPhotoCardUrl(final String remotePhotoSiteUserId, final int remotePhotoSitePhotoId) {
        return String.format("http://%s.%s/photo_%d/", remotePhotoSiteUserId, getRemotePhotoSiteHost(), remotePhotoSitePhotoId);
    }

    @Override
    public String getPhotoCategoryUrl(final RemotePhotoSiteCategory remotePhotoSiteCategory) {
        return String.format("http://www.%s/rating/photo_day/cat%d/", getRemotePhotoSiteHost(), remotePhotoSiteCategory.getId());
    }

    @Override
    protected AbstractRemotePhotoSitePageContentDataExtractor getRemotePhotoSiteContentDataExtractor() {
        return photosightContentDataExtractor;
    }

    @Override
    protected void addNecessaryCookies(final DefaultHttpClient httpClient, final String remotePhotoSiteUserId) {
        final CookieStore cookieStore = new BasicCookieStore();

        cookieStore.addCookie(getRemoteUserCardCookie("nude", "true"));
        cookieStore.addCookie(getRemoteUserCardCookie("user_lang", "en"));

        httpClient.setCookieStore(cookieStore);
    }
}

/*
<script type="text/javascript">
  var showNextBlock = "photoLibBlock";
  showNextList("photoUser");
  var nextListMoreOp = "&user_id=80842&show=r2";
  var showNextListId=702437;
</script>

http://35photo.ru/show_block.php?type=getNextPageData&page=photoUser&lastId=702437&user_id=80842&show=r2
where 702437 - last shown photoId
80842 - userId

* */
