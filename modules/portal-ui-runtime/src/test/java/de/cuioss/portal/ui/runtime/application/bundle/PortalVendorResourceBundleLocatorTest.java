/*
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.portal.ui.runtime.application.bundle;

import de.cuioss.test.juli.LogAsserts;
import de.cuioss.test.juli.TestLogLevel;
import de.cuioss.test.juli.junit5.EnableTestLogger;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import jakarta.inject.Inject;
import lombok.Getter;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertFalse;

@EnableAutoWeld
@EnableTestLogger
class PortalVendorResourceBundleLocatorTest implements ShouldHandleObjectContracts<PortalVendorResourceBundleLocator> {

    @Inject
    @Getter
    private PortalVendorResourceBundleLocator underTest;

    @Test
    void shouldHandleMissingBundle() {
        assertFalse(underTest.getBundle(Locale.getDefault()).isPresent());
        LogAsserts.assertLogMessagePresentContaining(TestLogLevel.INFO, "PORTAL-UI-RT-3");
    }
}