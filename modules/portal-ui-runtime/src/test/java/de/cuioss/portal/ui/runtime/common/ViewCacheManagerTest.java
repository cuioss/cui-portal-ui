package de.cuioss.portal.ui.runtime.common;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import javax.enterprise.event.Event;
import javax.inject.Inject;

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
