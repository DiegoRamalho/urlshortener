package ie.com.urlshortener.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * Properties of the project.
 *
 * <p> Properties are configured in the application.yml file. </p>
 *
 * @author Diego
 * @since 19/01/2019
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    /**
     * The Swagger Properties.
     *
     * @see Swagger
     */
    private final Swagger swagger;

    public ApplicationProperties() {
        swagger = new Swagger();
    }

    /**
     * Gets the Swagger Properties.
     *
     * @return Swagger
     */
    public Swagger getSwagger() {
        return swagger;
    }

    /**
     * Swagger Properties.
     *
     * @author Diego
     * @since 19/01/2019
     */
    @Data
    public class Swagger {

        /**
         * The API title.
         */
        private String title;
        /**
         * The API description.
         */
        private String description;
        /**
         * The API version.
         */
        private String version;
        /**
         * The regular expression for the paths that must be analysed.
         */
        private String defaultIncludePattern;
    }
}
