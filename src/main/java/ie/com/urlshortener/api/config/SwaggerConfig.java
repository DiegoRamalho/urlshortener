package ie.com.urlshortener.api.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger Configurations.
 *
 * @author Diego
 * @since 19/01/2019
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private final ApplicationProperties applicationProperties;

    public SwaggerConfig(final ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    /**
     * A bean which is intended to be the primary interface into the Springfox framework. Provides sensible defaults and convenience
     * methods for configuration.
     *
     * @return Docket
     */
    @Bean
    public Docket produceApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("ie.com.urlshortener.api"))
                .paths(PathSelectors.regex(applicationProperties.getSwagger().getDefaultIncludePattern()))
                .build();
    }

    /**
     * API Informatios.
     *
     * @return ApiInfo.
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(applicationProperties.getSwagger().getTitle())
                .description(applicationProperties.getSwagger().getDescription())
                .version(applicationProperties.getSwagger().getVersion())
                .build();
    }
}
