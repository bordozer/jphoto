package ui.context;

import org.springframework.web.context.ContextLoaderListener;

public class MyContextLoaderListener extends ContextLoaderListener {

	public MyContextLoaderListener() {

		System.out.println( "====================================================================================================================================" );
		System.out.println( "                                                       SPRING CONTEXT START                                                         " );
		System.out.println( "====================================================================================================================================" );
	}
}
