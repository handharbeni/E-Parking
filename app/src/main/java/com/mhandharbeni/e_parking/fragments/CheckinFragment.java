package com.mhandharbeni.e_parking.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Matrix;
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
import androidx.navigation.fragment.NavHostFragment;

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
import com.mhandharbeni.e_parking.apis.responses.data.DataMe;
import com.mhandharbeni.e_parking.apis.responses.data.DataPrice;
import com.mhandharbeni.e_parking.cores.BaseFragment;
import com.mhandharbeni.e_parking.database.models.Parked;
import com.mhandharbeni.e_parking.databinding.FragmentCheckinBinding;
import com.mhandharbeni.e_parking.utils.Constant;
import com.mhandharbeni.e_parking.utils.UtilPermission;
import com.priyankvasa.android.cameraviewex.CameraView;
import com.priyankvasa.android.cameraviewex.Image;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import kotlin.Unit;

public class CheckinFragment extends BaseFragment {
    private final String TAG = CheckinFragment.class.getSimpleName();

    FragmentCheckinBinding binding;
    CameraView cameraView;

    private TextRecognizer recognizer;
    private final Matrix matrix = new Matrix();
    private Parked parked;

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
        if (!UtilPermission.checkPermission(requireContext())) {
            setState(Constant.REQUEST_PERMISSION, CheckinFragment.class.getSimpleName());
        } else {
            setupLibs();
            setupCamera();
            setupTrigger();
        }
    }

    void setupLibs() {
        recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
    }

    void setupCamera() {
        cameraView = binding.imageKendaraan;
        cameraView.setAdjustViewBounds(true);
        cameraView.addCameraOpenedListener(() -> Unit.INSTANCE);
        cameraView.addPictureTakenListener((Image image) -> {
            showCapturePreview(image);
            return Unit.INSTANCE;
        });
        if (!UtilPermission.checkPermission(requireContext())) {
            setState(Constant.REQUEST_PERMISSION, CheckinFragment.class.getSimpleName());
            return;
        }
        new Handler().postDelayed(() -> {
            try {
                cameraView.start();
            } catch (Exception ignored) {}
        }, 1000);
    }

    void setupTrigger() {
        binding.typeMotor.setOnClickListener(this::setupGroupButton);
        binding.typeMobil.setOnClickListener(this::setupGroupButton);
        binding.typeBusMini.setOnClickListener(this::setupGroupButton);
        binding.typeBusBesar.setOnClickListener(this::setupGroupButton);
        binding.btnTakePicture.setOnClickListener(v -> {
            try {
                if (!UtilPermission.checkPermission(requireContext())) {
                    setState(Constant.REQUEST_PERMISSION, CheckinFragment.class.getSimpleName());
                    return;
                }
                cameraView.capture();
            } catch (Exception ignored) {
            }
        });
        binding.btnRetake.setOnClickListener(v -> {
            cameraView.start();
            binding.panelImage.setVisibility(View.INVISIBLE);
            binding.panelCamera.setVisibility(View.VISIBLE);
        });

    }

    @SuppressLint("NonConstantResourceId")
    void setupGroupButton(View view) {
        String image = toBase64();
        if (image != null) {

            if (!binding.edtPlatNomor.getEditText().getText().toString().equalsIgnoreCase("")) {
                int type = 0;
                int price = getPrice() != null ? getPrice().getMotor() : 1000;
                switch (view.getId()) {
                    case R.id.typeMotor:
                        break;
                    case R.id.typeMobil:
                        type = 1;
                        price = getPrice() != null ? getPrice().getMobil() : 5000;
                        break;
                    case R.id.typeBusMini:
                        type = 2;
                        price = getPrice() != null ? getPrice().getBusMini() : 10000;
                        break;
                    case R.id.typeBusBesar:
                        type = 3;
                        price = getPrice() != null ? getPrice().getBusBesar() : 15000;
                        break;
                }

                parked = new Parked();
                parked.setPlatNumber(Objects.requireNonNull(binding.edtPlatNomor.getEditText()).getText().toString().replaceAll(" ", "").toUpperCase());
                parked.setTicketNumber(String.valueOf(System.currentTimeMillis()));
                parked.setImage(image);
                parked.setDate(System.currentTimeMillis());
                parked.setCheckIn(System.currentTimeMillis());
                parked.setCheckOut(0);
                parked.setType(type);
                parked.setSync(false);
                parked.setPrice(price);

                appDb.parked().insert(parked);

                showSuccess();
            } else {
                showError(binding.edtPlatNomor, "Please Take a Picture first or fill it manually");
            }
        } else {
            showError(binding.btnTakePicture, "Please Take a Picture first");
        }

    }


    public void showSuccess() {
        if (balloon != null) {
            balloon.dismiss();
            balloon = null;
        }
        balloon = new Balloon.Builder(requireContext())
                .setLayout(R.layout.popup_success)
                .setIsVisibleArrow(false)
                .setWidth(BalloonSizeSpec.WRAP)
                .setHeight(BalloonSizeSpec.WRAP)
                .setTextSize(15f)
                .setCornerRadius(4f)
                .setAlpha(0.9f)
                .setBalloonAnimation(BalloonAnimation.ELASTIC)
                .setLifecycleOwner(getViewLifecycleOwner())
                .build();
        balloon.showAlignBottom(binding.panelCamera);
        balloon.setOnBalloonClickListener(view1 -> balloon.dismiss());
        balloon.setOnBalloonOverlayClickListener(balloon::dismiss);
        balloon.setOnBalloonDismissListener(() -> {
            if (parked != null) {
                Bundle args = new Bundle();
                args.putSerializable(Constant.KEY_DETAIL_TIKET, parked);
                navigate(R.id.action_checkin_to_detail, args);
            }
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
                .addOnFailureListener(e -> {
                });
    }

    private String toBase64() {
        try {
            BitmapDrawable drawable = (BitmapDrawable) binding.imagePreview.getDrawable();
            Bitmap bitmap = drawable.getBitmap();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] bb = bos.toByteArray();
            return Base64.encodeToString(bb, 0);
        } catch (Exception ignored) {
            return null;
        }
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
        if (!UtilPermission.checkPermission(requireContext())) {
            setState(Constant.REQUEST_PERMISSION, CheckinFragment.class.getSimpleName());
        } else {
            try {
                cameraView.start();
            } catch (Exception ignored) {
            }

        }
    }

    @Override
    public void onPause() {
        try {
            cameraView.stop();
        } catch (Exception ignored) {
        }
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        try {
            cameraView.destroy();
        } catch (Exception ignored) {
        }
        super.onDestroyView();
    }
}
