package de.cuioss.portal.ui.oauth;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;

import de.cuioss.portal.authentication.oauth.Oauth2Configuration;
import de.cuioss.portal.authentication.oauth.impl.Oauth2ConfigurationImpl;
import de.cuioss.portal.configuration.common.PortalPriorities;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApplicationScoped
@EqualsAndHashCode
@ToString
@Alternative
@Priority(PortalPriorities.PORTAL_ASSEMBLY_LEVEL)
public class Oauth2ConfigurationProducerMock {

    @Produces
    @Setter
    @Getter
    private Oauth2Configuration configuration = new Oauth2ConfigurationImpl();
}
