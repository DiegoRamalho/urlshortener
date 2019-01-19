package ie.com.urlshortener.api.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import ie.com.urlshortener.api.domain.ShortURL;

/**
 * Spring Data MongoDB repository for the ShortURL entity.
 *
 * @author Diego
 * @since 19/01/2019
 */
@Repository
public interface ShortenURLRepository extends MongoRepository<ShortURL, String> {

    /**
     * Searchs for a ShortURL by code.
     *
     * @param code Code for the ShortURL
     * @return Optional<ShortURL>.
     */
    Optional<ShortURL> findByCode(final String code);
}
