package com.mhandharbeni.e_parking.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.mhandharbeni.e_parking.R;
import com.mhandharbeni.e_parking.database.AppDb;
import com.mhandharbeni.e_parking.database.models.Parked;
import com.mhandharbeni.e_parking.databinding.FragmentCheckinBinding;
import com.priyankvasa.android.cameraviewex.CameraView;
import com.priyankvasa.android.cameraviewex.Image;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;
import com.skydoves.balloon.OnBalloonDismissListener;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import kotlin.Unit;

public class CheckinFragment extends Fragment {
    private final String TAG = CheckinFragment.class.getSimpleName();

    FragmentCheckinBinding binding;
    CameraView cameraView;

    NavController navController;

    private TextRecognizer recognizer;
    private RequestManager glideManager;
    private final Matrix matrix = new Matrix();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCheckinBinding.inflate(inflater, container, false);

        navController = NavHostFragment.findNavController(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupLibs();
        setupCamera();
        setupTrigger();
    }

    void setupLibs() {
        glideManager = Glide.with(this);
        recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
    }

    void setupCamera() {
        cameraView = binding.imageKendaraan;
        cameraView.addCameraOpenedListener(() -> Unit.INSTANCE);
        cameraView.addPictureTakenListener((Image image) -> {
            showCapturePreview(image);
            return Unit.INSTANCE;
        });
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        cameraView.start();
    }

    void setupTrigger() {
        binding.typeMotor.setOnClickListener(this::setupGroupButton);
        binding.typeMobil.setOnClickListener(this::setupGroupButton);
        binding.typeBusMini.setOnClickListener(this::setupGroupButton);
        binding.typeBusBesar.setOnClickListener(this::setupGroupButton);
        binding.btnTakePicture.setOnClickListener(v -> {
            try {
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                cameraView.capture();
            } catch (Exception ignored) {}
        });
        binding.btnRetake.setOnClickListener(v -> {
            cameraView.start();
            binding.panelImage.setVisibility(View.INVISIBLE);
            binding.panelCamera.setVisibility(View.VISIBLE);
        });

    }

    @SuppressLint("NonConstantResourceId")
    void setupGroupButton(View view) {
        int type = 0;
        int price = 1000;
        switch (view.getId()) {
            case R.id.typeMotor:
                break;
            case R.id.typeMobil:
                type = 1;
                price = 5000;
                break;
            case R.id.typeBusMini:
                type = 2;
                price = 15000;
                break;
            case R.id.typeBusBesar:
                type = 3;
                price = 20000;
                break;
        }

        Parked parked = new Parked();
        parked.setPlatNumber(Objects.requireNonNull(binding.edtPlatNomor.getEditText()).getText().toString());
        parked.setTicketNumber(String.valueOf(System.currentTimeMillis()));
        parked.setImage(toBase64());
        parked.setDate(System.currentTimeMillis());
        parked.setCheckIn(System.currentTimeMillis());
        parked.setCheckOut(0);
        parked.setType(type);
        parked.setSync(false);
        parked.setPrice(price);

        AppDb.getInstance(requireContext()).parked().insert(parked);

        Balloon balloon = new Balloon.Builder(requireContext())
                .setLayout(R.layout.popup_success)
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.TOP)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setArrowPosition(0.5f)
                .setWidth(BalloonSizeSpec.WRAP)
                .setHeight(BalloonSizeSpec.WRAP)
                .setTextSize(15f)
                .setCornerRadius(4f)
                .setAlpha(0.9f)
                .setBalloonAnimation(BalloonAnimation.ELASTIC)
                .setLifecycleOwner(getViewLifecycleOwner())
                .build();
        balloon.showAtCenter(binding.getRoot());
        balloon.setOnBalloonDismissListener(() -> navController.navigate(R.id.action_checkin_to_main));
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
                            binding.imagePreview.setImageDrawable(resource);
                            binding.imagePreview.buildDrawingCache();
                            getPlatNumber();
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
            binding.panelCamera.setVisibility(View.INVISIBLE);
            binding.panelImage.setVisibility(View.VISIBLE);
        });
    }

    void getPlatNumber() {
        BitmapDrawable drawable = (BitmapDrawable) binding.imagePreview.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        InputImage image = InputImage.fromBitmap(bitmap, 0);
        Task<Text> result = recognizer
                .process(image)
                .addOnSuccessListener(text -> Objects.requireNonNull(binding.edtPlatNomor.getEditText()).setText(text.getText()))
                .addOnFailureListener(e -> {});
    }

    private String toBase64() {
        BitmapDrawable drawable = (BitmapDrawable) binding.imagePreview.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bos);
        byte[] bb = bos.toByteArray();
        return Base64.encodeToString(bb, 0);
    }

    private Bitmap rotate(Bitmap bm, int rotation) {

        if (rotation == 0) return bm;

        matrix.setRotate(rotation);

        return Bitmap.createBitmap(
                bm,
                0,
                0,
                bm.getWidth(),
                bm.getHeight(),
                matrix,
                true
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED) {
            cameraView.start();
        }
    }

    @Override
    public void onPause() {
        cameraView.stop();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        cameraView.destroy();
        super.onDestroyView();
    }
}
