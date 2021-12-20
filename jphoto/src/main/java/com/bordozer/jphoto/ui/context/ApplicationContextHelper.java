package com.bordozer.jphoto.ui.context;

import com.bordozer.jphoto.core.exceptions.BaseRuntimeException;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.core.services.utils.EntityLinkUtilsService;
import com.bordozer.jphoto.core.services.utils.ImageFileUtilsService;
import com.bordozer.jphoto.core.services.utils.UrlUtilsService;
import com.bordozer.jphoto.core.services.utils.UrlUtilsServiceImpl;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextHelper implements ApplicationContextAware {

    private static ApplicationContext springContext = null;

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        springContext = applicationContext;
    }

    public static ApplicationContext getSpringContext() {
        return springContext;
    }

    public static void setSpringContext(final ApplicationContext springContext) {
        ApplicationContextHelper.springContext = springContext;
    }

    public static <T> T getBean(final String name) {
        T bean = (T) springContext.getBean(name);
        if (bean == null) {
            throw new BaseRuntimeException(String.format("No bean named %s has been configured; see Spring config ", name));
        }
        return bean;
    }

    public static UrlUtilsService getUrlUtilsService() {
        return getBean(UrlUtilsServiceImpl.BEAN_NAME);
    }

    public static EntityLinkUtilsService getEntityLinkUtilsService() {
        return getBean(EntityLinkUtilsService.BEAN_NAME);
    }

    public static ImageFileUtilsService getImageFileUtilsService() {
        return getBean(ImageFileUtilsService.BEAN_NAME);
    }

    public static DateUtilsService getDateUtilsService() {
        return getBean(DateUtilsService.BEAN_NAME);
    }

    public static ConfigurationService getConfigurationService() {
        return getBean(ConfigurationService.BEAN_NAME);
    }

    public static SecurityService getSecurityService() {
        return getBean(SecurityService.BEAN_NAME);
    }

    public static TranslatorService getTranslatorService() {
        return getBean(TranslatorService.BEAN_NAME);
    }
}
