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
package de.cuioss.portal.ui.runtime.application.lazyloading;

import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.generator.Generators;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import jakarta.inject.Inject;
import lombok.Getter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@EnablePortalUiEnvironment
class ThreadManagerTest implements ShouldHandleObjectContracts<ThreadManager> {

    @Inject
    @Getter
    private ThreadManager underTest;

    @Inject
    private PortalTestConfiguration configuration;

    @AfterEach
    void afterTest() {
        underTest.destroy();
    }

    @Test
    void handleGoodCase() throws ExecutionException, InterruptedException {
        configuration.fireEvent(PortalConfigurationKeys.PORTAL_LAZY_LOADING_REQUEST_HANDLE_TIMEOUT, "30");
        underTest.initialize();
        String id1 = Generators.nonBlankStrings().next();
        underTest.store(id1, () -> "Test", "A");
        assertNull(underTest.retrieve(Generators.nonBlankStrings().next()));
        var handle = underTest.retrieve(id1);
        assertNotNull(handle);
        assertEquals("A", handle.context());
        assertNotNull(handle.future());
        assertEquals("Test", handle.future().get());
        assertNull(underTest.retrieve(id1));
    }

    @Test
    @Disabled("""
        fails on CI because retrie(1) returns: \
        FutureHandle(future=java.util.concurrent.FutureTask@3ea4cfe0, context=A, timestamp=1625729207137)\
        """)
    void handleTimeout() throws InterruptedException {
        configuration.fireEvent(PortalConfigurationKeys.PORTAL_LAZY_LOADING_REQUEST_HANDLE_TIMEOUT, "1");
        underTest.initialize();
        String id1 = Generators.nonBlankStrings().next();
        underTest.store(id1, () -> "Test", "A");
        Thread.sleep(3000);
        assertNull(underTest.retrieve(id1));
    }
}
