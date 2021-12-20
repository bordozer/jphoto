package com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.filesystem;

import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.ImageToImport;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.ImageToImportData;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.importParameters.AbstractImportParameters;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.importParameters.FileSystemImportParameters;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.AbstractPhotoImportStrategy;
import com.bordozer.jphoto.admin.jobs.entries.AbstractJob;
import com.bordozer.jphoto.admin.jobs.entries.resources.photos.FakePhotoNameGenerator;
import com.bordozer.jphoto.admin.jobs.general.JobDateRange;
import com.bordozer.jphoto.core.exceptions.BaseRuntimeException;
import com.bordozer.jphoto.core.exceptions.SaveToDBException;
import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.PhotoImageLocationType;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.log.LogHelper;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.utils.PredicateUtilsService;
import com.bordozer.jphoto.utils.PhotoUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

public class FilesystemImportStrategy extends AbstractPhotoImportStrategy {

    private final List<ImageToImportData> imageToImportDatas;

    protected FileSystemImportParameters importParameters;

    public FilesystemImportStrategy(final AbstractJob job, final AbstractImportParameters parameters, final Services services) {
        super(job, services, new LogHelper(), parameters.getLanguage());

        importParameters = (FileSystemImportParameters) parameters;

        imageToImportDatas = collectFileSystemImages(new File(importParameters.getPictureDir()));

        createNecessaryGenres();
    }

    @Override
    public void doImport() throws IOException, SaveToDBException {
        final Set<Genre> genres = getGenres();

        final UserGeneratorFactory factory = new UserGeneratorFactory();
        final AbstractUserGenerator userGenerator = factory.getInstance(importParameters.getAssignAllGeneratedPhotosToUserId(), genres, services, job.getBeingProcessedUsers());

        final Iterator<ImageToImportData> pictureIterator = imageToImportDatas.iterator();

        log.debug("Processing images");

        int counter = 1;
        final int total = job.getTotalJopOperations();
        while (pictureIterator.hasNext()) {
            final ImageToImportData imageToImportData = pictureIterator.next();

            final ImageToImport imageToImport = imageToImportData.getImageToImport();
            final Genre genre = getGenreByName(genres, imageToImport.getGenreName());

            final User user = userGenerator.getUser(genre);
            imageToImportData.setUser(user);

            imageToImportData.setName(getRandomPhotoName(genre));

            final JobDateRange jobDateRange = importParameters.getJobDateRange();
            imageToImportData.setUploadTime(services.getRandomUtilsService().getRandomDate(jobDateRange.getStartDate(), jobDateRange.getEndDate()));

            imageToImportData.setPhotoDescription(String.format("The photo is imported from disk: %s", imageToImport.getImageFile().getCanonicalPath()));
            imageToImportData.setPhotoKeywords("imported, from, disk");

            pictureIterator.remove();

            createPhotoDBEntry(imageToImportData, counter, total);
            counter++;

            if (importParameters.isDeletePictureAfterImport()) {
                FileUtils.deleteQuietly(imageToImportData.getImageToImport().getImageFile());
            }

            job.increment();

            if (job.hasJobFinishedWithAnyResult() || job.isFinished()) {
                break;
            }
        }
    }

    private String getRandomPhotoName(final Genre genre) {
        return FakePhotoNameGenerator.getFakePhotoName(genre, services.getRandomUtilsService());
    }

    @Override
    public int calculateTotalPagesToProcess() throws IOException {
        return imageToImportDatas.size();
    }

    private void createNecessaryGenres() {

        log.debug("Creating necessary genres");

        final File pictureRoot = new File(importParameters.getPictureDir());
        final File[] genreDirList = pictureRoot.listFiles(getPredicateUtilsService().getDirFilter());
        for (File genreDir : genreDirList) {
            loadGenre(genreDir.getName());
        }
    }

    private Set<Genre> getGenres() {

        final Set<Genre> result = newHashSet();

        final List<Genre> persistedGenres = services.getGenreService().loadAll();
        for (final Genre genre : persistedGenres) {
            result.add(genre);
        }

        return result;
    }

    private Genre getGenreByName(final Set<Genre> genres, final String genreName) {
        for (final Genre genre : genres) {
            if (genre.getName().equalsIgnoreCase(genreName)) {
                return genre;
            }
        }
        throw new BaseRuntimeException(String.format("Can not find genre by name: '%s'", genreName));
    }

    private List<ImageToImportData> collectFileSystemImages(final File pictureRoot) {

        log.debug("Collecting images on disk info");

        final List<ImageToImportData> importedPictures = newArrayList();

        final File[] genreDirList = pictureRoot.listFiles(getPredicateUtilsService().getDirFilter());
        if (genreDirList == null) {
            return newArrayList();
        }

        for (File genreDir : genreDirList) {

            final String genreName = genreDir.getName();

            final File[] files = genreDir.listFiles(getPredicateUtilsService().getFileFilter());
            if (files == null) {
                return newArrayList();
            }

            for (File file : files) {

                final List<String> allowedExtensions = services.getConfigurationService().getListString(ConfigurationKey.PHOTO_UPLOAD_FILE_ALLOWED_EXTENSIONS);
                if (PhotoUtils.isPhotoContentTypeSupported(allowedExtensions, services.getImageFileUtilsService().getContentType(file))) {

                    final ImageToImport imageToImport = new ImageToImport(PhotosImportSource.FILE_SYSTEM, PhotoImageLocationType.FILE, genreName, file);
                    final ImageToImportData imageToImportData = new ImageToImportData(imageToImport);

                    importedPictures.add(imageToImportData);
                }
            }
        }

        Collections.shuffle(importedPictures);

        return importedPictures;
    }

    private PredicateUtilsService getPredicateUtilsService() {
        return services.getPredicateUtilsService();
    }
}
