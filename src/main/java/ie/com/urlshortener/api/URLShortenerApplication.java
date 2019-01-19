package ie.com.urlshortener.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import ie.com.urlshortener.api.config.ApplicationProperties;

@EnableConfigurationProperties(ApplicationProperties.class)
@SpringBootApplication(scanBasePackages = "ie.com.urlshortener.api")
public class URLShortenerApplication {

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments
     */
    public static void main(final String... args) {
        SpringApplication.run(URLShortenerApplication.class, args);
    }

}

