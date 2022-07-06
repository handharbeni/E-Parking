package com.mhandharbeni.e_parking.cores;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;

import com.mhandharbeni.e_parking.database.AppDb;

import java.util.Objects;

public class BaseFragment extends Fragment {
    public NavController navController;
    public AppDb appDb;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appDb = AppDb.getInstance(requireContext());
    }

    public <T> void observe(String key, Observer<T> observer) {
        Objects.requireNonNull(
                navController
                        .getCurrentBackStackEntry()
        ).getSavedStateHandle().getLiveData(key).observe(
                getViewLifecycleOwner(),
                (Observer<? super Object>) t -> observer.onChanged((T) t)
        );
    }

    public <T> void setState(String key, T value) {
        Objects.requireNonNull(
                navController
                        .getCurrentBackStackEntry()
        ).getSavedStateHandle().set(key, value);
    }

    public void navigate(int id) {
        try {
            navController.navigate(id);
        } catch (Exception ignored) {}
    }

    public void navigate(int id, Bundle bundle) {
        try {
            navController.navigate(id, bundle);
        } catch (Exception ignored) {}
    }
}
