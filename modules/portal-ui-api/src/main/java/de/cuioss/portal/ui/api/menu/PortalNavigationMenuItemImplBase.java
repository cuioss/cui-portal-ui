package de.cuioss.portal.ui.api.menu;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.MENU_BASE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.inject.Inject;

import de.cuioss.jsf.api.components.model.menu.NavigationMenuItem;
import de.cuioss.jsf.api.components.support.LabelResolver;
import de.cuioss.portal.configuration.types.ConfigAsFilteredMap;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Base implementation for any cdi portal based {@link NavigationMenuItem}
 * implementation.
 */
@EqualsAndHashCode(callSuper = true)
public class PortalNavigationMenuItemImplBase extends PortalNavigationMenuConfigParser implements NavigationMenuItem {

    private static final long serialVersionUID = -7137939377092965593L;

    @Inject
    @ConfigAsFilteredMap(startsWith = MENU_BASE, stripPrefix = true)
    @Getter(value = AccessLevel.PROTECTED)
    private Map<String, String> menuConfig;

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String iconStyleClass;

    /** @deprecated use {@link #isRendered()} instead */
    @Getter
    @Setter
    @Deprecated
    private boolean disabled = false;

    @Getter
    @Setter
    private String titleKey;

    @Getter
    @Setter
    private String titleValue;

    @Getter
    @Setter
    private List<String> activeForAdditionalViewId = new ArrayList<>();

    @Override
    public int compareTo(final NavigationMenuItem other) {
        return getOrder().compareTo(other.getOrder());
    }

    @Override
    public String getResolvedTitle() {
        return getResolvedLabel();
    }

    @Override
    public boolean isTitleAvailable() {
        return null != getResolvedTitle();
    }

    @Getter
    @Setter
    private String labelKey;

    @Getter
    @Setter
    private String labelValue;

    /**
     * @return the resolved label
     */
    public String getResolvedLabel() {
        return LabelResolver.builder().withLabelKey(labelKey).withLabelValue(labelValue).build()
                .resolve(FacesContext.getCurrentInstance());
    }

}
