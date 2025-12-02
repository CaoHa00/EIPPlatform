package com.EIPplatform.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.storage.local.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // registry.addResourceHandler("/files/**")
        // .addResourceLocations("file:///" + uploadDir + "/");
        String filePrefix = uploadDir.startsWith("/") ? "file:" : "file:///";

        registry.addResourceHandler("/files/**")
                .addResourceLocations(filePrefix + uploadDir + "/")
                .setCachePeriod(3600); // Optional: cache 1 giờ

        log.info("Configured file serving from: {}{}", filePrefix, uploadDir);
    }
}
