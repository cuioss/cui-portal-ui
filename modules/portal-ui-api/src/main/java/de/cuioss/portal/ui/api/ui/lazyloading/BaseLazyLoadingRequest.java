package de.cuioss.portal.ui.api.ui.lazyloading;

import javax.inject.Inject;

import de.cuioss.jsf.api.components.model.lazyloading.LazyLoadingThreadModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public abstract class BaseLazyLoadingRequest<T> implements LazyLoadingRequest<T> {

    @Getter
    @Inject
    private LazyLoadingThreadModel<T> viewModel;

    @Override
    public long getRequestId() {
        return viewModel.getRequestId();
    }
}
