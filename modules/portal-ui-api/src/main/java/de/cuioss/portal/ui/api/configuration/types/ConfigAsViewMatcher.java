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
package de.cuioss.portal.ui.api.configuration.types;

import de.cuioss.jsf.api.application.view.matcher.EmptyViewMatcher;
import de.cuioss.jsf.api.application.view.matcher.ViewMatcher;
import de.cuioss.portal.configuration.PortalConfigurationKeys;
import jakarta.enterprise.util.Nonbinding;
import jakarta.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Injects a config property as a {@link ViewMatcher}. In case the property
 * is null or empty it will be an {@link EmptyViewMatcher}. The default
 * splitting character for the individual paths is
 * {@value PortalConfigurationKeys#CONTEXT_PARAM_SEPARATOR}
 *
 * @author Oliver Wolff
 */
@Qualifier
@Target({TYPE, METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface ConfigAsViewMatcher {

    /**
     * @return the name of the property
     */
    @Nonbinding
    String name();

    /**
     * @return the separator char, defaults to
     * {@value PortalConfigurationKeys#CONTEXT_PARAM_SEPARATOR}
     */
    @Nonbinding
    char separator() default PortalConfigurationKeys.CONTEXT_PARAM_SEPARATOR;
}
