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
package de.cuioss.portal.ui.runtime.application.bundle;

import static de.cuioss.tools.collect.CollectionLiterals.mutableSet;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.Priority;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import de.cuioss.jsf.api.application.bundle.ResourceBundleWrapper;
import de.cuioss.portal.common.bundle.ResourceBundleRegistry;
import de.cuioss.portal.common.locale.PortalLocale;
import de.cuioss.portal.common.priority.PortalPriorities;
import de.cuioss.portal.ui.api.locale.LocaleChangeEvent;
import de.cuioss.tools.collect.CollectionBuilder;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.tools.string.Joiner;
import de.cuioss.uimodel.application.CuiProjectStage;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Portal specific implementation of of {@link ResourceBundleWrapper}. It can do
 * the following tricks:
 * <ul>
 * <li>Define a working serializable contract for resourceBundles</li>
 * <li>Listens and acts on {@link LocaleChangeEvent}s</li>
 * </ul>
 *
 * @author Oliver Wolff
 */
@Named
@SessionScoped
@Priority(PortalPriorities.DEFAULT_LEVEL)
@EqualsAndHashCode(of = "keyList", doNotUseGetters = true)
@ToString(of = "keyList", doNotUseGetters = true)
public class PortalResourceBundleWrapper implements ResourceBundleWrapper {

    private static final long serialVersionUID = 9136037316650210138L;

    private static final CuiLogger log = new CuiLogger(PortalResourceBundleWrapper.class);

    @SuppressWarnings("cdi-ambiguous-dependency")
    @Inject
    private ResourceBundleRegistry resourceBundleRegistry;

    private transient List<ResourceBundle> resolvedBundles;

    @Inject
    private Provider<CuiProjectStage> projectStage;

    @Inject
    @PortalLocale
    private Provider<Locale> localeProvider;

    private final List<String> keyList = new CopyOnWriteArrayList<>();

    @Override
    public String getMessage(final String key) {

        for (final ResourceBundle bundle : getResolvedBundles()) {
            if (bundle.containsKey(key)) {
                return bundle.getString(key);
            }
        }

        final var errMsg = "Portal-003 : No key '" + key + "' defined within any of the configured bundles: "
                + resourceBundleRegistry.getResolvedPaths();

        if (projectStage.get().isDevelopment()) {
            throw new MissingResourceException(errMsg, "ResourceBundleWrapperImpl", key);
        }

        log.warn(errMsg);
        return "??" + key + "??";

    }

    /**
     * Listener for instances of {@link LocaleChangeEvent} Therefore it can react on
     * changes of the locale by disposing the loaded {@link ResourceBundle}s and
     * therefore forcing a reload of the newly set locale
     *
     * @param newLocale
     */
    void actOnLocaleChangeEven(@Observes @LocaleChangeEvent final Locale newLocale) {
        resolvedBundles = null;
    }

    private List<ResourceBundle> getResolvedBundles() {
        if (null == resolvedBundles) {
            var builder = new CollectionBuilder<ResourceBundle>();
            for (final String path : resourceBundleRegistry.getResolvedPaths()) {
                builder.add(ResourceBundle.getBundle(path, localeProvider.get()));
            }
            resolvedBundles = builder.toImmutableList();
        }
        return resolvedBundles;
    }

    @Override
    public Set<String> keySet() {
        if (keyList.isEmpty()) {
            final Set<String> builder = new HashSet<>();
            for (final ResourceBundle bundle : getResolvedBundles()) {
                builder.addAll(bundle.keySet());
            }
            keyList.addAll(builder);
        }
        return mutableSet(keyList);
    }

    @Override
    public String getBundleContent() {
        return Joiner.on(", ").join(resourceBundleRegistry.getResolvedPaths());
    }
}
