package ie.com.urlshortener.api.rest.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ie.com.urlshortener.api.domain.StatisticsDto;
import ie.com.urlshortener.api.rest.ShortenURLRestService;
import ie.com.urlshortener.api.rest.dto.ShortURLDTO;
import ie.com.urlshortener.api.rest.util.HeaderUtil;
import ie.com.urlshortener.api.service.ShortenURLService;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing Short URLs.
 *
 * @author Diego
 * @since 19/01/2019
 */
@RestController
@RequestMapping("/api")
public class ShortenURLRestServiceImpl implements ShortenURLRestService {

    protected static final String URL_IS_NOT_VALID = "URL is not valid!";
    protected static final String ENTITY_NAME = "Short URL";
    protected static final String BASE_URI = "/api/url/";
    private static final UrlValidator URL_VALIDATOR = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);
    private final ShortenURLService service;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public ShortenURLRestServiceImpl(final ShortenURLService service) {
        this.service = service;
    }

    /**
     * POST /url: Create a shortened URL from any URL.
     *
     * @param url the URL to create.
     * @return the ResponseEntity with status 201 (Created) and with body the new DTO, or with status 200 (OK) if the shortened URL
     * already exists.
     */
    @Override
    public ResponseEntity<ShortURLDTO> create(@RequestBody @ApiParam final @Valid String url) throws URISyntaxException {
        log.debug("REST request to create {}: {}", ENTITY_NAME, url);
        return create(ShortURLDTO.builder().url(url).build());
    }

    /**
     * PUT /url: Create or update a shortened URL.
     *
     * @param dto the DTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated DTO, or with status 201 (Created) if the shortened URL
     * already exists.
     */
    @Override
    public ResponseEntity<ShortURLDTO> create(@RequestBody @ApiParam final @Valid ShortURLDTO dto) throws URISyntaxException {
        log.debug("REST request to update {}: {}", ENTITY_NAME, dto);
        if (StringUtils.isEmpty(dto.getUrl()) || !URL_VALIDATOR.isValid(dto.getUrl())) {
            return ResponseEntity
                    .badRequest()
                    .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, URL_IS_NOT_VALID))
                    .body(null);
        }
        ShortURLDTO dtoToSave = dto;
        boolean isInsert = StringUtils.isEmpty(dto.getId());
        if (isInsert && !StringUtils.isEmpty(dto.getCode())) {
            //If the code exists, there is no reason to create other.
            final ShortURLDTO dtoDB = service.findOneByCode(dto.getCode());
            if (dtoDB != null) {
                dtoToSave = dtoDB;
                isInsert = false;
            }
        }
        final ShortURLDTO result = service.save(dtoToSave);
        if (isInsert) {
            return ResponseEntity.created(new URI(BASE_URI + result.getCode()))
                    .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId()))
                    .body(result);
        }
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId()))
                .body(result);
    }

    /**
     * GET /url/{code} : Find the shortened URL by the code.
     *
     * @param code the Shortened URL code
     * @return the ResponseEntity with status 200 (OK) and with body the DTO, or with status 404 (Not Found) If there is no shortened
     * URL with this code.
     */
    @Override
    public ResponseEntity<ShortURLDTO> findOneByCode(
            @ApiParam(value = "Shortened URL code", required = true) @PathVariable("code") final String code) {
        log.debug("REST request to find One {}: By code {}", ENTITY_NAME, code);
        return Optional.ofNullable(service.findOneByCode(code))
                .map(it -> ResponseEntity.ok().body(it))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE  /entity/:id : Delete the entity by id.
     *
     * @param id the id of the DTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @Override
    public ResponseEntity<Void> delete(@ApiParam(value = "Object Id", required = true) @PathVariable("id") final String id) {
        log.debug("REST request to delete {}: {}", ENTITY_NAME, id);
        service.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * GET /url/redirect/{code} : Forwarding of short URLs to the original ones.
     *
     * @param code the Shortened URL code
     * @return the ResponseEntity with status 302 (Found) and the url in the header, or with status 404 (Not Found) If there is no
     * shortened URL with this code.
     */
    @Override
    public ResponseEntity<String> redirect(@PathVariable("code") final String code) {
        log.debug("REST request to redirect {}: {}", ENTITY_NAME, code);
        return Optional.ofNullable(service.findOneByCode(code))
                .map(it -> new ResponseEntity<String>(HeaderUtil.createRedirect(it.getUrl()), HttpStatus.FOUND))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /url/count : Count the amount of ShortURL.
     *
     * @return the ResponseEntity with status 200 (OK) and the amount of ShortURL.
     */
    @Override
    public ResponseEntity<Long> count() {
        log.debug("REST request to count");
        return ResponseEntity.ok().body(service.count());
    }

    /**
     * GET /url/last : Find the most recent Short URL.
     *
     * @return the ResponseEntity with status 200 (OK) and the last ShortURLDTO created/update.
     */
    @Override
    public ResponseEntity<ShortURLDTO> findLastUpdate() {
        log.debug("REST request to find last update");
        return ResponseEntity.ok().body(service.findLastUpdate());
    }

    /**
     * GET /url/top100days : Get the 100 days with more registrations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of 100 days with more registrations.
     */
    @Override
    public ResponseEntity<List<StatisticsDto>> findTop100CreationDate() {
        log.debug("REST request to find top 100 days");
        return ResponseEntity.ok().body(service.findTop100CreationDate());
    }
}
