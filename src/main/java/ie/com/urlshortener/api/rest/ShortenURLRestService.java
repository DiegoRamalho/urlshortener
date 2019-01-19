package ie.com.urlshortener.api.rest;

import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import ie.com.urlshortener.api.rest.dto.StatisticsDto;
import ie.com.urlshortener.api.rest.dto.ShortURLDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Shorten URL Rest Service.
 *
 * @author Diego
 * @since 19/01/2019
 */
public interface ShortenURLRestService {

    @PostMapping(value = "/url", consumes = MediaType.TEXT_PLAIN_VALUE)
    @ApiOperation("Create a shortened URL from any URL.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "If already exist an shortened URL for this URL."),
            @ApiResponse(code = 201, message = "If it's a new shortened URL and it was successfully registered.")
    })
    ResponseEntity<ShortURLDTO> create(@ApiParam @Valid @RequestBody String url) throws URISyntaxException;

    @PutMapping("/url")
    @ApiOperation("Create or update a shortened URL.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "If it is an update of shortened URL"),
            @ApiResponse(code = 201, message = "If it is an create of shortened URL")
    })
    ResponseEntity<ShortURLDTO> create(@ApiParam @Valid @RequestBody ShortURLDTO dto) throws URISyntaxException;

    @GetMapping("/url/{code}")
    @ApiOperation("Find the shortened URL by the code.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful query"),
            @ApiResponse(code = 404, message = "If there is no shortened URL with this code")
    })
    ResponseEntity<ShortURLDTO> findOneByCode(
            @ApiParam(value = "Shortened URL code", required = true) @PathVariable("code") String code);

    @DeleteMapping("/url/{id}")
    @ApiOperation("Delete the the shortened URL by ID.")
    @ApiResponses(
            @ApiResponse(code = 200, message = "Successful removal")
    )
    ResponseEntity<Void> delete(@ApiParam(value = "Shortened URL ID", required = true) @PathVariable("id") String id);

    @GetMapping("/url/redirect/{code}")
    @ApiOperation("Forwarding of short URLs to the original ones.")
    @ApiResponses({
            @ApiResponse(code = 302, message = "Successful query"),
            @ApiResponse(code = 404, message = "If there is no shortened URL with this code")
    })
    ResponseEntity<String> redirect(@PathVariable("code") String code);

    @GetMapping("/url/count")
    @ApiOperation("Count the amount of shortened URLs.")
    @ApiResponses(
            @ApiResponse(code = 200, message = "Successful query")
    )
    ResponseEntity<Long> count();

    /**
     * Find the last Instant that some URL was created/updated.
     *
     * @return the last ShortURLDTO created/update.
     */
    @GetMapping("/url/last")
    @ApiOperation("Find the most recent Short URL.")
    @ApiResponses(
            @ApiResponse(code = 200, message = "Successful query")
    )
    ResponseEntity<ShortURLDTO> findLastUpdate();

    /**
     * Get the 100 days with more registrations.
     *
     * @return List of 100 days with more registrations.
     */
    @GetMapping("/url/top100days")
    @ApiOperation("Get the 100 days with more registrations.")
    @ApiResponses(
            @ApiResponse(code = 200, message = "Successful query")
    )
    ResponseEntity<List<StatisticsDto>> findTop100CreationDate();
}
