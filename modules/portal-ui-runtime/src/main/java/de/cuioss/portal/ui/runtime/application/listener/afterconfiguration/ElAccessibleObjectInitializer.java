package de.cuioss.portal.ui.runtime.application.listener.afterconfiguration;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import de.cuioss.portal.configuration.initializer.ApplicationInitializer;
import de.cuioss.portal.configuration.initializer.PortalInitializer;
import de.cuioss.portal.ui.api.templating.MultiViewMapper;
import de.cuioss.portal.ui.api.templating.PortalMultiViewMapper;

/**
 * See package javadoc for details
 *
 * @author Oliver Wolff
 */
@PortalInitializer
@Dependent
public class ElAccessibleObjectInitializer implements ApplicationInitializer {

    @Inject
    @PortalMultiViewMapper
    private MultiViewMapper multiViewMapper;

    @Override
    public void initialize() {
        multiViewMapper.toString();
    }

    @Override
    public Integer getOrder() {
        return ApplicationInitializer.ORDER_LATE;
    }
}
