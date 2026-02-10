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
package de.cuioss.portal.ui.test.mocks;

import de.cuioss.portal.common.locale.LocaleChangeEvent;
import de.cuioss.portal.common.locale.PortalLocale;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;

/**
 * Mock version of {@link PortalLocale}
 *
 * @author Oliver Wolff
 */
@ApplicationScoped
@EqualsAndHashCode
@ToString
public class PortalLocaleProducerMock implements Serializable {

    @Serial
    private static final long serialVersionUID = 901932913924354093L;

    private final Event<Locale> localeChangeEvent;

    protected PortalLocaleProducerMock() {
        this.localeChangeEvent = null;
    }

    @Inject
    public PortalLocaleProducerMock(@LocaleChangeEvent Event<Locale> localeChangeEvent) {
        this.localeChangeEvent = localeChangeEvent;
    }
    @Getter
    @Setter
    @Produces
    @Dependent
    @PortalLocale
    private Locale locale = Locale.ENGLISH;
    @Getter
    @Setter
    private List<Locale> availableLocales = immutableList(Locale.GERMAN, Locale.ENGLISH);

}
