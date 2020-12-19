package me.taosunkist.hello.utility.picturechooser

import android.os.Parcel
import android.os.Parcelable

class LocalMedia(
    var path: String? = null,
    var compressPath: String? = null,
    var cutPath: String? = null,
    var duration: Long = 0,
    var isChecked: Boolean = false,
    var isCut: Boolean = false,
    var position: Int = 0,
    var num: Int = 0,
    var mimeType: Int = 0,
    var pictureType: String? = null,
    var compressed: Boolean = false,
    var width: Int = 0,
    var height: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readInt()
    )

    internal fun getPictureType() = when (pictureType.isNullOrEmpty()) {
        true -> "image/jpeg"
        false -> pictureType
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(path)
        parcel.writeString(compressPath)
        parcel.writeString(cutPath)
        parcel.writeLong(duration)
        parcel.writeByte(if (isChecked) 1 else 0)
        parcel.writeByte(if (isCut) 1 else 0)
        parcel.writeInt(position)
        parcel.writeInt(num)
        parcel.writeInt(mimeType)
        parcel.writeString(pictureType)
        parcel.writeByte(if (compressed) 1 else 0)
        parcel.writeInt(width)
        parcel.writeInt(height)
    }

    override fun describeContents() = 0


    companion object CREATOR : Parcelable.Creator<LocalMedia> {
        override fun createFromParcel(parcel: Parcel): LocalMedia {
            return LocalMedia(parcel)
        }

        override fun newArray(size: Int): Array<LocalMedia?> {
            return arrayOfNulls(size)
        }
    }

}

