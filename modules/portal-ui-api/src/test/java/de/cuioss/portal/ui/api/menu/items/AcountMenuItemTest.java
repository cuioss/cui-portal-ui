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

import javax.inject.Inject;
import javax.inject.Provider;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;

import de.cuioss.portal.core.test.junit5.EnablePortalConfiguration;
import de.cuioss.portal.ui.api.menu.PortalMenuItem;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;

@EnableAutoWeld
@AddBeanClasses(AccountMenuItem.class)
@EnablePortalConfiguration
class AcountMenuItemTest implements ShouldHandleObjectContracts<AccountMenuItem> {

    @Inject
    @PortalMenuItem
    private Provider<AccountMenuItem> underTestProvider;

    @Override
    public AccountMenuItem getUnderTest() {
        return underTestProvider.get();
    }

}
