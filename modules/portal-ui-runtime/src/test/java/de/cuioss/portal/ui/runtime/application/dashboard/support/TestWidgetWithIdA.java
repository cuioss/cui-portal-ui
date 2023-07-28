package de.cuioss.portal.ui.runtime.application.dashboard.support;

import javax.enterprise.context.Dependent;

import de.cuioss.portal.ui.api.dashboard.PortalDashboardWidget;
import de.cuioss.uimodel.nameprovider.IDisplayNameProvider;
import de.cuioss.uimodel.nameprovider.LabeledKey;

@PortalDashboardWidget
@Dependent
public class TestWidgetWithIdA extends AbstractTestWidget {

    private static final long serialVersionUID = -7473374640743772845L;

    @Override
    public String getId() {
        return "A";
    }

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
}
