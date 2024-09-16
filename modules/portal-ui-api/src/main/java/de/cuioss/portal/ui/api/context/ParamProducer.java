package de.cuioss.portal.ui.api.context;

import de.cuioss.portal.configuration.util.ConfigurationHelper;
import de.cuioss.tools.string.MoreStrings;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.faces.annotation.RequestParameterMap;
import jakarta.inject.Inject;

import java.util.Map;

/**
 * Producer for the CUI specific {@link Param} annotation.
 * This is a workaround for the Omnifaces Param annotation wich is not functioning under Quarkus,
 * see <a href="https://github.com/quarkiverse/quarkus-omnifaces/issues/3">Quarkiverse Issue #3</a>.
 * The produced value origins in request parameters map, not from query parameters nor from path parameters.
 */
@RequestScoped
public class ParamProducer {

    /**
     * Retrieves only the first value from a potential multi-values request parameter.
     * If a list of values is required, the {@code @RequestParameterValuesMap} annotation should be used.
     */
    @Inject
    @RequestParameterMap
    private Map<String, String> requestParamValueMap;

    @Produces
    @Param(name = "")
    public String produceStringParam(final InjectionPoint injectionPoint) {
        final var metaData = ConfigurationHelper.resolveAnnotation(injectionPoint, Param.class)
            .orElseThrow(() -> new IllegalStateException("Type must provide annotation " + Param.class.getName()));
        String value = requestParamValueMap.get(metaData.name());
        if (metaData.required() && MoreStrings.isBlank(value)) {
            throw new IllegalArgumentException(metaData.requiredMessage().formatted(metaData.name()));
        }
        return value;
    }

    @Produces
    @Param(name = "")
    public boolean produceBooleanParam(final InjectionPoint injectionPoint) {
        String value = produceStringParam(injectionPoint);
        return Boolean.parseBoolean(value);
    }

    @Produces
    @Param(name = "")
    public int produceIntParam(final InjectionPoint injectionPoint) {
        String value = produceStringParam(injectionPoint);
        return Integer.parseInt(value);
    }
}
