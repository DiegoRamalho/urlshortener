package ie.com.urlshortener.api.rest.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.junit.Test;
import org.springframework.http.HttpHeaders;

/**
 * Test class for the HeaderUtil.
 *
 * @author Diego
 * @see HeaderUtil
 * @since 19/01/2019
 */
public class HeaderUtilTest {

    private static final String ENTITY = "TEST";
    private static final String PARAM = "param";

    private static void assertAPPHeaders(final HttpHeaders headers, final String msg) {
        assertThat(headers.get(HeaderUtil.X_APP_ALERT)).isEqualTo(Collections.singletonList(String.format(msg, ENTITY, PARAM)));
        assertThat(headers.get(HeaderUtil.X_APP_PARAMS)).isEqualTo(Collections.singletonList(PARAM));
    }

    @Test
    public void createEntityCreationAlertTest() {
        assertAPPHeaders(HeaderUtil.createEntityCreationAlert(ENTITY, PARAM),
                         HeaderUtil.CREATE_MENSSAGE);
    }

    @Test
    public void createEntityUpdateAlertTest() {
        assertAPPHeaders(HeaderUtil.createEntityUpdateAlert(ENTITY, PARAM),
                         HeaderUtil.UPDATE_MENSSAGE);
    }

    @Test
    public void createEntityDeletionAlertTest() {
        assertAPPHeaders(HeaderUtil.createEntityDeletionAlert(ENTITY, PARAM),
                         HeaderUtil.DELETE_MENSSAGE);
    }

    @Test
    public void createFailureAlertTest() {
        final HttpHeaders headers = HeaderUtil.createFailureAlert(ENTITY, PARAM);
        assertThat(headers.get(HeaderUtil.X_APP_ERROR)).isEqualTo(Collections.singletonList(PARAM));
        assertThat(headers.get(HeaderUtil.X_APP_PARAMS_ERROR)).isEqualTo(Collections.singletonList(ENTITY));
    }

    @Test
    public void createFailureTest() {
        assertThat(HeaderUtil.createFailure(PARAM).get(HeaderUtil.X_APP_ERROR)).isEqualTo(Collections.singletonList(PARAM));
    }
}