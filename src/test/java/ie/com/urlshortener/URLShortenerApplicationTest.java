package ie.com.urlshortener;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import ie.com.urlshortener.api.URLShortenerApplication;

/**
 * Test class for the application.
 *
 * @author Diego
 * @see URLShortenerApplication
 * @since 19/01/2019
 */
@RunWith(SpringRunner.class)
public class URLShortenerApplicationTest {

    private static final String[] ARGS = {};

    @Test
    public void contextLoadsTest() {
        URLShortenerApplication.main(ARGS);
    }

}

