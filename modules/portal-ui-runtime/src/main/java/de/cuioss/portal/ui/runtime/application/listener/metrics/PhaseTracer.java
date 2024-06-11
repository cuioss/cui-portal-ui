/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.portal.ui.runtime.application.listener.metrics;

import static java.util.Objects.requireNonNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import jakarta.faces.event.PhaseId;

import de.cuioss.tools.concurrent.StopWatch;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.tools.string.MoreStrings;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Helper Object aggregating a {@link PhaseId} and a {@link StopWatch}
 *
 * @author Oliver Wolff
 *
 */
@EqualsAndHashCode(of = { "phaseOrdinal", "phaseName", "didRun" })
public class PhaseTracer implements Serializable, Comparable<PhaseTracer> {

    @Serial
    private static final long serialVersionUID = -8784042523143046556L;

    private static final CuiLogger log = new CuiLogger(PhaseTracer.class);

    @Getter
    private final Integer phaseOrdinal;

    @Getter
    private final String phaseName;

    @Getter(AccessLevel.PACKAGE)
    private final StopWatch stopWatch;

    /** Boolean indicating whether the timer for this phase was run at all. */
    @Getter
    private boolean didRun;

    /**
     * Creates a new Instance containing an unstarted timer
     *
     * @param phaseId must not be null
     */
    public PhaseTracer(PhaseId phaseId) {
        this(phaseId, StopWatch.createUnstarted());
    }

    /**
     * @param phaseId   identifying the {@link PhaseId} to be tracked, must not be
     *                  null
     * @param stopWatch to be used for stopping the time, must not be null.
     */
    public PhaseTracer(PhaseId phaseId, StopWatch stopWatch) {
        requireNonNull(phaseId);
        requireNonNull(stopWatch);
        phaseOrdinal = phaseId.getOrdinal();
        phaseName = phaseId.getName();
        this.stopWatch = stopWatch;
        didRun = false;
    }

    /**
     * Starts the timer for the contained phase. It implicitly checks whether it is
     * already running-
     *
     * @return The instance itself to enable a fluent-like access
     */
    public PhaseTracer start() {
        didRun = true;
        if (stopWatch.isRunning()) {
            log.warn("IllegalState: Must only be started once, phaseName='%s'", phaseName);
        } else {
            log.trace("Starting tracing for phase='%s'", phaseName);
            stopWatch.start();
        }
        return this;
    }

    /**
     * Stops the timer for the contained phase. It implicitly checks whether it is
     * already running.
     *
     * @return The instance itself to enable a fluent-like access
     */
    public PhaseTracer stop() {
        if (stopWatch.isRunning()) {
            log.trace("Stoping tracing for phase='%s'", phaseName);
            stopWatch.stop();
        } else {
            log.warn("IllegalState: Must ony be started once, phase='%s'", phaseName);
        }
        return this;
    }

    @Override
    public String toString() {
        if (didRun) {
            return MoreStrings.lenientFormat("Phase %s-%s took %s", phaseOrdinal, phaseName, stopWatch);
        }
        return MoreStrings.lenientFormat("Phase %s-%s did not run", phaseOrdinal, phaseName);
    }

    /**
     * @param timer List of Timer where the current time in Millis should be added
     *              to. If it did not run (was not called) the method is expected to
     *              add -1. Must not be null but may be empty
     */
    void addTimeToList(List<String> timer) {
        var builder = new StringBuilder();
        builder.append(phaseOrdinal).append("-").append(phaseName).append(": ");
        if (!didRun) {
            builder.append("-1");
        } else {
            builder.append(stopWatch.elapsed(TimeUnit.MILLISECONDS));
        }
        timer.add(builder.toString());
    }

    @Override
    public int compareTo(PhaseTracer o) {
        return phaseOrdinal.compareTo(o.phaseOrdinal);
    }
}
