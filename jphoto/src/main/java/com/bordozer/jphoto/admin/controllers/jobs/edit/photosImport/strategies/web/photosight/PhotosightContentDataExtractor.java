package com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.photosight;

import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.AbstractRemotePhotoSitePageContentDataExtractor;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteImage;
import com.bordozer.jphoto.core.exceptions.BaseRuntimeException;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.utils.NumberUtils;
import com.bordozer.jphoto.utils.StringUtilities;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.collect.Lists.newArrayList;

public class PhotosightContentDataExtractor extends AbstractRemotePhotoSitePageContentDataExtractor {

    @Override
    public List<RemotePhotoSiteImage> extractImageUrl(final String remotePhotoSiteUserId, final int remotePhotoSitePhotoId, final String photoPageContent) {

        final RemotePhotoSiteImage remotePhotoSiteImage = extractImageUrlByNewRules(remotePhotoSitePhotoId, photoPageContent);
        if (remotePhotoSiteImage != null) {
            return newArrayList(remotePhotoSiteImage);
        }

        return newArrayList(extractImageUrlByOldRules(remotePhotoSitePhotoId, photoPageContent));
    }

    @Override
    public String getPhotoIdRegex(final String remotePhotoSiteUserId) {
        // <a href="/photos/5575117/?from_member" class="preview230"><img src="http://img-6.photosight.ru/3cd/5575117_thumb.jpg" alt="????, ?? ????, ???? ??????? ???? ?? ???? ..."/></a>
        return "<a href=\"/photos/(.+?)/\\?from_member\" class=\"preview230\">";
    }

    @Override
    public String extractRemotePhotoSiteUserName(final String userPageContent) {
        final Pattern pattern = Pattern.compile("<div class=\"usertitle\">(.+?)<h1>(.+?)</h1>");
        final Matcher matcher = pattern.matcher(userPageContent);

        if (matcher.find()) {
            final String photosightUserName = matcher.group(2);
            return photosightUserName.trim();
        }

        return null;
    }

    @Override
    public int extractRemotePhotoSiteUserPhotosCount(final String remotePhotoSiteUserId) {
        final String userPageContent = new PhotosightUrlHelper().getUserPageContent(1, remotePhotoSiteUserId);
        // <a href="/users/344981/" class="uploaded current"><div>196</div>
        final Pattern pattern = Pattern.compile(String.format("<a href=\"/users/%s/\" class=\"uploaded current\">\\s+<div>(.+?)</div>", remotePhotoSiteUserId));
        final Matcher matcher = pattern.matcher(userPageContent);

        if (matcher.find()) {
            final String photosightUserName = matcher.group(1);
            return NumberUtils.convertToInt(photosightUserName.trim());
        }

        return 0;
    }

    @Override
    public int getRemoteUserPagesCount(final String userCardContent, final String remotePhotoSiteUserId) {
        // <a class="" href="/users/316896/?pager=8">8</a>
        final Pattern pattern = Pattern.compile(String.format("<a class=\"(.*?)\" href=\"/users/%s/\\?pager=(.+?)\">", remotePhotoSiteUserId));
        final Matcher matcher = pattern.matcher(userCardContent);

        int result = 1;
        while (matcher.find()) {
            result = NumberUtils.convertToInt(matcher.group(2));
        }

        return result;
    }

    @Override
    public int extractPhotoCategoryId(final String photoPageContent) {
        final Pattern pattern = Pattern.compile("href=\"/photos/category/(.+?)/\" id=\"currentcat\"");
        final Matcher matcher = pattern.matcher(photoPageContent);

        if (matcher.find()) {
            final String _categoryId = matcher.group(1);
            return NumberUtils.convertToInt(_categoryId);
        }

        throw new BaseRuntimeException(String.format("%s: can not find photo category from the page context", getHost()));
    }

    @Override
    public String extractPhotoName(final String photoPageContent) {

        final Pattern pattern = Pattern.compile("<div class=\"photoinfobox\">\\s+<h1>(.+?)</h1>");
        final Matcher matcher = pattern.matcher(photoPageContent);

        if (matcher.find()) {
            final String name = matcher.group(1);

            return StringUtilities.truncateString(name, 255);
        }

        return NO_PHOTO_NAME;
    }

    @Override
    public List<String> extractComments(final String photoPageContent) {
        final List<String> result = newArrayList();

        final Pattern pattern = Pattern.compile("<div class=\"commenttext\">(.+?)</div>");
        final Matcher matcher = pattern.matcher(photoPageContent);
        while (matcher.find()) {
            result.add(matcher.group(1));
        }

        return result;
    }

    private RemotePhotoSiteImage extractImageUrlByNewRules(final int remotePhotoSitePhotoId, final String photoPageContent) {
        // <img src="http://icon.s.photosight.ru/img/8/f4f/3725335_large.jpeg"
        final Pattern pattern = Pattern.compile(String.format("<img src=\"http://(.+?).%s/(.+?)/%d_large.jp(e*?)g\"", getHost(), remotePhotoSitePhotoId));
        final Matcher matcher = pattern.matcher(photoPageContent);

        if (matcher.find()) {
            final String photoImageServerUrl = matcher.group(1);
            final String someShit = matcher.group(2);
            final String extension = matcher.group(3);
            return new RemotePhotoSiteImage(String.format("%s.%s/%s/%d_large.jp%sg", photoImageServerUrl, getHost(), someShit, remotePhotoSitePhotoId, extension));
        }

        return null;
    }

    private RemotePhotoSiteImage extractImageUrlByOldRules(final int remotePhotoSitePhotoId, final String photoPageContent) {
        //<img src="http://img-2007-09.photosight.ru/24/2318529.jpg" alt="
        final Pattern pattern = Pattern.compile(String.format("<img src=\"http://(.+?).%s/(.+?)/%d.jp(e*?)g\"", getHost(), remotePhotoSitePhotoId));
        final Matcher matcher = pattern.matcher(photoPageContent);

        if (matcher.find()) {
            final String photoImageServerUrl = matcher.group(1);
            final String someShit = matcher.group(2);
            final String extension = matcher.group(3);
            return new RemotePhotoSiteImage(String.format("%s.%s/%s/%d.jp%sg", photoImageServerUrl, getHost(), someShit, remotePhotoSitePhotoId, extension));
        }

        return null;
    }

    @Override
    protected String getHost() {
        return PhotosImportSource.PHOTOSIGHT.getUrl();
    }

    @Override
    public Date extractPhotoUploadTime(final String photoPageContent, final Services services) {
        final Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}\\.\\d{4}\\s\\d{2}\\:\\d{2}");
        final Matcher matcher = pattern.matcher(photoPageContent);

        if (matcher.find()) {
            final String uploadDateTime = matcher.group();

            if (StringUtils.isEmpty(uploadDateTime)) {
                return null;
            }

            final String[] dateAndTime = uploadDateTime.split("\\s");

            if (dateAndTime.length < 2) {
                return null;
            }

            return services.getDateUtilsService().parseDateTime(dateAndTime[0], dateAndTime[1], "dd.MM.yyyy HH:mm");
        }

        return extractUploadTodayTime(photoPageContent, services);
    }

    private Date extractUploadTodayTime(final String photoPageContent, final Services services) {
        final Pattern pattern = Pattern.compile("\\s\\d{2}\\:\\d{2}(\\s*?)</b>");
        final Matcher matcher = pattern.matcher(photoPageContent);

        if (matcher.find()) {
            final String uploadDateTime = matcher.group();

            if (StringUtils.isEmpty(uploadDateTime)) {
                return null;
            }

            final String time = uploadDateTime.substring(0, uploadDateTime.length() - 4).trim();

            final DateUtilsService dateUtilsService = services.getDateUtilsService();
            return dateUtilsService.parseDateTime(dateUtilsService.formatDate(dateUtilsService.getCurrentDate()), String.format("%s:00", time));
        }

        return null;
    }
}
