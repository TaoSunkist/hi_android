package com.example.aliplayer

/* 定义UI界面通用的操作。 */
interface ViewAction {

    /* 隐藏类型 */
    enum class HideType {
        /* 正常情况下的隐藏 */
        Normal,
        /* 播放结束的隐藏，比如出错了 */
        End
    }

    /* 重置 */
    fun reset()

    /* 显示 */
    fun show()

    /**
     * 隐藏
     *
     * @param hideType 隐藏类型
     */
    fun hide(hideType: HideType)
}
