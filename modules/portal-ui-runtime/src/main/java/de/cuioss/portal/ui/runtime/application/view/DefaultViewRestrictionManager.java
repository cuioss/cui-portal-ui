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
import de.cuioss.jsf.api.application.view.matcher.ViewMatcher;
import de.cuioss.jsf.api.application.view.matcher.ViewMatcherImpl;
import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.authentication.AuthenticatedUserInfo;
import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.configuration.types.ConfigAsFilteredMap;
import de.cuioss.portal.ui.api.view.PortalViewRestrictionManager;
import de.cuioss.portal.ui.api.view.ViewRestrictionManager;
import de.cuioss.tools.collect.MapBuilder;
import de.cuioss.tools.string.Splitter;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.CONTEXT_PARAM_SEPARATOR;
import static de.cuioss.portal.configuration.PortalConfigurationKeys.VIEW_ROLE_RESTRICTION_PREFIX;

/**
 * Helper class that handles the checking of the roles needed to access a
 * certain view. For the configuration of the restricted views see
 * {@link PortalConfigurationKeys#VIEW_ROLE_RESTRICTION_PREFIX}. The actual
 * 'business' method is {@link #isUserAuthorized(ViewDescriptor)}
 *
 * @author Oliver Wolff
 */
@PortalViewRestrictionManager
@RequestScoped
@EqualsAndHashCode(of = "roleMatcherMap")
@ToString(of = "roleMatcherMap")
public class DefaultViewRestrictionManager implements ViewRestrictionManager {

    @Serial
    private static final long serialVersionUID = -3987276415425723519L;

    @Inject
    @ConfigAsFilteredMap(startsWith = VIEW_ROLE_RESTRICTION_PREFIX, stripPrefix = true)
    Provider<Map<String, String>> viewRestrictions;

    private Map<String, ViewMatcher> roleMatcherMap;

    @Inject
    AuthenticatedUserInfo userInfo;

    @Inject
    Provider<FacesContext> facesContextProvider;

    /**
     * Initializes the configured view Matcher
     */
    @PostConstruct
    public void init() {
        var builder = new MapBuilder<String, ViewMatcher>();
        for (Entry<String, String> entry : viewRestrictions.get().entrySet()) {
            var entries = Splitter.on(CONTEXT_PARAM_SEPARATOR).omitEmptyStrings().trimResults()
                    .splitToList(entry.getValue());
            builder.put(entry.getKey(), new ViewMatcherImpl(entries));
        }
        roleMatcherMap = builder.toImmutableMap();
    }

    @Override
    public boolean isUserAuthorized(final ViewDescriptor descriptor) {
        return new HashSet<>(userInfo.getRoles()).containsAll(getRequiredRolesForView(descriptor));
    }

    @Override
    public Set<String> getRequiredRolesForView(final ViewDescriptor descriptor) {
        Set<String> requiredRoles = new HashSet<>();
        for (Entry<String, ViewMatcher> entry : roleMatcherMap.entrySet()) {
            if (entry.getValue().match(descriptor)) {
                requiredRoles.add(entry.getKey());
            }
        }
        return requiredRoles;
    }

    @Override
    public boolean isUserAuthorizedForViewOutcome(final String viewOutcome) {
        return isUserAuthorized(NavigationUtils.lookUpToViewDescriptorBy(facesContextProvider.get(), viewOutcome));
    }

}
