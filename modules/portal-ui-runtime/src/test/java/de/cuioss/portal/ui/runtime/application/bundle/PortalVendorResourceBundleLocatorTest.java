package de.cuioss.portal.ui.runtime.application.bundle;

import de.cuioss.test.juli.junit5.EnableTestLogger;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import jakarta.inject.Inject;
import lombok.Getter;
import org.jboss.weld.junit5.auto.EnableAutoWeld;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertFalse;

@EnableAutoWeld
@EnableTestLogger
class PortalVendorResourceBundleLocatorTest implements ShouldHandleObjectContracts<PortalVendorResourceBundleLocator> {

    @Inject
    @Getter
    private PortalVendorResourceBundleLocator underTest;

    void shouldHandleMissingBundle() {
        assertFalse(underTest.getBundle(Locale.getDefault()).isPresent());
    }
}