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
package de.cuioss.portal.ui.test.mocks;

import static de.cuioss.portal.ui.test.mocks.PortalHistoryManagerMock.VIEW_HOME;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import jakarta.inject.Inject;

import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import de.cuioss.jsf.api.common.view.ViewDescriptorImpl;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import lombok.Getter;

@EnableAutoWeld
class PortalHistoryManagerMockTest implements ShouldBeNotNull<PortalHistoryManagerMock> {

    public static final String CURRENT_VIEW_XHTML = "current/view.jsf";

    private final ViewDescriptorImpl currentView = ViewDescriptorImpl.builder().withViewId(CURRENT_VIEW_XHTML)
            .withLogicalViewId(CURRENT_VIEW_XHTML).build();

    public static final String OTHER_VIEW_XHTML = "current/other.jsf";

    private final ViewDescriptorImpl otherView = ViewDescriptorImpl.builder().withViewId(OTHER_VIEW_XHTML)
            .withLogicalViewId(OTHER_VIEW_XHTML).build();

    @Getter
    @Inject
    private PortalHistoryManagerMock underTest;

    @Test
    void shouldDefaultSensibly() {
        assertNotNull(underTest.getCurrentView());
        assertDoesNotThrow(() -> underTest.addCurrentUriToHistory(currentView));
        assertNotNull(underTest.iterator());
        assertNotNull(underTest.peekPrevious());
        assertNotNull(underTest.popPrevious());
    }

    @Test
    void shouldHandleStack() {
        underTest.addCurrentUriToHistory(otherView);
        underTest.addCurrentUriToHistory(currentView);
        assertEquals(OTHER_VIEW_XHTML, underTest.peekPrevious().getViewId());
        assertEquals(OTHER_VIEW_XHTML, underTest.popPrevious().getViewId());

        assertEquals(VIEW_HOME, underTest.peekPrevious().getViewId());
        assertEquals(VIEW_HOME, underTest.popPrevious().getViewId());
    }

}
