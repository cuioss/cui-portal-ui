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
package de.cuioss.portal.ui.test.configuration;

import java.util.Collections;

import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.jsf.api.common.view.ViewDescriptorImpl;
import de.cuioss.portal.ui.api.ui.pages.ErrorPage;
import de.cuioss.portal.ui.api.ui.pages.HomePage;
import de.cuioss.portal.ui.api.ui.pages.LoginPage;
import de.cuioss.portal.ui.api.ui.pages.LogoutPage;
import de.cuioss.portal.ui.api.ui.pages.PreferencesPage;
import de.cuioss.test.jsf.config.ApplicationConfigurator;
import de.cuioss.test.jsf.config.decorator.ApplicationConfigDecorator;

/**
 * Provides methods and structure to mock the portal specific navigation.
 *
 * @author Oliver Wolff
 */
public class PortalNavigationConfiguration implements ApplicationConfigurator {

    /** The home view */
    public static final String VIEW_HOME_LOGICAL_VIEW_ID = "/portal/home.jsf";

    /** The home view */
    public static final String VIEW_HOME_VIEW_ID = "/portal/home.xhtml";

    /** The login view */
    public static final String VIEW_LOGIN_LOGICAL_VIEW_ID = "/guest/login.jsf";

    /** The login view */
    public static final String VIEW_LOGIN_VIEW_ID = "/guest/login.xhtml";

    /** The logout view */
    public static final String VIEW_LOGOUT_LOGICAL_VIEW_ID = "/guest/logout.jsf";

    /** The logout view */
    public static final String VIEW_LOGOUT_VIEW_ID = "/guest/logout.xhtml";

    /** The preferences view */
    public static final String VIEW_PREFERENCES_LOGICAL_VIEW_ID = "/portal/preferences.jsf";

    /** The preferences view */
    public static final String VIEW_PREFERENCES_VIEW_ID = "/portal/preferences.xhtml";

    /** The error view */
    public static final String VIEW_ERROR_LOGICAL_VIEW_ID = "/guest/error.jsf";

    /** The error view */
    public static final String VIEW_ERROR_VIEW_ID = "/guest/error.xhtml";

    /** A non existing view */
    public static final String VIEW_NOT_THERE_LOGICAL_VIEW_ID = "/not/there.jsf";

    /** A non existing view */
    public static final String VIEW_NOT_THERE_VIEW_ID = "/not/there.xhtml";

    /**
     * {@link ViewDescriptor}, representing home page.
     */
    public static final ViewDescriptor DESCRIPTOR_HOME = ViewDescriptorImpl.builder().withViewId(VIEW_HOME_VIEW_ID)
            .withUrlParameter(Collections.emptyList()).withLogicalViewId(VIEW_HOME_LOGICAL_VIEW_ID).build();

    /**
     * {@link ViewDescriptor}, representing login page.
     */
    public static final ViewDescriptor DESCRIPTOR_LOGIN = ViewDescriptorImpl.builder().withViewId(VIEW_LOGIN_VIEW_ID)
            .withUrlParameter(Collections.emptyList()).withLogicalViewId(VIEW_LOGIN_LOGICAL_VIEW_ID).build();

    /**
     * {@link ViewDescriptor}, representing preferences page.
     */
    public static final ViewDescriptor DESCRIPTOR_PREFERENCES = ViewDescriptorImpl.builder()
            .withViewId(VIEW_PREFERENCES_VIEW_ID).withUrlParameter(Collections.emptyList())
            .withLogicalViewId(VIEW_PREFERENCES_LOGICAL_VIEW_ID).build();

    /**
     * {@link ViewDescriptor}, representing error page.
     */
    public static final ViewDescriptor DESCRIPTOR_ERROR = ViewDescriptorImpl.builder().withViewId(VIEW_ERROR_VIEW_ID)
            .withUrlParameter(Collections.emptyList()).withLogicalViewId(VIEW_ERROR_LOGICAL_VIEW_ID).build();

    /**
     * {@link ViewDescriptor}, representing home page.
     */
    public static final ViewDescriptor DESCRIPTOR_NOT_THERE = ViewDescriptorImpl.builder()
            .withViewId(VIEW_NOT_THERE_VIEW_ID).withUrlParameter(Collections.emptyList())
            .withLogicalViewId(VIEW_NOT_THERE_LOGICAL_VIEW_ID).build();

    @Override
    public void configureApplication(final ApplicationConfigDecorator decorator) {
        decorator.registerNavigationCase(HomePage.OUTCOME, VIEW_HOME_LOGICAL_VIEW_ID)
                .registerNavigationCase(LoginPage.OUTCOME, VIEW_LOGIN_LOGICAL_VIEW_ID)
                .registerNavigationCase(ErrorPage.OUTCOME, VIEW_ERROR_LOGICAL_VIEW_ID)
                .registerNavigationCase(PreferencesPage.OUTCOME, VIEW_PREFERENCES_LOGICAL_VIEW_ID)
                .registerNavigationCase(LogoutPage.OUTCOME, VIEW_LOGOUT_LOGICAL_VIEW_ID);
    }
}
