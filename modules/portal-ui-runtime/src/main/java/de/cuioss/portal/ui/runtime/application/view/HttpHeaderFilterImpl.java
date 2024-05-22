/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.portal.ui.runtime.application.view;

import de.cuioss.jsf.api.application.navigation.NavigationUtils;
import de.cuioss.jsf.api.application.view.matcher.EmptyViewMatcher;
import de.cuioss.jsf.api.application.view.matcher.ViewMatcher;
import de.cuioss.jsf.api.application.view.matcher.ViewMatcherImpl;
import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.jsf.api.common.view.ViewDescriptorImpl;
import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.configuration.types.ConfigAsFilteredMap;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.tools.string.MoreStrings;
import de.cuioss.tools.string.Splitter;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.*;
import java.util.Map.Entry;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.HTTP_HEADER_BASE;
import static de.cuioss.portal.configuration.PortalConfigurationKeys.HTTP_HEADER_ENABLED;
import static de.cuioss.tools.string.MoreStrings.isEmpty;

/**
 * Allow setting of custom http headers via configuration.
 *
 * @author Matthias Walliczek
 */
@ApplicationScoped
public class HttpHeaderFilterImpl {

    private static final CuiLogger LOGGER = new CuiLogger(HttpHeaderFilterImpl.class);

    private static final String INVALID_KEY = "Portal-128: Invalid configuration key '{}'";

    private static final String INVALID_VALUE = "Portal-129: Invalid configuration value '{}' for key '{}'";

    @Inject
    @ConfigAsFilteredMap(startsWith = HTTP_HEADER_BASE, stripPrefix = true)
    private Provider<Map<String, String>> headerMapProvider;

    @Inject
    @ConfigProperty(name = HTTP_HEADER_ENABLED, defaultValue = "true")
    private Boolean enabled;

    private List<HttpHeader> headerList;

    @PostConstruct
    public void initialize() {

        headerList = Collections.emptyList();

        if (Boolean.FALSE.equals(enabled)) {
            return;
        }
        final var headerConfigurationMap = headerMapProvider.get();
        final Map<String, HttpHeader> headerMap = new HashMap<>(0);
        for (final Entry<String, String> entry : headerConfigurationMap.entrySet()) {
            final var split = Splitter.on('.').splitToList(entry.getKey());
            if (split.size() != 2) {
                LOGGER.error(INVALID_KEY, entry.getKey());
            } else {
                switch (split.get(1).toLowerCase(Locale.ROOT)) {
                    case "enabled":
                        getOrCreateHeader(split.get(0), headerMap)
                            .setEnabled(Boolean.parseBoolean(entry.getValue().trim()));
                        break;
                    case "views":
                        getOrCreateHeader(split.get(0), headerMap).setViewMatcher(createViewMatcher(entry.getValue()));
                        break;
                    case "content":
                        final var header = getOrCreateHeader(split.get(0), headerMap);
                        final var value = entry.getValue().trim();
                        final var splitValues = Splitter.on(": ").splitToList(value);
                        if (splitValues.size() < 2) {
                            LOGGER.warn(INVALID_VALUE, entry.getValue(), entry.getKey());
                            break;
                        }
                        header.setKey(splitValues.get(0).trim());
                        header.setValue(value.substring(value.indexOf(": ") + 2));
                        break;
                    default:
                        LOGGER.warn(INVALID_KEY, entry.getKey());
                }
            }
        }
        headerList = headerMap.values().stream().filter(HttpHeader::isEnabled).sorted().toList();
    }

    private static ViewMatcher createViewMatcher(final String value) {
        if (MoreStrings.isEmpty(value)) {
            return new EmptyViewMatcher(false);
        }
        return new ViewMatcherImpl(Splitter.on(PortalConfigurationKeys.CONTEXT_PARAM_SEPARATOR).trimResults()
            .omitEmptyStrings().splitToList(value));
    }

    private static HttpHeader getOrCreateHeader(final String key, final Map<String, HttpHeader> headerMap) {
        return headerMap.computeIfAbsent(key, v -> new HttpHeader());
    }


    /**
     * Add headers if view matches any defined httpHeader rule
     *
     * @param response {@link HttpServletResponse} must not be null
     */
    public void onCreate(HttpServletRequest request, HttpServletResponse response) {
        final var currentDescriptor = createViewDescriptor(request);
        LOGGER.debug("Act on Created HttpServletResponse for view %s", currentDescriptor);
        for (final HttpHeader entry : headerList) {
            if (null == entry.getViewMatcher() || entry.getViewMatcher().match(currentDescriptor)) {
                response.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    private ViewDescriptor createViewDescriptor(HttpServletRequest request) {
        final var builder = ViewDescriptorImpl.builder();
        final var foundId = NavigationUtils.extractRequestUri(request);

        if (!isEmpty(foundId)) {
            builder.withViewId(foundId);
            builder.withLogicalViewId(NavigationUtils.handleViewIdSuffix(foundId));
        }
        builder.withUrlParameter(NavigationUtils.extractUrlParameters(request));

        return builder.build();
    }

}
