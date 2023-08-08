package de.cuioss.portal.ui.runtime.application.listener.view;

import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.DESCRIPTOR_LOGIN;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Provider;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.configuration.application.ProjectStage;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.api.configuration.PortalNotConfiguredException;
import de.cuioss.portal.ui.api.exception.ExceptionAsEvent;
import de.cuioss.portal.ui.api.listener.view.PhaseExecution;
import de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;

@EnablePortalUiEnvironment
@AddBeanClasses({ PortalStageModeListener.class })
class PortalStageModeListenerTest implements ShouldHandleObjectContracts<PortalStageModeListener> {

    @Inject
    @PortalRestoreViewListener(PhaseExecution.AFTER_PHASE)
    private Provider<PortalStageModeListener> underTest;

    @Inject
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    private boolean exceptionWasThrown = false;

    @Test
    void shouldThrowPortalNotConfiguredException() {
        configuration.setPortalProjectStage(ProjectStage.CONFIGURATION);
        getUnderTest().handleView(DESCRIPTOR_LOGIN);
        assertTrue(exceptionWasThrown);
    }

    @Test
    void shouldNotThrowPortalNotConfiguredException() {
        getUnderTest().handleView(DESCRIPTOR_LOGIN);
        assertFalse(exceptionWasThrown); // default is production
    }

    void actOnExceptionToCatchEvent(@Observes final ExceptionAsEvent catchEvent) {
        final var exception = catchEvent.getException();
        exceptionWasThrown = exception instanceof PortalNotConfiguredException;
    }

    @Override
    public PortalStageModeListener getUnderTest() {
        return underTest.get();
    }
}
