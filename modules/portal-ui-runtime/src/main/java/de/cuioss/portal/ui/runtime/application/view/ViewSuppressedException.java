package de.cuioss.portal.ui.runtime.application.view;

import de.cuioss.jsf.api.common.view.ViewDescriptor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * To be thrown at if a view needs to be supressed.
 *
 * @author Oliver Wolff
 */
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class ViewSuppressedException extends RuntimeException {

    private static final long serialVersionUID = 7490217437815485262L;

    @Getter
    private final ViewDescriptor suppressedViewDescriptor;
}
