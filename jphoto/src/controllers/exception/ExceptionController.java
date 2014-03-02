package controllers.exception;

import core.exceptions.BaseRuntimeException;
import core.exceptions.ExceptionModel;
import core.exceptions.notFound.NotFoundExceptionEntryType;
import core.services.utils.UrlUtilsServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping( "/error" )
public class ExceptionController {

	private static final String MODEL_NAME = "exceptionModel";

	@ModelAttribute( MODEL_NAME )
	public ExceptionModel prepareModel( final HttpServletRequest request ) {
		final String url = ( String ) request.getAttribute( "javax.servlet.error.request_uri" );
		final Object exception = request.getAttribute( "javax.servlet.error.exception" );

		final ExceptionModel model = new ExceptionModel();

		if ( exception instanceof BaseRuntimeException ) {
			final String exceptionMessage = ( ( BaseRuntimeException ) exception ).getMessage();
			model.setExceptionMessage( exceptionMessage );
		}

		model.setExceptionUrl( url );
		model.setRefererUrl( request.getHeader( "referer" ) );

		return model;
	}

	@RequestMapping( "/general/" )
	public String exceptionHandler( final HttpServletResponse response ) {
		final int errorCode = response.getStatus();

		String errorJsp;
		switch ( errorCode ) {
			case 404:
				errorJsp = "Error404";
				break;
			default:
				errorJsp = "GeneralException";
				break;
		}

		return String.format( "errors/%s", errorJsp );
	}

	@RequestMapping( "/notFound/user/" )
	public String userNotFoundHandler( final @ModelAttribute( MODEL_NAME ) ExceptionModel model ) {
		model.setNotFoundExceptionEntryType( NotFoundExceptionEntryType.USER );
		return UrlUtilsServiceImpl.ENTRY_NOT_FOUND_VIEW;
	}

	@RequestMapping( "/notFound/photo/" )
	public String photoNotFoundHandler( final @ModelAttribute( MODEL_NAME ) ExceptionModel model ) {
		model.setNotFoundExceptionEntryType( NotFoundExceptionEntryType.PHOTO );
		return UrlUtilsServiceImpl.ENTRY_NOT_FOUND_VIEW;
	}

	@RequestMapping( "/notFound/genre/" )
	public String genreNotFoundHandler( final @ModelAttribute( MODEL_NAME ) ExceptionModel model ) {
		model.setNotFoundExceptionEntryType( NotFoundExceptionEntryType.GENRE );
		return UrlUtilsServiceImpl.ENTRY_NOT_FOUND_VIEW;
	}

	@RequestMapping( "/notLoggedUser/" )
	public String notLoggedUserHandler() {
		return UrlUtilsServiceImpl.NOT_LOGGED_USER_VIEW;
	}

	@RequestMapping( "/accessDenied/" )
	public String accessDeniedHandler() {
		return UrlUtilsServiceImpl.ACCESS_DENIED_VIEW;
	}

	@RequestMapping( "/nudeContentWarning/" )
	public String nudeContentHandler() {
		return UrlUtilsServiceImpl.NUDE_CONTENT_WARNING_VIEW;
	}

	@RequestMapping( "/userRequestSecurityException/" )
	public String userRequestSecurityExceptionHandler( final HttpServletRequest request ) {
		return String.format( "redirect:%s", request.getHeader( "referer" ) ); //UrlUtilsServiceImpl.ACCESS_DENIED_VIEW;
	}
}

/*
1	javax.servlet.error.status_code
This attribute give status code which can be stored and analysed after storing in a java.lang.Integer data type.
2	javax.servlet.error.exception_type
This attribute gives information about exception type which can be stored and analysed after storing in a java.lang.Class data type.
3	javax.servlet.error.message
This attribute gives information exact error message which can be stored and analysed after storing in a java.lang.String data type.
4	javax.servlet.error.request_uri
This attribute gives information about URL calling the servlet and it can be stored and analysed after storing in a java.lang.String data type.
5	javax.servlet.error.exception
This attribute gives information the exception raised which can be stored and analysed after storing in a java.lang.Throwable data type.
6	javax.servlet.error.servlet_name
This attribute gives servlet name which can be stored and analysed after storing in a java.lang.String data type.
* */
