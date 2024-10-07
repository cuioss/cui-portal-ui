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
package de.cuioss.portal.ui.test.tests;

import de.cuioss.jsf.api.components.model.menu.NavigationMenuItem;
import de.cuioss.portal.ui.api.menu.PortalMenuItem;
import de.cuioss.test.valueobjects.api.object.VetoType;
import de.cuioss.tools.reflect.MoreReflection;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static de.cuioss.tools.collect.CollectionLiterals.mutableList;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Test utility class for NavigationMenuItem tests. Runs object contract tests
 * and null checks.
 *
 * <h2>Usage</h2>
 * <p>
 * Extend your concrete module navigation menu test class with this class. Use
 * {@linkplain AddBeanClasses} to add your navigation menu item classes that
 * should be tested.
 * </p>
 * <p>
 * In case you want to Veto / exclude some types from test you can use
 * {@code @VetoType} for doing so.
 * </p>
 */
public class PortalNavigationMenuItemsTest {

    @Inject
    @PortalMenuItem
    private Instance<NavigationMenuItem> instances;

    @Test
    protected void instancesShouldFullfillBasicContracts() {
        var filteredInstances = getFilteredInstances();
        assertFalse(filteredInstances.isEmpty(),
                "No instance to be checked: Adapt you test or use a different base-class");
        filteredInstances.forEach(NavigationMenuItemAsserts::assertBasicAttributes);
    }

    /**
     * @return a filtered {@link List} of {@link NavigationMenuItem} containing all
     * injected {@link NavigationMenuItem} annotated with
     * {@link PortalMenuItem} and not being vetoed by {@link VetoType}
     */
    public List<NavigationMenuItem> getFilteredInstances() {
        Optional<VetoType> vetoAnnotationOptional = MoreReflection.extractAnnotation(this.getClass(), VetoType.class);
        final List<Class<?>> vetoedClasses = mutableList();
        vetoAnnotationOptional.ifPresent(vetoType -> vetoedClasses.addAll(mutableList(vetoType.value())));
        return instances.stream().filter(item -> !vetoedClasses.contains(item.getClass())).toList();
    }
}
