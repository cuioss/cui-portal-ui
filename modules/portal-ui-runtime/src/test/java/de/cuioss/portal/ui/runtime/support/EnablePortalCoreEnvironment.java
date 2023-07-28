package de.cuioss.portal.ui.runtime.support;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;

import org.jboss.weld.junit5.auto.ActivateScopes;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;

import de.cuioss.portal.configuration.impl.producer.PortalProjectStageImpl;
import de.cuioss.portal.core.test.junit5.EnablePortalConfiguration;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.mocks.PortalMirrorResourceBundle;
import de.cuioss.test.jsf.config.JsfTestConfiguration;
import de.cuioss.test.jsf.defaults.BasicApplicationConfiguration;
import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;

/**
 * Variant of {@link EnablePortalUiEnvironment} that will <em>not</em> register
 * a {@link PortalMirrorResourceBundle}. In case you want to add the runtime
 * bundles you can add {@link EnableResourceBundleSupport} as well
 *
 * @author Oliver Wolff
 *
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
@EnableAutoWeld
@EnablePortalConfiguration
@EnableJsfEnvironment
@JsfTestConfiguration(BasicApplicationConfiguration.class)
@AddBeanClasses({ PortalProjectStageImpl.class })
@ActivateScopes({ RequestScoped.class, SessionScoped.class, ConversationScoped.class, ViewScoped.class })
public @interface EnablePortalCoreEnvironment {
}
