package de.cuioss.portal.ui.runtime.application.dashboard.support;

import javax.enterprise.context.Dependent;

import de.cuioss.portal.ui.api.dashboard.PortalDashboardWidget;
import de.cuioss.uimodel.nameprovider.IDisplayNameProvider;
import de.cuioss.uimodel.nameprovider.LabeledKey;

@SuppressWarnings("javadoc")
@PortalDashboardWidget
@Dependent
public class TestWidgetWithoutId extends AbstractTestWidget {

    private static final long serialVersionUID = -9071842882930432524L;

    @Override
    protected void doInit() {
    }

    @Override
    public LabeledKey getNoItemsMessage() {
        return null;
    }

    @Override
    public IDisplayNameProvider<?> getTitle() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }
}
