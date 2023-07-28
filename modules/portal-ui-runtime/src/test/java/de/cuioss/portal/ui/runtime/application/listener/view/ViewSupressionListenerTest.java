package de.cuioss.portal.ui.runtime.application.listener.view;

import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.DESCRIPTOR_HOME;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.DESCRIPTOR_LOGIN;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.DESCRIPTOR_NOT_THERE;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.exception.control.event.ExceptionToCatchEvent;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.api.listener.view.PhaseExecution;
import de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener;
import de.cuioss.portal.ui.runtime.application.view.ViewSuppressedException;
import de.cuioss.portal.ui.runtime.application.view.matcher.ViewMatcherProducer;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;

@EnablePortalUiEnvironment
@AddBeanClasses({ ViewMatcherProducer.class })
class ViewSupressionListenerTest implements ShouldHandleObjectContracts<ViewSupressionListener> {

    @Inject
    @PortalRestoreViewListener(PhaseExecution.AFTER_PHASE)
    @Getter
    private ViewSupressionListener underTest;

    @Inject
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    private ViewSuppressedException viewSuppressedException = null;

    @Test
    void shouldAlwaysReturnFalseOnDefault() {
        underTest.handleView(DESCRIPTOR_HOME);
        underTest.handleView(DESCRIPTOR_NOT_THERE);
    }

    @Test
    void shouldThrowViewSuppressedException() {
        assertNull(viewSuppressedException);
        configuration.fireEvent(PortalConfigurationKeys.SUPPRESSED_VIEWS, "/");
        underTest.handleView(DESCRIPTOR_LOGIN);
        assertNotNull(viewSuppressedException);
    }

    void actOnExceptionToCatchEvent(@Observes final ExceptionToCatchEvent catchEvent) {
        viewSuppressedException = (ViewSuppressedException) catchEvent.getException();
    }

}
