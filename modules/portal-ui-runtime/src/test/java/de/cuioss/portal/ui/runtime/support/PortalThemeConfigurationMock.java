package de.cuioss.portal.ui.runtime.support;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

import de.cuioss.jsf.test.mock.application.ThemeConfigurationMock;
import de.cuioss.portal.configuration.common.PortalPriorities;
import de.cuioss.portal.ui.api.theme.PortalThemeConfiguration;

/**
 * @author Oliver Wolff
 */
@PortalThemeConfiguration
@Priority(PortalPriorities.PORTAL_MODULE_LEVEL)
@Alternative
@ApplicationScoped
public class PortalThemeConfigurationMock extends ThemeConfigurationMock {

    private static final long serialVersionUID = 8282617475575490267L;

}
