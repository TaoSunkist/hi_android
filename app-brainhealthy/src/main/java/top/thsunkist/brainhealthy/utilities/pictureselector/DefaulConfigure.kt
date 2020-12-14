package top.thsunkist.brainhealthy.utilities.pictureselector

import android.content.pm.ActivityInfo
import android.graphics.Color
import com.luck.picture.lib.PictureSelectionModel
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.language.LanguageConfig
import com.luck.picture.lib.style.PictureParameterStyle
import top.thsunkist.brainhealthy.R

fun PictureParameterStyle.default(): PictureParameterStyle {
    pictureUnCompleteTextColor = Color.WHITE
    pictureCompleteBackgroundStyle = R.drawable.ripple_button_gradient_size50_radius25
    pictureUnCompleteBackgroundStyle = R.drawable.shape_disabled_button_gradient_size50_radius25
    pictureLeftBackIcon = R.drawable.icon_arrow_left
    pictureWeChatTitleBackgroundStyle = R.drawable.bg_oval_stroke_transparent
    pictureTitleTextColor = R.color.colorAccent
    pictureTitleDownResId = R.drawable.ic_baseline_arrow_circle_down_24
    pictureTitleUpResId = R.drawable.ic_baseline_arrow_circle_up_24
    isChangeStatusBarFontColor = true
    pictureContainerBackgroundColor = Color.WHITE
    return this
}

fun PictureSelectionModel.default(): PictureSelectionModel {
    return this.imageEngine(PicassoEngine.createPicassoEngine()) // 外部传入图片加载引擎，必传项
        .setPictureStyle(PictureParameterStyle().default())
        .selectionMode(PictureConfig.SINGLE)
        .isSingleDirectReturn(true)
        .hideBottomControls(true)
        .isEnableCrop(true)
        .setLanguage(LanguageConfig.ENGLISH)
        .isWithVideoImage(false)
        .isPreviewImage(true)
        .withAspectRatio(3, 5)
        .imageSpanCount(3) // 每行显示个数
        .isEnableCrop(true)
        .isReturnEmpty(true) // 未选择数据时点击按钮是否可以返回
        .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) // 设置相册Activity方向，不设置默认使用系统
        .isOriginalImageControl(false) // 是否显示原图控制按钮，如果设置为true则用户可以自由选择是否使用原图，压缩、裁剪功能将会失效
        .isDragFrame(false)// 是否可拖动裁剪框(固定)
        .compressQuality(85)
        .cutOutQuality(80) // 裁剪输出质量 默认100
        .minimumCompressSize(500) // 小于100kb的图片不压缩
        .isUseCustomCamera(false) // 是否使用自定义相机
}