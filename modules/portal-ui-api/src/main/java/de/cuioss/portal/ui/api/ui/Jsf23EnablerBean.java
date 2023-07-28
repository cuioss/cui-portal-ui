package de.cuioss.portal.ui.api.ui;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.annotation.FacesConfig;

/**
 * The presence of the @FacesConfig annotation on a managed bean deployed within
 * an application enables version specific features. In this case, it enables
 * JSF CDI injection and EL resolution using CDI.
 *
 * @author Sven Haag
 */
@ApplicationScoped
@FacesConfig
public class Jsf23EnablerBean {
}
