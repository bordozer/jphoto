package com.bordozer.jphoto.ui.tags;

import com.bordozer.jphoto.ui.context.ApplicationContextHelper;
import com.bordozer.jphoto.ui.elements.PageModel;
import com.bordozer.jphoto.ui.services.page.PageTemplateService;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

public class PageTag extends BodyTagSupport {

    private PageModel pageModel;

    @Override
    public int doStartTag() throws JspException {
        pageContext.getResponse().setCharacterEncoding("UTF-8");
        pageContext.getResponse().setContentType("text/html");

        try {
            final PageTemplateService pageTemplateService = ApplicationContextHelper.getBean(PageTemplateService.BEAN_NAME);
            pageContext.getOut().println(pageTemplateService.renderPageHeader(pageModel));
        } catch (IOException e) {
            throw new JspTagException(e.getMessage(), e);
        }
        return BodyTag.EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            doCloseBodyHtml(pageContext.getOut());
        } catch (IOException e) {
            throw new JspTagException(e.getMessage(), e);
        }
        return BodyTag.EVAL_PAGE;
    }

    private void doCloseBodyHtml(JspWriter out) throws IOException {
        final PageTemplateService pageTemplateService = ApplicationContextHelper.getBean(PageTemplateService.BEAN_NAME);
        out.println(pageTemplateService.renderPageFooter());
    }

    public void setPageModel(PageModel pageModel) {
        this.pageModel = pageModel;
    }
}
