package de.cuioss.portal.ui.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 * Quite nonsense, coverage only
 */
class GlobalComponentIdsTest {

    @Test
    void eachShouldProvideIdentifier() {
        for (GlobalComponentIds ids : GlobalComponentIds.values()) {
            assertNotNull(ids.getId());
        }
    }

}
