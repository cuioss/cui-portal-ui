package de.cuioss.portal.ui.runtime.application.view;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.ui.api.ui.context.CuiCurrentView;
import de.cuioss.portal.ui.runtime.application.configuration.ViewConfiguration;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Managed bean bridging the view with instances of
 *
 * @author Oliver Wolff
 */
@Named(ViewTransientManagerBean.BEAN_NAME)
@RequestScoped
@EqualsAndHashCode(of = "transientView", doNotUseGetters = true)
@ToString(of = "transientView", doNotUseGetters = true)
public class ViewTransientManagerBean implements Serializable {

    /**
     * Bean name for looking up instances.
     */
    static final String BEAN_NAME = "viewTransientManager";

    private static final long serialVersionUID = -8225392922526412945L;

    @Getter
    private boolean transientView = false;

    @Inject
    private ViewConfiguration viewConfiguration;

    @Inject
    @CuiCurrentView
    private Provider<ViewDescriptor> currentViewProvider;

    /**
     * Initializes the bean. See class documentation for expected result.
     */
    @PostConstruct
    public void initBean() {
        final var viewDescriptor = currentViewProvider.get();
        if (viewDescriptor.isViewDefined()) {
            transientView = viewConfiguration.getTransientViewMatcher().match(viewDescriptor);
        }
    }
}
