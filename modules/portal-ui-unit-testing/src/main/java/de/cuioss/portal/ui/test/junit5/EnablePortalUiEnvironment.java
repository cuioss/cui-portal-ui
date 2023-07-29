package de.cuioss.portal.ui.test.junit5;

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

import de.cuioss.jsf.test.mock.application.MirrorCuiRessourcBundle;
import de.cuioss.portal.configuration.impl.producer.PortalProjectStageImpl;
import de.cuioss.portal.core.test.junit5.EnablePortalConfiguration;
import de.cuioss.portal.ui.api.ui.context.CurrentViewProducer;
import de.cuioss.portal.ui.api.ui.context.NavigationHandlerProducer;
import de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration;
import de.cuioss.portal.ui.test.mocks.PortalLocaleProducerMock;
import de.cuioss.portal.ui.test.mocks.PortalMessageProducerMock;
import de.cuioss.portal.ui.test.mocks.PortalMirrorResourceBundle;
import de.cuioss.portal.ui.test.mocks.PortalStickyMessageProducerMock;
import de.cuioss.test.jsf.config.JsfTestConfiguration;
import de.cuioss.test.jsf.defaults.BasicApplicationConfiguration;
import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import de.cuioss.test.jsf.producer.JsfObjectsProducers;

/**
 * Using this annotations at type-level of a junit 5 test defines the basic
 * infrastructure for creating unit-tests in the portal contexts. It is a
 * meta-annotation consisting of:
 *
 * <ul>
 * <li>{@link EnableAutoWeld}</li>
 * <li>{@link EnablePortalConfiguration}</li>
 * <li>{@link EnableJsfEnvironment}</li>
 * <li>{@link JsfTestConfiguration} with {@link BasicApplicationConfiguration}</
 * li>
 * </ul>
 * <p>
 * In addition it adds the {@link JsfObjectsProducers},
 * {@link PortalProjectStageImpl}, {@link PortalStickyMessageProducerMock},
 * {@link PortalMessageProducerMock} and {@link PortalMirrorResourceBundle}
 * using {@link AddBeanClasses}
 * </p>
 * <p>
 * It explicitly activates the Scopes:
 * <li>{@link RequestScoped}</li>
 * <li>{@link SessionScoped}</li>
 * <li>{@link ConversationScoped}</li>
 * <li>{@link ViewScoped}</li>
 * </p>
 * <p>
 * As a result it bootstraps a fully enabled cdi / portal / jsf container that
 * handles configuration as well.
 * </p>
 *
 * @author Oliver Wolff
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
@EnableAutoWeld
@EnablePortalConfiguration
@EnableJsfEnvironment
@JsfTestConfiguration({ BasicApplicationConfiguration.class, MirrorCuiRessourcBundle.class,
        PortalNavigationConfiguration.class })
@AddBeanClasses({ PortalProjectStageImpl.class, PortalMirrorResourceBundle.class, PortalLocaleProducerMock.class,
        PortalStickyMessageProducerMock.class, PortalMessageProducerMock.class, JsfObjectsProducers.class,
        CurrentViewProducer.class, NavigationHandlerProducer.class })
@ActivateScopes({ RequestScoped.class, SessionScoped.class, ConversationScoped.class, ViewScoped.class })
public @interface EnablePortalUiEnvironment {
}
