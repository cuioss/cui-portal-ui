package de.cuioss.portal.ui.runtime.application.dashboard.support;

import javax.enterprise.context.Dependent;

import de.cuioss.portal.ui.api.dashboard.PortalDashboardWidget;
import de.cuioss.uimodel.nameprovider.IDisplayNameProvider;
import de.cuioss.uimodel.nameprovider.LabeledKey;

@PortalDashboardWidget
@Dependent
public class TestWidgetWithIdB extends AbstractTestWidget {

    private static final long serialVersionUID = 1877752919399748846L;

    @Override
    public String getId() {
        return "B";
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
