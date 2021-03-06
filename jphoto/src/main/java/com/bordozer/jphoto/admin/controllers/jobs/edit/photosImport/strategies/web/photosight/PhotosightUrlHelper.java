package com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.photosight;

import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.AbstractRemotePhotoSitePageContentDataExtractor;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.AbstractRemotePhotoSiteUrlHelper;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;
import com.bordozer.jphoto.core.log.LogHelper;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;

public class PhotosightUrlHelper extends AbstractRemotePhotoSiteUrlHelper {

    private final AbstractRemotePhotoSitePageContentDataExtractor photosightContentDataExtractor = new PhotosightContentDataExtractor();

    public PhotosightUrlHelper() {
        super(new LogHelper());
    }

    @Override
    public PhotosImportSource getPhotosImportSource() {
        return PhotosImportSource.PHOTOSIGHT;
    }

    @Override
    public String getUserCardUrl(final String remotePhotoSiteUserId, final int pageNumber) {
        return String.format("http://www.%s/users/%s/?pager=%d", getRemotePhotoSiteHost(), remotePhotoSiteUserId, pageNumber);
    }

    @Override
    public String getPhotoCardUrl(final String remotePhotoSiteUserId, final int remotePhotoSitePhotoId) {
        return String.format("http://www.%s/%s/%d/", getRemotePhotoSiteHost(), "photos", remotePhotoSitePhotoId);
    }

    @Override
    public String getPhotoCategoryUrl(final RemotePhotoSiteCategory remotePhotoSiteCategory) {
        return String.format("http://www.%s/photos/category/%d/", getRemotePhotoSiteHost(), remotePhotoSiteCategory.getId());
    }

    @Override
    protected void addNecessaryCookies(final DefaultHttpClient httpClient, final String remotePhotoSiteUserId) {
        final CookieStore cookieStore = new BasicCookieStore();

        final BasicClientCookie cookieIsDisabledNude = getRemoteUserCardCookie(String.format("is_disabled_nude_profile_%s", remotePhotoSiteUserId), "1");
        cookieStore.addCookie(cookieIsDisabledNude);

        final BasicClientCookie cookieShowNude = getRemoteUserCardCookie("show_nude", "1");
        cookieStore.addCookie(cookieShowNude);

        httpClient.setCookieStore(cookieStore);
    }

    @Override
    protected AbstractRemotePhotoSitePageContentDataExtractor getRemotePhotoSiteContentDataExtractor() {
        return photosightContentDataExtractor;
    }

}
