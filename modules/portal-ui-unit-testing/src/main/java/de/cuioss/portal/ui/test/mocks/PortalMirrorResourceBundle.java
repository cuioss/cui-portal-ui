package de.cuioss.portal.ui.test.mocks;

import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

import de.cuioss.jsf.api.application.bundle.CuiResourceBundle;
import de.cuioss.portal.core.bundle.PortalResourceBundle;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Mock variant of {@link CuiResourceBundle}. Simulate
 * {@link #getString(String)} (={@link #getObject(String)}) by simply returning
 * the key {@link #getKeys()} will return an empty list.
 *
 * @author Oliver Wolff
 */
@Named("msgs")
@PortalResourceBundle
@Dependent
@EqualsAndHashCode(callSuper = false)
@ToString
public class PortalMirrorResourceBundle extends ResourceBundle implements Serializable {

    private static final long serialVersionUID = 3953649686127640297L;

    @Override
    protected Object handleGetObject(final String key) {
        return key;
    }

    @Override
    public Enumeration<String> getKeys() {
        return Collections.emptyEnumeration();
    }
}
