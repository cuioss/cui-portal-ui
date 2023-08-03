package de.cuioss.portal.ui.api.exception;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class ExceptionAsEvent implements Serializable {

    private static final long serialVersionUID = -1422214652443353900L;

    @Getter
    private HandleOutcome outcome;

    @Getter
    private Throwable exception;

    public ExceptionAsEvent(Throwable exception) {
        this.exception = exception;
        this.outcome = HandleOutcome.NO_OP;
    }

    /**
     * Sets the outcome of the handler
     * 
     * @param outcome must not be null
     * @return the object itself
     */
    public ExceptionAsEvent handled(HandleOutcome outcome) {
        this.outcome = outcome;
        return this;
    }

    /**
     * @return boolean indicating whether the contained exception was already
     *         handled
     */
    public boolean isHandled() {
        return HandleOutcome.NO_OP != outcome;
    }
}
