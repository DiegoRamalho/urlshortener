package ie.com.urlshortener.api.service.impl;

import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import ie.com.urlshortener.api.domain.ShortURL;
import ie.com.urlshortener.api.rest.dto.StatisticsDto;
import ie.com.urlshortener.api.repository.ShortenURLRepository;
import ie.com.urlshortener.api.repository.StatisticsRepository;
import ie.com.urlshortener.api.rest.dto.ShortURLDTO;
import ie.com.urlshortener.api.service.ShortenURLService;
import ie.com.urlshortener.api.service.mapper.ShortURLMapper;

/**
 * Service Implementation for managing Short URLs.
 *
 * @author Diego
 * @since 19/01/2019
 */
@Service
public class ShortenURLServiceImpl implements ShortenURLService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final ShortenURLRepository repository;
    private final StatisticsRepository statisticsRepository;
    private final ShortURLMapper mapper;

    public ShortenURLServiceImpl(final ShortenURLRepository repository, final StatisticsRepository statisticsRepository,
                                 final ShortURLMapper mapper) {
        this.repository = repository;
        this.statisticsRepository = statisticsRepository;
        this.mapper = mapper;
    }

    /**
     * Get all the Short URLs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<ShortURLDTO> findAll(final Pageable pageable) {
        log.debug("Request to find all Short URLs");
        return repository.findAll(pageable).map(mapper::toDto);
    }

    /**
     * Save a entity.
     *
     * @param dto the entity to save
     * @return the persisted entity
     */
    @Override
    public ShortURLDTO save(final ShortURLDTO dto) {
        log.debug("Request to save: {}", dto);
        final ShortURL entity = mapper.toEntity(dto);
        if (StringUtils.isEmpty(entity.getCode())) {
            entity.setCode(RandomStringUtils.randomAlphanumeric(20));
        }
        final ShortURL save = repository.save(entity);
        return mapper.toDto(save);
    }

    /**
     * Get the entity by code.
     *
     * @param code the id of the entity
     * @return the entity
     */
    @Override
    public ShortURLDTO findOneByCode(final String code) {
        log.debug("Request to get: {}", code);
        return mapper.toDto(repository.findByCode(code).orElse(null));
    }

    /**
     * Delete the entity by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(final String id) {
        log.debug("Request to delete: {}", id);
        repository.deleteById(id);
    }

    /**
     * Count the amount of ShortURL.
     *
     * @return the amount of ShortURL.
     */
    @Override
    public long count() {
        return repository.count();
    }

    /**
     * Find the most recent Short URL.
     *
     * @return the last created/update Instant.
     */
    @Override
    public ShortURLDTO findLastUpdate() {
        return mapper.toDto(statisticsRepository.findShortURLWithLastUpdate());
    }

    /**
     * Get the 100 days with more registrations.
     *
     * @return List of 100 days with more registrations.
     */
    @Override
    public List<StatisticsDto> findTop100CreationDate() {
        return statisticsRepository.findTop100CreationDate();
    }
}
