package com.leipeng.crop.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jph.takephoto.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageOptions;

/**
 * Created by 被咯苏州 on 2017/5/21.
 */

public class OriginalImageFragment extends Fragment {
    Activity _Activity;
    ImageView imageView;

    Uri sourceUri;
    Bitmap bitmap;
    int degree;

    public static OriginalImageFragment getInstance(@Nullable Uri sourceUri) {
        OriginalImageFragment fragment = new OriginalImageFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("sourceUri", sourceUri);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this._Activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(_Activity, R.layout.fragment_original_image, null);
        setupView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        sourceUri = bundle.getParcelable("sourceUri");
        imageView.setImageURI(sourceUri);
    }

    public void setupView(View view) {
        imageView = (ImageView) view.findViewById(R.id.imageView);
    }

    public void confirm() {
        _Activity.setResult(Activity.RESULT_FIRST_USER,getResultIntent(sourceUri));
        _Activity.finish();
        // _Activity.startActivityForResult(getIntent(_Activity), CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    protected Intent getResultIntent(Uri uri) {
        CropImage.ActivityResult result = new CropImage.ActivityResult(
                null,
                uri,
                null,
                null,
                null,
                0,
                0);
        Intent intent = new Intent();
        intent.putExtra(CropImage.CROP_IMAGE_EXTRA_RESULT, result);
        return intent;
    }

    public Intent getIntent(@NonNull Context context) {
        return getIntent(context, CropImageActivity.class);
    }

    public Intent getIntent(@NonNull Context context, @Nullable Class<?> cls) {
        CropImageOptions mOptions = new CropImageOptions();
        mOptions.validate();
        Uri mSource = sourceUri;
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(CropImage.CROP_IMAGE_EXTRA_SOURCE, mSource);
        intent.putExtra(CropImage.CROP_IMAGE_EXTRA_OPTIONS, mOptions);
        return intent;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
    }
}
