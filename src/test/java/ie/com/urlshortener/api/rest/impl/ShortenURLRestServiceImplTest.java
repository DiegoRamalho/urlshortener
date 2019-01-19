package ie.com.urlshortener.api.rest.impl;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ie.com.urlshortener.api.URLShortenerApplication;
import ie.com.urlshortener.api.repository.ShortenURLRepository;
import ie.com.urlshortener.api.rest.dto.ShortURLDTO;
import ie.com.urlshortener.api.service.ShortenURLService;
import ie.com.urlshortener.api.service.mapper.ShortURLMapper;

/**
 * Test class for the Shorten URL Rest Service.
 *
 * @author Diego
 * @see ShortenURLRestServiceImpl
 * @since 19/01/2019
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = URLShortenerApplication.class)
public class ShortenURLRestServiceImplTest {

    private static final String DEFAULT_ID = "1234";
    private static final String DEFAULT_CODE = "123";
    private static final String DEFAULT_URL = "http://localhost:8080/myapp";
    private static final String BASE_REDIRECT_URI = ShortenURLRestServiceImpl.BASE_URI + "redirect/";
    private static final String BASE_COUNT_URI = ShortenURLRestServiceImpl.BASE_URI + "count";
    private static final String BASE_LAST_URI = ShortenURLRestServiceImpl.BASE_URI + "last";
    private static final String BASE_TOP_100_DAYS_URI = ShortenURLRestServiceImpl.BASE_URI + "top100days";
    @Autowired
    @Qualifier("mappingJackson2HttpMessageConverter")
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;
    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
    @Autowired
    private ShortenURLRepository repository;
    @Autowired
    private ShortenURLService service;
    @Autowired
    private ShortURLMapper mapper;
    private MockMvc restMockMvc;

    /**
     * Convert an object to JSON byte array.
     *
     * @param object the object to convert
     * @return the JSON byte array
     */
    public static byte[] convertObjectToJsonBytes(final Object object)
            throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        final JavaTimeModule module = new JavaTimeModule();
        mapper.registerModule(module);

        return mapper.writeValueAsBytes(object);
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ShortenURLRestServiceImpl shortenURLRestService = new ShortenURLRestServiceImpl(service);
        restMockMvc = MockMvcBuilders.standaloneSetup(shortenURLRestService)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(new StringHttpMessageConverter(StandardCharsets.UTF_8), jacksonMessageConverter).build();

        repository.deleteAll();
    }

    @Test
    public void createValidShortURLTest() throws Exception {
        restMockMvc.perform(post(ShortenURLRestServiceImpl.BASE_URI)
                                    .contentType(MediaType.TEXT_PLAIN)
                                    .content(DEFAULT_URL))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.url").value(DEFAULT_URL))
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.code").value(notNullValue()));

    }

    @Test
    public void createURLEmptyTest() throws Exception {
        final ShortURLDTO shortURLDTO = ShortURLDTO.builder().url(StringUtils.EMPTY).build();
        restMockMvc.perform(put(ShortenURLRestServiceImpl.BASE_URI)
                                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                                    .content(convertObjectToJsonBytes(shortURLDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createURLNullEmptyTest() throws Exception {
        final ShortURLDTO shortURLDTO = ShortURLDTO.builder().build();
        restMockMvc.perform(put(ShortenURLRestServiceImpl.BASE_URI)
                                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                                    .content(convertObjectToJsonBytes(shortURLDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createWithoutCodeTest() throws Exception {
        final ShortURLDTO shortURLDTO = ShortURLDTO.builder().url(DEFAULT_URL).build();
        restMockMvc.perform(put(ShortenURLRestServiceImpl.BASE_URI)
                                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                                    .content(convertObjectToJsonBytes(shortURLDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.url").value(DEFAULT_URL))
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.code").value(notNullValue()));
    }

    @Test
    public void createWithCodeTest() throws Exception {
        final ShortURLDTO shortURLDTO = ShortURLDTO.builder().url(DEFAULT_URL).code(DEFAULT_CODE).build();
        restMockMvc.perform(put(ShortenURLRestServiceImpl.BASE_URI)
                                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                                    .content(convertObjectToJsonBytes(shortURLDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.url").value(DEFAULT_URL))
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.code").value(DEFAULT_CODE));

        final ShortURLDTO shortURLDB = mapper.toDto(repository.findByCode(DEFAULT_CODE).orElse(null));
        //Find by code
        restMockMvc.perform(get(ShortenURLRestServiceImpl.BASE_URI + DEFAULT_CODE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value(DEFAULT_URL))
                .andExpect(jsonPath("$.id").value(shortURLDB.getId()))
                .andExpect(jsonPath("$.code").value(DEFAULT_CODE));

        //Redirect
        restMockMvc.perform(get(BASE_REDIRECT_URI + DEFAULT_CODE))
                .andExpect(status().isFound());

        //Count
        restMockMvc.perform(get(BASE_COUNT_URI))
                .andExpect(status().isOk());

        //Last
        restMockMvc.perform(get(BASE_LAST_URI))
                .andExpect(status().isOk());

        //Top 100
        restMockMvc.perform(get(BASE_TOP_100_DAYS_URI))
                .andExpect(status().isOk());

        //Delete
        restMockMvc.perform(delete(ShortenURLRestServiceImpl.BASE_URI + shortURLDB.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void updateTest() throws Exception {
        final ShortURLDTO shortURLDTO = ShortURLDTO.builder().url(DEFAULT_URL).code(DEFAULT_CODE).id(DEFAULT_ID).build();
        restMockMvc.perform(put(ShortenURLRestServiceImpl.BASE_URI)
                                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                                    .content(convertObjectToJsonBytes(shortURLDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value(DEFAULT_URL))
                .andExpect(jsonPath("$.id").value(DEFAULT_ID))
                .andExpect(jsonPath("$.code").value(DEFAULT_CODE));

        testUpdateIdNull();
    }

    @Test
    public void createEmptyURLTest() throws Exception {
        restMockMvc.perform(post(ShortenURLRestServiceImpl.BASE_URI)
                                    .contentType(MediaType.TEXT_PLAIN)
                                    .content(StringUtils.EMPTY))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void createNullURLTest() throws Exception {
        restMockMvc.perform(post(ShortenURLRestServiceImpl.BASE_URI)
                                    .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void createInvalidURLTest() throws Exception {
        restMockMvc.perform(post(ShortenURLRestServiceImpl.BASE_URI)
                                    .contentType(MediaType.TEXT_PLAIN)
                                    .content("123"))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void findOneInvalidTest() throws Exception {
        restMockMvc.perform(get(ShortenURLRestServiceImpl.BASE_URI + DEFAULT_CODE))
                .andExpect(status().isNotFound());

    }

    @Test
    public void redirectInvalidTest() throws Exception {
        restMockMvc.perform(get(BASE_REDIRECT_URI + DEFAULT_CODE))
                .andExpect(status().isNotFound());

    }

    private void testUpdateIdNull() throws Exception {
        final ShortURLDTO shortURLDTO = mapper.toDto(repository.findByCode(DEFAULT_CODE).orElse(null));
        shortURLDTO.setId(null);
        restMockMvc.perform(put(ShortenURLRestServiceImpl.BASE_URI)
                                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                                    .content(convertObjectToJsonBytes(shortURLDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value(DEFAULT_URL))
                .andExpect(jsonPath("$.id").value(DEFAULT_ID))
                .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }
}