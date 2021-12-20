package com.bordozer.jphoto;

import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.jabsorb.JSONRPCServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class JPhotoApp extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(JPhotoApp.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(JPhotoApp.class, args);
    }

    @Bean
    public VelocityEngine getVelocityEngine() {
        final var velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        return velocityEngine;
    }

    @Bean("jsonrpcServlet")
    public JSONRPCServlet getJSONRPCServlet() {
        return new JSONRPCServlet();
    }
}
