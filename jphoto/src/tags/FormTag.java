package tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

public class FormTag extends BodyTagSupport {

	public final static String FORM_NAME = "FormName";

	private String action = null;
	private String formName = null;
	private boolean multipartForm = false;

	@Override
	public int doStartTag() throws JspException {
		JspWriter out = pageContext.getOut();

		try {
			// enctype="multipart/form-data" accept-charset="UTF-8"
			out.println( String.format( "<form name=\"%1$s\" id=\"%1$s\" method=\"POST\" action=\"%2$s\" %3$s>"
					, ( formName == null ? FORM_NAME : formName ), action, ( multipartForm ? "enctype=\"multipart/form-data\"" : "" ) ) );
		} catch ( IOException e ) {
			e.printStackTrace();
		}

		return BodyTag.EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		JspWriter out = pageContext.getOut();

		try {
			out.println( "</form>" );
		} catch ( IOException e ) {
			e.printStackTrace();
		}

		return BodyTag.EVAL_PAGE;
	}

	public void setAction( String action ) {
		this.action = action;
	}

	public void setFormName( final String formName ) {
		this.formName = formName;
	}

	public void setMultipartForm( boolean multipartForm ) {
		this.multipartForm = multipartForm;
	}
}