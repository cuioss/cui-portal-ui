package de.cuioss.portal.ui.runtime.application.storage;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.jboss.weld.junit5.auto.ActivateScopes;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;

import de.cuioss.portal.core.storage.PortalSessionStorage;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;

@EnableAutoWeld
@AddBeanClasses(PortalSessionStorageImpl.class)
@ActivateScopes(SessionScoped.class)
class PortalSessionStorageImplTest implements ShouldHandleObjectContracts<PortalSessionStorageImpl> {

    @Inject
    @PortalSessionStorage
    @Getter
    private PortalSessionStorageImpl underTest;

    // No more tests necessary

}
