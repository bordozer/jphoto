package com.bordozer.jphoto.utils;

import com.bordozer.jphoto.core.general.base.PagingModel;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class PagingUtils {

    public static final String PAGING_PARAMETER_NAME = "page";

    public static void initPagingModel(final PagingModel pagingModel, final HttpServletRequest request) {
        pagingModel.setRequestUrl(request.getRequestURL().toString());

        final int currentPage = getPageFromRequest(request);
        pagingModel.setCurrentPage(currentPage);
    }

    public static int getPageFromRequest(HttpServletRequest request) {
        int currentPage = 1;
        final String page = request.getParameter(PAGING_PARAMETER_NAME);
        if (StringUtils.isNotEmpty(page)) {
            currentPage = Integer.parseInt(page);
        }
        return currentPage;
    }

    public static int getPageItemStartIndex(int page, int onPage) {
        return onPage * (page - 1);
    }

    public static int getPageItemEndIndex(int page, int onPage) {
        return onPage * page - 1;
    }
}
