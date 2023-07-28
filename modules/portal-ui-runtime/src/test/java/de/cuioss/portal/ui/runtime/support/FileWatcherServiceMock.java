package de.cuioss.portal.ui.runtime.support;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

import de.cuioss.portal.configuration.schedule.FileChangedEvent;
import de.cuioss.portal.configuration.schedule.FileWatcherService;
import de.cuioss.portal.configuration.schedule.PortalFileWatcherService;
import lombok.ToString;

/**
 * Mock implementation of {@link FileWatcherService} that is solely capable of
 * managing paths. It is defiend as an {@link Alternative}, therefore you need
 * to activate it like <code><pre>
 *  &#64;ActivatedAlternatives(FileWatcherServiceMock.class)</pre></code>
 * <p>
 * In case your test needs {@link FileChangedEvent}s you can directly handle the
 * in you unit-test: <code><pre>
 *    &#64;Inject
 * &#64;FileChangedEvent
 * private Event<Path> fileChangeEvent;</pre></code>
 * </p>
 *
 * @author Oliver Wolff
 */
@ApplicationScoped
@PortalFileWatcherService
@Alternative
@ToString
public class FileWatcherServiceMock implements FileWatcherService {

    private final Set<Path> registeredPaths = new HashSet<>();

    @Override
    public void register(final Path... paths) {
        Collections.addAll(registeredPaths, paths);
    }

    @Override
    public void unregister(final Path... paths) {
        registeredPaths.removeAll(Arrays.asList(paths));
    }

    @Override
    public List<Path> getRegisteredPaths() {
        return immutableList(registeredPaths);
    }
}
