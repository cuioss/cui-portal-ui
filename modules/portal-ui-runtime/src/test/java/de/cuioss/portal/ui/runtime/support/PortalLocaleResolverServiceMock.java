/*
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.portal.ui.runtime.support;

import de.cuioss.portal.common.locale.LocaleChangeEvent;
import de.cuioss.portal.ui.api.locale.LocaleResolverService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.inject.Alternative;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Locale;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;

/**
 * Mock version of {@link LocaleResolverService}
 *
 * @author Oliver Wolff
 */
@ApplicationScoped
@Alternative
@EqualsAndHashCode
@ToString
public class PortalLocaleResolverServiceMock implements LocaleResolverService {

    @Inject
    @LocaleChangeEvent
    Event<Locale> localeChangeEvent;
    @Setter
    private Locale locale = Locale.ENGLISH;
    @Getter
    @Setter
    private List<Locale> availableLocales = immutableList(Locale.GERMAN, Locale.ENGLISH);

    @Override
    public void saveUserLocale(final Locale newLocale) {
        if (!availableLocales.contains(newLocale)) {
            throw new IllegalArgumentException();
        }
        locale = newLocale;
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
        localeChangeEvent.fire(locale);
    }

    @Override
    public Locale resolveUserLocale() {
        return locale;
    }

}
