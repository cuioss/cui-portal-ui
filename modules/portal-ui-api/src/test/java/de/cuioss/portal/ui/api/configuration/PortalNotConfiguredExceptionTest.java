package de.cuioss.portal.ui.api.configuration;

import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;

class PortalNotConfiguredExceptionTest implements ShouldBeNotNull<PortalNotConfiguredException> {

    @Override
    public PortalNotConfiguredException getUnderTest() {
        return new PortalNotConfiguredException();
    }

}
