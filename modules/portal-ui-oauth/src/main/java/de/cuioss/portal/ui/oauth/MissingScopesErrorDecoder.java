package de.cuioss.portal.ui.oauth;

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

import de.cuioss.tools.logging.CuiLogger;

/**
 * To detect and handle the error "missing scopes"
 * (https://tools.ietf.org/html/rfc6750)
 */
public class MissingScopesErrorDecoder implements ResponseExceptionMapper<MissingScopesException> {

    private static final CuiLogger log = new CuiLogger(MissingScopesErrorDecoder.class);

    private static final String WWW_AUTHENTICATE_HEADER_KEY = "www-authenticate";

    /**
     * @param status  HTTP status code
     * @param headers HTTP Headers
     *
     * @return MissingScopesException on HTTP 403 and existing www-authenticate
     *         header with missing scopes
     */
    public static MissingScopesException checkAndHandleMissingScopes(final int status,
            final MultivaluedMap<String, Object> headers) {
        if (SC_FORBIDDEN == status) {
            log.trace("response.status == 403");

            var wwwAuthenticate = filterHeader(headers);
            if (wwwAuthenticate.isEmpty()) {
                log.debug("www-authenticate not found!");
                return null;
            }
            log.trace("www-authenticate found: {}", wwwAuthenticate);

            List<String> wwwAuthenticateEntries = wwwAuthenticate.stream().map(value -> value.split(","))
                    .flatMap(Arrays::stream).map(String::trim).collect(Collectors.toList());
            if (wwwAuthenticateEntries.stream().anyMatch(entry -> entry.equalsIgnoreCase("error=\"insufficient_scope\"")
                    || entry.equalsIgnoreCase("Bearer error=\"insufficient_scope\""))) {
                var missingScopesEntry = wwwAuthenticateEntries.stream()
                        .filter(value -> value.trim().toLowerCase().startsWith("scope=\"")).findFirst();
                if (missingScopesEntry.isPresent() && missingScopesEntry.get().contains("=")) {
                    var missingScopesValue = missingScopesEntry.get().split("=")[1].replace('\"', ' ').trim();
                    return new MissingScopesException(missingScopesValue);
                }
                log.debug("scopes entry is missing");
            } else {
                log.debug("error=\"insufficient_scope\" is missing");
            }
        }
        return null;
    }

    @Override
    public MissingScopesException toThrowable(Response response) {
        return checkAndHandleMissingScopes(response.getStatus(), response.getHeaders());
    }

    @Override
    public boolean handles(int status, MultivaluedMap<String, Object> headers) {
        if (SC_FORBIDDEN == status) {
            log.trace("response.status == 403");

            var wwwAuthenticate = filterHeader(headers);
            if (wwwAuthenticate.isEmpty()) {
                log.debug("www-authenticate not found!");
                return false;
            }
            return true;
        }
        return false;
    }

    private static List<String> filterHeader(MultivaluedMap<String, Object> headers) {
        return headers.entrySet().stream().filter(entry -> entry.getKey().equalsIgnoreCase(WWW_AUTHENTICATE_HEADER_KEY))
                .map(Map.Entry::getValue).flatMap(Collection::stream).filter(value -> value instanceof String)
                .map(value -> (String) value).collect(Collectors.toList());
    }
}
