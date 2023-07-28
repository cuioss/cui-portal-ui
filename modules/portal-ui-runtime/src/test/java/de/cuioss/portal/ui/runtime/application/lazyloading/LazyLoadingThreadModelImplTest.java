package de.cuioss.portal.ui.runtime.application.lazyloading;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Provider;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.configuration.initializer.PortalInitializer;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.api.ui.lazyloading.LazyLoadingRequest;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.mocks.PortalMessageProducerMock;
import de.cuioss.portal.ui.test.mocks.PortalStickyMessageProducerMock;
import de.cuioss.test.juli.junit5.EnableTestLogger;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import de.cuioss.tools.concurrent.ConcurrentTools;
import de.cuioss.uimodel.nameprovider.DisplayName;
import de.cuioss.uimodel.result.ResultDetail;
import de.cuioss.uimodel.result.ResultObject;
import de.cuioss.uimodel.result.ResultState;

@SuppressWarnings("rawtypes")
@EnablePortalUiEnvironment
@AddBeanClasses({ LazyLoadingViewModelImpl.class, PortalMessageProducerMock.class,
        PortalStickyMessageProducerMock.class })
@EnableTestLogger(trace = { LazyLoadingViewModelImpl.class, ThreadManager.class })
class LazyLoadingThreadModelImplTest implements ShouldHandleObjectContracts<LazyLoadingViewModelImpl> {

    @Inject
    private Provider<LazyLoadingViewModelImpl> underTestProvider;

    @Inject
    private LazyLoadingViewControllerImpl viewController;

    @Inject
    @PortalInitializer
    private ThreadManager threadManager;

    @Inject
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    @BeforeEach
    void cleanTestResult() {
        testResult = null;
    }

    @AfterEach
    void afterTest() {
        threadManager.destroy();
    }

    private String testResult;

    @Override
    public LazyLoadingViewModelImpl getUnderTest() {
        return underTestProvider.get();
    }

    @Test
    void testGoodCase() {
        configuration.put(PortalConfigurationKeys.PORTAL_LAZYLOADING_REQUEST_HANDLE_TIMEOUT, "30");
        configuration.put(PortalConfigurationKeys.PORTAL_LAZYLOADING_REQUEST_RETRIEVE_TIMEOUT, "30");
        configuration.fireEvent();

        threadManager.initialize();

        var underTest = underTestProvider.get();

        viewController.startRequest(
                createRequestWithResult(underTest.getRequestId(), new ResultObject<>("Test", ResultState.VALID)));

        underTest.processAction(null);

        assertEquals("Test", testResult);
        assertTrue(underTest.isInitialized());
        assertTrue(underTest.isRenderContent());
        assertNull(underTest.getNotificationBoxValue());
    }

    @Test
    void testError() {
        configuration.put(PortalConfigurationKeys.PORTAL_LAZYLOADING_REQUEST_HANDLE_TIMEOUT, "30");
        configuration.put(PortalConfigurationKeys.PORTAL_LAZYLOADING_REQUEST_RETRIEVE_TIMEOUT, "30");
        configuration.fireEvent();

        threadManager.initialize();

        var underTest = underTestProvider.get();

        viewController.startRequest(createRequestWithResult(underTest.getRequestId(),
                new ResultObject<>("", ResultState.ERROR, new ResultDetail(new DisplayName("Test")), null)));

        underTest.processAction(null);

        assertEquals("", testResult);
        assertTrue(underTest.isInitialized());
        assertFalse(underTest.isRenderContent());
        assertEquals(new DisplayName("Test"), underTest.getNotificationBoxValue());
    }

    @Test
    void testTimeoutRetrieve() {
        configuration.put(PortalConfigurationKeys.PORTAL_LAZYLOADING_REQUEST_HANDLE_TIMEOUT, "30");
        configuration.put(PortalConfigurationKeys.PORTAL_LAZYLOADING_REQUEST_RETRIEVE_TIMEOUT, "1");
        configuration.fireEvent();

        threadManager.initialize();

        var underTest = underTestProvider.get();

        viewController.startRequest(new LazyLoadingRequest<String>() {

            @Override
            public ResultObject<String> backendRequest() {
                ConcurrentTools.sleepUninterruptibly(2000, TimeUnit.MILLISECONDS);
                return new ResultObject<>("Test", ResultState.VALID);
            }

            @Override
            public void handleResult(String result) {
                testResult = result;
            }

            @Override
            public long getRequestId() {
                return underTest.getRequestId();
            }
        });

        underTest.processAction(null);

        assertNull(testResult);
        assertTrue(underTest.isInitialized());
        assertFalse(underTest.isRenderContent());
        assertNotNull(underTest.getNotificationBoxValue());
    }

    @Test
    void testTimeoutHandle() {
        configuration.put(PortalConfigurationKeys.PORTAL_LAZYLOADING_REQUEST_HANDLE_TIMEOUT, "1");
        configuration.put(PortalConfigurationKeys.PORTAL_LAZYLOADING_REQUEST_RETRIEVE_TIMEOUT, "1");
        configuration.fireEvent();

        threadManager.initialize();

        var underTest = underTestProvider.get();

        viewController.startRequest(
                createRequestWithResult(underTest.getRequestId(), new ResultObject<>("Test", ResultState.VALID)));

        ConcurrentTools.sleepUninterruptibly(3000, TimeUnit.MILLISECONDS);
        underTest.processAction(null);

        assertNull(testResult);
        assertTrue(underTest.isInitialized());
        assertFalse(underTest.isRenderContent());
        assertNotNull(underTest.getNotificationBoxValue());
    }

    private LazyLoadingRequest createRequestWithResult(final long requestId, final ResultObject resultObject) {
        return new LazyLoadingRequest<String>() {

            @Override
            public ResultObject<String> backendRequest() {
                return resultObject;
            }

            @Override
            public void handleResult(final String result) {
                testResult = result;
            }

            @Override
            public long getRequestId() {
                return requestId;
            }
        };
    }
}
