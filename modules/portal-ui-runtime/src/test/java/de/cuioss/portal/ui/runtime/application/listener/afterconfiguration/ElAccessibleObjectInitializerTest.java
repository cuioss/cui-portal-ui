package de.cuioss.portal.ui.runtime.application.listener.afterconfiguration;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;

import de.cuioss.portal.configuration.impl.producer.PortalProjectStageImpl;
import de.cuioss.portal.configuration.initializer.PortalInitializer;
import de.cuioss.portal.ui.runtime.application.templating.PortalViewMapper;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import lombok.Getter;

@EnableAutoWeld
@AddBeanClasses({ PortalViewMapper.class, PortalProjectStageImpl.class })
class ElAccessibleObjectInitializerTest implements ShouldBeNotNull<ElAccessibleObjectInitializer> {

    @Getter
    @Inject
    @PortalInitializer
    private ElAccessibleObjectInitializer underTest;

}
