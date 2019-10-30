package me.taosunkist.hello.picturechooser

import android.content.CursorLoader
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.FragmentActivity

class PictureChooser(val type: Int = 1, val activity: FragmentActivity? = null, val isGif: Boolean) {

    companion object {
        val QUERY_URI = MediaStore.Files.getContentUri("external")
        val ORDER_BY = MediaStore.Files.FileColumns._ID + " DESC"
        val DURATION = "duration"
        val NOT_GIF = "!='image/gif'"

        //图片
        val SELECTION = (MediaStore.Files.FileColumns.MEDIA_TYPE + "=?" + " AND " + MediaStore.MediaColumns.SIZE + ">0")
        val SELECTION_NOT_GIF =
            (MediaStore.Files.FileColumns.MEDIA_TYPE + "=?" + " AND " + MediaStore.MediaColumns.SIZE + ">0" + " AND " + MediaStore.MediaColumns.MIME_TYPE + NOT_GIF)
        // 获取图片, 以后可能还要其他的多媒体信息
        val SELECTION_ALL_ARGS = arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString())

        // 全部模式下条件
        fun getSelectionArgsForAllMediaCondition(time_condition: String, isGif: Boolean): String {
            return ("(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + (if (isGif) "" else " AND " + MediaStore.MediaColumns.MIME_TYPE + NOT_GIF)
                    + " OR "
                    + (MediaStore.Files.FileColumns.MEDIA_TYPE + "=? AND " + time_condition) + ")"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0")
        }
    }

    fun loadAllMedia(/*callback*/) = run {
        this.activity?.loaderManager?.initLoader<Cursor>( this.type, Bundle(), object : android.app.LoaderManager.LoaderCallbacks<Cursor> {
                override fun onCreateLoader(id: Int, p1: Bundle?): android.content.Loader<Cursor?>? {
                    Log.i("taohui", "$type,$id")
                    var cursorLoader: CursorLoader? = null
                    when (id) {
                        PictureCfg.TYPE_IMAGE.tag -> {
                            val MEDIA_TYPE_IMAGE =
                                getSelectionArgsForSingleMediaType(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
                            cursorLoader = CursorLoader(
                                activity,
                                QUERY_URI,
                                PROJECTION,
                                if (isGif) SELECTION else SELECTION_NOT_GIF,
                                MEDIA_TYPE_IMAGE,
                                ORDER_BY
                            )
                            return cursorLoader
                        }
                    }
                    return cursorLoader
                }

                override fun onLoadFinished(loader: android.content.Loader<Cursor>?, cursor: Cursor) {
                    val count: Int = cursor.count
                    Log.i("taohui", "count: $count")
                    if (count == 0) return

                    cursor.moveToFirst()
                    do {
                        val path = cursor.getString(cursor.getColumnIndexOrThrow(PROJECTION[1]))
                        val pictureType = cursor.getString(cursor.getColumnIndexOrThrow(PROJECTION[2]))

                        val w = cursor.getInt(cursor.getColumnIndexOrThrow(PROJECTION[3]))
                        val h = cursor.getInt(cursor.getColumnIndexOrThrow(PROJECTION[4]))
                        val duration = cursor.getInt(cursor.getColumnIndexOrThrow(PROJECTION[5]))

                        Log.i("taohui", "$path, $pictureType, $w, $h, $duration")
                    } while (cursor.moveToNext())


                }

                override fun onLoaderReset(cursor: android.content.Loader<Cursor>?) {
                }

            })
    }

    /**
     * 获取指定类型的文件
     *
     * @param mediaType
     * @return
     */
    private fun getSelectionArgsForSingleMediaType(mediaType: Int): Array<String> {
        return arrayOf(mediaType.toString())
    }

    /**
     * 媒体文件数据库字段
     */
    private val PROJECTION = arrayOf(
        MediaStore.Files.FileColumns._ID,
        MediaStore.MediaColumns.DATA,
        MediaStore.MediaColumns.MIME_TYPE,
        MediaStore.MediaColumns.WIDTH,
        MediaStore.MediaColumns.HEIGHT,
        DURATION
    )


}

