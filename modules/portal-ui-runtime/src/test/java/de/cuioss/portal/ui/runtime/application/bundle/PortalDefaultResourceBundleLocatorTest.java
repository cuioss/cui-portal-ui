package de.cuioss.portal.ui.runtime.application.bundle;

import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeSerializable;
import jakarta.inject.Inject;
import lombok.Getter;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
