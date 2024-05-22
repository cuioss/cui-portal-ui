package de.cuioss.portal.ui.runtime.application.bundle;

import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.inject.Inject;

import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeSerializable;
import lombok.Getter;

@EnableAutoWeld
class PortalDefaultResourceBundleLocatorTest implements ShouldBeSerializable<PortalDefaultResourceBundleLocator> {

    @Getter
    @Inject
    private PortalDefaultResourceBundleLocator underTest;

    @Test
    void shouldProvideOneBundle() {
        assertTrue(getUnderTest().getBundlePath().isPresent());
    }

}
