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
package de.cuioss.portal.ui.runtime.support;

import de.cuioss.portal.common.bundle.PortalResourceBundleBean;
import de.cuioss.portal.common.bundle.ResourceBundleRegistry;
import de.cuioss.portal.common.bundle.ResourceBundleWrapper;
import de.cuioss.portal.ui.runtime.application.bundle.PortalDefaultResourceBundleLocator;
import de.cuioss.portal.ui.runtime.application.bundle.PortalVendorResourceBundleLocator;
import de.cuioss.portal.ui.test.mocks.PortalLocaleProducerMock;
import org.jboss.weld.junit5.auto.AddBeanClasses;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Using this annotation at type-level of a junit 5 test defines the basic
 * infrastructure for creating unit-tests containing the beans necessary for
 * actual resolving of resource-bundle content.
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
@AddBeanClasses({PortalDefaultResourceBundleLocator.class, PortalResourceBundleBean.class,
        PortalVendorResourceBundleLocator.class, ResourceBundleRegistry.class, ResourceBundleWrapper.class,
        PortalLocaleProducerMock.class})
public @interface EnableResourceBundleSupport {
}
