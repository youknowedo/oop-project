package it.chalmers.commit.api;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** Serves every {@link RestController} under the versioned base path. */
@Configuration
public class ApiConfig implements WebMvcConfigurer {

    /** Base path of the current API version. */
    public static final String BASE_PATH = "/api/v1";

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(BASE_PATH, c -> c.isAnnotationPresent(RestController.class));
    }
}
