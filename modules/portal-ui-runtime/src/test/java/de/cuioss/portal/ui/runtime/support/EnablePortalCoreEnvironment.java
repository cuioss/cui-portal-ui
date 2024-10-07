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

import de.cuioss.portal.configuration.impl.producer.PortalProjectStageImpl;
import de.cuioss.portal.core.test.junit5.EnablePortalConfiguration;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.mocks.PortalMirrorResourceBundle;
import de.cuioss.test.jsf.config.JsfTestConfiguration;
import de.cuioss.test.jsf.defaults.BasicApplicationConfiguration;
import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import jakarta.enterprise.context.ConversationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.view.ViewScoped;
import org.jboss.weld.junit5.auto.ActivateScopes;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

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
