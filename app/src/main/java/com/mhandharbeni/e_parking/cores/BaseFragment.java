package com.mhandharbeni.e_parking.cores;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;

import com.mhandharbeni.e_parking.database.AppDb;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;

import java.util.Objects;

public class BaseFragment extends Fragment {
    public NavController navController;
    public AppDb appDb;
    public Balloon balloon;
    public Resources resources;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appDb = AppDb.getInstance(requireContext());
        resources = requireContext().getResources();
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

    public void showError(View view, String message) {
        if (balloon != null) {
            balloon.dismiss();
            balloon = null;
        }
        balloon = new Balloon.Builder(requireContext())
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.TOP)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setArrowPosition(0.5f)
                .setIsVisibleArrow(true)
                .setText(message)
                .setPadding(9)
                .setWidth(BalloonSizeSpec.WRAP)
                .setHeight(BalloonSizeSpec.WRAP)
                .setTextSize(15f)
                .setCornerRadius(4f)
                .setAlpha(0.9f)
                .setBalloonAnimation(BalloonAnimation.ELASTIC)
                .setLifecycleOwner(getViewLifecycleOwner())
                .build();
        balloon.showAlignBottom(view);
        balloon.setOnBalloonClickListener(view1 -> balloon.dismiss());
        balloon.setOnBalloonOverlayClickListener(balloon::dismiss);
    }
}
