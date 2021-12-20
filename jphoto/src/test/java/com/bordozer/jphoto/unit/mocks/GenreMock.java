package mocks;

import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.PhotoVotingCategory;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class GenreMock extends Genre {

    public GenreMock() {
        this(333);
    }

    public GenreMock(final int id) {
        setId(id);
        setName(String.format("Mock Photo Category #%d", id));
        setPhotoVotingCategories(getFakePhotoVotingCategories());
    }

    private List<PhotoVotingCategory> getFakePhotoVotingCategories() {

        final List<PhotoVotingCategory> photoVotingCategories = newArrayList();

        photoVotingCategories.add(new mocks.PhotoVotingCategoryMock(3331));
        photoVotingCategories.add(new mocks.PhotoVotingCategoryMock(3332));

        return photoVotingCategories;
    }
}
