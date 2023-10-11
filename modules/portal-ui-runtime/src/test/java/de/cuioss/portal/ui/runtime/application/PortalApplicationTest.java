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
package de.cuioss.portal.ui.runtime.application;

import static de.cuioss.tools.collect.CollectionLiterals.mutableList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Locale;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.ApplicationWrapper;
import javax.faces.application.ProjectStage;
import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.configuration.impl.producer.PortalProjectStageImpl;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.runtime.application.configuration.LocaleConfiguration;
import de.cuioss.portal.ui.runtime.support.EnablePortalCoreEnvironment;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import lombok.Getter;
import lombok.Setter;

@EnablePortalCoreEnvironment
@AddBeanClasses({ LocaleConfiguration.class, PortalProjectStageImpl.class })
class PortalApplicationTest implements ShouldBeNotNull<PortalApplication>, JsfEnvironmentConsumer {

    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;

    @Inject
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    @Test
    void shouldHandleProjectProductionStage() {
        configuration.production();
        var wrapped = createFromFactory();
        assertEquals(ProjectStage.Production, wrapped.getProjectStage());
    }

    @Test
    void shouldHandleProjectDevelopmentStage() {
        configuration.development();
        var wrapped = createFromFactory();
        assertEquals(ProjectStage.Development, wrapped.getProjectStage());
    }

    @Test
    void shouldHandleProjectTestStage() {
        configuration.setPortalProjectStage(de.cuioss.portal.common.stage.ProjectStage.TEST);
        var wrapped = createFromFactory();
        assertEquals(ProjectStage.SystemTest, wrapped.getProjectStage());
    }

    @Test
    void shouldHandleSupportedLocales() {
        configuration.fireEvent(PortalConfigurationKeys.LOCALES_AVAILABLE, "de");
        var wrapped = createFromFactory();
        assertEquals(mutableList(Locale.GERMAN), mutableList(wrapped.getSupportedLocales()));
    }

    @Test
    void shouldHandleDefaultLocale() {
        configuration.fireEvent(PortalConfigurationKeys.LOCALE_DEFAULT, "fr");
        var wrapped = createFromFactory();
        assertEquals(Locale.FRENCH, wrapped.getDefaultLocale());
    }

    @Test
    void shouldProvideWrapped() {
        assertNotNull(((ApplicationWrapper) createFromFactory()).getWrapped());
    }

    private static Application createFromFactory() {
        var applicationFactory = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);

        var factory = new PortalApplicationFactory(applicationFactory);
        return factory.getApplication();
    }

    @Override
    public PortalApplication getUnderTest() {
        return new PortalApplication(getApplication());
    }
}
