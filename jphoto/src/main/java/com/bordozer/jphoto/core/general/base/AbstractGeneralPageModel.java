package com.bordozer.jphoto.core.general.base;

import com.bordozer.jphoto.ui.elements.PageModel;
import com.bordozer.jphoto.ui.elements.PageTitleData;

public class AbstractGeneralPageModel {

    private PageModel pageModel = new PageModel();

    public PageModel getPageModel() {
        return pageModel;
    }

    public void setPageModel(PageModel pageModel) {
        this.pageModel = pageModel;
    }

    public void setPageTitleData(PageTitleData pageTitleData) {
        pageModel.setPageTitleData(pageTitleData);
    }
}
