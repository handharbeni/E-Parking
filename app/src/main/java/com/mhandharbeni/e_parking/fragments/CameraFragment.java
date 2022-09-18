package com.mhandharbeni.e_parking.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.mhandharbeni.e_parking.cores.BaseBottomFragment;
import com.mhandharbeni.e_parking.databinding.FragmentCameraBinding;
import com.priyankvasa.android.cameraviewex.CameraView;
import com.priyankvasa.android.cameraviewex.Image;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;

import kotlin.Unit;

public class CameraFragment extends BaseBottomFragment {
    private final String TAG = CameraFragment.class.getSimpleName();
    FragmentCameraBinding binding;
    CameraView cameraView;
    View view;
    CameraCallback cameraCallback;

    public static CameraFragment newInstance() {
        return new CameraFragment();
    }

    public static CameraFragment newInstance(CameraCallback cameraCallback) {
        return new CameraFragment(cameraCallback);
    }

    public CameraFragment() {
    }

    public CameraFragment(CameraCallback cameraCallback) {
        this.cameraCallback = cameraCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCameraBinding.inflate(inflater, container, false);

        view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            cameraCallback.requestPermission();
            return;
        }

        cameraView = binding.camera;
        cameraView.setAdjustViewBounds(true);
        cameraView.addCameraOpenedListener(() -> Unit.INSTANCE);
        cameraView.addPictureTakenListener((Image image) -> {
            showCapturePreview(image);
            return Unit.INSTANCE;
        });

        requireActivity().runOnUiThread(() -> {
            try {
                cameraView.start();
            } catch (Exception ignored) {
            }
        });


        binding.btnTake.setOnClickListener(v -> {
            cameraView.capture();
        });

        binding.btnRetake.setOnClickListener(v -> {
            cameraView.start();
        });
        binding.ivApply.setOnClickListener(v -> {
            if (toBase64() != null) {
                cameraCallback.onApply(toBase64());
                dismiss();
            } else {
                showDialogOneButton("Camera", "Please Take Picture first", "DISMISS");
            }
        });
        binding.ivClose.setOnClickListener(v -> {
            dismiss();
        });
    }

    @SuppressLint("CheckResult")
    private void showCapturePreview(@NotNull Image image) {
        final RequestOptions requestOptions = new RequestOptions()
                .override((int) (image.getWidth()), (int) (image.getHeight()));
        requireActivity().runOnUiThread(() -> {
            glideManager.load(image.getData())
                    .apply(requestOptions)
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            binding.image.setImageDrawable(resource);
                            binding.image.buildDrawingCache();
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
            binding.panelCamera.setVisibility(View.INVISIBLE);
            binding.panelImage.setVisibility(View.VISIBLE);
        });
    }

    private String toBase64() {
        try {
            BitmapDrawable drawable = (BitmapDrawable) binding.image.getDrawable();
            Bitmap bitmap = drawable.getBitmap();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] bb = bos.toByteArray();
            return Base64.encodeToString(bb, 0);
        } catch (Exception ignored) {
            return null;
        }
    }

    public interface CameraCallback {
        void onApply(String image);
        void onCancel();
        void requestPermission();
    }
}
