package de.cuioss.portal.ui.runtime.application.bundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.core.bundle.PortalResourceBundle;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.api.locale.LocaleChangeEvent;
import de.cuioss.portal.ui.runtime.support.EnablePortalCoreEnvironment;
import de.cuioss.portal.ui.runtime.support.EnableResourceBundleSupport;
import de.cuioss.portal.ui.test.mocks.PortalLocaleProducerMock;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;

@EnablePortalCoreEnvironment
@EnableResourceBundleSupport
@AddBeanClasses({ InstallationResourceBundleWrapperMock.class })
class PortalResourceBundleBeanTest implements ShouldHandleObjectContracts<PortalResourceBundleBean> {

    @Inject
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    @Inject
    @PortalResourceBundle
    @Getter
    private PortalResourceBundleBean underTest;

    @Inject
    private PortalLocaleProducerMock portalLocaleProducerMock;

    @Inject
    @LocaleChangeEvent
    Event<Locale> localeChangeEvent;

    @Test
    void testGetMessage() {
        // InstallationResourceBundleWrapperMock
        assertEquals("Test", underTest.getString("page.error.title"));
        // portal_messages
        assertEquals("Internal server error", underTest.getString("page.error.srHeader"));
        // vendor_messages
        assertEquals(PortalResourceBundleWrapperTest.ICW_PORTAL, underTest.getString("portal.title"));
        // cui
        assertEquals("Provide a value", underTest.getString("message.error.validation.value.required"));
    }

    @Test
    void shouldSwitchMessageBundleOnLocaleChange() {
        assertEquals("Provide a value", underTest.getString("message.error.validation.value.required"));
        portalLocaleProducerMock.setLocale(Locale.GERMAN);
        localeChangeEvent.fire(Locale.GERMAN);

        assertEquals("Wert eingeben", underTest.getString("message.error.validation.value.required"));
    }

    @Test
    void shouldFailOnInvalidKey() {
        configuration.development();
        assertThrows(MissingResourceException.class, () -> {
            underTest.getString("not.there");
        });
    }

    @Test
    void testGetKeys() {
        final List<String> keys = Collections.list(underTest.getKeys());
        assertNotNull(keys);
        assertTrue(keys.size() > 60);
    }
}
