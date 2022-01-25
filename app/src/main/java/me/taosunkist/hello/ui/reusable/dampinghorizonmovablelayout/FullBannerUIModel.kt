package me.taosunkist.hello.ui.reusable.dampinghorizonmovablelayout

data class FullBannerUIModel(
    val name: String,
) {
    companion object {
        fun init(): FullBannerUIModel {
            return FullBannerUIModel(name = System.currentTimeMillis().toString())
        }
    }
}