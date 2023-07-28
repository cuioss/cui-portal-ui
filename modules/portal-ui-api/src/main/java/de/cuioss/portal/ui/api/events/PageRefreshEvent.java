package de.cuioss.portal.ui.api.events;

import java.io.Serializable;

import lombok.Data;

/**
 * Page refresh event payload.
 *
 * @author i000576
 */
@Data
public class PageRefreshEvent implements Serializable {

    private static final long serialVersionUID = 3367481133773296933L;

    /**
     * the String based identifier of the current view-id. It ends on the real
     * (physical) suffix, e.g. .xhtml
     */
    private final String viewId;

}
