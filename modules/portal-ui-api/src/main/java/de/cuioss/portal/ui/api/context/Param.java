package de.cuioss.portal.ui.api.context;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.enterprise.util.Nonbinding;
import jakarta.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Alternative to the Omnifaces Param annotation,
 * which is currently not working with Quarkus.
 * Its main feature is to provide the request parameter during PostConstruct,
 * which we don't require afaik. Thus, we can have a very simple producer instead.
 */
@Qualifier
@Retention(RUNTIME)
@Target({METHOD, FIELD, PARAMETER})
public @interface Param {

    /**
     * (Optional) The name of the request parameter.
     * If not specified the name of the injection target field will be used.
     *
     * @return The name of the request parameter.
     */
    @Nonbinding String name();

    /**
     * (Optional) Flag indicating if this parameter is required (must be present) or not.
     * A value is said to be not present if it turns out to be empty according to
     * the semantics of {@link de.cuioss.tools.string.MoreStrings#isBlank(CharSequence)}.
     *
     * @return Whether the absence of the parameter should cause a validation error.
     */
    @Nonbinding boolean required() default false;

    /**
     * (Optional) A message that will be used if an empty value is submitted.
     * If the message contains a <code>%s</code> it will be replaced with the missing query parameters name.
     *
     * @return The error message to be used on empty submit while {@link #required()} is <code>true</code>.
     */
    @Nonbinding String requiredMessage() default "Request parameter missing: %s";
}
