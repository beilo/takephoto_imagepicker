package com.jph.simple;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lzy.imagepicker.ImagePicker;


/**
 - 支持通过相机拍照获取图片
 - 支持从相册选择图片
 - 支持从文件选择图片
 - 支持多图选择
 - 支持批量图片裁切
 - 支持批量图片压缩
 - 支持对图片进行压缩
 - 支持对图片进行裁剪
 - 支持对裁剪及压缩参数自定义
 - 提供自带裁剪工具(可选)
 - 支持智能选取及裁剪异常处理
 - 支持因拍照Activity被回收后的自动恢复
 * Author: crazycodeboy
 * Date: 2016/9/21 0007 20:10
 * Version:4.0.0
 * 技术博文：http://www.cboy.me
 * GitHub:https://github.com/crazycodeboy
 * Eamil:crazycodeboy@gmail.com
 */
public class MainActivity extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);



        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setSelectLimit(9);    //选中数量限制
        imagePicker.setShowCamera(false);  //显示拍照按钮
        imagePicker.setCrop(false);        //允许裁剪（单选才有效）
        // imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        // imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        // imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        // imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        // imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        // imagePicker.setOutPutY(1000);//保存文件的高度。单位像素

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnTakePhotoActivity:
                startActivity(new Intent(this,SimpleActivity.class));
                break;
            case R.id.btnTakePhotoFragment:
                startActivity(new Intent(this,SimpleFragmentActivity.class));
                break;
            default:
        }
    }
}
