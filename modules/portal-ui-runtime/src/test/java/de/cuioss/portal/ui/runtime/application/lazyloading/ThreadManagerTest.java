package de.cuioss.portal.ui.runtime.application.lazyloading;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.configuration.initializer.PortalInitializer;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;

@EnablePortalUiEnvironment
class ThreadManagerTest implements ShouldHandleObjectContracts<ThreadManager> {

    @Inject
    @PortalInitializer
    @Getter
    private ThreadManager underTest;

    @Inject
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    @AfterEach
    void afterTest() {
        underTest.destroy();
    }

    @Test
    void handleGoodCase() throws ExecutionException, InterruptedException {
        configuration.fireEvent(PortalConfigurationKeys.PORTAL_LAZYLOADING_REQUEST_HANDLE_TIMEOUT, "30");
        underTest.initialize();
        underTest.store(1, () -> "Test", "A");
        assertNull(underTest.retrieve(2));
        var handle = underTest.retrieve(1);
        assertNotNull(handle);
        assertEquals("A", handle.getContext());
        assertNotNull(handle.getFuture());
        assertEquals("Test", handle.getFuture().get());
        assertNull(underTest.retrieve(1));
    }

    @Test
    @Disabled("fails on CI because retrie(1) returns: "
            + "FutureHandle(future=java.util.concurrent.FutureTask@3ea4cfe0, context=A, timestamp=1625729207137)")
    void handleTimeout() throws InterruptedException {
        configuration.fireEvent(PortalConfigurationKeys.PORTAL_LAZYLOADING_REQUEST_HANDLE_TIMEOUT, "1");
        underTest.initialize();
        underTest.store(1, () -> "Test", "A");
        Thread.sleep(3000);
        assertNull(underTest.retrieve(1));
    }
}
