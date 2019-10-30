package me.taosunkist.hello.picturechooser

/**
 * 多媒体类别的标示
 */
enum class PictureCfg(internal val tag: Int) {
    TYPE_IMAGE(1);

    fun get(): Int = this.tag
}