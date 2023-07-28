package de.cuioss.portal.ui.api.message;

import java.io.Serializable;
import java.util.Set;

/**
 * A provider of StickyMessages. Each class which implement this get collected
 * by StickyMessageCollectorViewListener, which does add the collected messages
 * to session scoped bean named stickyMessageProducer
 *
 * @author i000576
 */
public interface StickyMessageProvider extends Serializable {

    /**
     * Retrieve StickyMessages which should be added to StickyMessageProducer.
     *
     * @return set which could be empty but never null.
     */
    Set<StickyMessage> retrieveMessages();

}
