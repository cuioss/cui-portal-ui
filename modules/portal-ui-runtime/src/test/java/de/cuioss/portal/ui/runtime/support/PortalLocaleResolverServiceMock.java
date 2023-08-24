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
package de.cuioss.portal.ui.runtime.support;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;

import java.util.List;
import java.util.Locale;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Alternative;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import de.cuioss.portal.ui.api.locale.LocaleChangeEvent;
import de.cuioss.portal.ui.api.locale.LocaleResolverService;
import de.cuioss.portal.ui.api.locale.PortalLocaleResolver;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Mock version of {@link LocaleResolverService}
 *
 * @author Oliver Wolff
 */
@PortalLocaleResolver
@ApplicationScoped
@Alternative
@EqualsAndHashCode
@ToString
public class PortalLocaleResolverServiceMock implements LocaleResolverService {

    @Getter
    @Setter
    private Locale locale = Locale.ENGLISH;

    @Getter
    @Setter
    private List<Locale> availableLocales = immutableList(Locale.GERMAN, Locale.ENGLISH);

    @Inject
    @LocaleChangeEvent
    Event<Locale> localeChangeEvent;

    @Override
    public void saveUserLocale(final Locale newLocale) {
        if (!availableLocales.contains(newLocale)) {
            throw new IllegalArgumentException();
        }
        locale = newLocale;
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
        localeChangeEvent.fire(locale);
    }

}
