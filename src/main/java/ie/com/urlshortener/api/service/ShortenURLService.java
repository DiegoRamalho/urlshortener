package ie.com.urlshortener.api.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ie.com.urlshortener.api.domain.StatisticsDto;
import ie.com.urlshortener.api.rest.dto.ShortURLDTO;

/**
 * Service Interface for managing ShortURL.
 *
 * @author Diego
 * @since 19/01/2019
 */
public interface ShortenURLService {

    /**
     * Save a entity.
     *
     * @param dto the entity to save
     * @return the persisted entity
     */
    ShortURLDTO save(final ShortURLDTO dto);

    /**
     * Get all entities.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ShortURLDTO> findAll(final Pageable pageable);

    /**
     * Get the entity by code.
     *
     * @param code the id of the entity
     * @return the entity
     */
    ShortURLDTO findOneByCode(final String code);

    /**
     * Delete the entity by id.
     *
     * @param id the id of the entity
     */
    void delete(final String id);

    /**
     * Count the amount of ShortURL.
     *
     * @return the amount of ShortURL.
     */
    long count();

    /**
     * Find the most recent Short URL.
     *
     * @return the last ShortURLDTO created/update.
     */
    ShortURLDTO findLastUpdate();

    /**
     * Get the 100 days with more registrations.
     *
     * @return List of 100 days with more registrations.
     */
    List<StatisticsDto> findTop100CreationDate();
}
