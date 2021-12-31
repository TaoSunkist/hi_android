package top.thsunkist.appkit.engine


/** https://help.aliyun.com/document_detail/44688.html#title-y1e-xd2-5oo */
sealed class ResizeMode(
    private val mode: String,
    var width: Int,
    var height: Int,
    /* 调用resize，默认是不允许放大。即如果请求的图片比原图大，那么返回的仍然是原图。如果想取到放大的图片，即设置limit=0 */
    val limit: Int = 1
) {
    /* default mode, 等比缩放，限制在指定w与h的矩形内的最大图片。 */
    object LFIT : ResizeMode(
        mode = "m_lfit",
        width = 100,
        height = 100
    )

    /* 等比缩放，延伸出指定w与h的矩形框外的最小图片。 */
    object MFIT : ResizeMode(
        mode = "m_mfit",
        width = 0,
        height = 0
    )

    /* 固定宽高，将延伸出指定w与h的矩形框外的最小图片进行居中裁剪。 */
    object FILL : ResizeMode(
        mode = "m_fill",
        width = 0,
        height = 0
    )

    /* 固定宽高，缩放填充。 */
    object PAD : ResizeMode(
        mode = "m_pad",
        width = 0,
        height = 0
    )

    /* 固定宽高，强制缩放。 */
    object FIXED : ResizeMode(
        mode = "m_fixed",
        width = 0,
        height = 0
    )

    fun getUrl(profilePictureUrl: String?): String? {

        if (profilePictureUrl == null) {
            return null
        }
        if (profilePictureUrl.contains("yunoss.shijianline.cn")
                .not() || profilePictureUrl.contains("image/resize")
        ) {
            return profilePictureUrl
        }
        val originUrl = StringBuffer(profilePictureUrl)
        originUrl
            .append("?x-oss-process=image/resize,")
            .append(mode)
            .append(",w_${width}")
            .append(",h_${height}")
        return originUrl.toString()
    }

    fun getUrl(profilePictureUrl: String?, width: Int, height: Int): String? {

        if (profilePictureUrl == null) {
            return null
        }
        if (profilePictureUrl.contains("yunoss.shijianline.cn").not()) {
            return profilePictureUrl
        }
        val originUrl = StringBuffer(profilePictureUrl)
        originUrl
            .append("?x-oss-process=image/resize,")
            .append(mode)
            .append(",w_${width}")
            .append(",h_${height}")
        return originUrl.toString()
    }
}

