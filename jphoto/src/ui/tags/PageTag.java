package ui.tags;

import ui.elements.PageModel;
import ui.context.ApplicationContextHelper;
import ui.services.PageTemplateService;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

public class PageTag extends BodyTagSupport {

	private PageModel pageModel;
	private boolean errorPage = false;

	private final PageTemplateService pageTemplateService = ApplicationContextHelper.getBean( PageTemplateService.BEAN_NAME );

	@Override
	public int doStartTag() throws JspException {
		pageContext.getResponse().setCharacterEncoding( "UTF-8" );
		pageContext.getResponse().setContentType( "text/html" );

		try {
			pageContext.getOut().println( pageTemplateService.renderPageHeader( pageModel ) );
		} catch ( IOException e ) {
			throw new JspTagException( e.getMessage(), e );
		}
		return BodyTag.EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			doCloseBodyHtml( pageContext.getOut() );
		} catch ( IOException e ) {
			throw new JspTagException( e.getMessage(), e );
		}
		return BodyTag.EVAL_PAGE;
	}

	private void doCloseBodyHtml( JspWriter out ) throws IOException {
		out.println( pageTemplateService.renderPageFooter() );
	}

	public void setPageModel( PageModel pageModel ) {
		this.pageModel = pageModel;
	}

	public void setErrorPage( boolean errorPage ) {
		this.errorPage = errorPage;
	}
}
