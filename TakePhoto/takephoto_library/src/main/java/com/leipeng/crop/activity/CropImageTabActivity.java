package com.leipeng.crop.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jph.takephoto.R;
import com.leipeng.crop.fragment.CropImageFragment;
import com.leipeng.crop.fragment.OriginalImageFragment;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageOptions;

/**
 * Created by 被咯苏州 on 2017/5/21.
 */

public class CropImageTabActivity extends AppCompatActivity {

    OriginalImageFragment originalImageFragment;
    CropImageFragment cropImageFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("裁剪");
        Drawable drawable = ResourcesCompat.getDrawable(getResources(),R.color.bar_title,null);
        actionBar.setBackgroundDrawable(drawable);
        setContentView(R.layout.activity_tab_fragment);
        Intent intent = getIntent();
        Uri sourceUri = intent.getParcelableExtra(CropImage.CROP_IMAGE_EXTRA_SOURCE);
        CropImageOptions mOptions = intent.getParcelableExtra(CropImage.CROP_IMAGE_EXTRA_OPTIONS);
        if (savedInstanceState == null) {
            originalImageFragment = OriginalImageFragment.getInstance(sourceUri);
            cropImageFragment = CropImageFragment.getInstance(sourceUri, mOptions);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fl_content, originalImageFragment, originalImageFragment.getClass().getName())
                    .add(R.id.fl_content, cropImageFragment, cropImageFragment.getClass().getName())
                    .hide(cropImageFragment)
                    .commit();
        } else {
            originalImageFragment = (OriginalImageFragment) getSupportFragmentManager().findFragmentByTag("OriginalImageFragment");
            cropImageFragment = (CropImageFragment) getSupportFragmentManager().findFragmentByTag("OriginalImageFragment");
            // 解决重叠问题
            getSupportFragmentManager()
                    .beginTransaction()
                    .show(originalImageFragment)
                    .hide(cropImageFragment)
                    .commit();
        }
        setupView();
    }

    private void setupView() {
        final TextView original = (TextView) findViewById(R.id.tv_original);
        original.setTextColor(getResources().getColor(R.color.blue));
        final TextView tailoring = (TextView) findViewById(R.id.tv_tailoring);
        tailoring.setTextColor(getResources().getColor(R.color.while_crop));

        findViewById(R.id.tv_again)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        setResult(Activity.RESULT_CANCELED, intent);
                        finish();
                    }
                });
        original
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // 原图
                        original.setTextColor(getResources().getColor(R.color.blue));
                        tailoring.setTextColor(getResources().getColor(R.color.while_crop));
                        getSupportFragmentManager().beginTransaction()
                                .show(originalImageFragment)
                                .hide(cropImageFragment)
                                .commit();
                    }
                });
        tailoring
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 裁剪
                        tailoring.setTextColor(getResources().getColor(R.color.blue));
                        original.setTextColor(getResources().getColor(R.color.while_crop));
                        getSupportFragmentManager().beginTransaction()
                                .show(cropImageFragment)
                                .hide(originalImageFragment)
                                .commit();
                    }
                });
        findViewById(R.id.tv_send)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("Crop", "onClick: send");
                        if (originalImageFragment.isHidden()) {
                            cropImageFragment.crop();
                        } else {
                            originalImageFragment.confirm();
                        }
                    }
                });
    }
}
