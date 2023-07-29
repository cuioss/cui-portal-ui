package de.cuioss.portal.ui.api.ui;

import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;

class Jsf23EnablerBeanTest implements ShouldBeNotNull<Jsf23EnablerBean> {

    @Override
    public Jsf23EnablerBean getUnderTest() {
        return new Jsf23EnablerBean();
    }

}
