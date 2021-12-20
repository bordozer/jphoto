package com.bordozer.jphoto.admin.controllers.js;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin/js")
public class AdminJSController {

    @RequestMapping(method = RequestMethod.GET, value = "/common.js")
    public String adminCommonJS() {
        return "admin/js/common.js";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/photosight.js")
    public String photosightUserInfo() {
        return "admin/js/photosight-user-info.js";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/job-execution-progress.js")
    public String jobExecutionProgress() {
        return "admin/js/job-execution-progress.js";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/translations-reload.js")
    public String translationsReload() {
        return "admin/js/translations-reload.js";
    }
}
