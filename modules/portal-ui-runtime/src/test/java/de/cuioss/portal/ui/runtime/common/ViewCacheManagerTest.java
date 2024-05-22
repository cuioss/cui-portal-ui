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
package de.cuioss.portal.ui.runtime.common;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;
import org.omnifaces.util.cache.CacheFactory;

import de.cuioss.portal.ui.api.events.PageRefreshEvent;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;
import lombok.Setter;

@EnablePortalUiEnvironment
class ViewCacheManagerTest implements ShouldHandleObjectContracts<ViewCacheManager>, JsfEnvironmentConsumer {

    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;

    @Inject
    @Getter
    private ViewCacheManager underTest;

    @Inject
    private Event<PageRefreshEvent> event;

    @Test

    void testReset() {
        CacheFactory.getCache(getFacesContext(), "session").put("header", "<h1>Header</h1>");
        assertNotNull(CacheFactory.getCache(getFacesContext(), "session").get("header"));
        event.fire(new PageRefreshEvent("test"));
        assertNull(CacheFactory.getCache(getFacesContext(), "session").get("header"));
    }
}
