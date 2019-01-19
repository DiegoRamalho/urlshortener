package ie.com.urlshortener.api.config;

import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Database Configuration.
 *
 * @author Diego
 * @since 19/01/2019
 */
@Configuration
@EnableMongoRepositories("ie.com.urlshortener.api.repository")
@EnableMongoAuditing
@Import(MongoAutoConfiguration.class)
public class DatabaseConfiguration {

    /**
     * Bean that automatically validade entities before they are saved in database.
     *
     * @return ValidatingMongoEventListener.
     */
    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener() {
        return new ValidatingMongoEventListener(validator());
    }

    /**
     * Central class for {@code javax.validation} (JSR-303) setup in a Spring application context.
     *
     * @return LocalValidatorFactoryBean.
     */
    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

}
