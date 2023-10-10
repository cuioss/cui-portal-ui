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

import static de.cuioss.tools.collect.CollectionLiterals.mutableList;

import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import de.cuioss.jsf.api.application.bundle.ResourceBundleWrapper;
import de.cuioss.portal.configuration.common.PortalPriorities;
import de.cuioss.portal.core.bundle.PortalResourceBundle;
import de.cuioss.tools.collect.CollectionBuilder;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.tools.string.Joiner;
import de.cuioss.uimodel.application.CuiProjectStage;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Portal variant of CuiResourceBundle
 *
 * @author Oliver Wolff
 */
@Named(PortalResourceBundleBean.BEAN_NAME)
@PortalResourceBundle
@Dependent
@EqualsAndHashCode(callSuper = false)
@ToString
public class PortalResourceBundleBean extends ResourceBundle implements Serializable {

    private static final long serialVersionUID = 3953649686127640297L;

    private static final CuiLogger log = new CuiLogger(PortalResourceBundleBean.class);

    /** Lookup name for el-expression within views: "msgs" */
    public static final String BEAN_NAME = "msgs";

    private Set<String> knownKeys;

    @Inject
    private Instance<ResourceBundleWrapper> resourceBundleWrapper;

    private transient List<ResourceBundleWrapper> sortedBundleWrapper;

    private String allBundleNames;

    @Inject
    private Provider<CuiProjectStage> projectStageProducer;

    @Override
    protected Object handleGetObject(final String key) {

        for (final ResourceBundleWrapper wrapper : getSortedBundleWrapper()) {
            if (wrapper.keySet().contains(key))
                return wrapper.getMessage(key);
        }

        final var errMsg = "Portal-104: No key '" + key + "' defined within any of the configured bundles: "
                + allBundleNames;

        if (projectStageProducer.get().isDevelopment())
            throw new MissingResourceException(errMsg, "ResourceBundleWrapperImpl", key);

        log.warn(errMsg);

        return "??" + key + "??";
    }

    private List<ResourceBundleWrapper> getSortedBundleWrapper() {
        if (null == sortedBundleWrapper) {
            sortedBundleWrapper = PortalPriorities.sortByPriority(mutableList(resourceBundleWrapper));
            List<String> bundleNames = mutableList();
            sortedBundleWrapper.forEach(wrapper -> bundleNames.add(wrapper.getBundleContent()));
            allBundleNames = Joiner.on("; ").skipNulls().join(bundleNames);
            log.debug("Resolved bundles = {}", allBundleNames);
        }
        return sortedBundleWrapper;
    }

    @Override
    public Enumeration<String> getKeys() {
        return Collections.enumeration(collectKeys());
    }

    @Override
    public Set<String> keySet() {
        return collectKeys();
    }

    private Set<String> collectKeys() {
        if (null == knownKeys) {
            final var builder = new CollectionBuilder<String>();
            for (final ResourceBundleWrapper wrapper : resourceBundleWrapper) {
                builder.add(wrapper.keySet());
            }
            knownKeys = builder.toImmutableSet();
        }
        return knownKeys;
    }

}
