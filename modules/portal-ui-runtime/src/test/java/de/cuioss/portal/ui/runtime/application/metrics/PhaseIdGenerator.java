package de.cuioss.portal.ui.runtime.application.metrics;

import static de.cuioss.test.generator.Generators.fixedValues;

import javax.faces.event.PhaseId;

import de.cuioss.test.generator.TypedGenerator;

/**
 * @author Oliver Wolff
 *
 */
public class PhaseIdGenerator implements TypedGenerator<PhaseId> {

    private static final TypedGenerator<PhaseId> PHASES = fixedValues(PhaseId.VALUES);

    @Override
    public PhaseId next() {
        return PHASES.next();
    }

    @Override
    public Class<PhaseId> getType() {
        return PhaseId.class;
    }
}
