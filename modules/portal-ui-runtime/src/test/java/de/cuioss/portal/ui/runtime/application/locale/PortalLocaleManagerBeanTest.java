package de.cuioss.portal.ui.runtime.application.locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Locale;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAlternatives;
import org.jboss.weld.junit5.auto.ExcludeBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.ui.api.locale.LocaleChangeEvent;
import de.cuioss.portal.ui.runtime.support.PortalLocaleResolverServiceMock;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.mocks.PortalLocaleProducerMock;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;

@EnablePortalUiEnvironment
@AddBeanClasses({ PortalLocaleResolverServiceMock.class })
@EnableAlternatives({ PortalLocaleResolverServiceMock.class })
@ExcludeBeanClasses(PortalLocaleProducerMock.class)
class PortalLocaleManagerBeanTest implements ShouldHandleObjectContracts<PortalLocaleManagerBean> {

    @Inject
    @Getter
    private PortalLocaleManagerBean underTest;

    private Locale changeEventResult;

    @Test
    void shouldProvideCorrectLocales() {
        assertEquals(Locale.ENGLISH, underTest.getLocale());
        assertEquals(2, underTest.getAvailableLocales().size());
    }

    @Test
    void shouldSaveLocaleCorrectly() {
        assertNull(changeEventResult);
        assertEquals(Locale.ENGLISH, underTest.getLocale());
        underTest.saveUserLocale(Locale.GERMAN);
        assertEquals(Locale.GERMAN, changeEventResult);
        assertEquals(Locale.GERMAN, underTest.getLocale());
        underTest.saveUserLocale(Locale.ENGLISH);
        assertEquals(Locale.ENGLISH, underTest.getLocale());
    }

    @Test
    void shouldFailOnSavingInvalidLocale() {
        assertThrows(IllegalArgumentException.class, () -> {
            underTest.saveUserLocale(Locale.SIMPLIFIED_CHINESE);
        });
    }

    void actOnLocaleChangeEven(@Observes @LocaleChangeEvent final Locale newLocale) {
        changeEventResult = newLocale;
    }

}
