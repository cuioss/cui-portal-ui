package de.cuioss.portal.ui.components;

import org.junit.jupiter.api.Disabled;

import de.cuioss.portal.core.test.tests.BaseModuleConsistencyTest;

/**
 * Tests the complete cdi environment / wiring
 *
 * @author Oliver Wolff
 */
class ModuleConsistencyTest extends BaseModuleConsistencyTest {

    @Override
    @Disabled("Currently there is the need to portal-core-impl modules. This needs to be fixed: PortalHistoryManager")
    protected void shouldStartUpContainer() {
    }
}
