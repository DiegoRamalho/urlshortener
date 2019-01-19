package ie.com.urlshortener.api.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import ie.com.urlshortener.api.URLShortenerApplication;
import ie.com.urlshortener.api.domain.ShortURL;
import ie.com.urlshortener.api.rest.dto.StatisticsDto;
import ie.com.urlshortener.api.repository.ShortenURLRepository;
import ie.com.urlshortener.api.rest.dto.ShortURLDTO;
import ie.com.urlshortener.api.service.ShortenURLService;
import ie.com.urlshortener.api.service.mapper.ShortURLMapper;

/**
 * Test class for the Shorten URL Service.
 *
 * @author Diego
 * @see ShortenURLServiceImpl
 * @since 19/01/2019
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = URLShortenerApplication.class)
public class ShortenURLServiceImplTest {

    private static final String DEFAULT_CODE = "123";
    private static final String DEFAULT_URL = "http://localhost:8080";
    @Autowired
    private ShortenURLService service;
    @Autowired
    private ShortenURLRepository repository;
    @Autowired
    private ShortURLMapper mapper;

    private Pageable pageable;

    @Before
    public void setup() {
        //Clear the repository
        repository.deleteAll();
        pageable = PageRequest.of(0, 1);
    }

    @Test
    public void saveTest() {
        //save without code
        final ShortURLDTO shortURLDTO1 = saveAndValidate(DEFAULT_URL, null);

        //save with code
        final ShortURLDTO shortURLDTO2 = saveAndValidate(DEFAULT_URL, DEFAULT_CODE);

        //Validates the findOneByCode
        findOneByCodeAndValidade(shortURLDTO1.getCode(), shortURLDTO1.getId());
        findOneByCodeAndValidade(shortURLDTO2.getCode(), shortURLDTO2.getId());
    }

    @Test
    public void findOneByCodeNullTest() {
        final ShortURLDTO result = service.findOneByCode(null);
        assertThat(result).isNull();
    }

    @Test
    public void findOneByCodeEmptyDBTest() {
        final ShortURLDTO result = service.findOneByCode(DEFAULT_CODE);
        assertThat(result).isNull();
    }

    @Test
    public void findAllTest() {
        final long sizeBefore = service.findAll(pageable).getTotalElements();

        //Save 1
        saveAndValidate(DEFAULT_URL, null);
        findAllAndValidate(1L, sizeBefore + 1);

        //Save 2
        final ShortURLDTO shortURLDTO = saveAndValidate(DEFAULT_URL, null);
        findAllAndValidate(2L, sizeBefore + 2);

        final List<StatisticsDto> res = service.findTop100CreationDate();
        assertThat(res).isNotNull();
        assertThat(res.size()).isEqualTo(1);
        assertThat(res.get(0).getValue()).isEqualTo(2);

        final ShortURLDTO lastUpdate = service.findLastUpdate();
        assertThat(lastUpdate).isNotNull();
        assertThat(lastUpdate.getId()).isEqualTo(shortURLDTO.getId());

        service.delete(shortURLDTO.getId());
        findAllAndValidate(1L, sizeBefore + 1);
    }

    @Test
    public void mapperTest() {
        assertThat(mapper.toEntity((ShortURLDTO) null)).isNull();
        assertThat(mapper.toEntity((List<ShortURLDTO>) null)).isNull();
        assertThat(mapper.toDto((ShortURL) null)).isNull();
        assertThat(mapper.toDto((List<ShortURL>) null)).isNull();

        final ShortURLDTO shortURLDTO = saveAndValidate(DEFAULT_URL, null);
        final ShortURL shortURL = mapper.toEntity(shortURLDTO);

        assertThat(mapper.toDto(Collections.singletonList(shortURL))).isNotEmpty();
        assertThat(mapper.toEntity(Collections.singletonList(shortURLDTO))).isNotEmpty();
    }

    private void findAllAndValidate(final long totalPagesExpected, final long totalElementsExpected) {
        final Page<ShortURLDTO> listAfter = service.findAll(pageable);
        assertThat(listAfter).isNotEmpty();
        assertThat(listAfter.getTotalPages()).isEqualTo(totalPagesExpected);
        assertThat(listAfter.getTotalElements()).isEqualTo(totalElementsExpected);
        assertThat(service.count()).isEqualTo(totalElementsExpected);
    }

    private void findOneByCodeAndValidade(final String code, final String idExpected) {
        final ShortURLDTO result = service.findOneByCode(code);
        assertThat(result.getId()).isEqualTo(idExpected);
    }

    private ShortURLDTO saveAndValidate(final String url, final String code) {
        final ShortURLDTO result = service.save(ShortURLDTO.builder()
                                                        .url(url)
                                                        .code(code)
                                                        .build());
        assertThat(result.getId()).isNotEmpty();
        assertThat(result.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(result.getCode()).isNotEmpty();
        if (!StringUtils.isEmpty(code)) {
            assertThat(result.getCode()).isEqualTo(DEFAULT_CODE);
        }
        return result;
    }


}