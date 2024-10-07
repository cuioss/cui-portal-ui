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
package de.cuioss.portal.ui.api.menu.items;

import de.cuioss.portal.core.test.junit5.EnablePortalConfiguration;
import de.cuioss.portal.core.test.mocks.authentication.PortalTestUserProducer;
import de.cuioss.portal.ui.api.menu.PortalMenuItem;
import de.cuioss.portal.ui.api.test.support.PortalResourceBundleMock;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;

@EnableAutoWeld
@AddBeanClasses({ UserMenuItem.class, PortalTestUserProducer.class, PortalResourceBundleMock.class })
@EnablePortalConfiguration
class UserMenuItemTest implements ShouldHandleObjectContracts<UserMenuItem> {

    @Inject
    @PortalMenuItem
    private Provider<UserMenuItem> underTestProvider;

    @Override
    public UserMenuItem getUnderTest() {
        return underTestProvider.get();
    }

}
