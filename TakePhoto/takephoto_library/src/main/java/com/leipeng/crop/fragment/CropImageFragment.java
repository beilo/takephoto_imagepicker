package com.leipeng.crop.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jph.takephoto.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageOptions;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;

/**
 * Created by 被咯苏州 on 2017/5/21.
 */

public class CropImageFragment extends Fragment implements CropImageView.OnCropImageCompleteListener {
    Activity _Activity;
    CropImageView cropImageView;

    Uri mSourceUri;
    CropImageOptions mOptions;

    public static CropImageFragment getInstance(@Nullable Uri sourceUri, CropImageOptions mOptions) {
        CropImageFragment fragment = new CropImageFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("mSourceUri", sourceUri);
        bundle.putParcelable("mOptions", mOptions);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this._Activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(_Activity, R.layout.fragment_crop_image, null);
        setupView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        mSourceUri = bundle.getParcelable("mSourceUri");
        mOptions = bundle.getParcelable("mOptions");
        cropImageView.setImageUriAsync(mSourceUri);
    }

    @Override
    public void onStop() {
        super.onStop();
        cropImageView.setOnCropImageCompleteListener(null);
    }

    private void setupView(View view) {
        cropImageView = (CropImageView) view.findViewById(R.id.cropImageView);
        cropImageView.setAspectRatio(5, 10);
        cropImageView.setFixedAspectRatio(false);
        cropImageView.setGuidelines(CropImageView.Guidelines.ON);
        cropImageView.setCropShape(CropImageView.CropShape.RECTANGLE);
        cropImageView.setScaleType(CropImageView.ScaleType.FIT_CENTER);
        cropImageView.setAutoZoomEnabled(true);
        cropImageView.setShowProgressBar(true);
        cropImageView.setCropRect(new Rect(0, 0, 100, 100));
        cropImageView.setOnCropImageCompleteListener(this);
    }

    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        Uri uri = null;
        if (result.getError() == null) {
            if (result.getUri() != null) {
                uri = result.getUri();
            } else {
                Bitmap bitmap = cropImageView.getCropShape() == CropImageView.CropShape.OVAL
                        ? CropImage.toOvalBitmap(result.getBitmap())
                        : result.getBitmap();
                uri = Uri.parse(MediaStore.Images.Media.insertImage(_Activity.getContentResolver(), bitmap, null, null));
            }
        }
        setResult(uri, result.getError(), result.getSampleSize());
    }

    protected void setResult(Uri uri, Exception error, int sampleSize) {
        int resultCode = error == null ? Activity.RESULT_OK : CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE;
        _Activity.setResult(resultCode, getResultIntent(uri, error, sampleSize));
        _Activity.finish();
    }

    protected Intent getResultIntent(Uri uri, Exception error, int sampleSize) {
        CropImage.ActivityResult result = new CropImage.ActivityResult(
                cropImageView.getImageUri(),
                uri,
                error,
                cropImageView.getCropPoints(),
                cropImageView.getCropRect(),
                cropImageView.getRotatedDegrees(),
                sampleSize);
        Intent intent = new Intent();
        intent.putExtra(CropImage.CROP_IMAGE_EXTRA_RESULT, result);
        return intent;
    }

    public void rotate90() {
        cropImageView.rotateImage(90);
    }

    public void crop() {
        cropImage();
        // cropImageView.getCroppedImageAsync();
    }

    protected void cropImage() {
        if (mOptions.noOutputImage) {
            setResult(null, null, 1);
        } else {
            Uri outputUri = getOutputUri();
            cropImageView.saveCroppedImageAsync(outputUri,
                    mOptions.outputCompressFormat,
                    mOptions.outputCompressQuality,
                    mOptions.outputRequestWidth,
                    mOptions.outputRequestHeight,
                    mOptions.outputRequestSizeOptions);
        }
    }

    protected Uri getOutputUri() {
        Uri outputUri = mOptions.outputUri;
        if (outputUri.equals(Uri.EMPTY)) {
            try {
                String ext = mOptions.outputCompressFormat == Bitmap.CompressFormat.JPEG ? ".jpg" :
                        mOptions.outputCompressFormat == Bitmap.CompressFormat.PNG ? ".png" : ".webp";
                outputUri = Uri.fromFile(File.createTempFile("cropped", ext, _Activity.getCacheDir()));
            } catch (IOException e) {
                throw new RuntimeException("Failed to create temp file for output image", e);
            }
        }
        return outputUri;
    }
}
