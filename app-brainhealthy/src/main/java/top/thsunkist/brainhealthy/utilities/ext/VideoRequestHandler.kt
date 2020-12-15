package top.thsunkist.brainhealthy.utilities.ext

import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.text.TextUtils
import com.squareup.picasso.Picasso
import com.squareup.picasso.Request
import com.squareup.picasso.RequestHandler
import java.io.IOException

class VideoRequestHandler : RequestHandler() {
    companion object {
        const val SCHEME_VIDEO = "video"
    }

    override fun canHandleRequest(data: Request): Boolean {
        val scheme = data.uri.scheme
        return SCHEME_VIDEO == scheme
    }

    @Throws(IOException::class)
    override fun load(request: Request, networkPolicy: Int): Result? {
        val uri = request.uri
        val path = uri.path
        if (TextUtils.isEmpty(path).not()) {
            val bm = ThumbnailUtils.createVideoThumbnail(path!!, MediaStore.Images.Thumbnails.MINI_KIND)
            return Result(bm!!, Picasso.LoadedFrom.DISK)
        }
        return null
    }
}