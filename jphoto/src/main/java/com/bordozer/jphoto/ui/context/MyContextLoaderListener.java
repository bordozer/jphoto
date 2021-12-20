package com.bordozer.jphoto.ui.context;

import org.springframework.web.context.ContextLoaderListener;

public class MyContextLoaderListener extends ContextLoaderListener {

    public MyContextLoaderListener() {

        System.out.println("====================================================================================================================================");
        System.out.println("                                                    STARTING SPRING CONTEXT                                                         ");
        System.out.println("====================================================================================================================================");
    }
}
