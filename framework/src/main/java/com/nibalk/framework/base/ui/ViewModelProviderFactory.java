package com.nibalk.framework.base.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelProviderFactory<VM> implements ViewModelProvider.Factory {

    private VM viewModel;

    public ViewModelProviderFactory(VM viewModel) {
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(viewModel.getClass())) {
            return (T) viewModel;
        }

        throw new IllegalArgumentException("Unknown class name");
    }
}
