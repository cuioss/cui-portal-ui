/**
 * <p>
 * Provides structures enabling / extending the portal for (multi-) templating.
 * </p>
 * <h2>Multi-Templating</h2>
 * <p>
 * The idea of multi-templating is to load template (hierarchies) from the
 * jars/modules/external folders, using a {@link de.cuioss.tools.io.FileLoader},
 * see {@link de.cuioss.tools.io.FileLoaderUtility}, not by relative path, as
 * within classical JSF-applications. The JSF 2.2. introduced 'contracts' do not
 * suffice our needs, especially regarding spreading the templates over multiple
 * jars. Therefore we created our own approach, that is similar to contracts.
 * </p>
 * <p>
 * The ability to resolve template paths dynamically allows to configure
 * something like technical root template in the cui-portal and/or other
 * templates inside of specific modules and / or applications.
 * </p>
 * <h3>Definitions</h3>
 * <ul>
 * <li>Template-Root-Path: All multi-templating artifacts are to be found at
 * <em>META-INF/templates</em> or at
 * <em>ExternalConfiguration/templates</em></li>
 * <li>Template-Directory: Within Template-Root-Path are the concrete
 * sub-directories located, containing the actual templates for that directory.
 * e.g. "/META-INF/templates/portal/" for the portal provided templates or
 * "/META-INF/templates/referral/" for the referral provided</li>
 * <li>TEMPLATES: Are the actual templates located under the concrete
 * Template-Directory e.g. "/META-INF/templates/portal/root.xhtml"</li>
 * </ul>
 * <h2>Usage</h2>
 * <p>
 * The usage of multi-templating is straight forward. In your facelet
 * template-client you address it like
 * </p>
 *
 * <pre>
 * {@code <ui:composition template="/templates/technical_root.xhtml">}
 * </pre>
 * <p>
 * The <em>/templates</em> part is used for our
 * {@link de.cuioss.portal.ui.api.application.templating.ViewResourceHandler} to
 * intercept the resolution. The second part identifies the concrete template
 * without the template-directory being part of the path: "Give me that thingy"
 * instead of "Give me the content of that path". This approach let us keep the
 * actual source dynamic. The default implementation will resolve them to
 * "/META-INF/templates/portal/technical_root.xhtml"
 * <h2>Implementation Details</h2>
 * 
 * <p>
 * While the
 * {@link de.cuioss.portal.ui.api.application.templating.ViewResourceHandler}
 * takes care regarding the delivery of the template the actual logic of which
 * template to choose is implemented within concrete instances of
 * {@link de.cuioss.portal.ui.api.templating.MultiTemplatingMapper} that needs
 * to be registered as managed-bean under the key
 * {@value de.cuioss.portal.ui.api.PortalCoreBeanNames#MULTI_TEMPLATING_MAPPER_BEAN_NAME}
 * </p>
 * <h3>Overriding Existing templates</h3>
 * <p>
 * If a concrete web module wants to to overwrite one or more of the templates,
 * e.g "root.xhtml" it needs to:
 * </p>
 * <ul>
 * <li>Create a corresponding file, e.g.
 * "/META-INF/templates/concrete/root.xhtml"</li>
 * <li>Provide either your own implementation of
 * {@link de.cuioss.portal.ui.api.templating.MultiTemplatingMapper} resolving
 * the request path "root.xhtml" to /concrete/root.xhtml</li>
 * <li>Or, <em>preferred</em>, configure the default implementation provided by
 * the portal accordingly by Registering concrete instances of
 * {@link de.cuioss.portal.ui.api.templating.StaticTemplateDescriptor}</li>
 * </ul>
 *
 * @author Oliver Wolff
 */
package de.cuioss.portal.ui.api.templating;
