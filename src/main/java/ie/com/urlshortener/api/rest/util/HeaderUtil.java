package ie.com.urlshortener.api.rest.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

/**
 * Utility class for HTTP headers creation.
 *
 * @author Diego
 * @since 19/01/2019
 */
public final class HeaderUtil {

    public static final String X_APP_ALERT = "X-App-alert";
    public static final String X_APP_PARAMS = "X-App-params";
    public static final String X_APP_ERROR = "X-App-error";
    public static final String X_APP_PARAMS_ERROR = "X-App-params";
    public static final String LOCATION = "Location";
    public static final String CREATE_MENSSAGE = "A new %s is created with identifier %s";
    public static final String UPDATE_MENSSAGE = "A %s is updated with identifier %s";
    public static final String DELETE_MENSSAGE = "A %s is deleted with identifier %s";
    private static final Logger LOG = LoggerFactory.getLogger(HeaderUtil.class);

    private HeaderUtil() {
    }

    public static HttpHeaders createRedirect(final String url) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, url);
        return headers;
    }

    public static HttpHeaders createAlert(final String message, final String param) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(X_APP_ALERT, message);
        headers.add(X_APP_PARAMS, param);
        return headers;
    }

    public static HttpHeaders createEntityCreationAlert(final String entityName, final String param) {
        return createAlert(String.format(CREATE_MENSSAGE, entityName, param), param);
    }

    public static HttpHeaders createEntityUpdateAlert(final String entityName, final String param) {
        return createAlert(String.format(UPDATE_MENSSAGE, entityName, param), param);
    }

    public static HttpHeaders createEntityDeletionAlert(final String entityName, final String param) {
        return createAlert(String.format(DELETE_MENSSAGE, entityName, param), param);
    }

    public static HttpHeaders createFailureAlert(final String entityName, final String defaultMessage) {
        final HttpHeaders headers = createFailure(defaultMessage);
        headers.add(X_APP_PARAMS_ERROR, entityName);
        return headers;
    }

    public static HttpHeaders createFailure(final String defaultMessage) {
        LOG.error("Entity processing failed, {}", defaultMessage);
        final HttpHeaders headers = new HttpHeaders();
        headers.add(X_APP_ERROR, defaultMessage);
        return headers;
    }
}
