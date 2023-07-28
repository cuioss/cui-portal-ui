package de.cuioss.portal.ui.runtime.application.listener.view.testhelper;

import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.ui.api.listener.view.ViewListener;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@SuppressWarnings("javadoc")
@EqualsAndHashCode
@ToString
public class ViewListenerCallRecorder implements ViewListener {

    private static final long serialVersionUID = -7770258964826877169L;

    @Getter
    private ViewDescriptor handledView;

    @Override
    public void handleView(final ViewDescriptor viewId) {
        this.handledView = viewId;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
