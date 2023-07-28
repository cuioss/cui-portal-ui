package de.cuioss.portal.ui.runtime.support;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.jboss.weld.junit5.auto.AddBeanClasses;

import de.cuioss.portal.configuration.impl.bundles.PortalDefaultResourceBundleLocator;
import de.cuioss.portal.configuration.impl.bundles.PortalVendorResourceBundleLocator;
import de.cuioss.portal.configuration.impl.bundles.ResourceBundleRegistryImpl;
import de.cuioss.portal.ui.runtime.application.bundle.PortalResourceBundleBean;
import de.cuioss.portal.ui.runtime.application.bundle.PortalResourceBundleWrapper;
import de.cuioss.portal.ui.test.mocks.PortalLocaleProducerMock;

/**
 * Using this annotations at type-level of a junit 5 test defines the basic
 * infrastructure for creating unit-tests containing the beans necessary for
 * actual resolving of resource-bundle content.
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
@AddBeanClasses({ PortalDefaultResourceBundleLocator.class, PortalResourceBundleBean.class,
        PortalVendorResourceBundleLocator.class, ResourceBundleRegistryImpl.class, PortalResourceBundleWrapper.class,
        PortalLocaleProducerMock.class })
public @interface EnableResourceBundleSupport {
}
