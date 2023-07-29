package de.cuioss.portal.ui.api.dashboard;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;

class BaseLazyLoadingListItemWidgetTest implements ShouldHandleObjectContracts<TestLazyLoadingListItemWidget> {

    @Override
    public TestLazyLoadingListItemWidget getUnderTest() {
        return new TestLazyLoadingListItemWidget();
    }

    @Test
    void shouldReturnEmptyListOnMap() {
        var underTest = getUnderTest();
        underTest.handleResult("");
        assertTrue(underTest.getItems().isEmpty());
    }

}
