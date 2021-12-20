package com.bordozer.jphoto.ui.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AppErrorController implements ErrorController {

    private final static String ERROR_PATH = "/error";

    @RequestMapping(value = ERROR_PATH, produces = "text/html")
    public ModelAndView errorHtml(HttpServletRequest request) {
        return new ModelAndView("forward:/error/general/");
    }

    @RequestMapping(value = ERROR_PATH)
    @ResponseBody
    public ModelAndView error(HttpServletRequest request) {
        return new ModelAndView("forward:/error/general/");
    }
}
