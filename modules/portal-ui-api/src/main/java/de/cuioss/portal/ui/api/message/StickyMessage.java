package de.cuioss.portal.ui.api.message;

import java.io.Serializable;

import de.cuioss.jsf.api.components.css.ContextState;
import de.cuioss.uimodel.nameprovider.IDisplayNameProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * A sticky message consisting of dismissable flag, message string and state.
 *
 * @author Matthias Walliczek
 */
@Builder
@Data
@AllArgsConstructor
public class StickyMessage implements Serializable {

    private static final long serialVersionUID = -3226075374956046365L;

    /**
     * if true, message could be removed by UI interaction
     */
    private final boolean dismissable;

    /**
     * {@linkplain ContextState} is required
     */
    @NonNull
    private final ContextState state;

    /**
     * Message content as {@linkplain IDisplayNameProvider} is required
     */
    @NonNull
    private final IDisplayNameProvider<?> message;

}
